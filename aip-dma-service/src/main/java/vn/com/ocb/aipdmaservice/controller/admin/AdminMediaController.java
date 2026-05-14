package vn.com.ocb.aipdmaservice.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.com.ocb.aipdmaservice.service.MediaLibraryService;

@Controller
@RequiredArgsConstructor
public class AdminMediaController {

    private final MediaLibraryService mediaLibraryService;

    @GetMapping("/admin/media")
    public String media(Model model) {
        model.addAttribute("activePage", "media");
        model.addAttribute("pageTitle", "Thư viện ảnh");
        model.addAttribute("mediaRoot", mediaLibraryService.getMediaTree());
        return "admin/media";
    }
}
