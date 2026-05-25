package kr.osj.livving.feature.relations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.osj.livving.core.ui.LivvingAvatar
import kr.osj.livving.core.ui.LivvingBadge
import kr.osj.livving.core.ui.LivvingCard
import kr.osj.livving.core.ui.LivvingCoral
import kr.osj.livving.core.ui.LivvingDanger
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingInfoBox
import kr.osj.livving.core.ui.LivvingMuted
import kr.osj.livving.core.ui.LivvingPrimaryButton
import kr.osj.livving.core.ui.LivvingSegmented
import kr.osj.livving.core.ui.LivvingSuccess
import kr.osj.livving.core.ui.LivvingTextField
import kr.osj.livving.core.ui.LivvingTone
import kr.osj.livving.core.ui.LivvingWarning
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RelationsScreen(
    selectedTab: RelationsTab,
    guardians: List<RelationGuardianUiModel>,
    watchingUsers: List<WatchingUserUiModel>,
    deadline: String,
    alertAt: String,
    manualInviteCode: String,
    manualInviteError: String?,
    onMyGuardiansClick: () -> Unit,
    onWatchingClick: () -> Unit,
    onGuardianClick: (RelationGuardianUiModel) -> Unit,
    onManualInviteCodeChange: (String) -> Unit,
    onManualInviteSubmit: () -> Unit,
    onWatchingUserClick: (WatchingUserUiModel) -> Unit,
    onWatchingCallClick: (WatchingUserUiModel) -> Unit,
    viewModel: RelationsViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "관계",
        sub = "내 보호자와 내가 보호 중인 사람을 관리해요.",
    )
    LivvingSegmented(
        left = "내 보호자",
        right = "내가 보호 중",
        selectedLeft = selectedTab == RelationsTab.MyGuardians,
        onLeft = onMyGuardiansClick,
        onRight = onWatchingClick,
    )
    Spacer(Modifier.height(24.dp))
    var searchQuery by remember(selectedTab) { mutableStateOf("") }
    val normalizedQuery = searchQuery.trim()
    val filteredGuardians = remember(guardians, normalizedQuery) {
        guardians.filter { guardian ->
            normalizedQuery.isBlank() ||
                guardian.name.contains(normalizedQuery, ignoreCase = true) ||
                guardian.relation.contains(normalizedQuery, ignoreCase = true) ||
                guardian.status.label.contains(normalizedQuery, ignoreCase = true)
        }
    }
    val filteredWatchingUsers = remember(watchingUsers, normalizedQuery) {
        watchingUsers.filter { user ->
            normalizedQuery.isBlank() ||
                user.name.contains(normalizedQuery, ignoreCase = true) ||
                user.text.contains(normalizedQuery, ignoreCase = true) ||
                user.sub.contains(normalizedQuery, ignoreCase = true)
        }
    }

    if (selectedTab == RelationsTab.MyGuardians) {
        LivvingInfoBox(
            text = "보호자 그룹 링크를 한 번 공유하고, 각 보호자가 앱에서 수락하면 연결됩니다.",
            tone = LivvingTone.Neutral,
        )
        Spacer(Modifier.height(14.dp))
        ManualInviteCodeCard(
            value = manualInviteCode,
            error = manualInviteError,
            onValueChange = onManualInviteCodeChange,
            onSubmit = onManualInviteSubmit,
        )
        Spacer(Modifier.height(18.dp))
        RelationSearchField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = "이름, 관계, 상태 검색",
            resultCount = filteredGuardians.size,
        )
        Spacer(Modifier.height(14.dp))
        if (filteredGuardians.isEmpty()) {
            EmptySearchResult()
        }
        filteredGuardians.forEachIndexed { index, guardian ->
            GuardianRow(
                guardian = guardian,
                tone = listOf(LivvingTone.Coral, LivvingTone.Green, LivvingTone.Orange).getOrElse(index) { LivvingTone.Purple },
                onClick = { onGuardianClick(guardian) },
            )
            Spacer(Modifier.height(14.dp))
        }
    } else {
        RelationSearchField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = "이름, 안부 상태 검색",
            resultCount = filteredWatchingUsers.size,
        )
        Spacer(Modifier.height(14.dp))
        if (filteredWatchingUsers.isEmpty()) {
            EmptySearchResult()
        }
        filteredWatchingUsers.forEach { user ->
            WatchingRow(
                user = user,
                deadline = deadline,
                alertAt = alertAt,
                onClick = { onWatchingUserClick(user) },
                onCallClick = { onWatchingCallClick(user) },
            )
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
private fun RelationSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    resultCount: Int,
) {
    LivvingTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = "검색 결과 ${resultCount}명",
        color = LivvingMuted,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun EmptySearchResult() {
    LivvingCard(Modifier.fillMaxWidth()) {
        Text(
            text = "검색 결과가 없어요",
            modifier = Modifier.padding(18.dp),
            color = LivvingMuted,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
    }
    Spacer(Modifier.height(14.dp))
}

@Composable
private fun ManualInviteCodeCard(
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    LivvingCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp)) {
            Text("초대코드 직접 입력", fontSize = 17.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(6.dp))
            Text("링크가 열리지 않으면 초대코드나 링크 전체를 붙여넣으세요.", color = LivvingMuted, fontSize = 13.sp)
            Spacer(Modifier.height(14.dp))
            LivvingTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = "예: livving://join/AB12CD 또는 AB12CD",
            )
            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Text(error, color = LivvingDanger, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            LivvingPrimaryButton(
                text = "초대 요청 확인하기",
                enabled = value.isNotBlank(),
                onClick = onSubmit,
            )
        }
    }
}

private val RelationGuardianStatus.label: String
    get() = when (this) {
        RelationGuardianStatus.Accepted -> "연결됨"
        RelationGuardianStatus.Pending -> "초대 대기"
    }

@Composable
private fun GuardianRow(
    guardian: RelationGuardianUiModel,
    tone: LivvingTone,
    onClick: () -> Unit,
) {
    LivvingCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            LivvingAvatar(guardian.name, tone = tone)
            Column(Modifier.weight(1f)) {
                Text(guardian.name, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(guardian.relation, color = LivvingMuted)
            }
            when (guardian.status) {
                RelationGuardianStatus.Accepted -> LivvingBadge("연결됨", LivvingTone.Green)
                RelationGuardianStatus.Pending -> LivvingBadge("초대 대기", LivvingTone.Orange)
            }
        }
    }
}

@Composable
private fun WatchingRow(
    user: WatchingUserUiModel,
    deadline: String,
    alertAt: String,
    onClick: () -> Unit,
    onCallClick: () -> Unit,
) {
    val tone = when (user.state) {
        WatchingState.Safe -> LivvingTone.Green
        WatchingState.Waiting -> LivvingTone.Orange
        WatchingState.Missed -> LivvingTone.Coral
    }
    val color = when (user.state) {
        WatchingState.Safe -> LivvingSuccess
        WatchingState.Waiting -> LivvingWarning
        WatchingState.Missed -> LivvingDanger
    }
    LivvingCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            LivvingAvatar(user.name, tone = tone)
            Column(Modifier.weight(1f)) {
                Text(user.name, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(user.text, color = color, fontWeight = FontWeight.Bold)
                Text("마감 $deadline · 알림 $alertAt", color = Color(0xFFA3A3A3), fontSize = 12.sp)
            }
            Text(
                text = if (user.phoneNumber != null) "전화" else "번호 없음",
                modifier = Modifier.clickable(
                    enabled = user.phoneNumber != null,
                    onClick = onCallClick,
                ),
                color = if (user.phoneNumber != null) LivvingCoral else LivvingMuted,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
