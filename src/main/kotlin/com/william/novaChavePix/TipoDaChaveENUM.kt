package com.william.novaChavePix

import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator

enum class TipoDaChaveENUM {

    CPF {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false

            }
            if (!chave.matches("[0-9]+".toRegex())) {
                return false;

            }
            return CPFValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    CELULAR {
        override fun valida(chave: String?): Boolean {
            TODO("Not yet implemented")
        }
    },
    CHAVE_ALEATORIA {
        override fun valida(chave: String?): Boolean {
            TODO("Not yet implemented")
        }
    },
    EMAIL {
        override fun valida(chave: String?): Boolean {
            TODO("Not yet implemented")
        }
    };


    abstract fun valida(chave: String?): Boolean


}
