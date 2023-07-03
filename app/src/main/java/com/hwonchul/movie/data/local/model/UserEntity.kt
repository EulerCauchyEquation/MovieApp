package com.hwonchul.movie.data.local.model

import com.hwonchul.movie.data.remote.model.UserDto
import com.hwonchul.movie.domain.model.User
import java.time.LocalDateTime

data class UserEntity(
    val uid: String,
    val phone: String,
    val nickname: String?,
    val userImage: String?,
    val updatedAt: String?,
)

fun UserEntity.toDomain() = User(
    uid = uid,
    phone = phone,
    nickname = nickname,
    userImage = userImage,
    updatedAt = updatedAt?.let { LocalDateTime.parse(it) },
)

fun UserEntity.toDto() = UserDto(
    uid = uid,
    phone = phone,
    nickname = nickname,
    userImage = userImage,
    updatedAt = updatedAt,
)
