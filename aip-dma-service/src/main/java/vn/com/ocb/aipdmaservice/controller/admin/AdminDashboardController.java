package vn.com.ocb.aipdmaservice.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.com.ocb.aipdmaservice.repository.BlogPostRepository;
import vn.com.ocb.aipdmaservice.repository.CategoryRepository;
import vn.com.ocb.aipdmaservice.repository.ProductRepository;
import vn.com.ocb.aipdmaservice.service.AdminLeadService;

@Controller
@RequiredArgsConstructor
public class AdminDashboardController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BlogPostRepository blogPostRepository;
    private final AdminLeadService adminLeadService;

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("pageTitle", "Tổng quan");
        model.addAttribute("newLeadCount", adminLeadService.countNewLeads());
        model.addAttribute("productCount", productRepository.count());
        model.addAttribute("categoryCount", categoryRepository.count());
        model.addAttribute("blogPostCount", blogPostRepository.count());
        model.addAttribute("recentLeads", adminLeadService.getRecentLeads(10));
        return "admin/dashboard";
    }
}
