package com.william.adicionaEremoveNoBcb.entidades

import java.time.LocalDateTime

class CriarChaveBcbResponse(
    val pixId: String?,
    val clientId: String?,
    val keyType: KeyTypeEnum,
    val key: String,
    val bankAccount: BankAccountResponse,
    val owner: OwnerResponse,
    val createdAt: LocalDateTime
) {
    override fun toString(): String {
        return "CriarChaveBcbResponse(keyType=$keyType, key='$key', bankAccount=$bankAccount, owner=$owner, createdAt=$createdAt)"
    }
}

class BankAccountResponse(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: AccountType,

    ) {
    override fun toString(): String {
        return "BankAccountResponse(participant='$participant', branch='$branch', accountNumber='$accountNumber', accountType=$accountType)"
    }
}

class OwnerResponse(
    val type: TypeEnum,
    val name: String,
    val taxIdNumber: String

) {
    override fun toString(): String {
        return "OwnerResponse(type=$type, name='$name', taxIdNumber='$taxIdNumber')"
    }
}
