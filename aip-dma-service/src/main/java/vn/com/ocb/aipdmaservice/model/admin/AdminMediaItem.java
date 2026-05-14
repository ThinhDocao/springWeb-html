package vn.com.ocb.aipdmaservice.model.admin;

import java.util.ArrayList;
import java.util.List;

public class AdminMediaItem {

    private String name;
    private String path;
    private String url;
    private boolean directory;
    private boolean image;
    private long size;
    private List<AdminMediaItem> children = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<AdminMediaItem> getChildren() {
        return children;
    }

    public void setChildren(List<AdminMediaItem> children) {
        this.children = children;
    }
}
