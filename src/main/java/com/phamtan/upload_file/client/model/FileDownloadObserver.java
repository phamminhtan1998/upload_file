package com.phamtan.upload_file.client.model;

import com.example.grpc_base.proto.FileOuterClass;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileDownloadObserver
        implements StreamObserver<FileOuterClass.FileDownloadResponse> {
    OutputStream writer;
    private  FileDownloadModel fileDownloadModel;

    public FileDownloadObserver(FileDownloadModel fileDownloadModel) {
        this.fileDownloadModel = fileDownloadModel;
        try {
            writer = getFilePath(fileDownloadModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(FileOuterClass.FileDownloadResponse value) {
        System.out.println("Downloading ------->");
        writeFile(writer,value.getFile().getContent());
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("Download File Error !");
        t.printStackTrace();
        closeFile(writer);
    }

    @Override
    public void onCompleted() {
        closeFile(writer);
        System.out.println("Received File Completed !");
    }
    private OutputStream getFilePath(FileDownloadModel fileDownloadModel) throws IOException {
        String filename = fileDownloadModel.getName()+"."+fileDownloadModel.getType();
        Path destinationPath = Paths.get(fileDownloadModel.getDestinationPath());
        return Files.newOutputStream(destinationPath.resolve(filename), StandardOpenOption.CREATE,StandardOpenOption.APPEND);

    }
    private void writeFile(OutputStream writer, ByteString content)  {
        try {
            writer.write(content.toByteArray());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void closeFile(OutputStream writer){
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
