package hs.project.cof.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val message: String,
    val sendBy: Int
) : Parcelable
