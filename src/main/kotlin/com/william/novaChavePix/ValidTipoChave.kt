package com.william.novaChavePix

import com.william.novaChavePix.entidades.NovaChavePixRequest
import com.william.exceptions.ParametrosInvalidos
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
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
        if (value?.tipoDaChave == null) {
            return false

        }

        val ehValido = value.tipoDaChave.valida(value.valorChave)

        if (!ehValido) {
            throw ParametrosInvalidos("O campo ${value.tipoDaChave} está inválido")
        }

        return ehValido
    }


}