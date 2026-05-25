package kr.osj.livving.feature.notifications

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.osj.livving.core.ui.LivvingCenterText
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingInfoBox
import kr.osj.livving.core.ui.LivvingMuted
import kr.osj.livving.core.ui.LivvingNoticeRow
import kr.osj.livving.core.ui.LivvingTone
import org.koin.compose.viewmodel.koinViewModel

data class NotificationUiModel(
    val id: String,
    val title: String,
    val time: String,
    val desc: String,
    val tone: LivvingTone,
    val read: Boolean,
    val opensAlert: Boolean,
    val opensRequest: Boolean,
)

@Composable
fun NotificationsScreen(
    notifications: List<NotificationUiModel>,
    pushEnabled: Boolean,
    relationPushEnabled: Boolean,
    missedPushEnabled: Boolean,
    onNotificationClick: (NotificationUiModel) -> Unit,
    viewModel: NotificationsViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "알림",
        sub = "요청과 안부 상태 알림을 확인해요.",
    )
    if (!pushEnabled || !relationPushEnabled || !missedPushEnabled) {
        LivvingInfoBox(
            text = disabledNotificationText(pushEnabled, relationPushEnabled, missedPushEnabled),
            tone = LivvingTone.Orange,
        )
        Spacer(Modifier.height(14.dp))
    }
    if (notifications.isEmpty()) {
        LivvingCenterText("아직 도착한 알림이 없어요.", color = LivvingMuted)
        Spacer(Modifier.height(14.dp))
    }
    notifications.forEach { notification ->
        LivvingNoticeRow(
            title = notification.title,
            time = notification.time,
            desc = notification.desc,
            tone = notification.tone,
            read = notification.read,
            onClick = { onNotificationClick(notification) },
        )
        Spacer(Modifier.height(14.dp))
    }
}

private fun disabledNotificationText(
    pushEnabled: Boolean,
    relationPushEnabled: Boolean,
    missedPushEnabled: Boolean,
): String {
    return when {
        !pushEnabled -> "푸시 알림이 꺼져 있어요. 설정에서 켜면 보호자 요청과 안부 미확인 알림을 받을 수 있어요."
        !relationPushEnabled && !missedPushEnabled -> "관계 요청 알림과 안부 미확인 알림이 꺼져 있어요."
        !relationPushEnabled -> "관계 요청 알림이 꺼져 있어요."
        else -> "안부 미확인 알림이 꺼져 있어요."
    }
}
