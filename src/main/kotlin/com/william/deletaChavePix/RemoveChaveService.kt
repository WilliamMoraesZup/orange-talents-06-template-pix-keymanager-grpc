package com.william.deletaChavePix

import com.william.EmptyReturn
import com.william.adicionaEremoveNoBcb.BancoCentralClient
import com.william.adicionaEremoveNoBcb.entidades.DeletePixKeyRequest
import com.william.adicionaEremoveNoBcb.entidades.IsbbCodigo
import com.william.novaChavePix.ChavePixRepository
import com.william.novaChavePix.ItauClient
import com.william.shared.ErroCustomizado
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
        val consultaUsuario = clientItau.consultaUsuario(requestDTO.clienteId!!)
        if (consultaUsuario.code() == 404) {
            LOGGER.warn("ClienteId nao encontrado")
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("Cliente nao encontrado")
                    .asRuntimeException()
            )
        }



        if (repository.existsByValorChaveAndIdCliente(requestDTO.chavePix, requestDTO.clienteId)) {

            LOGGER.info("[REMOVE SERVICE] dados encontrados no banco.. buscando ByID")
            val clienteBuscado = repository.findByIdCliente(requestDTO.clienteId)


            LOGGER.info("[REMOVE SERVICE] Encontrado ${clienteBuscado.get()}")

            val isbp = IsbbCodigo(clienteBuscado.get().conta.instituicao).isbp
            println(isbp)
            val chaveDeletada = clientBcb.removeChavePix(
                key = clienteBuscado.get().valorChave,
                body = DeletePixKeyRequest(clienteBuscado.get().valorChave, isbp)
            )

            LOGGER.info("[REMOVE SERVICE] Resposta do Cliente BCB: ${chaveDeletada.status()}")

            if (chaveDeletada.status.equals(HttpStatus.NOT_FOUND)) {
                throw ErroCustomizado("Chave nao encontrada no BCB")
            }

            if (chaveDeletada.status.equals(HttpStatus.OK)) {
                repository.delete(clienteBuscado.get())
                LOGGER.info("deletado com sucesso")
            }


            responseObserver.onNext(EmptyReturn.newBuilder().build())

        }
    }

}
