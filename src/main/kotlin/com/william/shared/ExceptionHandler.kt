package com.william.shared

import io.grpc.Status
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler

//NAO ESTA FUNCIONANDO

@GrpcAdvice
class ExceptionHandler {
    @GrpcExceptionHandler(ErroCustomizado::class)
    fun handleResourceNotFoundException(e: ErroCustomizado): StatusRuntimeException {


        println("erro capturado")
        println(e.message)

        //val errorMetaData: Unit = cause.getErrorMetaData()
//        val errorInfo: Unit = ErrorInfo.newBuilder()
//            .setReason("Resource not found")
//            .setDomain("Product")
//            .putAllMetadata(errorMetaData)
//            .build()
//        val status: Status = Status.newBuilder()
//            .setCode(Code.NOT_FOUND.number)
//            .setMessage("Resource not found")
//            .addDetails(Any.pack(errorInfo))
//            .build()
//        return StatusProto.toStatusRuntimeException(Status.newBuilder().build())
        return Status.NOT_FOUND.asRuntimeException()
    }
}