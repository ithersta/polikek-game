package com.ithersta.polikekgame.repository

import com.ithersta.polikekgame.entities.Identity
import java.util.concurrent.ConcurrentHashMap

class InMemoryIdentityRepository : IdentityRepository {
    private val identities = ConcurrentHashMap<Long, Identity>()

    override fun get(userId: Long): Identity? {
        return identities[userId]
    }

    override fun set(userId: Long, identity: Identity) {
        identities[userId] = identity
    }
}
