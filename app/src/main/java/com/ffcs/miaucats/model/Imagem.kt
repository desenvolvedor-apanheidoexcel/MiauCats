package com.ffcs.miaucats.model

import java.io.Serializable

data class Imagem(
    val id: String,
    val description: String?,
    val link: String
): Serializable


