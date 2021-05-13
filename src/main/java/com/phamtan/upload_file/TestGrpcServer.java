package com.phamtan.upload_file;

import com.example.grpc_base.proto.GreeterGrpc;
import com.example.grpc_base.proto.Hello;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class TestGrpcServer extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(Hello.HelloRequest request, StreamObserver<Hello.HelloReply> responseObserver) {
       responseObserver.onNext(Hello.HelloReply.newBuilder()
               .setMessage("This is test message for grpc service")
               .build());
       responseObserver.onCompleted();
    }
}
