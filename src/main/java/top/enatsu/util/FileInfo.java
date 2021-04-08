package top.enatsu.util;

import java.io.Serializable;

public interface FileInfo extends Serializable {
    String getName();

    String getPath();

    byte[] getContent();

    void setInfo(String name, String path, byte[] content);

}
