package com.william.consultaChavePix

import com.william.adicionaEremoveNoBcb.BancoCentralClient
import com.william.novaChavePix.ChavePixRepository
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class ConsultaService(
    val repository: ChavePixRepository,
    val bcbClient: BancoCentralClient
) {


    fun consultaDados(@Valid request: ConsultaChavePixRequestInterface) {

        if (request is PixIdRequest) {
            println(request)
            println(
                repository.existsByValorChaveAndIdCliente(
                    valorChave = request.pixId,
                    idCLiente = request.clienteId
                )
            )



            if (!repository.existsByValorChaveAndIdCliente(
                    valorChave = request.pixId,
                    idCLiente = request.clienteId
                )
            ) {
                //throw ErroCustomizado("CHave nao pertence ao cliente")
            }


            val consultaPorChave = bcbClient.consultaChavePix(request.pixId)

            println(consultaPorChave)
        }
        if (request is ChaveRequest) println("ChaveRequest")


    }


}