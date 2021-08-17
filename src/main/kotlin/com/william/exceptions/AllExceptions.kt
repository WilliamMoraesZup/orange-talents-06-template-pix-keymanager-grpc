package com.william.exceptions

class ParametrosInvalidos(message: String) : RuntimeException(message)

class StatusAlreadyExists(message: String) : RuntimeException(message)

class StatusNotFound(message: String) : RuntimeException(message)

class ErroChaveJaExisteBCB(message: String) : RuntimeException(message)

class ErroCustomizado(mensagem: String) : RuntimeException(mensagem)

