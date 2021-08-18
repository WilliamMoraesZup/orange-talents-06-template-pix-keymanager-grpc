package com.william.listaChavesCliente

import com.william.TipoDaConta
import com.william.novaChavePix.TipoDaChaveENUM
import com.william.novaChavePix.entidades.ChavePix
import java.time.LocalDateTime


 class ListaChaveRequest(
      chave: ChavePix
) {
    var pixId: String = chave.valorChave
    var clienteId: String? = chave.idCliente
    var tipoDaChave: TipoDaChaveENUM = chave.tipoDaChave
    var valorChave: String = chave.valorChave
    var tipoDaConta: TipoDaConta = chave.tipoDaConta
    var dataCriacao: LocalDateTime? = chave.createdAt
     override fun toString(): String {
         return "ListaChaveRequest(pixId='$pixId', clienteId=$clienteId, tipoDaChave=$tipoDaChave, valorChave='$valorChave', tipoDaConta=$tipoDaConta, dataCriacao=$dataCriacao)"
     }

 }