package com.william.deletaChavePix


import com.william.ChavePixServiceGrpc
import com.william.RemoveChavePixRequest
import com.william.TipoDaChave
import com.william.TipoDaConta
import com.william.novaChavePix.ChavePixRepository
import com.william.novaChavePix.classes.ChavePix
import com.william.novaChavePix.classes.ContaAssociada
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.inject.Singleton


@MicronautTest(transactional = false)
internal class RemoveChavePixEndPointTest(
    val grpcClient: ChavePixServiceGrpc.ChavePixServiceBlockingStub,
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
    fun `Nao deve deletar quando idCliente for invalida`() {
        println(chavePixSalva.id)
        println(chavePixSalva.idCliente)

        val build = RemoveChavePixRequest.newBuilder().setPixId(chavePixSalva.id.toString())
            .setClienteId(chavePixSalva.idCliente)
            .build()


        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.remove(
                build
            )
        }

        println(erro)

    }

    fun `Nao deve deletar quando pixId for invalida`() {
    }

    fun `Nao deve deletar quando pixId nao pertencer ao usuario`() {
    }
}

@Factory
class DeletaChaveClient
{
@Bean
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
            ChavePixServiceGrpc.ChavePixServiceBlockingStub {
        return ChavePixServiceGrpc.newBlockingStub(channel)
    }
}