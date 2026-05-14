package vn.com.ocb.aipdmaservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.ocb.aipdmaservice.model.admin.AdminMediaItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MediaLibraryService {

    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>(
            Arrays.asList("jpg", "jpeg", "png", "webp", "gif", "svg"));

    private final UploadService uploadService;

    public AdminMediaItem getMediaTree() {
        Path root = uploadService.getUploadRoot();
        AdminMediaItem item = new AdminMediaItem();
        item.setName("uploads");
        item.setPath("uploads");
        item.setDirectory(true);
        item.setChildren(readChildren(root, root));
        return item;
    }

    private List<AdminMediaItem> readChildren(Path root, Path folder) {
        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            return java.util.Collections.emptyList();
        }

        try (Stream<Path> stream = Files.list(folder)) {
            return stream
                    .filter(path -> Files.isDirectory(path) || isImage(path))
                    .sorted(Comparator
                            .comparing((Path path) -> !Files.isDirectory(path))
                            .thenComparing(path -> path.getFileName().toString().toLowerCase(Locale.ROOT)))
                    .map(path -> toMediaItem(root, path))
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            return java.util.Collections.emptyList();
        }
    }

    private AdminMediaItem toMediaItem(Path root, Path path) {
        AdminMediaItem item = new AdminMediaItem();
        boolean directory = Files.isDirectory(path);
        item.setName(path.getFileName().toString());
        item.setDirectory(directory);
        item.setImage(!directory && isImage(path));

        String relativePath = root.relativize(path).toString().replace('\\', '/');
        item.setPath(relativePath.isEmpty() ? "uploads" : "uploads/" + relativePath);
        if (item.isImage()) {
            item.setUrl("/uploads/" + relativePath);
            item.setSize(sizeOf(path));
        }
        if (directory) {
            item.setChildren(readChildren(root, path));
        }
        return item;
    }

    private boolean isImage(Path path) {
        String filename = path.getFileName().toString();
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            return false;
        }
        return IMAGE_EXTENSIONS.contains(filename.substring(dotIndex + 1).toLowerCase(Locale.ROOT));
    }

    private long sizeOf(Path path) {
        try {
            return Files.size(path);
        } catch (IOException ex) {
            return 0L;
        }
    }
}
