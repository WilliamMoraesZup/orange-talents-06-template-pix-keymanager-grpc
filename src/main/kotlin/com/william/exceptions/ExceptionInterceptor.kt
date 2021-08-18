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


        val responseObserver =
            if (context!!.parameterValues[1] is StreamObserver<*>) context!!.parameterValues[1] as StreamObserver<*>
            else context!!.parameterValues[0] as StreamObserver<*>

        try {
            return context.proceed()
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
        } catch (e: StatusAlreadyExists) {

            return responseObserver.onError(
                Status.ALREADY_EXISTS
                    .withDescription(e.message)
                    .asRuntimeException()
            )
        } catch (e: StatusNotFound) {
            return responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription(e.message)
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