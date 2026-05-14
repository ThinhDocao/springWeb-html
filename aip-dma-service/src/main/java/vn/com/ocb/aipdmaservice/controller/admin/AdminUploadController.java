package vn.com.ocb.aipdmaservice.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.com.ocb.aipdmaservice.service.UploadService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminUploadController {

    private final UploadService uploadService;

    @PostMapping("/admin/uploads/ckeditor")
    public ResponseEntity<Map<String, String>> uploadCkEditorImage(@RequestParam("upload") MultipartFile file,
                                                                   @RequestParam(required = false) String folder) {
        try {
            String imageUrl = uploadService.storeImage(file, folder);
            return ResponseEntity.ok(Collections.singletonMap("url", imageUrl));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", ex.getMessage()));
        }
    }
}
