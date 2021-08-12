package com.william.novaChavePix

import com.william.CadastraChavePixRequest
import com.william.CadastraChavePixResponse
import com.william.ChavePixServiceGrpc
import com.william.novaChavePix.classes.NovaChavePixRequest
import com.william.shared.ErroCustomizado
import io.grpc.Status
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.ConstraintViolationException


@Singleton
class NovaChavePixEndPoint(
    @Inject private val service: NovaChavePixService
) : ChavePixServiceGrpc.ChavePixServiceImplBase() {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun registra(
        request: CadastraChavePixRequest,
        responseObserver: StreamObserver<CadastraChavePixResponse>
    ) {

        try {
        LOGGER.info("passando NovaChavePixRequest para toModel")
        val novaChave: NovaChavePixRequest = request.toModel()

        LOGGER.info("Sucesso, chamando service para salvar chave pix")
        val chavePixSalvaNoBanco = service.registraChavePix(novaChave, responseObserver)

        LOGGER.info("Sucesso, chamando retorno do OnNext")
        responseObserver.onNext(
        CadastraChavePixResponse.newBuilder()
        .setPixId(chavePixSalvaNoBanco.id.toString())
        .setClienteId(chavePixSalvaNoBanco.idCliente).build()
        )
        responseObserver.onCompleted()
        LOGGER.info("onCompleted")
        }

        catch (erro: ErroCustomizado) {
        LOGGER.info("Putz, caiu no erro customizado, Status.INVALID_ARGUMENT")
        responseObserver.onError(
        Status.INVALID_ARGUMENT
        .withDescription(erro.message)
        .asRuntimeException()
        )

        LOGGER.info("Sobrevivi pelo Catch ERRO CUSTOMIZADO")
        }

        catch (erro: ConstraintViolationException) {
            LOGGER.info("Putz, caiu no ConstraintViolationException, Status.INVALID_ARGUMENT")
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription(erro.message)
                    .asRuntimeException()
            )
            LOGGER.info("Chamei INVALID")
        }

        LOGGER.info("PASSEI dos CATCHS ERRO e CONSTRAINT")

    }
}