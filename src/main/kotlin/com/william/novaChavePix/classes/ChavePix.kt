package com.william.novaChavePix.classes

import com.william.TipoDaConta
import com.william.bcbClient.classes.CriarChaveBcbResponse
import com.william.bcbClient.classes.KeyTypeEnum
import com.william.novaChavePix.TipoDaChaveENUM
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@Entity
class ChavePix(
    @field:NotNull
    @Column(nullable = false)
    val idCliente: String?,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoDaChave: TipoDaChaveENUM,

    @field:NotBlank
    @Column(nullable = false, unique = true)
    @field:Size(max = 77)
    var valorChave: String,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoDaConta: TipoDaConta,

    @field:Valid
    @Embedded
    val conta: ContaAssociada


) {

    var createdAt: LocalDateTime? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    override fun toString(): String {
        return "ChavePix(idCliente=$idCliente, tipoDaChave=$tipoDaChave, valorChave='$valorChave', tipoDaConta=$tipoDaConta, id=$id)"
    }

    fun atualizaHorarioChaveAleatoria(novosDados: CriarChaveBcbResponse) {
        createdAt = novosDados.createdAt
        if (novosDados.keyType == KeyTypeEnum.RANDOM) {
            valorChave = novosDados.key
        }

    }

}
