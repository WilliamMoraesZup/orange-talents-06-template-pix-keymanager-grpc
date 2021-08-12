package com.william.deletaChavePix

import com.william.novaChavePix.ChavePixRepository
import com.william.shared.ErroCustomizado
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Target
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.reflect.KClass

@MustBeDocumented
@Target(
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER,
    ElementType.TYPE_USE
)
@Constraint(validatedBy = [ExistsPixIdValidator::class])
@Retention(RUNTIME)
annotation class ExistsPixId
    (
    val message: String = "O PixId nao foi encontrado",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ExistsPixIdValidator(
    @Inject val repository: ChavePixRepository
) : ConstraintValidator<ExistsPixId, String> {
    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<ExistsPixId>,
        context: ConstraintValidatorContext
    ): Boolean {
        println("validando pix id")

        if (repository.existsById(value!!.toLong())) {
            println("chave pix valida")
            return true
        } else throw ErroCustomizado("Chave Pix nao encontrada !!")

    }


}
