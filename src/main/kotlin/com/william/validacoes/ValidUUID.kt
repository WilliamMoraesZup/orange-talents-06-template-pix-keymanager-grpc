package com.william.validacoes

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Pattern
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass


@ReportAsSingleViolation
@Constraint(validatedBy = [])
@Pattern(
    regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}\$",
    flags = [Pattern.Flag.CASE_INSENSITIVE]
)

@Retention(RUNTIME)
@Target(
    FIELD,
    CONSTRUCTOR, VALUE_PARAMETER, PROPERTY
)
annotation class ValidUUID
    (
    val message: String = "Não é um fomarto UIDD válido",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)