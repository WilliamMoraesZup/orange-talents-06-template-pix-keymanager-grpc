package com.william.novaChavePix

import com.william.novaChavePix.classes.NovaChavePixRequest
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass


@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidTipoChave(
    val message: String = "chave Pix inválida (\${validatedValue.tipoDaChave})",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ValidPixKeyValidator : javax.validation.ConstraintValidator<ValidTipoChave, NovaChavePixRequest> {

    override fun isValid(value: NovaChavePixRequest?, context: javax.validation.ConstraintValidatorContext): Boolean {

        // must be validated with @NotNull
        if (value?.tipoDaChave == null) {
            return true
        }

        val valid = value.tipoDaChave.valida(value.valorChave)
        if (!valid) {
            // https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-custom-property-paths
            context.disableDefaultConstraintViolation()
            context
                .buildConstraintViolationWithTemplate(context.defaultConstraintMessageTemplate) // or "chave Pix inválida (${value.tipo})"
                .addPropertyNode("chave").addConstraintViolation()
        }

        return valid
    }
}
