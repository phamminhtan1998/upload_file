package com.phamtan.upload_file.server.exception;


import io.grpc.Status;
import io.grpc.StatusException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ExceptionHandler {
    @GrpcExceptionHandler
    public StatusException outOfMemory(OutOfMemoryError error){
        return Status.INTERNAL
                .withDescription("Too many request/ response to handle")
                .withCause(error).asException();
    }
    @GrpcExceptionHandler(CustomException.class)
    public StatusException customError(CustomException customException){
        return Status.UNKNOWN
                .withCause(customException.getCause())
                .withDescription(customException.getMessage())
                .asException();
    }

}
