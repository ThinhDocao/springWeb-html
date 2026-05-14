package vn.com.ocb.aipdmaservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class UploadServiceTest {

    private static final Pattern UPLOAD_SRC_PATTERN = Pattern.compile("src=\"/uploads/products/demo/content/([^\"]+)\"");

    @TempDir
    Path uploadDir;

    @Test
    void normalizeRichTextImagesStoresBase64ImageAndKeepsOnlyUploadPathInHtml() {
        UploadService uploadService = new UploadService(uploadDir.toString());
        String html = "<p>Mo ta</p><img src=\"data:image/png;base64,iVBORw0KGgo=\" alt=\"demo\">";

        String normalized = uploadService.normalizeRichTextImages(html, "products/demo/content");

        assertThat(normalized).doesNotContain("data:image");
        assertThat(normalized).contains("src=\"/uploads/products/demo/content/");
        Matcher matcher = UPLOAD_SRC_PATTERN.matcher(normalized);
        assertThat(matcher.find()).isTrue();
        assertThat(Files.exists(uploadDir.resolve("products/demo/content").resolve(matcher.group(1)))).isTrue();
    }

    @Test
    void storeImageUsesRequestedFolder() {
        UploadService uploadService = new UploadService(uploadDir.toString());
        MockMultipartFile file = new MockMultipartFile(
                "upload", "demo.png", "image/png", new byte[]{1, 2, 3});

        String url = uploadService.storeImage(file, "products/demo/gallery");

        assertThat(url).startsWith("/uploads/products/demo/gallery/");
        String filename = url.substring("/uploads/products/demo/gallery/".length());
        assertThat(Files.exists(uploadDir.resolve("products/demo/gallery").resolve(filename))).isTrue();
    }
}
