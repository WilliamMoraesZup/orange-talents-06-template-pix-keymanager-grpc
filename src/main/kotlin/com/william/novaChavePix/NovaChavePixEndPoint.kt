package com.william.novaChavePix

import com.william.CadastraChavePixRequest
import com.william.CadastraChavePixResponse
import com.william.ChavePixServiceGrpc
import com.william.novaChavePix.classes.NovaChavePixRequest
import com.william.shared.ErrorHandler
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@ErrorHandler
@Singleton
class NovaChavePixEndPoint(
    val service: NovaChavePixService
) : ChavePixServiceGrpc.ChavePixServiceImplBase() {

    override fun registra(
        request: CadastraChavePixRequest,
        responseObserver: StreamObserver<CadastraChavePixResponse>
    ) {

        val novaChave: NovaChavePixRequest = request.toModel();


        val chavePixSalvaNoBanco = service.registraChavePix(novaChave)


        responseObserver.onNext(
            CadastraChavePixResponse.newBuilder()
                .setPixId(chavePixSalvaNoBanco.id.toString())
                .setClienteId(novaChave.idCliente.toString()).build()
        )
        responseObserver.onCompleted()
    }


}