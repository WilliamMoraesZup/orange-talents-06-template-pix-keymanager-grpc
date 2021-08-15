package com.william.exceptions

class ParametrosInvalidos(message: String) : Exception(message)

class ChaveJaExisteSistema(message: String) : Exception(message)

class ChaveNaoEncontradaNoSistema(message: String) : Exception(message)

class ClienteNotFound(message: String) : Exception(message)

class ErroChaveJaExisteBCB(message: String) : Exception(message)

class ErroCustomizado(mensagem: String) : RuntimeException(mensagem)