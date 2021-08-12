package com.william.shared

import io.grpc.Status
import javax.validation.ConstraintViolationException

//NAO ESTA FUNCIONANDO

//@GrpcAdvice
class ExceptionHandler {
    // @GrpcExceptionHandler(ConstraintViolationException::class)
    fun handleResourceNotFoundException(e: ConstraintViolationException): Status {


        println("erro capturado")

//        return StatusProto.toStatusRuntimeException(Status.newBuilder().build())
        return Status.fromCode(Status.Code.INVALID_ARGUMENT)
            .withDescription("Email or password malformed")
    }
}


