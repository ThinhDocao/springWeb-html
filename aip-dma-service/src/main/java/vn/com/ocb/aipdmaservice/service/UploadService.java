package vn.com.ocb.aipdmaservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.com.ocb.aipdmaservice.util.SlugUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UploadService {

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
            Arrays.asList("jpg", "jpeg", "png", "webp"));
    private static final Pattern DATA_IMAGE_SRC_PATTERN = Pattern.compile(
            "(src\\s*=\\s*[\"'])data:image/(png|jpe?g|webp);base64,([^\"']+)([\"'])",
            Pattern.CASE_INSENSITIVE);
    private static final int MAX_DATA_IMAGE_BYTES = 10 * 1024 * 1024;

    private final Path uploadRoot;

    public UploadService(@Value("${app.upload-dir:uploads}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String storeImage(MultipartFile file) {
        return storeImage(file, null);
    }

    public String storeImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < originalName.length() - 1) {
            extension = originalName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
        }
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Chi ho tro anh JPG, JPEG, PNG hoac WEBP.");
        }

        try {
            Path uploadFolder = resolveUploadFolder(folder);
            Files.createDirectories(uploadFolder);
            String filename = UUID.randomUUID().toString() + "." + normalizeExtension(extension);
            Path target = uploadFolder.resolve(filename).normalize();
            if (!target.startsWith(uploadRoot)) {
                throw new IllegalArgumentException("Ten file upload khong hop le.");
            }
            file.transferTo(target.toFile());
            return toPublicUrl(target);
        } catch (IOException e) {
            throw new IllegalStateException("Khong the luu file upload.", e);
        }
    }

    public String normalizeRichTextImages(String html) {
        return normalizeRichTextImages(html, null);
    }

    public String normalizeRichTextImages(String html, String folder) {
        if (!StringUtils.hasText(html) || !html.toLowerCase(Locale.ROOT).contains("data:image/")) {
            return html;
        }

        Matcher matcher = DATA_IMAGE_SRC_PATTERN.matcher(html);
        StringBuffer normalized = new StringBuffer();
        while (matcher.find()) {
            String imageUrl = storeBase64Image(matcher.group(2), matcher.group(3), folder);
            matcher.appendReplacement(normalized,
                    Matcher.quoteReplacement(matcher.group(1) + imageUrl + matcher.group(4)));
        }
        matcher.appendTail(normalized);
        return normalized.toString();
    }

    public Path getUploadRoot() {
        return uploadRoot;
    }

    private String storeBase64Image(String extension, String base64Data, String folder) {
        String normalizedExtension = normalizeExtension(extension);
        if (!ALLOWED_EXTENSIONS.contains(normalizedExtension)) {
            throw new IllegalArgumentException("Unsupported image type.");
        }

        byte[] imageBytes;
        try {
            imageBytes = Base64.getDecoder().decode(base64Data.replaceAll("\\s", ""));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid embedded image data.", ex);
        }
        if (imageBytes.length > MAX_DATA_IMAGE_BYTES) {
            throw new IllegalArgumentException("Embedded image is too large.");
        }

        return storeImageBytes(imageBytes, normalizedExtension, folder);
    }

    private String storeImageBytes(byte[] imageBytes, String extension, String folder) {
        try {
            Path uploadFolder = resolveUploadFolder(folder);
            Files.createDirectories(uploadFolder);
            String filename = UUID.randomUUID().toString() + "." + extension;
            Path target = uploadFolder.resolve(filename).normalize();
            if (!target.startsWith(uploadRoot)) {
                throw new IllegalArgumentException("Invalid upload path.");
            }
            Files.write(target, imageBytes);
            return toPublicUrl(target);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot save embedded image.", e);
        }
    }

    private String normalizeExtension(String extension) {
        String value = extension == null ? "" : extension.toLowerCase(Locale.ROOT);
        return "jpeg".equals(value) ? "jpg" : value;
    }

    private Path resolveUploadFolder(String folder) {
        if (!StringUtils.hasText(folder)) {
            return uploadRoot;
        }

        Path target = uploadRoot;
        String normalizedFolder = folder.replace('\\', '/');
        for (String segment : normalizedFolder.split("/")) {
            String safeSegment = SlugUtils.toSlug(segment);
            if (StringUtils.hasText(safeSegment)) {
                target = target.resolve(safeSegment);
            }
        }

        Path normalized = target.normalize();
        if (!normalized.startsWith(uploadRoot)) {
            throw new IllegalArgumentException("Invalid upload folder.");
        }
        return normalized;
    }

    private String toPublicUrl(Path target) {
        String relativePath = uploadRoot.relativize(target).toString().replace('\\', '/');
        return "/uploads/" + relativePath;
    }
}
