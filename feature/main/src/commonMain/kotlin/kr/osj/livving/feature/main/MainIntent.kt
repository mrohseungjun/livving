package kr.osj.livving.feature.main

import kr.osj.livving.feature.relations.RelationsTab

sealed interface MainIntent {
    data object CompleteCheckIn : MainIntent
    data object ToggleLateState : MainIntent
    data class SelectDeadline(val time: String) : MainIntent
    data class SaveDeadline(val fromSettings: Boolean) : MainIntent
    data class SelectDelay(val minutes: Int) : MainIntent
    data class SaveDelay(val fromSettings: Boolean) : MainIntent
    data class ToggleTerms(val item: TermsItem) : MainIntent
    data object ToggleAllTerms : MainIntent
    data class SelectRelationTab(val tab: RelationsTab) : MainIntent
    data class SelectGuardian(val guardianId: Long) : MainIntent
    data class SelectWatchingUser(val userId: String) : MainIntent
    data object RefreshRelations : MainIntent
    data object DisconnectSelectedGuardian : MainIntent
    data class ChangePhoneNumber(val value: String) : MainIntent
    data object TogglePhoneCallEnabled : MainIntent
    data object SavePhoneContact : MainIntent
    data class RegisterPushToken(val token: MainPushToken) : MainIntent
    data object PushTokenFetchStarted : MainIntent
    data object PushTokenFetchFailed : MainIntent
    data object LoadNotifications : MainIntent
    data class SelectNotification(val notificationId: String) : MainIntent
    data object CreateInvite : MainIntent
    data class ChangeManualInviteCode(val value: String) : MainIntent
    data object SubmitManualInviteCode : MainIntent
    data object AcceptInvite : MainIntent
    data object RejectInvite : MainIntent
    data object TogglePush : MainIntent
    data object ToggleRelationPush : MainIntent
    data object ToggleMissedPush : MainIntent
    data object SendTestNotification : MainIntent
    data class SendCheckInRequest(val targetUserId: String, val message: String) : MainIntent
    data object Logout : MainIntent
}
