package com.william.adicionaEremoveNoBcb.entidades

import com.william.exceptions.ErroCustomizado

data class IsbbCodigo(val instituicao: String) {
    var isbp: String

    init {
        when (instituicao) {
            "ITAÚ UNIBANCO S.A." -> isbp = "60701190"
            else -> throw  ErroCustomizado("Instituicao nao encontrada")
        }
    }


}