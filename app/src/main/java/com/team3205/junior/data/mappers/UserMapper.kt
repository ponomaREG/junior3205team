package com.team3205.junior.data.mappers

import com.team3205.junior.data.db.entities.UserDB
import com.team3205.junior.data.network.entities.UserResponse
import com.team3205.junior.data.repository.entities.User

class UserMapper {
    fun fromUserDB(userDB: UserDB): User{
        return User(userDB.id, userDB.login, userDB.avatar_url)
    }

    fun fromUserResponse(userResponse: UserResponse): User{
        return User(userResponse.id, userResponse.login, userResponse.avatar_url)
    }
}