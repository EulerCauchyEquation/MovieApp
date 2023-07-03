package com.hwonchul.movie.data.remote.model

import com.hwonchul.movie.data.local.model.UserEntity
import com.hwonchul.movie.domain.model.User

data class UserDto(
    val uid: String,
    val phone: String,
    val nickname: String?,
    val userImage: String?,
    val updatedAt: String?,
) {
    fun isTemp(): Boolean = uid == User.TEMP_UID

    fun toMap(): HashMap<String, Any?> {
        return hashMapOf(
            FIELD_PHONE_NUMBER to phone,
            FIELD_NICK_NAME to nickname,
            FIELD_USER_IMAGE to userImage,
            FIELD_UPDATED_AT to updatedAt,
        )
    }

    companion object {
        const val FIELD_PHONE_NUMBER = "phoneNumber"
        const val FIELD_NICK_NAME = "nickName"
        const val FIELD_USER_IMAGE = "userImage"
        const val FIELD_UPDATED_AT = "updatedAt"
    }
}

fun UserDto.toEntity() = UserEntity(
    uid = uid,
    phone = phone,
    nickname = nickname,
    userImage = userImage,
    updatedAt = updatedAt,
)