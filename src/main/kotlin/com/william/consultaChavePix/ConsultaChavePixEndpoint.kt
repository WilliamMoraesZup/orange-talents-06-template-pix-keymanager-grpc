package com.william.consultaChavePix

import com.william.ChavePixServiceConsultaGrpc
import com.william.ConsultaChavePixRequest
import com.william.ConsultaChavePixResponse
import com.william.exceptions.ErrorHandler
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton
//@ErrorHandler
@Singleton
class ConsultaChavePixEndpoint(@Inject val consultaService: ConsultaService) :
    ChavePixServiceConsultaGrpc.ChavePixServiceConsultaImplBase() {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)
    override fun consulta(
        request: ConsultaChavePixRequest,
        responseObserver: StreamObserver<ConsultaChavePixResponse>
    ) {
        LOGGER.info("[ENDPOINT] Request -> to Model")

        val toModel = request.toModel()

        LOGGER.info("[ENDPOINT] Chamando Service")
        val consultaDados = consultaService.consultaDados(toModel, responseObserver)

        LOGGER.info("[ENDPOINT] objeto retornado {$consultaDados}")

        responseObserver.onNext(consultaDados)
        responseObserver.onCompleted()

    }


}