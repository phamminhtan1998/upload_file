package com.phamtan.upload_file.client.service.impl;

import com.example.grpc_base.proto.FileOuterClass;
import com.example.grpc_base.proto.FileServiceGrpc;
import com.phamtan.upload_file.client.config.FileUploadConfig;
import com.phamtan.upload_file.client.model.FileDownloadModel;
import com.phamtan.upload_file.client.model.FileDownloadObserver;
import com.phamtan.upload_file.client.service.FileDownloadService;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.StatusException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class FileDownloadServiceImpl implements FileDownloadService {
    private ManagedChannel channel ;
    private FileServiceGrpc.FileServiceStub stub ;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final FileUploadConfig fileUploadConfig;

    public FileDownloadServiceImpl(@Qualifier("custom-executor-bean") ThreadPoolExecutor threadPoolExecutor,
                                   FileUploadConfig fileUploadConfig) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.fileUploadConfig = fileUploadConfig;
    }

    @PostConstruct
    private void init(){
        channel = NettyChannelBuilder.forAddress(fileUploadConfig.getDomainServer(),fileUploadConfig.getPortServer())
                .maxInboundMessageSize(fileUploadConfig.getMaxSizePerMessage())
                .keepAliveTimeout(3, TimeUnit.MINUTES)
                .usePlaintext()
                .build();
        Deadline deadline = Deadline.after(fileUploadConfig.getDeadline(),TimeUnit.SECONDS);
        stub = FileServiceGrpc.newStub(channel)
                .withExecutor(threadPoolExecutor)
                .withDeadline(deadline);
    }
    @Override
    public void download(FileDownloadModel fileDownloadModel) {
        FileOuterClass.FileDownloadRequest fileDownloadRequest = FileOuterClass.FileDownloadRequest.newBuilder()
                .setName("test.png")
                .setPath("Test pass server passing whatever")
                .build();
            stub.download(fileDownloadRequest,new FileDownloadObserver(fileDownloadModel));
    }

}
