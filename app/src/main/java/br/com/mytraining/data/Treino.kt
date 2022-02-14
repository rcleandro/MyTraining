package br.com.mytraining.data

import java.io.Serializable
import java.sql.Timestamp
import java.util.*

data class Treino(
    val nome: Long = 0,
    val descricao: String = "",
    val data: String = "",
    val exercicio: List<Exercicio> = listOf()
) : Serializable

