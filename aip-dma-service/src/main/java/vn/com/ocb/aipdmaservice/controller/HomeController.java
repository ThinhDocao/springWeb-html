package vn.com.ocb.aipdmaservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.com.ocb.aipdmaservice.model.BlogPost;
import vn.com.ocb.aipdmaservice.model.Product;
import vn.com.ocb.aipdmaservice.service.AppService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final AppService appService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categories", appService.getCategories());
        model.addAttribute("bestSellers", appService.getBestSellers());
        model.addAttribute("newProducts", appService.getNewProducts());
        model.addAttribute("premiumProducts", appService.getPremiumProducts());
        model.addAttribute("allProducts", appService.getAllProducts());
        model.addAttribute("blogPosts", appService.getAllBlogPosts());
        model.addAttribute("pageTitle", "Đồ Đồng Mỹ Nghệ - Tinh Hoa Đồng Việt");
        model.addAttribute("pageDescription",
                "Chuyên cung cấp đồ đồng mỹ nghệ cao cấp: tượng đồng, đỉnh đồng, tranh đồng, đồ phong thủy. Sản phẩm thủ công tinh xảo từ nghệ nhân lành nghề.");
        return "index";
    }

    @GetMapping("/gioi-thieu")
    public String about(Model model) {
        model.addAttribute("pageTitle", "Giới Thiệu - Đồ Đồng Mỹ Nghệ");
        model.addAttribute("pageDescription",
                "Tìm hiểu về thương hiệu đồ đồng mỹ nghệ hàng đầu Việt Nam với hơn 30 năm kinh nghiệm chế tác thủ công.");
        return "about";
    }

    @GetMapping("/san-pham")
    public String allProducts(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String price,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String material,
            Model model) {
        model.addAttribute("products", appService.getFilteredProducts("san-pham", price, material));
        model.addAttribute("categories", appService.getCategories());
        model.addAttribute("categoryName", "Tất Cả Sản Phẩm");
        model.addAttribute("currentSlug", "san-pham");
        model.addAttribute("currentPrice", price);
        model.addAttribute("currentMaterial", material);
        model.addAttribute("pageTitle", "Sản Phẩm Đồ Đồng Mỹ Nghệ");
        model.addAttribute("pageDescription",
                "Khám phá bộ sưu tập đồ đồng mỹ nghệ cao cấp: tượng đồng, đỉnh đồng, tranh đồng, đồ phong thủy và quà tặng.");
        return "category";
    }

    @GetMapping("/san-pham/{slug}")
    public String productDetail(@PathVariable String slug, Model model) {
        Product product = appService.getProductBySlug(slug);
        if (product == null) {
            return "error";
        }
        List<Product> relatedProducts = appService.getProductsByCategory(product.getCategorySlug());
        relatedProducts.removeIf(p -> p.getSlug().equals(slug));

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("pageTitle", product.getName() + " - Đồ Đồng Mỹ Nghệ");
        model.addAttribute("pageDescription", product.getShortDescription());
        return "product-detail";
    }

    @GetMapping("/{slug}")
    public String categoryBySlug(@PathVariable String slug,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String price,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String material,
            Model model) {
        vn.com.ocb.aipdmaservice.entity.CategoryEntity category = appService.getCategoryEntityBySlug(slug);
        if (category != null) {
            model.addAttribute("products", appService.getFilteredProducts(slug, price, material));
            model.addAttribute("categories", appService.getCategories());
            model.addAttribute("categoryName", category.getName());
            model.addAttribute("currentSlug", slug);
            model.addAttribute("currentPrice", price);
            model.addAttribute("currentMaterial", material);
            model.addAttribute("pageTitle", category.getName() + " - Đồ Đồng Mỹ Nghệ");
            model.addAttribute("pageDescription",
                    category.getDescription() != null ? category.getDescription() : category.getName());
            return "category";
        }
        return "error";
    }

    @GetMapping("/tin-tuc")
    public String blog(@org.springframework.web.bind.annotation.RequestParam(required = false) String category, Model model) {
        if (category != null && !category.isEmpty()) {
            model.addAttribute("blogPosts", appService.getBlogPostsByCategorySlug(category));
            model.addAttribute("currentCategory", category);
        } else {
            model.addAttribute("blogPosts", appService.getAllBlogPosts());
            model.addAttribute("currentCategory", "all");
        }
        model.addAttribute("blogCategories", appService.getAllBlogCategories());
        model.addAttribute("pageTitle", "Tin Tức & Kiến Thức Đồ Đồng");
        model.addAttribute("pageDescription",
                "Tin tức, kiến thức phong thủy, hướng dẫn bảo quản đồ đồng mỹ nghệ và xu hướng trang trí nội thất.");
        return "blog";
    }

    @GetMapping("/tin-tuc/{slug}")
    public String blogDetail(@PathVariable String slug, Model model) {
        BlogPost post = appService.getBlogPostBySlug(slug);
        if (post == null) {
            return "error";
        }
        model.addAttribute("post", post);
        model.addAttribute("recentPosts", appService.getAllBlogPosts());
        model.addAttribute("pageTitle", post.getTitle() + " - Đồ Đồng Mỹ Nghệ");
        model.addAttribute("pageDescription", post.getExcerpt());
        return "blog-detail";
    }

    @GetMapping("/lien-he")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Liên Hệ - Đồ Đồng Mỹ Nghệ");
        model.addAttribute("pageDescription",
                "Liên hệ với chúng tôi để được tư vấn miễn phí về đồ đồng mỹ nghệ. Hotline: 0987.654.321");
        return "contact";
    }

    @GetMapping("/xac-nhan-don-hang")
    public String checkout(Model model) {
        model.addAttribute("pageTitle", "Xác nhận đơn hàng - Fine Art Bronzes");
        model.addAttribute("pageDescription", "Hoàn tất thông tin đặt hàng để đội ngũ Fine Art Bronzes liên hệ xác nhận sớm nhất.");
        return "checkout";
    }

    @GetMapping("/dat-hang-thanh-cong")
    public String orderSuccess(Model model) {
        model.addAttribute("pageTitle", "Đặt hàng thành công - Fine Art Bronzes");
        model.addAttribute("pageDescription", "Cảm ơn bạn đã tin tưởng Fine Art Bronzes. Đơn hàng của bạn đã được ghi nhận.");
        return "order-success";
    }
}
