package com.william.deletaChavePix

import com.william.novaChavePix.ChavePixRepository
import com.william.shared.ErroCustomizado
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import java.lang.annotation.ElementType.*
import java.lang.annotation.Target
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.reflect.KClass

@MustBeDocumented
@Target(METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE)
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

        if (repository.existsByValorChave(value!!)) {
            println("chave pix valida")
            return true
        } else
        //Preciso de um ERROR handler para retornar  um Status runtime
            throw ErroCustomizado("Chave Pix nao encontrada !!")

    }


}
