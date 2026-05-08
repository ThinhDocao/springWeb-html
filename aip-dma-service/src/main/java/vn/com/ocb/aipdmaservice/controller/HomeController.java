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
    public String allProducts(Model model) {
        model.addAttribute("products", appService.getAllProducts());
        model.addAttribute("categories", appService.getCategories());
        model.addAttribute("categoryName", "Tất Cả Sản Phẩm");
        model.addAttribute("currentSlug", "san-pham");
        model.addAttribute("pageTitle", "Sản Phẩm Đồ Đồng Mỹ Nghệ");
        model.addAttribute("pageDescription",
                "Khám phá bộ sưu tập đồ đồng mỹ nghệ cao cấp: tượng đồng, đỉnh đồng, tranh đồng, đồ phong thủy và quà tặng.");
        return "category";
    }

    @GetMapping("/tuong-dong")
    public String tuongDong(Model model) {
        model.addAttribute("products", appService.getProductsByCategory("tuong-dong"));
        model.addAttribute("categories", appService.getCategories());
        model.addAttribute("categoryName", "Tượng Đồng");
        model.addAttribute("currentSlug", "tuong-dong");
        model.addAttribute("pageTitle", "Tượng Đồng Mỹ Nghệ Cao Cấp");
        model.addAttribute("pageDescription",
                "Bộ sưu tập tượng đồng mỹ nghệ: Quan Công, Phật Di Lặc, tượng phong thủy. Đúc thủ công, chất lượng cao.");
        return "category";
    }

    @GetMapping("/dinh-dong")
    public String dinhDong(Model model) {
        model.addAttribute("products", appService.getProductsByCategory("dinh-dong"));
        model.addAttribute("categories", appService.getCategories());
        model.addAttribute("categoryName", "Đỉnh Đồng");
        model.addAttribute("currentSlug", "dinh-dong");
        model.addAttribute("pageTitle", "Tượng Đồng Mỹ Nghệ Cao Cấp");
        model.addAttribute("pageDescription",
                "Bộ sưu tập tượng đồng mỹ nghệ: Quan Công, Phật Di Lặc, tượng phong thủy. Đúc thủ công, chất lượng cao.");
        return "category";
    }

    @GetMapping("/tranh-dong")
    public String tranhDong(Model model) {
        model.addAttribute("products", appService.getProductsByCategory("tranh-dong"));
        model.addAttribute("categories", appService.getCategories());
        model.addAttribute("categoryName", "Tranh Đồng");
        model.addAttribute("currentSlug", "tranh-dong");
        model.addAttribute("pageTitle", "Tranh Đồng Nghệ Thuật");
        model.addAttribute("pageDescription",
                "Tranh đồng chạm nổi nghệ thuật: Thuận Buồm Xuôi Gió, Mã Đáo Thành Công, tranh đồng quê.");
        return "category";
    }

    @GetMapping("/do-tho-dong")
    public String doThoDong(Model model) {
        model.addAttribute("products", appService.getProductsByCategory("do-tho-dong"));
        model.addAttribute("categories", appService.getCategories());
        model.addAttribute("categoryName", "Đồ Thờ Đồng");
        model.addAttribute("currentSlug", "do-tho-dong");
        model.addAttribute("pageTitle", "Đồ Thờ Cúng Bằng Đồng");
        model.addAttribute("pageDescription",
                "Đồ thờ cúng bằng đồng: bộ tam sự, ngũ sự, đỉnh đồng, chân nến. Trang nghiêm, tôn kính.");
        return "category";
    }

    @GetMapping("/do-phong-thuy")
    public String doPhongThuy(Model model) {
        model.addAttribute("products", appService.getProductsByCategory("do-phong-thuy"));
        model.addAttribute("categories", appService.getCategories());
        model.addAttribute("categoryName", "Đồ Phong Thủy");
        model.addAttribute("currentSlug", "do-phong-thuy");
        model.addAttribute("pageTitle", "Đồ Phong Thủy Bằng Đồng");
        model.addAttribute("pageDescription",
                "Đồ phong thủy bằng đồng: Tỳ Hưu, Cóc Ba Chân, Rồng Phong Thủy. Chiêu tài, hóa sát.");
        return "category";
    }

    @GetMapping("/qua-tang")
    public String quaTang(Model model) {
        model.addAttribute("products", appService.getProductsByCategory("qua-tang"));
        model.addAttribute("categories", appService.getCategories());
        model.addAttribute("categoryName", "Quà Tặng Doanh Nghiệp");
        model.addAttribute("currentSlug", "qua-tang");
        model.addAttribute("pageTitle", "Quà Tặng Đồ Đồng Cao Cấp");
        model.addAttribute("pageDescription",
                "Quà tặng doanh nghiệp bằng đồng: trống đồng, kỷ niệm chương, biểu trưng. Sang trọng, ý nghĩa.");
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
    public String categoryBySlug(@PathVariable String slug, Model model) {
        vn.com.ocb.aipdmaservice.entity.CategoryEntity category = appService.getCategoryEntityBySlug(slug);
        if(category != null) {
            model.addAttribute("products", appService.getProductsByCategory(slug));
            model.addAttribute("categories", appService.getCategories());
            model.addAttribute("categoryName", category.getName());
            model.addAttribute("currentSlug", slug);
            model.addAttribute("pageTitle", category.getName() + " - Đồ Đồng Mỹ Nghệ");
            model.addAttribute("pageDescription", category.getDescription() != null ? category.getDescription() : category.getName());
            return "category";
        }
        return "error";
    }

    @GetMapping("/tin-tuc")
    public String blog(Model model) {
        model.addAttribute("blogPosts", appService.getAllBlogPosts());
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
}
