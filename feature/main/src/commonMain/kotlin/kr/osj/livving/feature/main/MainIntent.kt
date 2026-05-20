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
    data object CreateInvite : MainIntent
    data object TogglePush : MainIntent
    data object ToggleRelationPush : MainIntent
    data object ToggleMissedPush : MainIntent
}
