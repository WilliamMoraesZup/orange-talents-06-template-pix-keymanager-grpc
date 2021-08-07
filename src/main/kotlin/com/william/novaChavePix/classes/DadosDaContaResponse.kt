package com.william.novaChavePix

import com.william.novaChavePix.classes.ContaAssociada

data class DadosDaContaResponse(
    val tipo: String,
    val instituicao: InstituicaoResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularResponse
) {
    fun toModel(): ContaAssociada {
        return ContaAssociada(
            instuticao = this.instituicao.nome,
            nomeDoTitular = this.titular.nome,
            cpfDoTitual = this.titular.cpf,
            agencia = this.instituicao.nome,
            numeroDaConta = this.numero
        )
    }

    override fun toString(): String {
        return "DadosDaContaResponse(tipo='$tipo', instituicao=$instituicao, agencia='$agencia', numero='$numero', titular=$titular)"
    }
}

data class InstituicaoResponse(val nome: String, val ispb: String) {}
data class TitularResponse(val nome: String, val cpf: String) {}
