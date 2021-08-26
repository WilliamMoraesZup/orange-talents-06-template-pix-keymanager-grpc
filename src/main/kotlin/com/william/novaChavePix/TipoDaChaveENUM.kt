package com.william.novaChavePix

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator

enum class TipoDaChaveENUM {

    CPF {
        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false

            }
            if (!chave.matches("^[0-9]{11}\$".toRegex())) {
                return false

            }
            return CPFValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    CELULAR {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return (chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex()))
        }
    },

    CHAVE_ALEATORIA {
        override fun valida(chave: String?) = chave.isNullOrBlank()

    },

    EMAIL {  override fun valida(chave: String?): Boolean {

        if (chave.isNullOrBlank()) {

            return false
        }

        return EmailValidator().run {
            initialize(null)
            isValid(chave, null)
        }
    }



    };


    abstract fun valida(chave: String?): Boolean


}
