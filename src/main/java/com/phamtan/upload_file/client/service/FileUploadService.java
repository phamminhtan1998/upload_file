package com.phamtan.upload_file.client.service;

import com.phamtan.upload_file.client.model.FileUploadModel;

import java.io.IOException;
import java.util.List;

public interface FileUploadService {
    public void uploadMultipartFile(List<FileUploadModel> files) throws IOException, InterruptedException;
}
