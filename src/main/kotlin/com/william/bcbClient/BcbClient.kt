package com.william.bcbClient

import com.william.bcbClient.classes.CriarChaveBcbRequest
import com.william.bcbClient.classes.CriarChaveBcbResponse
import com.william.bcbClient.classes.DeletePixKeyRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client


@Client("\${endereco.client.bcb}")
interface BcbClient {


    @Post(
        "/api/v1/pix/keys",
        consumes = [MediaType.APPLICATION_XML],
        processes = [MediaType.APPLICATION_XML]
    )
    fun registraChavePix(@Body request: CriarChaveBcbRequest): HttpResponse<CriarChaveBcbResponse>

    @Delete("/api/v1/pix/keys/{id}",
        consumes = [MediaType.APPLICATION_XML],
        processes = [MediaType.APPLICATION_XML]
    )
    fun removeChavePix(@PathVariable id:String, @Body request: DeletePixKeyRequest) : HttpResponse<DeletePixKeyResponse>
}