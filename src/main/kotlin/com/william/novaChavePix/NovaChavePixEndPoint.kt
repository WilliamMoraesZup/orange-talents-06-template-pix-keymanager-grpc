package com.william.novaChavePix

import com.william.CadastraChavePixRequest
import com.william.CadastraChavePixResponse
import com.william.ChavePixServiceGrpc
import com.william.novaChavePix.classes.NovaChavePixRequest
import com.william.shared.ErroCustomizado
import io.grpc.Status
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.ConstraintViolationException


@Singleton
class NovaChavePixEndPoint(
    @Inject private val service: NovaChavePixService
) : ChavePixServiceGrpc.ChavePixServiceImplBase() {

    override fun registra(
        request: CadastraChavePixRequest,
        responseObserver: StreamObserver<CadastraChavePixResponse>
    ) {

        try {
            val novaChave: NovaChavePixRequest = request.toModel()
            val chavePixSalvaNoBanco = service.registraChavePix(novaChave, responseObserver)

            responseObserver.onNext(
                CadastraChavePixResponse.newBuilder()
                    .setPixId(chavePixSalvaNoBanco.id.toString())
                    .setClienteId(chavePixSalvaNoBanco.idCliente).build()
            )


        } catch (erro: ErroCustomizado) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription(erro.message)
                    .asRuntimeException()
            )


        } catch (erro: ConstraintViolationException) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription(erro.message)
                    .asRuntimeException()
            )
        }

    }
}