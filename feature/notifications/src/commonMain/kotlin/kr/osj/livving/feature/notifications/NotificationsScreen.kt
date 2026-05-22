package kr.osj.livving.feature.notifications

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.osj.livving.core.ui.LivvingCenterText
import kr.osj.livving.core.ui.LivvingHeader
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
)

@Composable
fun NotificationsScreen(
    notifications: List<NotificationUiModel>,
    onNotificationClick: (NotificationUiModel) -> Unit,
    onRequestClick: () -> Unit,
    viewModel: NotificationsViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "알림",
        sub = "요청과 안부 상태 알림을 확인해요.",
    )
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
            onClick = { onNotificationClick(notification) },
        )
        Spacer(Modifier.height(14.dp))
    }
    LivvingNoticeRow(
        title = "보호자 요청",
        time = "지금",
        desc = "박선영님이 나를 보호자로 초대했어요.",
        tone = LivvingTone.Purple,
        onClick = onRequestClick,
    )
    Spacer(Modifier.height(14.dp))
    LivvingNoticeRow("연결 완료", "어제", "김지연님이 보호자 요청을 수락했어요.", LivvingTone.Green) {}
}
