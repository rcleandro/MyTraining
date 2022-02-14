package br.com.mytraining.data

import java.io.Serializable

data class Exercicio(
    val imagem: String? = "",
    val nome: Long = 0,
    val observacoes: String = "",
    var selecionado: Boolean? = false
) : Serializable
