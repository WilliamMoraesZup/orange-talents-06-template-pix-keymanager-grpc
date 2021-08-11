package com.william.novaChavePix

import com.william.CadastraChavePixResponse
import com.william.novaChavePix.classes.ChavePix
import com.william.novaChavePix.classes.NovaChavePixRequest
import com.william.shared.ErroCustomizado
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class NovaChavePixService(
    val client: ItauClient,
    val repository: ChavePixRepository
) {


    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun registraChavePix(
        @Valid novaChavePixRequest: NovaChavePixRequest,
        responseObserver: StreamObserver<CadastraChavePixResponse>
    ): ChavePix {
        LOGGER.info("[SERVICE] Chamando cliente ConsultaConta")

        val respostaConta = client.consultaConta(novaChavePixRequest.idCliente!!, novaChavePixRequest.tipoDaConta!!)

        LOGGER.info("[SERVICE] Cliente chamado $respostaConta")

        if (respostaConta.body() == null) {
            LOGGER.info("[SERVICE] Pelo jeito a resposta veio nula, chamando response OnError")
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("\${erro.clienteNaoExiste}")
                    .asRuntimeException()
            )

            LOGGER.info("[SERVICE] Lançando ErroCustomizado")

            throw ErroCustomizado("\${erro.clienteNaoExiste}")
        }


        if (repository.existsByValorChave(novaChavePixRequest.valorChave!!)) {
            LOGGER.info("[SERVICE] Pelo jeito essa chave já existe, chamando ALREADY_EXISTS")

            responseObserver.onError(
                Status.ALREADY_EXISTS
                    .withDescription("\${erro.valorChaveJaExiste}")
                    .asRuntimeException()
            )

            LOGGER.info("[SERVICE] Lancando ErroCustomizado valorChaveJaExiste")

            throw ErroCustomizado("\${erro.valorChaveJaExiste}")
        }

        LOGGER.info("[SERVICE] Passou pelos IFS, fazendo o conta associada")

        val contaAssociada = respostaConta.body()?.toModel()

        LOGGER.info("[SERVICE] Criando nova chave pix definitiva ")
        val chavePixCriada: ChavePix? = contaAssociada?.let { novaChavePixRequest.toModel(it) }

        repository.save(chavePixCriada)
        LOGGER.info("Chave pix salva no banco com sucesso $chavePixCriada, retornado a chave pro END POINT ")

        return chavePixCriada!!

    }


}
