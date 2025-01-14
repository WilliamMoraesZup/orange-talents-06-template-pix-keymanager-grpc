package com.william.adicionaEremoveNoBcb.entidades

import com.william.novaChavePix.entidades.ChavePix
import com.william.exceptions.ErroCustomizado

class CriarChaveBcbRequest(chavePix: ChavePix) {
    val keyType: KeyTypeEnum? = when (chavePix.tipoDaChave.name) {
        "CPF" -> KeyTypeEnum.CPF
        "CELULAR " -> KeyTypeEnum.PHONE
        "EMAIL" -> KeyTypeEnum.EMAIL
        "CHAVE_ALEATORIA" -> KeyTypeEnum.RANDOM
        else -> null
    }


    val key: String? = chavePix.valorChave
    val bankAccount: BankAccountRequest = BankAccountRequest(chavePix)
    val owner: OwnerRequest = OwnerRequest(chavePix)

    override fun toString(): String {
        return "CriarChaveBcbRequest(keyType=$keyType, key=$key, bankAccount=$bankAccount, owner=$owner)"
    }


}

class BankAccountRequest(chavePix: ChavePix) {

    val participant: String = IsbpCodigo(chavePix.conta.instituicao).retornoInstituicao
    val branch: String = chavePix.conta.agencia
    val accountNumber: String = chavePix.conta.numeroDaConta
    val accountType: AccountType = when (chavePix.tipoDaConta.name) {
        "CONTA_CORRENTE" -> AccountType.CACC
        "CONTA_POUPANCA" -> AccountType.SVGS
        else -> throw ErroCustomizado("AccountType nao encontrada")
    }

    override fun toString(): String {
        return "BankAccountRequest(participant='$participant', branch='$branch', accountNumber='$accountNumber', accountType=$accountType )"
    }


}

class OwnerRequest(chavePix: ChavePix) {
    val type: TypeEnum = TypeEnum.NATURAL_PERSON
    val name: String = chavePix.conta.nomeDoTitular
    val taxIdNumber: String = chavePix.conta.cpfDoTitular
    override fun toString(): String {
        return "OwnerRequest(type=$type, name='$name', taxIdNumber='$taxIdNumber')"
    }


}

