package top.enatsu.util;

import java.io.Serializable;

public interface FileInfo extends Serializable {
    String getName();

    String getPath();

    Boolean isDirectory();

    Long getSize();

    byte[] getContent();

    void setInfo(String name, String path, byte[] content);

    void setInfo(String name, String path, Boolean directory, Long size);
}
