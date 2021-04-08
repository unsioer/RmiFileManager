package top.enatsu.util;

import java.io.File;

public class ResponseImpl implements Response {
    Boolean status = false;

    String message = null;

    File[] files = null;

    public Boolean getStatus() {
        return status;
    }

    public File[] getFiles() {
        return files;
    }

    public String getMessage() {
        return message;
    }

    public void setResponse(Boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public void setResponse(Boolean status, File[] files, String message) {
        this.status = status;
        this.files = files;
        this.message = message;
    }

    public ResponseImpl() {

    }

    public ResponseImpl(Boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}
