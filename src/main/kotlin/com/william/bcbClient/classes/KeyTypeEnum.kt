package com.william.bcbClient.classes

import com.william.novaChavePix.TipoDaChaveENUM

enum class KeyTypeEnum(tipoDaChave: TipoDaChaveENUM?) {

    CPF(TipoDaChaveENUM.CPF),
    CNPJ(null),
    PHONE(TipoDaChaveENUM.CELULAR),
    EMAIL(TipoDaChaveENUM.EMAIL),
    RANDOM(TipoDaChaveENUM.CHAVE_ALEATORIA)

}
