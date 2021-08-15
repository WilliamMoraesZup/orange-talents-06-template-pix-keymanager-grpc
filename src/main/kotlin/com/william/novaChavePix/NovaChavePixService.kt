package com.william.novaChavePix

import com.william.CadastraChavePixResponse
import com.william.adicionaEremoveNoBcb.BancoCentralClient
import com.william.adicionaEremoveNoBcb.entidades.CriarChaveBcbRequest
import com.william.novaChavePix.entidades.ChavePix
import com.william.novaChavePix.entidades.NovaChavePixRequest
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
class NovaChavePixService(
    val itauClient: ItauClient,
    val bancoCentralClient: BancoCentralClient,
    val repository: ChavePixRepository
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun registraChavePix(
        @Valid novaChavePixRequest: NovaChavePixRequest,
        responseObserver: StreamObserver<CadastraChavePixResponse>
    ): ChavePix {

        LOGGER.info("[SERVICE] Consultando cliente no Itau")
        val respostaConta =
            itauClient.consultaConta(novaChavePixRequest.idCliente!!, novaChavePixRequest.tipoDaConta!!)

        LOGGER.info("[SERVICE] Retorno do cliente: ${respostaConta!!.status}")

        if (respostaConta!!.code() != 200 && respostaConta.code() != 404) {
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Houve um erro ao conectar no sistema do ITAU")
                    .asRuntimeException()
            )
            responseObserver.onCompleted()
            LOGGER.warn("[SERVICE] O cliente itau parece estar offline")

        }

        if (respostaConta.status.equals(HttpStatus.NOT_FOUND)) {
            LOGGER.warn("[SERVICE] A chave nao existe -> NOT_FOUND")

            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("{erro.cliente.nao.existe}")
                    .asRuntimeException()
            )
            responseObserver.onCompleted()
            LOGGER.info("[SERVICE] Lançando ErroCustomizado \${erro.cliente.nao.existe}")
            throw ErroCustomizado("\${erro.cliente.nao.existe}")
        }

        if (repository.existsByValorChave(novaChavePixRequest.valorChave!!)) {
            LOGGER.warn("[SERVICE] A chave já existe -> ALREADY_EXISTS")

            responseObserver.onError(
                Status.ALREADY_EXISTS
                    .withDescription("\${erro.valor.chave.ja.existe}")
                    .asRuntimeException()
            )

            // responseObserver.onCompleted()
            LOGGER.info("[SERVICE] Lancando ErroCustomizado valorchavejaexiste")
            throw ErroCustomizado("\${erro.valor.chave.ja.existe}")

        }

        LOGGER.info("[SERVICE] Criando uma conta associada")
        val contaAssociada = respostaConta.body()?.toModel()

        LOGGER.info("[SERVICE] Criando nova chave pix definitiva")
        val chavePixCriada: ChavePix? = contaAssociada?.let { novaChavePixRequest.toModel(it) }


//        //Confere se a chave está cadastrada no BCB
//        if (registraChavePix.status.equals(HttpStatus.NOT_FOUND)) {
//            LOGGER.info("[SERVICE] Chave nao encontrada no BCB")
//            responseObserver.onError(
//                Status.NOT_FOUND
//                    .withDescription("Chave nao encontrada no BCB")
//                    .asRuntimeException()
//            )
//            responseObserver.onCompleted()
//            LOGGER.warn("[SERVICE] Essa chave já está cadatrada no sistema do BCB")
//
//            throw ErroCustomizado("\${erro.valor.chave.ja.existe}")
//        }

        LOGGER.info("[SERVICE] CONFERINDO SE A CHAVE JÀ EXISTE NO BCB...")
        val registraChavePix = bancoCentralClient.registraChavePix(CriarChaveBcbRequest(chavePixCriada!!))

        if (registraChavePix.status.equals(HttpStatus.UNPROCESSABLE_ENTITY)) {

            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("Essa chave já foi cadastrada no BCB")
                    .asRuntimeException()
            )
            responseObserver.onCompleted()
            LOGGER.warn("[SERVICE] Essa chave já está cadatrada no sistema do BCB")

            throw ErroCustomizado("\${erro.valor.chave.ja.existe}")
        }

        if (!registraChavePix.status.equals(HttpStatus.CREATED)) {
            throw   ErroCustomizado("erro ao salvar a nova chave no BCB")
        }

        chavePixCriada.atualizaHorarioChaveAleatoria(registraChavePix.body())
        repository.save(chavePixCriada)
        LOGGER.info("Chave pix salva no banco com sucesso $chavePixCriada, retornado a chave pro END POINT ")

        return chavePixCriada!!

    }


}
