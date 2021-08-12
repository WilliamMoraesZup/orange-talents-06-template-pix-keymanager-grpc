package com.william.deletaChavePix

import com.william.EmptyReturn
import com.william.novaChavePix.ChavePixRepository
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid


@Singleton
@Validated
class RemoveChaveService(
    val repository: ChavePixRepository,
    val clienteRemocao: ItauClienteRemocao
) {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun removeChavePix(
        @Valid requestDTO: RemoveChaveRequestDTO,
        responseObserver: StreamObserver<EmptyReturn>
    ) {
        LOGGER.info("[RemoveChaveService] Passei da validação")

        if (!repository.existsById(requestDTO.chavePix!!.toLong())) {
            LOGGER.info("Nao achei PIX ID")
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("Chave Pix nao encontrada")
                    .asRuntimeException()
            )
        }
        LOGGER.info("Passei do chave pix nao encontrada")

        if (clienteRemocao.consultaUsuario(requestDTO.clienteId!!).code() == 404) {
         responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("Cliente nao encontrado")
                    .asRuntimeException()
            )
        }
        LOGGER.info("Passei do CLIENTE nao encontrada")
        println(repository.existsByIdAndIdCliente(requestDTO.chavePix.toLong(), requestDTO.clienteId))
        if (repository.existsByIdAndIdCliente(requestDTO.chavePix.toLong(), requestDTO.clienteId)) {
            repository.deleteById(requestDTO.chavePix.toLong())
            LOGGER.info("deletado com sucesso")
        }
    }

}
