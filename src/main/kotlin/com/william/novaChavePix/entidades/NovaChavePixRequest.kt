package com.william.novaChavePix.entidades

import com.william.TipoDaConta
import com.william.novaChavePix.TipoDaChaveENUM
import com.william.novaChavePix.ValidTipoChave
import com.william.validacoes.ValidUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidTipoChave
@Introspected
data class NovaChavePixRequest
    (
    @field:ValidUUID @field:NotBlank val idCliente: String?,
    @field:NotNull val tipoDaChave: TipoDaChaveENUM?,
    @field:Size(
        message = "O valor da chave deve conter no máximo 77 caracteres",
        max = 77
    ) val valorChave: String?,
    @field:NotNull val tipoDaConta: TipoDaConta?
) {

    fun toModel(contaAssociada: ContaAssociada): ChavePix {

        return ChavePix(
            idCliente = this.idCliente,
            tipoDaChave = TipoDaChaveENUM.valueOf(this.tipoDaChave!!.name),
            valorChave = if (this.tipoDaChave == TipoDaChaveENUM.CHAVE_ALEATORIA) UUID.randomUUID()
                .toString()
            else this.valorChave!!,
            tipoDaConta = TipoDaConta.valueOf(this.tipoDaConta!!.name),
            conta = contaAssociada

        )
    }

    override fun toString(): String {
        return "NovaChavePix(idCliente=$idCliente, tipoDaChave=$tipoDaChave, valorChave=$valorChave, tipoDaConta=$tipoDaConta)"
    }
}
