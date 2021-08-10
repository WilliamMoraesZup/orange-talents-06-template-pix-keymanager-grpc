package com.william.novaChavePix

import com.william.TipoDaConta
import com.william.novaChavePix.classes.NovaChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*
import javax.inject.Inject


@MicronautTest(transactional = false)
internal class NovaChavePixEndPointTestMockito() {


    @MockBean(ItauClient::class)
    fun enderecoMock(): ItauClient {
        return Mockito.mock(ItauClient::class.java)
    }

    @Inject
    lateinit var clienteItau: ItauClient

//    @field:Inject
//    @field:Client("/")
//    lateinit var client: HttpClient

    @Test
    fun `Deve retornar pixId e ClienteId`() {

        val novaChavePixRequest = NovaChavePixRequest(
            "c56dfef4-7901-44fb-84e2-a2cefb157890",
            TipoDaChaveENUM.CPF,
            "41911390880",
            TipoDaConta.CONTA_CORRENTE
        )
        val
                thenReturn =
            `when`(clienteItau.consultaConta(id = UUID.randomUUID().toString(), TipoDaConta.CONTA_CORRENTE))
                .thenReturn(
                    HttpResponse.ok(
                        DadosDaContaResponse(
                            "tipo",
                            InstituicaoResponse("ITAU", "1234"),
                            "8939", "21234",
                            TitularResponse("teste", "41111133939")
                        )
                    )
                )

        println(thenReturn)


    }

//    @Factory
//    class Clients {
//        @Singleton
//        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
//                ChavePixServiceGrpc.ChavePixServiceBlockingStub? {
//            return ChavePixServiceGrpc.newBlockingStub(channel)
//        }
//    }

}