package com.william.novaChavePix

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ChavesValidasTest {


    @Nested
    inner class CPF {
        @Test
        fun `deve ser valido quando CPF for valido`() {

            with(TipoDaChaveENUM.CPF) {
                assertTrue(valida("41957740043"))
            }
        }

        @Test
        fun `nao deve validar quando CPF for invalido`() {
            with(TipoDaChaveENUM.CPF) {
                assertFalse(valida("41937740043"))
            }
        }

        @Test
        fun `nao deve validar quando CPF nulo ou vazio`() {
            with(TipoDaChaveENUM.CPF) {
                assertFalse(valida(""))
                assertFalse(valida(null))
            }
        }
    }

    @Nested
    inner class EMAIL {
        @Test
        fun `deve ser valido quando EMAIL for valido`() {
            with(TipoDaChaveENUM.EMAIL) {
                assertTrue(valida("foo@bar.com"))
            }
        }

        @Test
        fun `nao deve validar quando EMAIL for invalido`() {
            with(TipoDaChaveENUM.EMAIL) {
                assertFalse(valida("foo.com.br"))
            }
        }

        @Test
        fun `nao deve validar quando EMAIL nulo ou vazio`() {
            with(TipoDaChaveENUM.EMAIL) {
                assertFalse(valida(""))
                assertFalse(valida(null))
            }
        }

        @Test
        fun `nao deve validar quando EMAIL tiver mais que 77 caracteres`() {
            with(TipoDaChaveENUM.EMAIL) {
                assertFalse(valida("a".repeat(78)))
            }
        }
    }

    @Nested
    inner class CELULAR {
        @Test
        fun `deve ser valido quando CELULAR for valido`() {
            with(TipoDaChaveENUM.CELULAR) {
                assertTrue(valida("+5585988714077"))
            }
        }

        @Test
        fun `nao deve validar quando CELULAR for invalido`() {
            with(TipoDaChaveENUM.CELULAR) {
                assertFalse(valida("5585988714077"))
            }
        }

        @Test
        fun `nao deve validar quando CELULAR conter letras`() {
            with(TipoDaChaveENUM.CELULAR) {
                assertFalse(valida("+558598871a077"))
            }
        }

        @Test
        fun `nao deve validar quando CELULAR nulo ou vazio`() {
            with(TipoDaChaveENUM.CELULAR) {
                assertFalse(valida(""))
                assertFalse(valida(null))
            }
        }

        @Test
        fun `nao deve validar quando CELULAR tiver mais que 77 caracteres`() {
            with(TipoDaChaveENUM.CELULAR) {
                assertFalse(valida("1".repeat(78)))
            }
        }
    }

    @Nested
    inner class ALEATORIA {
        @Test
        fun `deve ser valido quando CHAVE_ALEATORIA for vazia ou nula`() {
            with(TipoDaChaveENUM.CHAVE_ALEATORIA) {
                assertTrue(valida(""))
                assertTrue(valida(null))
            }
        }

        @Test
        fun `nao deve validar quando CHAVE_ALEATORIA for inserida`() {
            with(TipoDaChaveENUM.CHAVE_ALEATORIA) {
                assertFalse(valida("1232313"))
            }
        }

    }

}