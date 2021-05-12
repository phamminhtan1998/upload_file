package com.phamtan.upload_file.client.model;

import java.time.LocalDate;

public class FileUploadModel {
    private String name ;
    private String type;
    private String path;
    private LocalDate uploadDate;
    private String description;

    public FileUploadModel() {
    }

    public FileUploadModel(String name, String type, String path, LocalDate uploadDate, String description,
                           String desPath) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.uploadDate = uploadDate;
        this.description = description;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
