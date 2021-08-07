package com.william.novaChavePix

import com.william.novaChavePix.classes.ChavePix
import com.william.novaChavePix.classes.NovaChavePixRequest
import com.william.shared.ChavePixExistenteException
import com.william.shared.ErroCustomizado
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class NovaChavePixService(
    @Inject val client: ItauClient,
    @Inject val repository: ChavePixRepository
) {

    fun registraChavePix(@Valid novaChavePixRequest: NovaChavePixRequest): ChavePix {


        val respostaConta =
            novaChavePixRequest.idCliente?.let {
                novaChavePixRequest.tipoDaConta?.let { it1 ->
                    client.consultaConta(
                        it,
                        it1
                    )
                }
            } ?: throw ErroCustomizado("Houve um erro ao buscar os dados ")

        if (respostaConta.body() == null) {
            throw ErroCustomizado("Cliente não encontrado no Itau")
        }


        if (repository.existsByValorChave(novaChavePixRequest.valorChave!!)) {
            throw ChavePixExistenteException("Não foi possivel cadastrar essa chave. Ela já se encontra cadastrada no sistema. Consulte sua agencia");
        }

        val contaAssociada =
            respostaConta.body()?.toModel() ?: throw ErroCustomizado("Cliente não encontrado no Itau")

        val chavePixCriada: ChavePix = novaChavePixRequest.toModel(contaAssociada);

        repository.save(chavePixCriada)

        return chavePixCriada


    }


}
