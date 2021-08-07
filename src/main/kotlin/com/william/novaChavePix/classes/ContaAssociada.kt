package com.william.novaChavePix.classes

import javax.persistence.Embeddable

@Embeddable
class ContaAssociada(

    val instuticao: String,
    val nomeDoTitular: String,
    val cpfDoTitual: String,
    val agencia: String,
    val numeroDaConta: String

) {
    override fun toString(): String {
        return "ContaAssociada(instuticao='$instuticao', nomeDoTitular='$nomeDoTitular', cpfDoTitual='$cpfDoTitual', agencia='$agencia', numeroDaConta='$numeroDaConta')"
    }
}
