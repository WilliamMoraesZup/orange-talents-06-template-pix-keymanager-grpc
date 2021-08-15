package com.william.deletaChavePix


import com.william.ChavePixServiceRemoveGrpc
import com.william.EmptyReturn
import com.william.RemoveChavePixRequest
import com.william.TipoDaConta
import com.william.adicionaEremoveNoBcb.BancoCentralClient
import com.william.adicionaEremoveNoBcb.entidades.DeletePixKeyRequest
import com.william.novaChavePix.ChavePixRepository
import com.william.novaChavePix.ItauClient
import com.william.novaChavePix.TipoDaChaveENUM
import com.william.novaChavePix.entidades.ChavePix
import com.william.novaChavePix.entidades.ContaAssociada
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*
import javax.inject.Singleton


@MicronautTest(transactional = false)
internal class RemoveChavePixEndPointTest(
    val grpcClient: ChavePixServiceRemoveGrpc.ChavePixServiceRemoveBlockingStub,
    val repository: ChavePixRepository,
    val itauClient: ItauClient,
    val clientBcb: BancoCentralClient

) {
    lateinit var chavePixSalva: ChavePix

    @MockBean(BancoCentralClient::class)
    fun bcbClientMock(): BancoCentralClient {
        return Mockito.mock(BancoCentralClient::class.java)
    }

//    @MockBean(ItauClient::class)
//    fun itauClientMock(): ItauClient {
//        return Mockito.mock(ItauClient::class.java)
//    }

    fun createChavePix(): ChavePix {
        return ChavePix(
            "c56dfef4-7901-44fb-84e2-a2cefb157890", TipoDaChaveENUM.CPF, "41911390880",
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

    @BeforeEach
    fun start() {
        repository.save(createChavePix())
    }

    @Test
    fun `Deve remover chave pix do bcb e nao dar erro`() {


        val valorChave = createChavePix().valorChave
        Mockito.`when`(
            clientBcb.removeChavePix(
                valorChave,
                DeletePixKeyRequest(valorChave, "60701190")
            )
        ).thenReturn(HttpResponse.ok())


        val retorno = grpcClient.remove(
            RemoveChavePixRequest
                .newBuilder()
                .setPixId(valorChave)
                .setClienteId(createChavePix().idCliente)
                .build()
        )
        println(retorno)

        assertEquals(EmptyReturn.newBuilder().build(), retorno)
        assertTrue(repository.findByValorChave(valorChave).isEmpty())

    }

    @Test //VOU REFATORAR EM BREVE
    fun `Nao deve deletar quando pixId for invalida`() {
        val build = RemoveChavePixRequest.newBuilder().setPixId("3")
            .setClienteId(chavePixSalva.idCliente)
            .build()

        val assertThrows = assertThrows<StatusRuntimeException> {
            grpcClient.remove(build)
        }

        assertNotNull(assertThrows)
    }

    @Test
    fun `Nao deve deletar quando pixId nao pertencer ao usuario`() {

        val build = RemoveChavePixRequest.newBuilder().setPixId(chavePixSalva.id.toString())
            .setClienteId(UUID.randomUUID().toString())
            .build()

        val assertThrows = assertThrows<StatusRuntimeException> {
            grpcClient.remove(build)
        }
        assertNotNull(assertThrows)
    }


}


@Factory
class RemoveChaveClient {
    @Singleton
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel) =
        ChavePixServiceRemoveGrpc.newBlockingStub(channel)
}

