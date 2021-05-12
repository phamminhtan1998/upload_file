package com.phamtan.upload_file;

import com.phamtan.upload_file.client.model.FileDownloadModel;
import com.phamtan.upload_file.client.model.FileUploadModel;
import com.phamtan.upload_file.client.service.FileDownloadService;
import com.phamtan.upload_file.client.service.FileUploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class TestController {
    private final FileUploadService fileUploadService;
    private final FileDownloadService fileDownloadService;

    public TestController(FileUploadService fileUploadService,
                          FileDownloadService fileDownloadService) {
        this.fileUploadService = fileUploadService;
        this.fileDownloadService = fileDownloadService;
    }
    @PostMapping("/uploadFile")
    public String testUploadMutiple(@RequestBody List<FileUploadModel> files) throws IOException, InterruptedException {
        fileUploadService.uploadMultipartFile(files);
        return "Upload File";
    }
    @PostMapping("/downloadFile")
    public String testDownloadMultiple(@RequestBody FileDownloadModel file){
        fileDownloadService.download(file);
        return "Download File";
    }
}
