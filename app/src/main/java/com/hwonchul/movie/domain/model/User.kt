package com.hwonchul.movie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String = TEMP_UID,
    val phone: String,
    val nickname: String? = "이름없음",
) : Parcelable {

    fun isTemp(): Boolean = uid == TEMP_UID

    fun toMap() : HashMap<String, Any?> {
        return  hashMapOf(
            FIELD_PHONE_NUMBER to phone,
            FIELD_NICK_NAME to nickname,
        )
    }

    companion object {
        val EMPTY: User = User(
            phone = "0000000000"
        )

        private const val TEMP_UID = "temp"

        const val FIELD_PHONE_NUMBER = "phoneNumber"
        const val FIELD_NICK_NAME = "nickName"
    }
}


