package com.william.novaChavePix

import com.william.CadastraChavePixRequest
import com.william.CadastraChavePixResponse
import com.william.ChavePixServiceRegistraGrpc
import com.william.exceptions.ErrorHandler
import com.william.novaChavePix.entidades.NovaChavePixRequest
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ErrorHandler
class NovaChavePixEndPoint(
    @Inject private val service: NovaChavePixService
) : ChavePixServiceRegistraGrpc.ChavePixServiceRegistraImplBase() {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun registra(
        request: CadastraChavePixRequest,
        responseObserver: StreamObserver<CadastraChavePixResponse>
    ) {
        LOGGER.info("[ENDPOINT] Passando NovaChavePixRequest para toModel")
        val novaChave: NovaChavePixRequest = request.toModel()

        LOGGER.info("[ENDPOINT] Sucesso, chamando service para salvar chave pix")
        val chavePixSalvaNoBanco = service.registraChavePix(novaChave, responseObserver)

        LOGGER.info("[ENDPOINT] Sucesso, chamando retorno do OnNext")

        responseObserver.onNext(
            CadastraChavePixResponse.newBuilder()
                .setPixId(chavePixSalvaNoBanco.valorChave)
                .setClienteId(chavePixSalvaNoBanco.idCliente).build()
        )
        responseObserver.onCompleted()


        LOGGER.info("[ENDPOINT] FINAL]")


    }
}