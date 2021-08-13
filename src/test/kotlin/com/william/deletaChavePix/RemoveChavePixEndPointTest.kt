package com.william.deletaChavePix


import com.william.*
import com.william.novaChavePix.ChavePixRepository
import com.william.novaChavePix.classes.ChavePix
import com.william.novaChavePix.classes.ContaAssociada
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import javax.inject.Singleton


@MicronautTest(transactional = false)
internal class RemoveChavePixEndPointTest(
    val grpcClient: ChavePixServiceRemoveGrpc.ChavePixServiceRemoveBlockingStub,
    val repository: ChavePixRepository
) {

    lateinit var chavePixSalva: ChavePix

    @BeforeEach
    fun start() {
        val chavePix = ChavePix(
            "c56dfef4-7901-44fb-84e2-a2cefb157890", TipoDaChave.CPF, "41911390990",
            TipoDaConta.CONTA_CORRENTE,
            ContaAssociada(
                "ITAU",
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

        val build = RemoveChavePixRequest.newBuilder().setPixId(chavePixSalva.id.toString())
            .setClienteId(chavePixSalva.idCliente)
            .build()

        assertEquals(EmptyReturn.newBuilder().build(), grpcClient.remove(build))
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
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
            ChavePixServiceRemoveGrpc.ChavePixServiceRemoveBlockingStub {
        return ChavePixServiceRemoveGrpc.newBlockingStub(channel)
    }
}
