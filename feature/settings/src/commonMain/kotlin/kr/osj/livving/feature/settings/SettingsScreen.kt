package kr.osj.livving.feature.settings

import androidx.compose.runtime.Composable
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingInfoBox
import kr.osj.livving.core.ui.LivvingPrimaryButton
import kr.osj.livving.core.ui.LivvingSecondaryButton
import kr.osj.livving.core.ui.LivvingSettingGroup
import kr.osj.livving.core.ui.LivvingSettingRow
import kr.osj.livving.core.ui.LivvingToggleRow
import kr.osj.livving.core.ui.LivvingTone
import kr.osj.livving.core.ui.addLivvingMinutes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    userName: String,
    deadline: String,
    delayMinutes: Int,
    pushEnabled: Boolean,
    relationPushEnabled: Boolean,
    missedPushEnabled: Boolean,
    hasRegisteredPushToken: Boolean,
    pushTokenRegistering: Boolean,
    pushTokenMessage: String?,
    settingsSaving: Boolean,
    settingsMessage: String?,
    testNotificationSending: Boolean,
    testNotificationMessage: String?,
    onDeadlineClick: () -> Unit,
    onDelayClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onPushToggleClick: () -> Unit,
    onRelationPushToggleClick: () -> Unit,
    onMissedPushToggleClick: () -> Unit,
    onSendTestNotificationClick: () -> Unit,
    onRetryPushTokenClick: () -> Unit,
    onProfileClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val effectiveRelationPushEnabled = pushEnabled && relationPushEnabled
    val effectiveMissedPushEnabled = pushEnabled && missedPushEnabled
    val canSendTestNotification = pushEnabled && hasRegisteredPushToken && !testNotificationSending && !pushTokenRegistering

    LivvingHeader(
        title = "설정",
        sub = "안부 확인과 알림 방식을 설정해요.",
    )
    LivvingSettingGroup("시간 설정") {
        LivvingSettingRow("안부 마감 시간", deadline, onDeadlineClick)
        LivvingSettingRow("보호자 알림 기준", if (delayMinutes == 0) "마감 즉시" else "마감 ${delayMinutes}분 후", onDelayClick)
        LivvingSettingRow("알림 흐름 보기", "$deadline → ${addLivvingMinutes(deadline, delayMinutes)}", onScheduleClick)
    }
    LivvingSettingGroup("알림 설정") {
        LivvingToggleRow("푸시 알림", pushEnabled, enabled = !settingsSaving, onClick = onPushToggleClick)
        LivvingToggleRow(
            "관계 요청 알림",
            effectiveRelationPushEnabled,
            enabled = pushEnabled && !settingsSaving,
            onClick = onRelationPushToggleClick,
        )
        LivvingToggleRow(
            "안부 미확인 알림",
            effectiveMissedPushEnabled,
            enabled = pushEnabled && !settingsSaving,
            onClick = onMissedPushToggleClick,
        )
        if (settingsMessage != null) {
            LivvingInfoBox(
                text = settingsMessage,
                tone = LivvingTone.Neutral,
            )
        }
        if (!pushEnabled) {
            LivvingInfoBox(
                text = "푸시 알림이 꺼져 있으면 보호자 요청과 안부 미확인 알림을 받을 수 없어요.",
                tone = LivvingTone.Orange,
            )
        }
    }
    LivvingSettingGroup("알림 테스트") {
        LivvingPrimaryButton(
            text = if (testNotificationSending) "전송 중..." else "테스트 알림 보내기",
            enabled = canSendTestNotification,
            onClick = onSendTestNotificationClick,
        )
        if (pushEnabled && !hasRegisteredPushToken) {
            LivvingInfoBox(
                text = pushTokenMessage
                    ?: "이 기기의 푸시 토큰을 아직 등록하지 못했어요. 알림 권한을 허용한 뒤 다시 시도해 주세요.",
                tone = LivvingTone.Orange,
            )
            LivvingSecondaryButton(
                text = if (pushTokenRegistering) "토큰 등록 중..." else "푸시 토큰 다시 등록",
                enabled = !pushTokenRegistering,
                onClick = onRetryPushTokenClick,
            )
        } else if (pushTokenMessage != null) {
            LivvingInfoBox(
                text = pushTokenMessage,
                tone = LivvingTone.Neutral,
            )
        }
        if (testNotificationMessage != null) {
            LivvingInfoBox(
                text = testNotificationMessage,
                tone = LivvingTone.Neutral,
            )
        }
    }
    LivvingSettingGroup("계정 및 정보") {
        LivvingSettingRow("내 정보", userName, onProfileClick)
        LivvingSettingRow("개인정보 처리", "", onPrivacyClick)
        LivvingSettingRow("로그아웃", "", onLogoutClick)
    }
}
