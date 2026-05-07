package vn.com.ocb.aipdmaservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Đồ Đồng Thành Đạt - Lưu truyền phát triển văn hóa Việt");
        return "index";
    }

    @GetMapping("/gioi-thieu")
    public String gioiThieu(Model model) {
        model.addAttribute("pageTitle", "Giới thiệu - Đồ Đồng Thành Đạt");
        return "gioi-thieu";
    }

    @GetMapping("/san-pham")
    public String sanPham(Model model) {
        model.addAttribute("pageTitle", "Sản phẩm - Đồ Đồng Thành Đạt");
        return "san-pham";
    }

    @GetMapping("/tin-tuc")
    public String tinTuc(Model model) {
        model.addAttribute("pageTitle", "Tin tức - Đồ Đồng Thành Đạt");
        return "tin-tuc";
    }

    @GetMapping("/lien-he")
    public String lienHe(Model model) {
        model.addAttribute("pageTitle", "Liên hệ - Đồ Đồng Thành Đạt");
        return "lien-he";
    }

    @GetMapping("/san-pham/{slug}")
    public String chiTietSanPham(Model model) {
        model.addAttribute("pageTitle", "Chi tiết sản phẩm - Đồ Đồng Thành Đạt");
        return "chi-tiet-san-pham";
    }

    @GetMapping("/danh-muc/{slug}")
    public String danhMuc(Model model) {
        model.addAttribute("pageTitle", "Danh mục sản phẩm - Đồ Đồng Thành Đạt");
        return "danh-muc";
    }
}
