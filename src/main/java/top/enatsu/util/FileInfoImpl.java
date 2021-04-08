package top.enatsu.util;

public class FileInfoImpl implements FileInfo {
    String name = null;

    String path = null;

    byte[] content = null;

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public byte[] getContent() {
        return content;
    }

    public void setInfo(String name, String path, byte[] content) {
        this.name = name;
        this.path = path;
        this.content = content;
    }
}
