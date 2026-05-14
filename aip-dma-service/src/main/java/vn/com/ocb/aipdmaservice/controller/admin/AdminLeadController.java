package vn.com.ocb.aipdmaservice.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.com.ocb.aipdmaservice.model.admin.AdminLead;
import vn.com.ocb.aipdmaservice.service.AdminLeadService;

@Controller
@RequiredArgsConstructor
public class AdminLeadController {

    private final AdminLeadService adminLeadService;

    @GetMapping("/admin/leads")
    public String leads(Model model) {
        model.addAttribute("activePage", "leads");
        model.addAttribute("pageTitle", "Liên hệ & tư vấn");
        model.addAttribute("leads", adminLeadService.getAllLeads());
        return "admin/leads/list";
    }

    @GetMapping("/admin/leads/{type}/{id}")
    public String detail(@PathVariable String type, @PathVariable Long id, Model model) {
        AdminLead lead = adminLeadService.findLead(type, id);
        if (lead == null) {
            return "redirect:/admin/leads";
        }
        model.addAttribute("activePage", "leads");
        model.addAttribute("pageTitle", "Chi tiết yêu cầu");
        model.addAttribute("lead", lead);
        if ("ORDER".equalsIgnoreCase(type)) {
            model.addAttribute("order", adminLeadService.findOrder(id));
        } else {
            model.addAttribute("inquiry", adminLeadService.findInquiry(id));
        }
        return "admin/leads/detail";
    }

    @PostMapping("/admin/leads/{type}/{id}/status")
    public String updateStatus(@PathVariable String type,
                               @PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes redirectAttributes) {
        adminLeadService.updateStatus(type, id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái.");
        return "redirect:/admin/leads/" + type + "/" + id;
    }
}
