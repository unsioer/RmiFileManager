package top.enatsu.util;

import java.io.File;
import java.io.Serializable;

public interface Response extends Serializable {
    Boolean getStatus();

    File[] getFiles();

    String getMessage();

    void setResponse(Boolean status, File[] files, String message);

    void setResponse(Boolean status, String message);
}
