package hs.project.cof.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import hs.project.cof.data.db.ChatListDataBase

@HiltAndroidApp
class ApplicationClass : Application() {

    val database: ChatListDataBase by lazy { ChatListDataBase.getDatabase(this) }

    companion object {
        // 발신자 분류
        enum class SendBy(val viewType: Int, val viewName: String) {
            BOT(1, "Bot"),
            USER(2, "User"),
            VERSION(3, "Version"),
            TYPING(4, "Typing"),
            ERROR(5, "Error")
        }

        fun getViewType(sendBy: SendBy): Int {
            return sendBy.viewType
        }

        fun getViewName(viewType: Int): String {
            return when(viewType) {
                SendBy.BOT.viewType -> SendBy.BOT.viewName
                SendBy.USER.viewType -> SendBy.USER.viewName
                SendBy.VERSION.viewType -> SendBy.VERSION.viewName
                SendBy.TYPING.viewType -> SendBy.TYPING.viewName
                SendBy.ERROR.viewType -> SendBy.ERROR.viewName
                else -> ""
            }
        }

        // 채팅 모델명 및 버전명 분류
        enum class ChatVersion(val model: String, val versionNm: String) {
            CHAT("gpt-3.5-turbo", "채팅"),
            COMPLETION("text-davinci-003", "completion"),
            EDIT("text-davinci-edit-001", "문법 확인")
        }

        fun getChatModel(cv: ChatVersion): String {
            return cv.model
        }

        fun getChatVerNm(cv: ChatVersion): String {
            return cv.versionNm
        }

        // 다이얼로그 type 분류
        enum class DialogType(val typeNm: String) {
            VERSION("version"),
            TEMPERATURE("temperature"),
            RESET("reset"),
            REPORT("report")
        }

        fun getDialogType(dialogType: DialogType): String {
            return dialogType.typeNm
        }
    }

}