package com.william.novaChavePix

import com.william.CadastraChavePixResponse
import com.william.novaChavePix.classes.ChavePix
import com.william.novaChavePix.classes.NovaChavePixRequest
import com.william.shared.ErroCustomizado
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class NovaChavePixService(
    val client: ItauClient,
    val repository: ChavePixRepository
) {

    @Transactional
    fun registraChavePix(
        @Valid novaChavePixRequest: NovaChavePixRequest,
        responseObserver: StreamObserver<CadastraChavePixResponse>
    ): ChavePix {
        println("CHeguei")
        val respostaConta = client.consultaConta(novaChavePixRequest.idCliente!!, novaChavePixRequest.tipoDaConta!!)
        println(respostaConta.body())

        if (respostaConta.body() == null) {
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("\${erro.clienteNaoExiste}")
                    .asRuntimeException()
            )

            throw ErroCustomizado("O Cliente nao foi encontrado no sistema")
        }
        println("buscando")
        if (repository.existsByValorChave(novaChavePixRequest.valorChave!!)) {
            responseObserver.onError(
                Status.ALREADY_EXISTS
                    .withDescription("\${erro.valorChaveJaExiste}")
                    .asRuntimeException()
            )

            throw ErroCustomizado("A chave informada já existe")
        }

        val contaAssociada = respostaConta.body()?.toModel()

        val chavePixCriada: ChavePix? = contaAssociada?.let { novaChavePixRequest.toModel(it) }

        repository.save(chavePixCriada)

        return chavePixCriada!!

    }


}
