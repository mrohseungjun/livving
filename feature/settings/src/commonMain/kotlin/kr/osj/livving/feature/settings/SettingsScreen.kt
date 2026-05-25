package kr.osj.livving.feature.settings

import androidx.compose.runtime.Composable
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingInfoBox
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
    onDeadlineClick: () -> Unit,
    onDelayClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onPushToggleClick: () -> Unit,
    onRelationPushToggleClick: () -> Unit,
    onMissedPushToggleClick: () -> Unit,
    onProfileClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "설정",
        sub = "안부 확인과 알림 방식을 설정해요.",
    )
    LivvingSettingGroup("시간 설정") {
        LivvingSettingRow("안부 마감 시간", deadline, onDeadlineClick)
        LivvingSettingRow("보호자 알림 기준", "마감 ${delayMinutes}분 후", onDelayClick)
        LivvingSettingRow("알림 흐름 보기", "$deadline → ${addLivvingMinutes(deadline, delayMinutes)}", onScheduleClick)
    }
    LivvingSettingGroup("알림 설정") {
        LivvingToggleRow("푸시 알림", pushEnabled, onClick = onPushToggleClick)
        LivvingToggleRow("관계 요청 알림", relationPushEnabled, enabled = pushEnabled, onClick = onRelationPushToggleClick)
        LivvingToggleRow("안부 미확인 알림", missedPushEnabled, enabled = pushEnabled, onClick = onMissedPushToggleClick)
        if (!pushEnabled) {
            LivvingInfoBox(
                text = "푸시 알림이 꺼져 있으면 보호자 요청과 안부 미확인 알림을 받을 수 없어요.",
                tone = LivvingTone.Orange,
            )
        }
    }
    LivvingSettingGroup("계정 및 정보") {
        LivvingSettingRow("내 정보", userName, onProfileClick)
        LivvingSettingRow("개인정보 처리", "", onPrivacyClick)
        LivvingSettingRow("로그아웃", "", onLogoutClick)
    }
}
