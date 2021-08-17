package com.william.consultaChavePix;

import com.william.ConsultaChavePixRequest
import com.william.ConsultaChavePixResponse
import com.william.TipoDaChave
import com.william.novaChavePix.entidades.ChavePix


fun ConsultaChavePixRequest.toModel(): ConsultaChavePixRequestInterface {

    if (this.hasChave()) return ChaveRequest(chave)
    if (this.hasPixIdRequest()) return PixIdRequest(clienteId = pixIdRequest.clienteId, pixId = pixIdRequest.pixId)
    else throw IllegalStateException("erro ao receber os dados do request")
}

/**
 *   Opção 2, é retorando do banco quando o usuario passar somente a chave
 *   Se existe no banco, ele retorna porem sem ID cliente nem Pix Id
 *
 */
fun ChavePix.toStream(): ConsultaChavePixResponse? {

    val dadosConta = ConsultaChavePixResponse.newBuilder().dadosConta
        .toBuilder()
        .setNomeInstituicao(conta.instituicao)
        .setAgencia(conta.agencia)
        .setNumeroConta(conta.numeroDaConta)
        .setTipoConta(tipoDaConta).build()

    val dadosPessoais = ConsultaChavePixResponse.newBuilder().dadosPessoais
        .toBuilder()
        .setNome(conta.nomeDoTitular).setCpf(conta.cpfDoTitular)
        .build()

    return ConsultaChavePixResponse.newBuilder()

        .setTipoChave(TipoDaChave.valueOf(tipoDaChave.name))
        .setValorChave(valorChave)
        .setDadosConta(dadosConta)
        .setDadosPessoais(dadosPessoais)
        .setCriadoEm(createdAt.toString())
         .build()

}

