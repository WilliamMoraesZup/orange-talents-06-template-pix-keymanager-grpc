package com.william.consultaChavePix

import com.william.ChavePixServiceConsultaGrpc
import com.william.ConsultaChavePixRequest
import com.william.ConsultaChavePixResponse
import com.william.novaChavePix.toModel
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
class ConsultaChavePixEndpoint(val consultaService: ConsultaService) :
    ChavePixServiceConsultaGrpc.ChavePixServiceConsultaImplBase() {

    override fun consulta(
        request: ConsultaChavePixRequest?,
        responseObserver: StreamObserver<ConsultaChavePixResponse>?
    ) {


        val toModel = request!!.toModel()
        consultaService.consultaDados(toModel)


    }
}