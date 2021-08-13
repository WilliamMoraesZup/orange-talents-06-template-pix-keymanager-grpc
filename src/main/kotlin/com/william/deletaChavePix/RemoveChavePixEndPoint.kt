package com.william.deletaChavePix

import com.william.ChavePixServiceRemoveGrpc
import com.william.EmptyReturn
import com.william.RemoveChavePixRequest
import com.william.novaChavePix.toModel
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoveChavePixEndPoint(
    @Inject private val service: RemoveChaveService
) : ChavePixServiceRemoveGrpc.ChavePixServiceRemoveImplBase(

) {


    override fun remove(request: RemoveChavePixRequest, responseObserver: StreamObserver<EmptyReturn>) {
        println("[RemoveChavePixEndPoint] Chamando on completed")
        val removeChavePix = service.removeChavePix(request.toModel(), responseObserver)


        println("Chamando on completed")
        responseObserver.onCompleted()


    }


}