package com.hwonchul.movie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String = TEMP_UID,
    val phone: String,
    val nickname: String? = TEMP_NICK,
    val userImage: String? = null,
) : Parcelable {
    fun isTemp(): Boolean = uid == User.TEMP_UID

    fun toMap(): HashMap<String, Any?> {
        return hashMapOf(
            FIELD_PHONE_NUMBER to phone,
            FIELD_NICK_NAME to nickname,
            FIELD_USER_IMAGE to userImage,
        )
    }

    companion object {
        const val FIELD_PHONE_NUMBER = "phoneNumber"
        const val FIELD_NICK_NAME = "nickName"
        const val FIELD_USER_IMAGE = "userImage"

        val EMPTY: User = User(
            phone = "0000000000"
        )

        private const val TEMP_UID = "temp"
        private const val TEMP_NICK = "이름없음"
    }
}