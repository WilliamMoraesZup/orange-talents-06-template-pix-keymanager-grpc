package com.william.bcbClient.classes

import com.william.shared.ErroCustomizado

data class IsbbCodigo(val instituicao: String) {
    var isbp: String

    init {
        when (instituicao) {
            "ITAÚ UNIBANCO S.A." -> isbp = "60701190"
            else -> throw  ErroCustomizado("Instituicao nao encontrada")
        }
    }


}