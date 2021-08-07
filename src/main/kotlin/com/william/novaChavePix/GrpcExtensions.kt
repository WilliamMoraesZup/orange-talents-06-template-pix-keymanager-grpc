package com.william.novaChavePix

import com.william.CadastraChavePixRequest
import com.william.TipoDaChave
import com.william.TipoDaConta
import com.william.novaChavePix.classes.NovaChavePixRequest

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

