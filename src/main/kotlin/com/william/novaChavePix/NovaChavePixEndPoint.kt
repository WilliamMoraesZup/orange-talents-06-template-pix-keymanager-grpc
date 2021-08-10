package com.william.novaChavePix

import com.william.CadastraChavePixRequest
import com.william.CadastraChavePixResponse
import com.william.ChavePixServiceGrpc
import com.william.novaChavePix.classes.NovaChavePixRequest
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Valid

//@ErrorHandler
@Singleton
class NovaChavePixEndPoint(
    @Inject private val service: NovaChavePixService
) : ChavePixServiceGrpc.ChavePixServiceImplBase() {

    override fun registra(
    request: CadastraChavePixRequest,
        responseObserver: StreamObserver<CadastraChavePixResponse>
    ) {

        val novaChave: NovaChavePixRequest = request.toModel();

        val chavePixSalvaNoBanco = service.registraChavePix(novaChave, responseObserver)

        responseObserver.onNext(
            CadastraChavePixResponse.newBuilder()
                .setPixId(chavePixSalvaNoBanco.id.toString())
                .setClienteId(chavePixSalvaNoBanco.idCliente).build()
        )
        responseObserver.onCompleted()
    }


}