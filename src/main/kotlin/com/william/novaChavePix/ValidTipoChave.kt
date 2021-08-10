package com.william.novaChavePix

import com.william.novaChavePix.classes.NovaChavePixRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass


@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
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
        return value.tipoDaChave.valida(value.valorChave)
    }


}