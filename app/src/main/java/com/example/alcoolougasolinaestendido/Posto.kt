package com.example.alcoolougasolinaestendido

import java.util.UUID

data class Posto(
    val id: String = UUID.randomUUID().toString(),
    var nome: String,
    var precoAlcool: Double,
    var precoGasolina: Double,
    var dataRegistro: String,
    var latitude: Double?,
    var longitude: Double?,
    var melhorOpcao: String // <-- ADICIONE ESTA LINHA
)