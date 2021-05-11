package com.phamtan.upload_file;

import com.phamtan.upload_file.client.model.FileUploadModel;
import com.phamtan.upload_file.client.service.FileUploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
public class TestController {
    private final FileUploadService fileUploadService;

    public TestController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }
    @PostMapping("/uploadFile")
    public String testUploadMutiple(@RequestBody List<FileUploadModel> files) throws IOException, InterruptedException {
        fileUploadService.uploadMultipartFile(files);
        return "Upload File";
    }
}
