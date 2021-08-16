package com.william.novaChavePix

import com.william.*
import com.william.consultaChavePix.ChaveRequest
import com.william.consultaChavePix.ConsultaChavePixRequestInterface
import com.william.consultaChavePix.PixIdRequest
import com.william.deletaChavePix.RemoveChaveRequestDTO
import com.william.novaChavePix.entidades.NovaChavePixRequest

fun CadastraChavePixRequest.toModel(): NovaChavePixRequest {
    return NovaChavePixRequest(
        idCliente = idCliente,
        tipoDaChave = when (this.tipoDaChave) {
            TipoDaChave.UNKNOWN_TIPO_CHAVE -> null
            else -> TipoDaChaveENUM.valueOf(tipoDaChave.name)
        },
        valorChave = valorChave,
        tipoDaConta = when (tipoDaConta) {
            TipoDaConta.UNKNOWN_TIPO_CONTA -> null
            else -> TipoDaConta.valueOf(tipoDaConta.name)
        }
    )
}

fun RemoveChavePixRequest.toModel(): RemoveChaveRequestDTO {
    return RemoveChaveRequestDTO(pixId, clienteId)

}

fun ConsultaChavePixRequest.toModel(): ConsultaChavePixRequestInterface {
    if (this.hasChave()) return ChaveRequest(chave)
    if (this.hasPixIdRequest()) return PixIdRequest(pixIdRequest.clienteId, pixIdRequest.clienteId)
    else throw IllegalStateException("erro ao receber os dados do request")
}