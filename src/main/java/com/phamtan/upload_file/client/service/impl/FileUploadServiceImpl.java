package com.phamtan.upload_file.client.service.impl;

import com.example.grpc_base.proto.FileOuterClass;
import com.example.grpc_base.proto.FileServiceGrpc;
import com.google.protobuf.ByteString;
import com.phamtan.upload_file.client.config.FileUploadConfig;
import com.phamtan.upload_file.client.model.FileUploadModel;
import com.phamtan.upload_file.client.model.FileUploadObserver;
import com.phamtan.upload_file.client.service.FileUploadService;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
@ConditionalOnBean(FileUploadConfig.class)
public class FileUploadServiceImpl implements FileUploadService {
    private final ThreadPoolExecutor threadPoolExecutor;
    private final FileUploadConfig fileUploadConfig;
    private ManagedChannel channel ;
    private FileServiceGrpc.FileServiceStub stub ;

    public FileUploadServiceImpl(@Qualifier("custom-executor-bean") ThreadPoolExecutor threadPoolExecutor,
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
    public void uploadMultipartFile(List<FileUploadModel> files) throws IOException, InterruptedException {
        for (FileUploadModel file : files){
            uploadFile(file);
        }
    }
    private void uploadFile(FileUploadModel file) throws IOException, InterruptedException{
        String nameFile ;
        String typeFile ;
        if(file.getName()==null||file.getName().trim().isEmpty()){
            String[] listString = file.getPath().split(Pattern.quote("/"));
            String[] nameAndTypeFile = listString[listString.length-1].split(Pattern.quote("."));
             nameFile = listString[listString.length-1]
                    .replace("."+nameAndTypeFile[nameAndTypeFile.length-1],"");
             typeFile = nameAndTypeFile[nameAndTypeFile.length-1];
        }else {
            nameFile=file.getName();
            typeFile= file.getType();
        }

        StreamObserver<FileOuterClass.FileUploadRequest> streamObserver = stub.upload(new FileUploadObserver());
        Path path = Paths.get(file.getPath());
        final ClientCallStreamObserver<FileOuterClass.FileUploadRequest> clientCallStreamObserver = (ClientCallStreamObserver<FileOuterClass.FileUploadRequest>) streamObserver;

        FileOuterClass.FileUploadRequest metadata = FileOuterClass.FileUploadRequest.newBuilder()
                .setMetadata(FileOuterClass.MetaData.newBuilder()
                        .setName(nameFile)
                        .setType(typeFile).build()).build();
        streamObserver.onNext(metadata);
        int chunkSize = 1024 * 1024 * 10;
        InputStream inputStream = null;
        try  {
            byte[] bytes;
            File fileSource = new File(file.getPath());
            inputStream = new BufferedInputStream(new FileInputStream(fileSource));
/**
 *  Create for size of message per request
 *  If size of file less than chunk size , size of message equal size of file
 *
 */
            if(fileSource.length()<chunkSize){
                bytes = new byte[(int) fileSource.length()];
            }
            else {
                bytes = new byte[chunkSize];
            }

            while (true) {
                if (inputStream.available() != 0) {
                    if(inputStream.available()<=chunkSize){
                        chunkSize=inputStream.available();
                    }
                    inputStream.mark(chunkSize);
                    inputStream.read(bytes);
                    if (clientCallStreamObserver.isReady()) {
                        FileOuterClass.FileUploadRequest uploadRequest = FileOuterClass.FileUploadRequest.newBuilder()
                                .setFile(FileOuterClass.File.newBuilder()
                                        .setContent(ByteString.copyFrom(bytes, 0,chunkSize))
                                        .build()).build();
                        streamObserver.onNext(uploadRequest);
                    } else {
                        inputStream.reset();
                    }
                } else {
                    inputStream.close();
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        streamObserver.onCompleted();
    }

}


