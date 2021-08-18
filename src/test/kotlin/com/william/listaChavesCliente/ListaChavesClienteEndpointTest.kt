package com.william.listaChavesCliente

import com.william.ChavePixServiceListaChavesClienteGrpc
import com.william.ListaChavesClienteRequest
import com.william.TipoDaConta
import com.william.novaChavePix.ChavePixRepository
import com.william.novaChavePix.TipoDaChaveENUM
import com.william.novaChavePix.entidades.ChavePix
import com.william.novaChavePix.entidades.ContaAssociada
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat
import org.testcontainers.shaded.org.hamcrest.Matchers.containsInAnyOrder
import java.util.*
import javax.inject.Singleton


@MicronautTest(transactional = false)
internal class ListaChavesClienteEndpointTest(
    val grpcClient: ChavePixServiceListaChavesClienteGrpc.ChavePixServiceListaChavesClienteBlockingStub,
    val repository: ChavePixRepository
) {

    @BeforeEach
    fun start() {
        repository.save(createChavePix(TipoDaChaveENUM.CPF, "41911390880"))
        repository.save(createChavePix(TipoDaChaveENUM.CHAVE_ALEATORIA, "6a05b5bb-5f34-4a91-b282-b748304eb001"))

    }

    @Test
    fun `Deve listar todas chaves do cliente`() {
        val listaChavesCliente = grpcClient.listaChavesCliente(
            ListaChavesClienteRequest.newBuilder().setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890").build()
        )
        with(listaChavesCliente.listaChavesList) {
            val toList = this.map { it.valorChave }.toList()
            assertThat(
                toList,
                containsInAnyOrder(
                    "41911390880", "6a05b5bb-5f34-4a91-b282-b748304eb001"
                )
            )
        }
    }

    @Test
    fun `deve retornar NOT FOUND se cliente ID nao existir`() {

        val listaChavesCliente = assertThrows<StatusRuntimeException> {
            grpcClient.listaChavesCliente(
                ListaChavesClienteRequest.newBuilder().setClienteId(UUID.randomUUID().toString()).build()
            )
        }

        assertEquals(Status.NOT_FOUND.code, listaChavesCliente.status.code)


    }

    @Test
    fun `deve retornar erro se clienteId for nulo ou vazio`() {
        val respostaBlank = assertThrows<StatusRuntimeException> {
            grpcClient.listaChavesCliente(
                ListaChavesClienteRequest.newBuilder().setClienteId("").build()
            )
        }
        val respostaNull = assertThrows<StatusRuntimeException> {
            grpcClient.listaChavesCliente(
             null
            )
        }
        assertEquals(Status.INVALID_ARGUMENT.code, respostaBlank.status.code)
        assertEquals(Status.INVALID_ARGUMENT.code, respostaNull.status.code)
    }

    fun createChavePix(tipoChave: TipoDaChaveENUM, valor: String): ChavePix {
        return ChavePix(
            "c56dfef4-7901-44fb-84e2-a2cefb157890", tipoChave, valor,
            TipoDaConta.CONTA_CORRENTE,
            ContaAssociada(
                "ITAÚ UNIBANCO S.A.",
                "Rafael",
                "41911390880",
                "3939",
                "042220"
            )
        )
    }
}

@Factory
class RemoveChaveClient {
    @Singleton
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel) =
        ChavePixServiceListaChavesClienteGrpc.newBlockingStub(channel)
}