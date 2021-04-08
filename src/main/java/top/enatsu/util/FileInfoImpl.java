package top.enatsu.util;

public class FileInfoImpl implements FileInfo {
    String name = null;

    String path = null;

    Boolean directory=false;

    Long size = 0L;

    byte[] content = null;

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Boolean isDirectory() { return directory;}

    public Long getSize() { return size; }

    public byte[] getContent() {
        return content;
    }

    public void setInfo(String name, String path, byte[] content) {
        this.name = name;
        this.path = path;
        this.content = content;
    }

    public void setInfo(String name, String path, Boolean directory, Long size) {
        this.name = name;
        this.path = path;
        this.directory = directory;
        this.size = size;
    }
}
