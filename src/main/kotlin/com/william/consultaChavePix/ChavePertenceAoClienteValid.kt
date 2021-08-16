package com.william.consultaChavePix

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
@Constraint(validatedBy = [ChavePertenceAoClienteValidator::class])
annotation class ValidPertenceAoCliente
    (
    val message: String = "erro",
    val groyps: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ChavePertenceAoClienteValidator : ConstraintValidator<ValidPertenceAoCliente, ConsultaChavePixRequestInterface> {


    override fun isValid(
        value: ConsultaChavePixRequestInterface?,
        annotationMetadata: AnnotationValue<ValidPertenceAoCliente>,
        context: ConstraintValidatorContext
    ): Boolean {

        // preciso voltar para validar aqui depois e eliminar o pertence ao cliente
        println("Vaidans")
        println(value!!)


        return true
    }


}