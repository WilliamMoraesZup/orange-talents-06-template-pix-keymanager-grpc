package com.william.consultaChavePix

import com.william.ConsultaChavePixResponse
import com.william.adicionaEremoveNoBcb.BancoCentralClient
import com.william.exceptions.StatusNotFound
import com.william.exceptions.ErroCustomizado
import com.william.novaChavePix.ChavePixRepository
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class ConsultaService(
    val repository: ChavePixRepository,
    val bcbClient: BancoCentralClient
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)
    fun consultaDados(
        @Valid request: ConsultaChavePixRequestInterface,
        responseObserver: StreamObserver<ConsultaChavePixResponse>?
    ): ConsultaChavePixResponse? {


        /**
         * Abordagem 1
         * retorna Pix Id e Cliente ID
         * Nao retorna se nao existir no BCB
         * Não encontrada, retorna NOT_FOUND
         */
        // CASE 1-> Pesquisa somente por PixIdRequest, DEVE RETORNAR CHAVE PIX nem CLIENTE ID
        if (request is PixIdRequest) {
            LOGGER.info("[SERVICE]  PixIdRequest -> pesquisando no banco de dados")
            // verifica se a chave de fato pertence ao Cliente

            if (!repository.existsByValorChaveAndIdCliente(
                    valorChave = request.pixId,
                    idCLiente = request.clienteId
                )
            ) throw StatusNotFound(("Essa chave nao pertence a esse cliente")       )
                .also { LOGGER.info("[SERVICE]  Nao foi encontrado no banco") }
            //  TODO("Inserir o handler para captura o erro")


            LOGGER.info("[SERVICE] Antes de exibir preciso verificar existencia no BCB")
            val response = bcbClient.consultaChavePix(request.pixId)

            LOGGER.info("[SERVICE] Retorno do BCB ${response.status.code}")



            LOGGER.info("[PixIdRequest] RECEBEU OBJETO DO bcbClient")
            //  200  faz o return com  objeto Stream e finaliza,
            //  404  não retorna e lança erro pois nao foi localizado
            when (response.status.code) {
                404 -> throw ErroCustomizado("A chave nao foi localizada no BCB")
                200 -> return response.body().comPixIdRequest(request).toStream()
            }
        }

        // CASE 2-> Pesquisa somente por Chave Pix, NAO DEVE RETORNAR CHAVE PIX nem CLIENTE ID
        if (request is ChaveRequest) {
            LOGGER.info("[SERVICE]  ChaveRequest -> pesquisando no banco de dados")

            // localiza antes se esta no meu sistema, SENAO pesquisa no BCB
            val byValorChave = repository.findByValorChave(request.chave)
            if (byValorChave.size == 1) {
                val chavePix = byValorChave[0]
                LOGGER.info("[ChaveRequest] RECEBEU OBJETO DO BANCO, sem passar pelo BCB")
                //converte em Stream
                return chavePix.toStream()

            } else {
                LOGGER.info("[ChaveRequest] Não achou no banco, vai procurar no BCB")
                val response = bcbClient.consultaChavePix(request.chave)
                LOGGER.info("[ChaveRequest] RECEBEU OBJETO DO bcbClient")
                println(response.body())


                when (response.status.code) {
                    404 -> throw StatusNotFound("A chave nao foi localizada no BCB")
                    200 -> return response.body().toStream()    //converte em Stream
                }
            }
        }
        return null
    }
}