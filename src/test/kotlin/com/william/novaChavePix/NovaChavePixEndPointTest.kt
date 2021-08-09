package com.william.novaChavePix

import com.william.CadastraChavePixRequest
import com.william.ChavePixServiceGrpc
import com.william.TipoDaChave
import com.william.TipoDaConta
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import javax.inject.Singleton


@MicronautTest(transactional = false)
internal class NovaChavePixEndPointTest(val grpcClient: ChavePixServiceGrpc.ChavePixServiceBlockingStub) {


    @Test
    fun `Deve criar um novo objeto no banco`() {
        val model =
            CadastraChavePixRequest.newBuilder()
                .setIdCliente("123")
                .setTipoDaChave(TipoDaChave.CPF)
                .setValorChave("41911390880")
                .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                .build()
        val pixResponse = grpcClient.registra(model)
        println(pixResponse)

        assertNotNull(pixResponse.pixId)

    }


}

@Factory
class Clients {
    @Singleton
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
            ChavePixServiceGrpc.ChavePixServiceBlockingStub {
        return ChavePixServiceGrpc.newBlockingStub(channel)
    }
}