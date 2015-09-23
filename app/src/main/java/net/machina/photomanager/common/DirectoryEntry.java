package net.machina.photomanager.common;

public class DirectoryEntry {
    private String name;
    private String path;
    private int iconResId;

    public DirectoryEntry(int iconResId, String name, String path) {
        this.iconResId = iconResId;
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
