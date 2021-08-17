package com.william.adicionaEremoveNoBcb.entidades

import com.william.exceptions.ErroCustomizado

data class IsbpCodigo(val instituicao: String?) {
    var retornoInstituicao: String

    init {
        when (instituicao) {
            "ITAÚ UNIBANCO S.A." -> retornoInstituicao = "60701190"
            "60701190" -> retornoInstituicao = "ITAÚ UNIBANCO S.A."
            else -> throw  ErroCustomizado("Instituicão não encontrada")
        }
    }


}