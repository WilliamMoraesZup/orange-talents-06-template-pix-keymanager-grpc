package com.william.tentandoHandlersDeErro

import io.grpc.Status
import javax.inject.Singleton


@Singleton
class DefaultExceptionHandler : ExceptionHandler<Exception> {


    override fun handle(e: Exception): StatusWithDetails {
        val status = when (e) {
            is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(e.message)
            is IllegalStateException -> Status.FAILED_PRECONDITION.withDescription(e.message)
            else -> Status.UNKNOWN
        }

        println("handle")
        return StatusWithDetails(status.withCause(e))
    }

    override fun supports(e: Exception): Boolean {
        println("erro")
        return true
    }

}