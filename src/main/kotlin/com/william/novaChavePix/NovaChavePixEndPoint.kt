package com.william.novaChavePix

import com.william.CadastraChavePixRequest
import com.william.CadastraChavePixResponse
import com.william.ChavePixServiceRegistraGrpc
import com.william.novaChavePix.entidades.NovaChavePixRequest
import com.william.shared.ErroCustomizado
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.http.client.exceptions.HttpClientException
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
class NovaChavePixEndPoint(
    @Inject private val service: NovaChavePixService
) : ChavePixServiceRegistraGrpc.ChavePixServiceRegistraImplBase() {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun registra(
        request: CadastraChavePixRequest,
        responseObserver: StreamObserver<CadastraChavePixResponse>
    ) {


        try {
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
            LOGGER.info("[ENDPOINT] onCompleted")


        } catch (erro: ErroCustomizado) {
            LOGGER.warn("[ENDPOINT] Putz, caiu no erro customizado, Status.INVALID_ARGUMENT")
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription(erro.message)
                    .asRuntimeException()
            )
        } catch (erro: ConstraintViolationException) {
            LOGGER.warn("[ENDPOINT] Putz, caiu no ConstraintViolationException, Status.INVALID_ARGUMENT")
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription(erro.message)
                    .asRuntimeException()
            )
        } catch (erro: HttpClientException) {
            LOGGER.warn("[ENDPOINT] Putz, parece que o sistema está offline")
            responseObserver.onError(
                Status.UNAVAILABLE
                    .withDescription("Parece que o sistema está offline")
                    .asRuntimeException()
            )
        }

        LOGGER.info("[ENDPOINT] FINAL]")

    }
}