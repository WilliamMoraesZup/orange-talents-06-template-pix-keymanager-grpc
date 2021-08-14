package com.william.deletaChavePix

import com.william.ChavePixServiceRemoveGrpc
import com.william.EmptyReturn
import com.william.RemoveChavePixRequest
import com.william.novaChavePix.toModel
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.http.client.exceptions.HttpClientException
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoveChavePixEndPoint(
    @Inject private val service: RemoveChaveService
) : ChavePixServiceRemoveGrpc.ChavePixServiceRemoveImplBase() {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun remove(request: RemoveChavePixRequest, responseObserver: StreamObserver<EmptyReturn>) {

        try {
            LOGGER.info("[REMOVE_ENDPOINT] Chamando service.removeChavePix(")
            val removeChavePix = service.removeChavePix(request.toModel(), responseObserver)

        } catch (erro: HttpClientException) {
            LOGGER.warn("[REMOVE_ENDPOINT] Putz, parece que o sistema está offline")
            responseObserver.onError(
                Status.UNAVAILABLE
                    .withDescription("Parece que o sistema está offline")
                    .asRuntimeException()
            )
        }

        responseObserver.onCompleted()
    }

}