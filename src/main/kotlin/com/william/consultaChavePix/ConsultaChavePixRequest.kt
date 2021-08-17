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
   @field:NotBlank    @field:Size(
       message = "O valor da chave deve conter no máximo 77 caracteres",
       max = 77
   )  val chave: String,
) : ConsultaChavePixRequestInterface

interface ConsultaChavePixRequestInterface
