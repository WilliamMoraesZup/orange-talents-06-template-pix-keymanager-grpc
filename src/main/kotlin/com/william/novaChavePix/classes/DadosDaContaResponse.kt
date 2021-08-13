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
            instituicao = this.instituicao.nome,
            nomeDoTitular = this.titular.nome,
            cpfDoTitular = this.titular.cpf,
            agencia = this.agencia,
            numeroDaConta = this.numero
        )
    }

    override fun toString(): String {
        return "DadosDaContaResponse(tipo='$tipo', instituicao=$instituicao, agencia='$agencia', numero='$numero', titular=$titular)"
    }
}

data class InstituicaoResponse(val nome: String, val ispb: String)
data class TitularResponse(val nome: String, val cpf: String)
