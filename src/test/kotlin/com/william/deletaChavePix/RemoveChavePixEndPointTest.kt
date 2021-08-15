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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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

    @BeforeEach
    fun start() {
        val chavePix = ChavePix(
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
        chavePixSalva = repository.save(chavePix)
    }

    @Test
    fun `Deve remover chave pix e nao dar erro`() {

        Mockito.`when`(
            clientBcb.removeChavePix(
                "41911390880",
                DeletePixKeyRequest("41911390880", "60701190")
            )
        ).thenReturn(HttpResponse.ok())


        println("run")

        val retorno = grpcClient.remove(
            RemoveChavePixRequest
                .newBuilder()
                .setPixId("41911390880")
                .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .build()
        )
        println(retorno)

        assertEquals(
            EmptyReturn.newBuilder().build(), retorno
        )
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

