package com.william.exceptions

import io.grpc.BindableService
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.http.client.exceptions.HttpClientException
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
class ExceptionInterceptor : MethodInterceptor<BindableService, Any?> {
    override fun intercept(context: MethodInvocationContext<BindableService, Any?>?): Any? {

        //context traz o contexto do metodo chamado, no endpoint: void registra(CadastraChavePixRequest request,StreamObserver responseObserver)
        val responseObserver = context!!.parameterValues[1] as StreamObserver<*>

        try {
            return context?.proceed()
        } catch (e: HttpClientException) {
            return responseObserver.onError(
                Status.INTERNAL
                    .withDescription(
                        "Houve um erro ao conectar no sistema externo"
                    )
                    .asRuntimeException()
            )

        } catch (e: ParametrosInvalidos) {

            return responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription(e.message)
                    .asRuntimeException()
            )
        } catch (e: ChaveJaExisteSistema) {

            return responseObserver.onError(
                Status.ALREADY_EXISTS
                    .withDescription("Essa chave já está cadastrada")
                    .asRuntimeException()
            )

        } catch (e: ConstraintViolationException) {

            return responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription(e.message)
                    .asRuntimeException()
            )

        }
    }

}