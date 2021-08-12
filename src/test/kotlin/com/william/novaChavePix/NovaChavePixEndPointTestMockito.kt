package com.william.novaChavePix

import io.micronaut.test.extensions.junit5.annotation.MicronautTest


@MicronautTest(transactional = false)
internal class NovaChavePixEndPointTestMockito() {

/*
    @MockBean(ItauClient::class)
    fun enderecoMock(): ItauClient {
        return Mockito.mock(ItauClient::class.java)
    }

    @Inject
    lateinit var clienteItau: ItauClient


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
// NAO ESTOU ENTENDO SOBRE COMO E PORQUE USAR MOCKITO. PERGUNTAR NO CHECKOUT


    }
*/

}