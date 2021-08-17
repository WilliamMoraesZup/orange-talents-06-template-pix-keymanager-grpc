package com.william.consultaChavePix

import com.william.ConsultaChavePixResponse
import com.william.TipoDaChave
import com.william.TipoDaConta
import com.william.adicionaEremoveNoBcb.entidades.AccountType
import com.william.adicionaEremoveNoBcb.entidades.IsbpCodigo
import com.william.adicionaEremoveNoBcb.entidades.KeyTypeEnum
import com.william.adicionaEremoveNoBcb.entidades.TypeEnum
import com.william.novaChavePix.TipoDaChaveENUM
import java.time.LocalDateTime


class ConsultaChavePixResponseDTO(

    val keyType: KeyTypeEnum,
    val key: String,
    val bankAccount: BankAccountResponse,
    val owner: OwnerResponse,
    val createdAt: LocalDateTime
) {
    var pixId: String? = ""
    var clienteId: String? = ""

    
    fun comPixIdRequest(request: PixIdRequest): ConsultaChavePixResponseDTO {
        pixId = request.pixId
        clienteId = request.clienteId
        return this
    }

    fun toStream(): ConsultaChavePixResponse? {
        val tipoChave = when (keyType.name) {
            "CPF" -> TipoDaChaveENUM.CPF
            "PHONE " -> TipoDaChaveENUM.CELULAR
            "EMAIL" -> TipoDaChaveENUM.EMAIL
            "RANDOM" -> TipoDaChaveENUM.CHAVE_ALEATORIA
            else -> null
        }
        val tipoConta = when (bankAccount.accountType.name) {
            "CACC" -> TipoDaConta.CONTA_CORRENTE
            "SVGS" -> TipoDaConta.CONTA_POUPANCA
            else -> TipoDaConta.UNKNOWN_TIPO_CONTA
        }

        val dadosConta = ConsultaChavePixResponse.newBuilder().dadosConta
            .toBuilder()
            .setNomeInstituicao(IsbpCodigo(bankAccount.participant).retornoInstituicao)
            .setAgencia(bankAccount.branch)
            .setNumeroConta(bankAccount.accountNumber)
            .setTipoConta(tipoConta).build()

        val dadosPessoais = ConsultaChavePixResponse.newBuilder().dadosPessoais
            .toBuilder()
            .setNome(owner.name).setCpf(owner.taxIdNumber)
            .build()

        return ConsultaChavePixResponse.newBuilder()

            .setTipoChave(TipoDaChave.valueOf(tipoChave!!.name))
            .setValorChave(key)
            .setDadosConta(dadosConta)
            .setDadosPessoais(dadosPessoais)
            .setCriadoEm(createdAt.toString())
            .setIdCliente(clienteId)
            .setPixId(pixId).build()

    }

    override fun toString(): String {
        return "ConsultaChavePixResponseDTO(pixId=$pixId, clienteId=$clienteId, keyType=$keyType, key='$key', bankAccount=$bankAccount, owner=$owner, createdAt=$createdAt)"
    }


}


data class BankAccountResponse(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: AccountType,

    )


data class OwnerResponse(
    val type: TypeEnum,
    val name: String,
    val taxIdNumber: String
)

