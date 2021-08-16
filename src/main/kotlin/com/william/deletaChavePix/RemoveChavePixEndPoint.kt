package com.william.deletaChavePix

import com.william.ChavePixServiceRemoveGrpc
import com.william.EmptyReturn
import com.william.RemoveChavePixRequest
import com.william.exceptions.ErrorHandler
import com.william.novaChavePix.toModel
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ErrorHandler
class RemoveChavePixEndPoint(
    @Inject private val service: RemoveChaveService
) : ChavePixServiceRemoveGrpc.ChavePixServiceRemoveImplBase() {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun remove(request: RemoveChavePixRequest, responseObserver: StreamObserver<EmptyReturn>) {


        LOGGER.info("[REMOVE_ENDPOINT] Chamando service.removeChavePix(")
        val removeChavePix = service.removeChavePix(request.toModel(), responseObserver)



        responseObserver.onCompleted()
    }

}