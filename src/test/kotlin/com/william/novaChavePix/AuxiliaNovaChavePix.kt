package com.william.novaChavePix

import com.william.TipoDaConta
import com.william.adicionaEremoveNoBcb.entidades.*
import com.william.consultaChavePix.BankAccountResponse
import com.william.consultaChavePix.ConsultaChavePixResponseDTO
import com.william.consultaChavePix.OwnerResponse
import com.william.novaChavePix.entidades.ChavePix
import com.william.novaChavePix.entidades.ContaAssociada
import java.time.LocalDateTime
import java.util.*

open class AuxiliaNovaChavePix {
    fun criaChavePix(tipoChave: TipoDaChaveENUM, valor: String): ChavePix {
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

    val clienteId = "c56dfef4-7901-44fb-84e2-a2cefb157890"

    val mockItauDadosDaContaResponse = DadosDaContaResponse(
        "tipo",
        InstituicaoResponse("ITAÚ UNIBANCO S.A.", "60701190"),
        "48484", "12345",
        TitularResponse("William", "4191139990")
    )
    val mockRegistraBCBRequest =
        CriarChaveBcbRequest(criaChavePix(TipoDaChaveENUM.CPF, "41911390880"))

    val mockRegistraBCBResponse = CriarChaveBcbResponse(
        keyType = KeyTypeEnum.CPF,
        key = UUID.randomUUID().toString(),
        bankAccount = com.william.adicionaEremoveNoBcb.entidades.BankAccountResponse(
            "60701190",
            "8888",
            "123456",
            AccountType.CACC
        ), createdAt = LocalDateTime.now(),
        pixId = "41911390880",
        clientId = UUID.randomUUID().toString(),
        owner = com.william.adicionaEremoveNoBcb.entidades.OwnerResponse(
            TypeEnum.LEGAL_PERSON, "william", "4191133339"
        )
    )
    fun dadosDaContaResponse(): DadosDaContaResponse? {
        return DadosDaContaResponse(
            tipo = TipoDaConta.CONTA_CORRENTE.name,
            instituicao = InstituicaoResponse("ITAÚ UNIBANCO S.A.", "60701190"),
            agencia = "9494",
            numero = "123452",
            titular = TitularResponse("nome", "41133323223")

        )
    }
    val mockRetornoConsulta = ConsultaChavePixResponseDTO(
        keyType = KeyTypeEnum.CPF,
        key = UUID.randomUUID().toString(),
        bankAccount = BankAccountResponse("123456", "8888", "123456", AccountType.CACC),
        owner = OwnerResponse(TypeEnum.LEGAL_PERSON, "william", "4191133339"),
        createdAt = LocalDateTime.now()
    )
}