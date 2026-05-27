package kr.osj.livving.feature.relations

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.osj.livving.core.ui.LivvingCard
import kr.osj.livving.core.ui.LivvingCoral
import kr.osj.livving.core.ui.LivvingCoralSoft
import kr.osj.livving.core.ui.LivvingGradientCard
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingInfoBox
import kr.osj.livving.core.ui.LivvingMenuRow
import kr.osj.livving.core.ui.LivvingMuted
import kr.osj.livving.core.ui.LivvingSurface
import kr.osj.livving.core.ui.LivvingTone
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InviteScreen(
    inviteCode: String?,
    inviteLink: String?,
    pendingCount: Int,
    acceptedCount: Int,
    onBackClick: () -> Unit,
    onCreateInviteClick: () -> Unit,
    onCopyInviteClick: () -> Unit,
    onShareInviteClick: () -> Unit,
    viewModel: InviteViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "보호자 초대하기",
        sub = "한 명씩 보내지 말고, 보호자 그룹 링크를 가족 단톡방에 한 번 공유하세요.",
        showLogo = false,
        onBack = onBackClick,
    )
    Spacer(Modifier.height(18.dp))
    LivvingMenuRow("그룹 초대 링크 생성", "보호자 여러 명이 같은 링크로 참여", "↗", onClick = onCreateInviteClick)
    Spacer(Modifier.height(14.dp))
    LivvingMenuRow("카카오톡 단톡방에 공유", "가족/친구 방에 링크 한 번 전송", "톡", onClick = onShareInviteClick)
    Spacer(Modifier.height(24.dp))
    LivvingCard {
        Column(Modifier.padding(18.dp)) {
            Text("보호자 그룹 초대 링크", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(LivvingSurface, RoundedCornerShape(14.dp))
                    .border(1.dp, LivvingCoralSoft, RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = inviteLink ?: "아직 생성된 링크가 없어요",
                    modifier = Modifier.weight(1f),
                    color = LivvingCoral,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "복사",
                    modifier = Modifier.clickable(
                        enabled = inviteLink != null,
                        onClick = onCopyInviteClick,
                    ),
                    color = LivvingMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            if (inviteCode != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "초대코드 $inviteCode",
                    color = LivvingMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
    if (inviteLink != null) {
        Spacer(Modifier.height(14.dp))
        LivvingInfoBox(
            text = "그룹 초대 링크가 생성되었어요. 링크로 들어온 사람은 각자 수락 후 보호자로 연결됩니다.",
            tone = LivvingTone.Orange,
        )
    }
}
