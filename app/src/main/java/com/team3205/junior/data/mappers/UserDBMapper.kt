package com.team3205.junior.data.mappers

import com.team3205.junior.data.db.entities.UserDB
import com.team3205.junior.data.repository.entities.User
import java.util.*

class UserDBMapper {
    fun fromUser(user: User): UserDB{
        return UserDB(
            user.id, user.login.toUpperCase(Locale.ROOT), user.avatar_url
        )
    }
}