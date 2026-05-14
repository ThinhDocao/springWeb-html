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
import vn.com.ocb.aipdmaservice.entity.CategoryEntity;
import vn.com.ocb.aipdmaservice.entity.MaterialEntity;
import vn.com.ocb.aipdmaservice.entity.ProductEntity;
import vn.com.ocb.aipdmaservice.entity.ProductImageEntity;
import vn.com.ocb.aipdmaservice.repository.CategoryRepository;
import vn.com.ocb.aipdmaservice.repository.MaterialRepository;
import vn.com.ocb.aipdmaservice.repository.ProductRepository;
import vn.com.ocb.aipdmaservice.service.UploadService;
import vn.com.ocb.aipdmaservice.util.SlugUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AdminCatalogController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MaterialRepository materialRepository;
    private final UploadService uploadService;

    @GetMapping("/admin/products")
    public String products(@RequestParam(required = false) String q, Model model) {
        List<ProductEntity> products = q != null && !q.trim().isEmpty()
                ? productRepository.findByNameContainingIgnoreCaseOrderBySortOrderAsc(q.trim())
                : productRepository.findAllByOrderBySortOrderAsc();
        model.addAttribute("activePage", "products");
        model.addAttribute("pageTitle", "Sản phẩm");
        model.addAttribute("products", products);
        model.addAttribute("q", q);
        return "admin/products/list";
    }

    @GetMapping("/admin/products/new")
    public String newProduct(Model model) {
        ProductEntity product = new ProductEntity();
        product.setActive(true);
        product.setPrice(BigDecimal.ZERO);
        prepareProductForm(model, product, "Thêm sản phẩm");
        return "admin/products/form";
    }

    @GetMapping("/admin/products/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        ProductEntity product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/admin/products";
        }
        prepareProductForm(model, product, "Sửa sản phẩm");
        return "admin/products/form";
    }

    @PostMapping("/admin/products")
    public String createProduct(@RequestParam String name,
                                @RequestParam(required = false) String slug,
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) Long materialId,
                                @RequestParam(required = false) String price,
                                @RequestParam(required = false) String originalPrice,
                                @RequestParam(required = false) String shortDescription,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) String detailDescription,
                                @RequestParam(required = false) String specifications,
                                @RequestParam(required = false) String fengShuiMeaning,
                                @RequestParam(required = false) String size,
                                @RequestParam(required = false) String weight,
                                @RequestParam(required = false) Integer sortOrder,
                                @RequestParam(required = false) String metaTitle,
                                @RequestParam(required = false) String metaDescription,
                                @RequestParam(required = false) Boolean isBestSeller,
                                @RequestParam(required = false) Boolean isNew,
                                @RequestParam(required = false) Boolean isPremium,
                                @RequestParam(required = false) Boolean isActive,
                                @RequestParam(required = false) Long primaryImageId,
                                @RequestParam(required = false) List<Long> imageIds,
                                @RequestParam(required = false) List<Integer> imageSortOrders,
                                @RequestParam(required = false) List<Long> removeImageIds,
                                @RequestParam(required = false, name = "imageFiles") MultipartFile[] imageFiles,
                                RedirectAttributes redirectAttributes) {
        ProductEntity product = new ProductEntity();
        saveProduct(product, name, slug, categoryId, materialId, price, originalPrice, shortDescription, description,
                detailDescription, specifications, fengShuiMeaning, size, weight, sortOrder, metaTitle,
                metaDescription, isBestSeller, isNew, isPremium, isActive, primaryImageId, imageIds,
                imageSortOrders, removeImageIds, imageFiles);
        redirectAttributes.addFlashAttribute("successMessage", "Đã tạo sản phẩm.");
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/{id}")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam(required = false) String slug,
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) Long materialId,
                                @RequestParam(required = false) String price,
                                @RequestParam(required = false) String originalPrice,
                                @RequestParam(required = false) String shortDescription,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) String detailDescription,
                                @RequestParam(required = false) String specifications,
                                @RequestParam(required = false) String fengShuiMeaning,
                                @RequestParam(required = false) String size,
                                @RequestParam(required = false) String weight,
                                @RequestParam(required = false) Integer sortOrder,
                                @RequestParam(required = false) String metaTitle,
                                @RequestParam(required = false) String metaDescription,
                                @RequestParam(required = false) Boolean isBestSeller,
                                @RequestParam(required = false) Boolean isNew,
                                @RequestParam(required = false) Boolean isPremium,
                                @RequestParam(required = false) Boolean isActive,
                                @RequestParam(required = false) Long primaryImageId,
                                @RequestParam(required = false) List<Long> imageIds,
                                @RequestParam(required = false) List<Integer> imageSortOrders,
                                @RequestParam(required = false) List<Long> removeImageIds,
                                @RequestParam(required = false, name = "imageFiles") MultipartFile[] imageFiles,
                                RedirectAttributes redirectAttributes) {
        ProductEntity product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/admin/products";
        }
        saveProduct(product, name, slug, categoryId, materialId, price, originalPrice, shortDescription, description,
                detailDescription, specifications, fengShuiMeaning, size, weight, sortOrder, metaTitle,
                metaDescription, isBestSeller, isNew, isPremium, isActive, primaryImageId, imageIds,
                imageSortOrders, removeImageIds, imageFiles);
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật sản phẩm.");
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sản phẩm.");
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/categories")
    public String categories(Model model) {
        model.addAttribute("activePage", "categories");
        model.addAttribute("pageTitle", "Danh mục");
        model.addAttribute("categories", getHierarchicalCategories(false));
        return "admin/categories/list";
    }

    @GetMapping("/admin/categories/new")
    public String newCategory(Model model) {
        CategoryEntity category = new CategoryEntity();
        category.setActive(true);
        prepareCategoryForm(model, category, "Thêm danh mục");
        return "admin/categories/form";
    }

    @GetMapping("/admin/categories/{id}/edit")
    public String editCategory(@PathVariable Long id, Model model) {
        CategoryEntity category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return "redirect:/admin/categories";
        }
        prepareCategoryForm(model, category, "Sửa danh mục");
        return "admin/categories/form";
    }

    @PostMapping({"/admin/categories", "/admin/categories/{id}"})
    public String saveCategory(@PathVariable(required = false) Long id,
                               @RequestParam String name,
                               @RequestParam(required = false) String slug,
                               @RequestParam(required = false) Long parentId,
                               @RequestParam(required = false) String imageUrl,
                               @RequestParam(required = false) MultipartFile imageFile,
                               @RequestParam(required = false) String description,
                               @RequestParam(required = false) Integer sortOrder,
                               @RequestParam(required = false) String metaTitle,
                               @RequestParam(required = false) String metaDescription,
                               @RequestParam(required = false) Boolean isActive,
                               RedirectAttributes redirectAttributes) {
        CategoryEntity category = id == null ? new CategoryEntity() : categoryRepository.findById(id).orElse(new CategoryEntity());
        category.setName(name);
        category.setSlug(resolveSlug(slug, name));
        category.setParent(parentId == null ? null : categoryRepository.findById(parentId).orElse(null));
        category.setLevel(category.getParent() == null ? 0 : category.getParent().getLevel() + 1);
        category.setDescription(description);
        category.setSortOrder(sortOrder == null ? 0 : sortOrder);
        category.setMetaTitle(metaTitle);
        category.setMetaDescription(metaDescription);
        category.setActive(Boolean.TRUE.equals(isActive));
        String uploadedUrl = uploadService.storeImage(imageFile, categoryFolder(category.getSlug()));
        category.setImageUrl(uploadedUrl != null ? uploadedUrl : imageUrl);
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu danh mục.");
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa danh mục.");
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/materials")
    public String materials(Model model) {
        model.addAttribute("activePage", "materials");
        model.addAttribute("pageTitle", "Chất liệu");
        model.addAttribute("materials", materialRepository.findAllByOrderByNameAsc());
        return "admin/materials/list";
    }

    @GetMapping("/admin/materials/new")
    public String newMaterial(Model model) {
        MaterialEntity material = new MaterialEntity();
        material.setActive(true);
        prepareMaterialForm(model, material, "Thêm chất liệu");
        return "admin/materials/form";
    }

    @GetMapping("/admin/materials/{id}/edit")
    public String editMaterial(@PathVariable Long id, Model model) {
        MaterialEntity material = materialRepository.findById(id).orElse(null);
        if (material == null) {
            return "redirect:/admin/materials";
        }
        prepareMaterialForm(model, material, "Sửa chất liệu");
        return "admin/materials/form";
    }

    @PostMapping({"/admin/materials", "/admin/materials/{id}"})
    public String saveMaterial(@PathVariable(required = false) Long id,
                               @RequestParam String name,
                               @RequestParam(required = false) String description,
                               @RequestParam(required = false) Boolean isActive,
                               RedirectAttributes redirectAttributes) {
        MaterialEntity material = id == null ? new MaterialEntity() : materialRepository.findById(id).orElse(new MaterialEntity());
        material.setName(name);
        material.setDescription(description);
        material.setActive(Boolean.TRUE.equals(isActive));
        materialRepository.save(material);
        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu chất liệu.");
        return "redirect:/admin/materials";
    }

    @PostMapping("/admin/materials/{id}/delete")
    public String deleteMaterial(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        materialRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa chất liệu.");
        return "redirect:/admin/materials";
    }

    private void prepareProductForm(Model model, ProductEntity product, String title) {
        model.addAttribute("activePage", "products");
        model.addAttribute("pageTitle", title);
        model.addAttribute("product", product);
        model.addAttribute("categories", getHierarchicalCategories(true));
        model.addAttribute("materials", materialRepository.findByIsActiveTrueOrderByNameAsc());
    }

    private void prepareCategoryForm(Model model, CategoryEntity category, String title) {
        model.addAttribute("activePage", "categories");
        model.addAttribute("pageTitle", title);
        model.addAttribute("category", category);
        model.addAttribute("parentCategories", categoryRepository.findByParentIsNullOrderBySortOrderAsc());
    }

    private void prepareMaterialForm(Model model, MaterialEntity material, String title) {
        model.addAttribute("activePage", "materials");
        model.addAttribute("pageTitle", title);
        model.addAttribute("material", material);
    }

    private void saveProduct(ProductEntity product, String name, String slug, Long categoryId, Long materialId,
                             String price, String originalPrice, String shortDescription, String description,
                             String detailDescription, String specifications, String fengShuiMeaning,
                             String size, String weight, Integer sortOrder, String metaTitle, String metaDescription,
                             Boolean isBestSeller, Boolean isNew, Boolean isPremium, Boolean isActive,
                             Long primaryImageId, List<Long> imageIds, List<Integer> imageSortOrders,
                             List<Long> removeImageIds, MultipartFile[] imageFiles) {
        String productSlug = resolveSlug(slug, name);
        String productFolder = productFolder(productSlug);
        product.setName(name);
        product.setSlug(productSlug);
        product.setCategory(categoryId == null ? null : categoryRepository.findById(categoryId).orElse(null));
        product.setMaterial(materialId == null ? null : materialRepository.findById(materialId).orElse(null));
        product.setPrice(parseMoney(price));
        product.setOriginalPrice(parseNullableMoney(originalPrice));
        product.setShortDescription(shortDescription);
        product.setDescription(uploadService.normalizeRichTextImages(description, productFolder + "/content"));
        product.setDetailDescription(uploadService.normalizeRichTextImages(detailDescription, productFolder + "/content"));
        product.setSpecifications(uploadService.normalizeRichTextImages(specifications, productFolder + "/content"));
        product.setFengShuiMeaning(uploadService.normalizeRichTextImages(fengShuiMeaning, productFolder + "/content"));
        product.setSize(size);
        product.setWeight(weight);
        product.setSortOrder(sortOrder == null ? 0 : sortOrder);
        product.setMetaTitle(metaTitle);
        product.setMetaDescription(metaDescription);
        product.setBestSeller(Boolean.TRUE.equals(isBestSeller));
        product.setNew(Boolean.TRUE.equals(isNew));
        product.setPremium(Boolean.TRUE.equals(isPremium));
        product.setActive(Boolean.TRUE.equals(isActive));

        syncExistingImages(product, imageIds, imageSortOrders, primaryImageId, removeImageIds);
        appendUploadedImages(product, imageFiles, productFolder + "/gallery");
        ensurePrimaryImage(product, primaryImageId);
        productRepository.save(product);
    }

    private void syncExistingImages(ProductEntity product, List<Long> imageIds, List<Integer> imageSortOrders,
                                    Long primaryImageId, List<Long> removeImageIds) {
        if (product.getImages() == null) {
            product.setImages(new ArrayList<>());
        }
        if (product.getId() == null) {
            return;
        }
        Set<Long> removedIds = removeImageIds == null ? new HashSet<>() : new HashSet<>(removeImageIds);
        if (imageIds == null) {
            if (!removedIds.isEmpty()) {
                product.getImages().removeIf(image -> image.getId() != null && removedIds.contains(image.getId()));
            }
            return;
        }
        Set<Long> keepIds = new HashSet<>(imageIds);
        product.getImages().removeIf(image -> image.getId() != null
                && (!keepIds.contains(image.getId()) || removedIds.contains(image.getId())));
        if (imageIds != null) {
            for (int i = 0; i < imageIds.size(); i++) {
                Long imageId = imageIds.get(i);
                for (ProductImageEntity image : product.getImages()) {
                    if (imageId.equals(image.getId())) {
                        image.setSortOrder(imageSortOrders != null && imageSortOrders.size() > i ? imageSortOrders.get(i) : i);
                        image.setPrimary(primaryImageId != null && primaryImageId.equals(image.getId()));
                    }
                }
            }
        }
    }

    private void appendUploadedImages(ProductEntity product, MultipartFile[] imageFiles, String folder) {
        if (imageFiles == null) {
            return;
        }
        int sortOrder = product.getImages() == null ? 0 : product.getImages().size();
        for (MultipartFile file : imageFiles) {
            String imageUrl = uploadService.storeImage(file, folder);
            if (imageUrl != null) {
                ProductImageEntity image = ProductImageEntity.builder()
                        .product(product)
                        .imageUrl(imageUrl)
                        .sortOrder(sortOrder++)
                        .isPrimary(false)
                        .build();
                product.getImages().add(image);
            }
        }
    }

    private void ensurePrimaryImage(ProductEntity product, Long primaryImageId) {
        if (product.getImages() == null || product.getImages().isEmpty()) {
            return;
        }
        boolean hasPrimary = product.getImages().stream().anyMatch(ProductImageEntity::isPrimary);
        if (!hasPrimary || primaryImageId == null) {
            product.getImages().forEach(image -> image.setPrimary(false));
            product.getImages().get(0).setPrimary(true);
        }
    }

    private String productFolder(String slug) {
        return "products/" + resolveFolderName(slug, "draft");
    }

    private String categoryFolder(String slug) {
        return "categories/" + resolveFolderName(slug, "draft");
    }

    private String resolveFolderName(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value;
    }

    private String resolveSlug(String slug, String fallback) {
        String value = slug != null && !slug.trim().isEmpty() ? slug : fallback;
        return SlugUtils.toSlug(value);
    }

    private BigDecimal parseMoney(String value) {
        BigDecimal parsed = parseNullableMoney(value);
        return parsed == null ? BigDecimal.ZERO : parsed;
    }

    private BigDecimal parseNullableMoney(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            return null;
        }
        return new BigDecimal(digits);
    }

    private List<CategoryEntity> getHierarchicalCategories(boolean activeOnly) {
        List<CategoryEntity> all = activeOnly
                ? categoryRepository.findByIsActiveTrueOrderBySortOrderAsc()
                : categoryRepository.findAllByOrderBySortOrderAsc();
        List<CategoryEntity> result = new ArrayList<>();
        Set<Long> visitedIds = new HashSet<>();

        for (CategoryEntity category : all) {
            if (category.getParent() == null) {
                appendCategoryWithChildren(category, all, result, visitedIds);
            }
        }
        for (CategoryEntity category : all) {
            appendCategoryWithChildren(category, all, result, visitedIds);
        }
        return result;
    }

    private void appendCategoryWithChildren(CategoryEntity category, List<CategoryEntity> all,
                                            List<CategoryEntity> result, Set<Long> visitedIds) {
        if (category.getId() != null && !visitedIds.add(category.getId())) {
            return;
        }
        result.add(category);
        for (CategoryEntity candidate : all) {
            if (candidate.getParent() != null
                    && candidate.getParent().getId() != null
                    && candidate.getParent().getId().equals(category.getId())) {
                appendCategoryWithChildren(candidate, all, result, visitedIds);
            }
        }
    }
}
