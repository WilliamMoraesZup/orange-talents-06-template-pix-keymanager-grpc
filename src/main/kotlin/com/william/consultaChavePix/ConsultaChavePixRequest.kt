package com.william.consultaChavePix

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
@ValidPertenceAoCliente
data class PixIdRequest(
    @field:NotBlank val clienteId: String,
    @field:NotBlank val pixId: String
) : ConsultaChavePixRequestInterface

@Introspected
data class ChaveRequest(
   @field:NotBlank  @field:Size(max=77) val chave: String,
) : ConsultaChavePixRequestInterface

interface ConsultaChavePixRequestInterface

// Desativada por enquanto.
//class ConsultaChavePixRequestDto() {

//
//    val clienteId = request.pixIdRequest.clienteId
//    val pixId = request.pixIdRequest.pixId
//
//    val chave = request.chave
//
//    fun toModel(): ConsultaChavePixRequestInterface {
//
//        if (chave.isNullOrBlank()) return PixIdRequest(clienteId, pixId)
//        if (chave.isNotBlank()) return ChaveRequest(chave)
//        else throw IllegalStateException("erro ao receber os dados do request")
//    }
//
//    override fun toString(): String {
//        return "ConsultaChavePixRequest(clienteId='$clienteId', pixId='$pixId', chave='$chave')"
//    }
//
//}
