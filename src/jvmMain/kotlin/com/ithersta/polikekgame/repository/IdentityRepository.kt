package com.ithersta.polikekgame.repository

import com.ithersta.polikekgame.entities.Identity

interface IdentityRepository {
    fun get(userId: Long): Identity?
    fun set(userId: Long, identity: Identity)
}
