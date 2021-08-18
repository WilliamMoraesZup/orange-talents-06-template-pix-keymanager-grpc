package com.william.listaChavesCliente

import com.william.*
import com.william.exceptions.ErrorHandler
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ErrorHandler
class ListaChavesClienteEndpoint(
    @Inject private val service: ListaChavesService
) : ChavePixServiceListaChavesClienteGrpc.ChavePixServiceListaChavesClienteImplBase() {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun listaChavesCliente(
        request: ListaChavesClienteRequest,
        responseObserver: StreamObserver<ListaChavesClienteResponse>
    ) {

        val listaChaves = service.consulta(request.clienteId)

        val mapChaves = listaChaves!!.map {
            ListaChavesClienteResponse.ChaveClienteLista.newBuilder()
                .setPixId(it.pixId)
                .setValorChave(it.valorChave)
                .setTipoDaChave(TipoDaChave.valueOf(it.tipoDaChave.name))
                .setTipoDaConta(TipoDaConta.valueOf(it.tipoDaConta.name))
                .setDataCriacao(it.dataCriacao.toString())
                .build()
        }


        responseObserver.onNext(
            ListaChavesClienteResponse.newBuilder().setClienteId(request.clienteId).addAllListaChaves(mapChaves).build()
        )
        responseObserver.onCompleted()
    }
}