package vn.com.ocb.aipdmaservice.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.com.ocb.aipdmaservice.service.SiteSettingService;
import vn.com.ocb.aipdmaservice.service.UploadService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminSettingsController {

    private final SiteSettingService siteSettingService;
    private final UploadService uploadService;

    @GetMapping("/admin/settings")
    public String settings(Model model) {
        model.addAttribute("activePage", "settings");
        model.addAttribute("pageTitle", "Cài đặt hệ thống");
        model.addAttribute("settings", siteSettingService.getSettingsMap());
        return "admin/settings";
    }

    @PostMapping("/admin/settings")
    public String saveSettings(@RequestParam Map<String, String> params,
                               @RequestParam(required = false) MultipartFile logoFile,
                               RedirectAttributes redirectAttributes) {
        params.put("faviconUrl", siteSettingService.get("faviconUrl", ""));
        String logoUrl = uploadService.storeImage(logoFile, "settings/logo");
        if (logoUrl != null) {
            params.put("logoUrl", logoUrl);
        }
        siteSettingService.saveSettings(params);
        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu cài đặt.");
        return "redirect:/admin/settings";
    }
}
