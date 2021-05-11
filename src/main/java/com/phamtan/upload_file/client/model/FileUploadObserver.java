package com.phamtan.upload_file.client.model;

import com.example.grpc_base.proto.FileOuterClass;
import io.grpc.stub.StreamObserver;

import java.time.Duration;

public class FileUploadObserver implements StreamObserver<FileOuterClass.FileUploadResponse> {

    FileOuterClass.FileUploadResponse fileUploadResponse;
    @Override
    public void onNext(FileOuterClass.FileUploadResponse value) {
        fileUploadResponse = value;
        System.out.println(
                "File upload status :: " + value.getStatus()
        );
    }

    @Override
    public void onError(Throwable t) {
        System.out.println(t.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Sending on onCompleted");
    }
}
