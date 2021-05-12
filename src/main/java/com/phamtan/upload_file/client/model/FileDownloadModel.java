package com.phamtan.upload_file.client.model;

public class FileDownloadModel {
    private String name;
    private String path;
    private String destinationPath;
    private String type;

    public FileDownloadModel() {
    }

    public FileDownloadModel(String name, String path, String destinationPath, String type) {
        this.name = name;
        this.path = path;
        this.destinationPath = destinationPath;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
