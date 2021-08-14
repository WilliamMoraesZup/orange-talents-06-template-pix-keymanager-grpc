package com.william.novaChavePix.entidades

import javax.persistence.Embeddable

@Embeddable
data class ContaAssociada(

    val instituicao: String,
    val nomeDoTitular: String,
    val cpfDoTitular: String,
    val agencia: String,
    val numeroDaConta: String

) {
    override fun toString(): String {
        return "ContaAssociada(instuticao='$instituicao', nomeDoTitular='$nomeDoTitular', cpfDoTitual='$cpfDoTitular', agencia='$agencia', numeroDaConta='$numeroDaConta')"
    }
}
