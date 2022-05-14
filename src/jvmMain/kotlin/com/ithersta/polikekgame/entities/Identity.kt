package com.ithersta.polikekgame.entities

data class Identity(
    val firstName: String,
    val lastName: String?,
    val username: String?
) {
    override fun toString(): String {
        return "$username ($firstName $lastName)"
    }
}
