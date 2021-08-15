package com.william.validacoes

import com.william.shared.ErroCustomizado
import io.micronaut.context.annotation.Requires
import io.micronaut.core.exceptions.ExceptionHandler
import io.micronaut.http.annotation.Produces
import javax.inject.Singleton

@Produces
@Singleton
@Requires(classes = [ErroCustomizado::class, ExceptionHandler::class])
class OutOfStockExceptionHandler : ExceptionHandler<ErroCustomizado> {
    override fun handle(exception: ErroCustomizado?) {
        println(exception!!.message)
        println("capturei o erro")


    }


}