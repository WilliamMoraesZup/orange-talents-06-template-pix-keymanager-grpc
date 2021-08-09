package com.william.novaChavePix.classes

import com.william.TipoDaChave
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
    @ValidUUID @field:NotBlank val idCliente: String?,
    @field:NotNull val tipoDaChave: TipoDaChaveENUM?,
    @field:NotBlank @Size(max = 77) val valorChave: String?,
    @field:NotNull val tipoDaConta: TipoDaConta?
) {

    fun toModel(contaAssociada: ContaAssociada): ChavePix {

        return ChavePix(
//            idCliente = UUID.fromString(this.idCliente),
            idCliente = this.idCliente,
            tipoDaChave = TipoDaChave.valueOf(this.tipoDaChave!!.name),
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
