package com.william.novaChavePix

import com.william.CadastraChavePixRequest
import com.william.ChavePixServiceGrpc
import com.william.TipoDaChave
import com.william.TipoDaConta
import com.william.novaChavePix.classes.ChavePix
import com.william.novaChavePix.classes.ContaAssociada
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import javax.inject.Singleton


@MicronautTest(transactional = false)
internal class NovaChavePixEndPointTest(
    val grpcClient: ChavePixServiceGrpc.ChavePixServiceBlockingStub,
    val repository: ChavePixRepository
) {
    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun `Deve criar um novo objeto no banco quando CPF for valido`() {

        val pixResponse = grpcClient.registra(
            CadastraChavePixRequest.newBuilder()
                .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setTipoDaChave(TipoDaChave.CPF)
                .setValorChave("41911390880")
                .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                .build()
        )

        with(pixResponse) {
            assertNotNull(pixId)
            assertTrue(repository.existsById(pixId.toLong()))
        }
    }

    @Test
    fun `Deve criar um novo objeto no banco quando EMAIL for valido`() {


        val pixResponse = grpcClient.registra(
            CadastraChavePixRequest.newBuilder()
                .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setTipoDaChave(TipoDaChave.EMAIL)
                .setValorChave("wil_ff@hotmail.com")
                .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                .build()
        )

        with(pixResponse) {
            assertNotNull(pixId)
            assertTrue(repository.existsById(pixId.toLong()))
        }
    }


    @Test
    fun `Deve criar um novo objeto no banco quando CHAVE ALEATORIA for valido`() {


        val pixResponse = grpcClient.registra(
            CadastraChavePixRequest.newBuilder()
                .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setTipoDaChave(TipoDaChave.CHAVE_ALEATORIA)

                .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                .build()
        )

        with(pixResponse) {
            assertNotNull(pixId)
            assertTrue(repository.existsById(pixId.toLong()))
        }
    }

    @Test
    fun `Nao deve permitir chaves com valores iguais`() {
        val valorChave = "41911390880"

        repository.save(
            ChavePix(
                "c56dfef4-7901-44fb-84e2-a2cefb157890", TipoDaChave.CPF, valorChave,
                TipoDaConta.CONTA_CORRENTE,
                ContaAssociada(
                    "ITAU",
                    "Rafael",
                    "41911390880",
                    "3939",
                    "042220"
                )
            )
        )
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registra(
                CadastraChavePixRequest.newBuilder()
                    .setIdCliente("0d1bb194-3c52-4e67-8c35-a93c0af9284f")
                    .setTipoDaChave(TipoDaChave.CPF)
                    .setValorChave("41911390880")
                    .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                    .build()
            )
        }

        with(erro) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("{erro.valorChaveJaExiste}", status.description)
            assertTrue(repository.findByValorChave(valorChave).size == 1)
        }


    }

    @Test
    fun `nao deve registrar quando nao encontrar o cliente no itau`() {
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registra(
                CadastraChavePixRequest.newBuilder()
                    .setIdCliente(UUID.randomUUID().toString())
                    .setTipoDaChave(TipoDaChave.CPF)
                    .setValorChave("41911390880")
                    .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                    .build()
            )

        }

        with(erro) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("{erro.clienteNaoExiste}", status.description)
        }
    }

    @Test
    fun `nao deve registrar quando CPF for invalido`() {
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registra(
                CadastraChavePixRequest.newBuilder()
                    .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
                    .setTipoDaChave(TipoDaChave.CPF)
                    .setValorChave("41911u90850")
                    .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                    .build()
            )
        }

        with(erro) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("O campo CPF está inválido", status.description)
        }
    }

    @Test
    fun `nao deve registrar quando EMAIL for invalido`() {
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registra(
                CadastraChavePixRequest.newBuilder()
                    .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
                    .setTipoDaChave(TipoDaChave.EMAIL)
                    .setValorChave("asdasdsd.com.br")
                    .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                    .build()
            )
        }

        println(erro)
        with(erro) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("O campo EMAIL está inválido", status.description)
        }
    }

    @Test
    fun `nao deve registrar quando CELULAR for invalido`() {
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registra(
                CadastraChavePixRequest.newBuilder()
                    .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
                    .setTipoDaChave(TipoDaChave.CELULAR)
                    .setValorChave("wil_fft@hotmail.com")
                    .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                    .build()
            )
        }

        with(erro) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("O campo CELULAR está inválido", status.description)
        }
    }

    @Test
    fun `nao deve registrar quando CHAVE ALEATORIA for invalido`() {
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registra(
                CadastraChavePixRequest.newBuilder()
                    .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
                    .setTipoDaChave(TipoDaChave.CHAVE_ALEATORIA)
                    .setValorChave("wil_fft@hotmail.com")
                    .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                    .build()
            )
        }
        with(erro) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("O campo CHAVE_ALEATORIA está inválido", status.description)
        }
    }

    @Test
    fun `nao deve permitir valorChave maior 77`() {

        val valorInvalido = "a".repeat(100)

        val modelo = CadastraChavePixRequest.newBuilder()
            .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoDaChave(TipoDaChave.CPF)
            .setValorChave(valorInvalido)
            .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
            .build()


        val erroAcima77comCELULAR = assertThrows<StatusRuntimeException> {
            grpcClient.registra(modelo.toBuilder().setTipoDaChave(TipoDaChave.CELULAR).build())
        }
        val erroAcima77comCPF = assertThrows<StatusRuntimeException> {
            grpcClient.registra(modelo.toBuilder().setTipoDaChave(TipoDaChave.CPF).build())
        }
        val erroAcima77comEMAIL = assertThrows<StatusRuntimeException> {
            grpcClient.registra(modelo.toBuilder().setTipoDaChave(TipoDaChave.EMAIL).build())
        }


        assertTrue(erroAcima77comCELULAR.status.code == Status.INVALID_ARGUMENT.code)

        //PRECISO ARRANJAR UMA MANEIRA DE VALIDAR OS CARACTERES ANTES
        assertTrue(erroAcima77comCPF.status.code == Status.INVALID_ARGUMENT.code)
        assertTrue(erroAcima77comCPF.status.description.equals("O campo CPF está inválido"))

        //PRECISO ARRANJAR UMA MANEIRA DE VALIDAR OS CARACTERES ANTES
        assertTrue(erroAcima77comEMAIL.status.code == Status.INVALID_ARGUMENT.code)
        assertTrue(erroAcima77comEMAIL.status.description.equals("O campo EMAIL está inválido"))

    }

}


@Factory
class RegistraChaveClient {
    @Singleton
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
            ChavePixServiceGrpc.ChavePixServiceBlockingStub {
        return ChavePixServiceGrpc.newBlockingStub(channel)
    }
}
