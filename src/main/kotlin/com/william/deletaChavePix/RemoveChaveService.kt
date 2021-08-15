package com.william.deletaChavePix

import com.william.EmptyReturn
import com.william.adicionaEremoveNoBcb.BancoCentralClient
import com.william.adicionaEremoveNoBcb.entidades.DeletePixKeyRequest
import com.william.adicionaEremoveNoBcb.entidades.IsbbCodigo
import com.william.novaChavePix.ChavePixRepository
import com.william.novaChavePix.ItauClient
import com.william.exceptions.ErroCustomizado
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid


@Singleton
@Validated
class RemoveChaveService(
    val repository: ChavePixRepository,
    val clientItau: ItauClient,
    val clientBcb: BancoCentralClient
) {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun removeChavePix(
        @Valid requestDTO: RemoveChaveRequestDTO,
        responseObserver: StreamObserver<EmptyReturn>
    ) {
        LOGGER.info("[RemoveChaveService] Passei da validação")

        if (!repository.existsByValorChave(requestDTO.chavePix!!)) {
            LOGGER.warn("PixId nao encontrada")
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("Chave Pix nao encontrada")
                    .asRuntimeException()
            )
        }
        LOGGER.info("[RemoveChaveService] Buscando o cliente no sistema do ITAU")
        val consultaUsuario = clientItau.consultaUsuario(requestDTO.clienteId!!)


        LOGGER.info("[RemoveChaveService] Resultado da consulta no ITAU= ${consultaUsuario.status}")
        if (consultaUsuario.code() == 404) {
            LOGGER.warn("ClienteId nao encontrado")
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("Cliente nao encontrado")
                    .asRuntimeException()
            )
        }

        LOGGER.info("[RemoveChaveService] Verifcando se a chavePix e Cliente ID coincidem no banco local")
        if (repository.existsByValorChaveAndIdCliente(requestDTO.chavePix, requestDTO.clienteId)) {

            //Posso Refatorar para eliminar essa parte
            LOGGER.info("[REMOVE SERVICE] dados encontrados no banco para pegar a instituiçao. ISBP")
            val clienteBuscado = repository.findByIdCliente(requestDTO.clienteId)


            val isbp = IsbbCodigo(clienteBuscado.get().conta.instituicao).isbp
            val chavePix = requestDTO.chavePix
            val deletePixKeyRequest = DeletePixKeyRequest(chavePix, isbp)

            println(deletePixKeyRequest)
            val chaveDeletada = clientBcb.removeChavePix(
                key = chavePix,
                body = deletePixKeyRequest
            )

            LOGGER.info("[REMOVE SERVICE] Resposta do Cliente BCB: ${chaveDeletada.status()}")

            if (chaveDeletada.status.equals(HttpStatus.OK)) {
                repository.delete(clienteBuscado.get())
                LOGGER.info("deletado com sucesso")
            }

            if (chaveDeletada.status.equals(HttpStatus.NOT_FOUND)) {
                LOGGER.warn("[REMOVE_ENDPOINT] Chave nao encontrada no BCB")
                responseObserver.onError(
                    Status.NOT_FOUND
                        .withDescription("Chave nao encontrada no BCB")
                        .asRuntimeException()
                )
                throw ErroCustomizado("Chave nao encontrada no BCB")
            }


            responseObserver.onNext(EmptyReturn.newBuilder().build())

        }
    }

}
