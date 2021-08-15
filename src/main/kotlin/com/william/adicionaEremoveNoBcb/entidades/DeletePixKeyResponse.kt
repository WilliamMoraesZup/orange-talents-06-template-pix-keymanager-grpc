package com.william.bcbClient

import java.time.LocalDateTime

data class DeletePixKeyResponse(
    val key: String,
    val participant: String,
    var deletedAt: LocalDateTime
)
