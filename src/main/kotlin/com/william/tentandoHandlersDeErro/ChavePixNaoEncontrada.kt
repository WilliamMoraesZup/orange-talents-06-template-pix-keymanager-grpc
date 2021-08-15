package com.william.tentandoHandlersDeErro

import com.william.shared.ErroCustomizado
import io.grpc.Status
import io.grpc.StatusRuntimeException
import javax.inject.Singleton

@Singleton
class ChavePixNaoEncontrada  : ExceptionHandler<ErroCustomizado> {

    override fun handle(e: ErroCustomizado): StatusWithDetails {
        println("chegieo")
        return StatusWithDetails(
            Status.NOT_FOUND
            .withDescription(e.message)
            .withCause(e))
    }

    override fun supports(e: Exception): Boolean {println("123123")
        return e is ErroCustomizado
    }
}