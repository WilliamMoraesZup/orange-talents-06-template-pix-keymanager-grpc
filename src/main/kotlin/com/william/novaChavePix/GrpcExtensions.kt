package com.william.novaChavePix

import com.william.CadastraChavePixRequest
import com.william.RemoveChavePixRequest
import com.william.TipoDaChave
import com.william.TipoDaConta
import com.william.bcbClient.classes.AccountType
import com.william.deletaChavePix.ExistsPixId
import com.william.deletaChavePix.RemoveChaveRequestDTO
import com.william.novaChavePix.classes.NovaChavePixRequest
import com.william.shared.ErroCustomizado

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
    return RemoveChaveRequestDTO( pixId, clienteId)

}

