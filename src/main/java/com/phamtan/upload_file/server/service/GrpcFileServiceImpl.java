package com.phamtan.upload_file.server.service;
import com.example.grpc_base.proto.FileOuterClass;
import com.example.grpc_base.proto.FileServiceGrpc;
import com.google.protobuf.ByteString;
import com.phamtan.upload_file.server.config.ServerConfigProperties;
import com.phamtan.upload_file.server.exception.CustomException;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@GrpcService
@Component
@ConditionalOnBean(ServerConfigProperties.class)
@Slf4j
public class GrpcFileServiceImpl extends FileServiceGrpc.FileServiceImplBase {

   private final ServerConfigProperties serverConfigProperties ;



    public GrpcFileServiceImpl(ServerConfigProperties serverConfigProperties) {
        this.serverConfigProperties = serverConfigProperties;
    }
    private Path SERVER_BASE_PATH;
    @PostConstruct
    public void init(){
        SERVER_BASE_PATH = Paths.get(serverConfigProperties.getBasePath());
    }

    @Override
    public void download(FileOuterClass.FileDownloadRequest request,
                         StreamObserver<FileOuterClass.FileDownloadResponse> responseObserver) {
        int chunkSize = 1024 * 1024 * 10;
        InputStream inputStream = null;
        try {
            byte[] bytes;
            File fileSource = new File("/home/allinone/Desktop/test.zip");
            inputStream = new BufferedInputStream(new FileInputStream(fileSource));
            ServerCallStreamObserver<FileOuterClass.FileDownloadResponse> serverCallStreamObserver = (ServerCallStreamObserver<FileOuterClass.FileDownloadResponse>) responseObserver;
/**
 *  Create for size of message per request
 *  If size of file less than chunk size , size of message equal size of file
 *
 */
            if (fileSource.length() < chunkSize) {
                bytes = new byte[(int) fileSource.length()];
            } else {
                bytes = new byte[chunkSize];
            }
            while (true) {
                if (inputStream.available() != 0) {
                    if (inputStream.available() <= chunkSize) {
                        chunkSize = inputStream.available();
                    } else {
                        bytes = new byte[chunkSize];
                    }
                    inputStream.mark(chunkSize);
                    inputStream.read(bytes);
                    if (serverCallStreamObserver.isReady()) {
                        FileOuterClass.FileDownloadResponse response = FileOuterClass.FileDownloadResponse.newBuilder()
                                .setFile(FileOuterClass.File.newBuilder()
                                        .setContent(ByteString.copyFrom(bytes, 0, chunkSize))
                                        .build())
                                .build();
                        responseObserver.onNext(response);
                    } else {
                        inputStream.reset();
                    }
                }else {
                    inputStream.close();
                    responseObserver.onCompleted();
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            log.error("File not found !");
           throw  new CustomException(
                    "FileNotFoundException",
                    "File Not Existing on Server",
                    e
            );
        } catch (IOException e) {
            log.error("I/O File error :"+e.getMessage());
            throw  new CustomException(
                    "IOException",
                    "File Not Existing on Server",
                    e
            );
        }
    }
        @Override
    public StreamObserver<FileOuterClass.FileUploadRequest> upload(StreamObserver<FileOuterClass.FileUploadResponse> responseObserver) {
        return new StreamObserver<FileOuterClass.FileUploadRequest>() {
            OutputStream writer;
            FileOuterClass.Status status = FileOuterClass.Status.IN_PROGRESS;

            @Override
            public void onNext(FileOuterClass.FileUploadRequest  fileUploadRequest) {
                try {
                    System.out.println("File upload request !");
                    if(fileUploadRequest.hasMetadata()){
                        writer = getFilePath(fileUploadRequest);
                    }else{
                        writeFile(writer, fileUploadRequest.getFile().getContent());
                    }
                }catch (Exception ex){
                    this.onError(ex);
                }
            }

            @Override
            public void onError(Throwable t) {
                status= FileOuterClass.Status.FAILED;
                this.onCompleted();
            }

            @Override
            public void onCompleted() {
                closeFile(writer);
                status = FileOuterClass.Status.IN_PROGRESS.equals(status) ? FileOuterClass.Status.SUCCESS : status;
                FileOuterClass.FileUploadResponse response = FileOuterClass.FileUploadResponse.newBuilder()
                        .setStatus(status)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }
    private OutputStream getFilePath(FileOuterClass.FileUploadRequest request) throws IOException {
        String fileName = request.getMetadata().getName() + "." + request.getMetadata().getType();
        OutputStream outputStream =  Files.newOutputStream(SERVER_BASE_PATH.resolve(fileName), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        return  outputStream;
    }

    private void writeFile(OutputStream writer, ByteString content) throws IOException {
        writer.write(content.toByteArray());
        writer.flush();
    }

    private void closeFile(OutputStream writer){
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

