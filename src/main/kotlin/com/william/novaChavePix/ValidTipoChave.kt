package com.william.novaChavePix

import com.william.novaChavePix.classes.NovaChavePixRequest
import com.william.shared.ErroCustomizado
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass


@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidTipoChave
    (
    val message: String = "Chave Pix Invalida",
    val groyps: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ValidPixKeyValidator : ConstraintValidator<ValidTipoChave, NovaChavePixRequest> {
    override fun isValid(
        value: NovaChavePixRequest?,
        annotationMetadata: AnnotationValue<ValidTipoChave>,
        context: ConstraintValidatorContext
    ): Boolean {
        println("VALIDANDO KEY")
        if (value?.tipoDaChave == null) {
            return false

        }
        println(value.tipoDaChave)
        println(value.valorChave)
        val ehValido = value.tipoDaChave.valida(value.valorChave)
        if (ehValido == false) {
            throw ErroCustomizado("O campo ${value.tipoDaChave} está inválido")
        }

        return ehValido
    }


}