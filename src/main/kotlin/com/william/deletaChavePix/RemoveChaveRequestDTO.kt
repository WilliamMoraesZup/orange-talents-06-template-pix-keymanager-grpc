package com.william.deletaChavePix

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank


@Introspected
data class RemoveChaveRequestDTO(
    @field:ExistsPixId
    @field:NotBlank
    val chavePix: String?,

    @field:NotBlank
    val clienteId: String?
)