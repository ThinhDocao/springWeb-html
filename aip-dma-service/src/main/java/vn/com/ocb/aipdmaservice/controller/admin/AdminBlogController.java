package vn.com.ocb.aipdmaservice.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.com.ocb.aipdmaservice.entity.BlogCategoryEntity;
import vn.com.ocb.aipdmaservice.entity.BlogPostEntity;
import vn.com.ocb.aipdmaservice.repository.BlogCategoryRepository;
import vn.com.ocb.aipdmaservice.repository.BlogPostRepository;
import vn.com.ocb.aipdmaservice.service.UploadService;
import vn.com.ocb.aipdmaservice.util.SlugUtils;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminBlogController {

    private final BlogPostRepository blogPostRepository;
    private final BlogCategoryRepository blogCategoryRepository;
    private final UploadService uploadService;

    @GetMapping("/admin/blog")
    public String posts(Model model) {
        model.addAttribute("activePage", "blog");
        model.addAttribute("pageTitle", "Bài viết");
        model.addAttribute("posts", blogPostRepository.findAllByOrderByPublishDateDesc());
        return "admin/blog/list";
    }

    @GetMapping("/admin/blog/new")
    public String newPost(Model model) {
        BlogPostEntity post = new BlogPostEntity();
        post.setPublished(true);
        post.setPublishDate(LocalDate.now());
        preparePostForm(model, post, "Thêm bài viết");
        return "admin/blog/form";
    }

    @GetMapping("/admin/blog/{id}/edit")
    public String editPost(@PathVariable Long id, Model model) {
        BlogPostEntity post = blogPostRepository.findById(id).orElse(null);
        if (post == null) {
            return "redirect:/admin/blog";
        }
        preparePostForm(model, post, "Sửa bài viết");
        return "admin/blog/form";
    }

    @PostMapping({"/admin/blog", "/admin/blog/{id}"})
    public String savePost(@PathVariable(required = false) Long id,
                           @RequestParam String title,
                           @RequestParam(required = false) String slug,
                           @RequestParam(required = false) Long categoryId,
                           @RequestParam(required = false) String excerpt,
                           @RequestParam(required = false) String content,
                           @RequestParam(required = false) String imageUrl,
                           @RequestParam(required = false) MultipartFile imageFile,
                           @RequestParam(required = false) String author,
                           @RequestParam(required = false) String publishDate,
                           @RequestParam(required = false) String metaTitle,
                           @RequestParam(required = false) String metaDescription,
                           @RequestParam(required = false) Boolean isPublished,
                           RedirectAttributes redirectAttributes) {
        BlogPostEntity post = id == null ? new BlogPostEntity() : blogPostRepository.findById(id).orElse(new BlogPostEntity());
        String postSlug = resolveSlug(slug, title);
        String postFolder = blogPostFolder(postSlug);
        post.setTitle(title);
        post.setSlug(postSlug);
        post.setCategory(categoryId == null ? null : blogCategoryRepository.findById(categoryId).orElse(null));
        post.setExcerpt(excerpt);
        post.setContent(uploadService.normalizeRichTextImages(content, postFolder + "/content"));
        post.setAuthor(author);
        post.setPublishDate(parseDate(publishDate));
        post.setMetaTitle(metaTitle);
        post.setMetaDescription(metaDescription);
        post.setPublished(Boolean.TRUE.equals(isPublished));
        String uploadedUrl = uploadService.storeImage(imageFile, postFolder + "/thumbnail");
        post.setImageUrl(uploadedUrl != null ? uploadedUrl : imageUrl);
        blogPostRepository.save(post);
        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu bài viết.");
        return "redirect:/admin/blog";
    }

    @PostMapping("/admin/blog/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            blogPostRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa bài viết.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa bài viết này do có ràng buộc dữ liệu liên quan.");
        }
        return "redirect:/admin/blog";
    }

    @GetMapping("/admin/blog/categories")
    public String categories(Model model) {
        model.addAttribute("activePage", "blogCategories");
        model.addAttribute("pageTitle", "Chuyên mục bài viết");
        model.addAttribute("categories", blogCategoryRepository.findAllByOrderByNameAsc());
        return "admin/blog-categories/list";
    }

    @GetMapping("/admin/blog/categories/new")
    public String newCategory(Model model) {
        BlogCategoryEntity category = new BlogCategoryEntity();
        category.setActive(true);
        prepareCategoryForm(model, category, "Thêm chuyên mục");
        return "admin/blog-categories/form";
    }

    @GetMapping("/admin/blog/categories/{id}/edit")
    public String editCategory(@PathVariable Long id, Model model) {
        BlogCategoryEntity category = blogCategoryRepository.findById(id).orElse(null);
        if (category == null) {
            return "redirect:/admin/blog/categories";
        }
        prepareCategoryForm(model, category, "Sửa chuyên mục");
        return "admin/blog-categories/form";
    }

    @PostMapping({"/admin/blog/categories", "/admin/blog/categories/{id}"})
    public String saveCategory(@PathVariable(required = false) Long id,
                               @RequestParam String name,
                               @RequestParam(required = false) String slug,
                               @RequestParam(required = false) String description,
                               @RequestParam(required = false) Boolean isActive,
                               RedirectAttributes redirectAttributes) {
        BlogCategoryEntity category = id == null ? new BlogCategoryEntity() : blogCategoryRepository.findById(id).orElse(new BlogCategoryEntity());
        category.setName(name);
        category.setSlug(resolveSlug(slug, name));
        category.setDescription(description);
        category.setActive(Boolean.TRUE.equals(isActive));
        blogCategoryRepository.save(category);
        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu chuyên mục.");
        return "redirect:/admin/blog/categories";
    }

    @PostMapping("/admin/blog/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Check if any blog post is using this category
            long postCount = blogPostRepository.findAll().stream()
                    .filter(p -> p.getCategory() != null && id.equals(p.getCategory().getId()))
                    .count();
            if (postCount > 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa chuyên mục này vì đang có bài viết thuộc về nó. Vui lòng chuyển hoặc xóa các bài viết trước.");
                return "redirect:/admin/blog/categories";
            }
            blogCategoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa chuyên mục.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa chuyên mục này do có ràng buộc dữ liệu liên quan.");
        }
        return "redirect:/admin/blog/categories";
    }

    private void preparePostForm(Model model, BlogPostEntity post, String title) {
        List<BlogCategoryEntity> categories = blogCategoryRepository.findByIsActiveTrueOrderByNameAsc();
        model.addAttribute("activePage", "blog");
        model.addAttribute("pageTitle", title);
        model.addAttribute("post", post);
        model.addAttribute("categories", categories);
    }

    private void prepareCategoryForm(Model model, BlogCategoryEntity category, String title) {
        model.addAttribute("activePage", "blogCategories");
        model.addAttribute("pageTitle", title);
        model.addAttribute("category", category);
    }

    private String resolveSlug(String slug, String fallback) {
        String value = slug != null && !slug.trim().isEmpty() ? slug : fallback;
        return SlugUtils.toSlug(value);
    }

    private String blogPostFolder(String slug) {
        return "blog/" + (slug == null || slug.trim().isEmpty() ? "draft" : slug);
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return LocalDate.now();
        }
        return LocalDate.parse(value);
    }
}
