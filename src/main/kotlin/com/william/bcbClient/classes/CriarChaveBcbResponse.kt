package com.william.bcbClient.classes

import java.time.LocalDateTime

class CriarChaveBcbResponse(
    val keyType: KeyTypeEnum,
    val key: String,
    val bankAccount: BankAccountResponse,
    val owner: OwnerResponse,
    val createdAt: LocalDateTime
) {


}

class BankAccountResponse(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: AccountType,

    )

class OwnerResponse(
    val type: TypeEnum,
    val name: String,
    val taxIdNumber: String

)
