package com.william.novaChavePix

import com.william.CadastraChavePixResponse
import com.william.adicionaEremoveNoBcb.BancoCentralClient
import com.william.adicionaEremoveNoBcb.entidades.CriarChaveBcbRequest
import com.william.exceptions.StatusAlreadyExists
import com.william.exceptions.StatusNotFound
import com.william.exceptions.ErroChaveJaExisteBCB
import com.william.exceptions.ErroCustomizado
import com.william.novaChavePix.entidades.ChavePix
import com.william.novaChavePix.entidades.NovaChavePixRequest
import io.grpc.stub.StreamObserver
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientException
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

        LOGGER.info("[SERVICE] Retorno do cliente: ${respostaConta.status}")


        //Confere se sistema está offline
        if (respostaConta.code() != 200 && respostaConta.code() != 404) throw HttpClientException("Sistema do Itau está offilne").also {
            LOGGER.warn("[SERVICE] O cliente itau parece estar offline")
        }



        if (respostaConta.status.equals(HttpStatus.NOT_FOUND)) LOGGER.warn("[SERVICE] A chave nao existe -> NOT_FOUND")
            .also { throw StatusNotFound(" A chave nao existe -> NOT_FOUND") }

        if (repository.existsByValorChave(novaChavePixRequest.valorChave!!))
            LOGGER.warn("[SERVICE] A chave já existe -> ALREADY_EXISTS")
                .also {
                    throw StatusAlreadyExists("\${erro.valor.chave.ja.existe}")
                }

        LOGGER.info("[SERVICE] Criando uma conta associada")
        val contaAssociada = respostaConta.body()?.toModel()

        LOGGER.info("[SERVICE] Criando nova chave pix definitiva")
        val chavePixCriada: ChavePix? = contaAssociada?.let { novaChavePixRequest.toModel(it) }


        LOGGER.info("[SERVICE] CONFERINDO SE A CHAVE JÀ EXISTE NO BCB...")
        val registraChavePix = bancoCentralClient.registraChavePix(CriarChaveBcbRequest(chavePixCriada!!))

        if (registraChavePix.status.equals(HttpStatus.UNPROCESSABLE_ENTITY))
            throw ErroChaveJaExisteBCB(" Essa chave já está cadatrada no sistema do BCB  ")

        LOGGER.warn("[SERVICE] Essa chave já está cadatrada no sistema do BCB")

        if (!registraChavePix.status.equals(HttpStatus.CREATED)) {
            throw   ErroCustomizado("erro ao salvar a nova chave no BCB")
        }

        chavePixCriada.atualizaHorarioChaveAleatoria(registraChavePix.body())
        repository.save(chavePixCriada)
        LOGGER.info("Chave pix salva no banco com sucesso $chavePixCriada, retornado a chave pro END POINT ")

        return chavePixCriada

    }


}
