package com.hwonchul.movie.domain.model

import android.os.Parcelable
import com.hwonchul.movie.data.remote.model.UserDto
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class User(
    val uid: String = TEMP_UID,
    val phone: String,
    val nickname: String? = TEMP_NICK,
    val userImage: String? = null,
    val updatedAt: LocalDateTime? = LocalDateTime.now()
) : Parcelable {

    companion object {
        val EMPTY: User = User(
            phone = "0000000000"
        )

        const val TEMP_UID = "temp"
        const val TEMP_NICK = "이름없음"
    }
}

fun User.toDto() = UserDto(
    uid = uid,
    phone = phone,
    nickname = nickname,
    userImage = userImage,
    updatedAt = updatedAt?.toString(),
)