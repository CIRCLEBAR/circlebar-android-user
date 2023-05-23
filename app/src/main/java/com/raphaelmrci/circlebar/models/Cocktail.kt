package com.raphaelmrci.circlebar.models

data class Cocktail(
    var name: String? = null,
    val id: Int? = null,
    var image: String? = null,
    var description: String? = null,
    var collections: MutableList<String>
)