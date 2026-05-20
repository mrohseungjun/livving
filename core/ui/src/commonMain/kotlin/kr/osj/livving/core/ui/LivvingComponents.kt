package kr.osj.livving.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val LivvingGradient = Brush.linearGradient(
    colors = listOf(LivvingCoral, LivvingPurple),
)

@Composable
fun LivvingScreen(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .background(LivvingBackground)
            .safeContentPadding()
            .fillMaxSize(),
        content = content,
    )
}

@Composable
fun LivvingScrollableScreen(
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LivvingBackground)
            .safeContentPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    top = 20.dp,
                    end = 24.dp,
                    bottom = if (bottomBar == null) 24.dp else 88.dp,
                )
                .verticalScroll(rememberScrollState()),
        ) {
            content()
        }
        if (bottomBar != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
            ) {
                bottomBar()
            }
        }
    }
}

@Composable
fun LivvingLogo(
    modifier: Modifier = Modifier,
    small: Boolean = false,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(if (small) 36.dp else 48.dp)
                .clip(RoundedCornerShape(if (small) 14.dp else 18.dp))
                .background(LivvingGradient),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "✓",
                color = Color.White,
                fontSize = if (small) 20.sp else 26.sp,
                fontWeight = FontWeight.Black,
            )
        }
        Text(
            text = "livving",
            color = LivvingCoral,
            fontSize = if (small) 26.sp else 40.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
fun LivvingHeader(
    title: String? = null,
    sub: String? = null,
    showLogo: Boolean = true,
    onBack: (() -> Unit)? = null,
    right: (@Composable () -> Unit)? = null,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            when {
                onBack != null -> LivvingIconCircle(text = "‹", onClick = onBack)
                showLogo -> LivvingLogo(small = true)
                else -> Spacer(Modifier.width(44.dp))
            }
            right?.invoke() ?: Spacer(Modifier.width(44.dp))
        }
        if (title != null) {
            Spacer(Modifier.height(28.dp))
            Text(
                text = title,
                fontSize = 30.sp,
                lineHeight = 36.sp,
                color = LivvingText,
                fontWeight = FontWeight.Black,
            )
        }
        if (sub != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = sub,
                fontSize = 15.sp,
                lineHeight = 23.sp,
                color = LivvingMuted,
            )
        }
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
fun LivvingCard(
    modifier: Modifier = Modifier,
    background: Color = LivvingSurface,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        border = BorderStroke(1.dp, LivvingLine),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        content()
    }
}

@Composable
fun LivvingGradientCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFFFF1F0), Color(0xFFF4EFFF)),
                ),
            )
            .border(1.dp, LivvingLine, RoundedCornerShape(24.dp))
            .padding(20.dp),
    ) {
        content()
    }
}

@Composable
fun LivvingPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LivvingCoral,
            disabledContainerColor = Color(0xFFE5E5E5),
            disabledContentColor = Color(0xFFA3A3A3),
        ),
    ) {
        Text(text = text, fontSize = 17.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun LivvingSecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    danger: Boolean = false,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, if (danger) Color(0xFFFEE2E2) else Color(0xFFE5E5E5)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (danger) Color(0xFFFEF2F2) else Color.White,
            contentColor = if (danger) LivvingDanger else LivvingText,
        ),
    ) {
        Text(text = text, fontSize = 17.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun LivvingIconCircle(
    text: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    background: Color = Color(0xFFFAFAFA),
    color: Color = LivvingText,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = background,
        onClick = onClick,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, color = color, fontSize = 22.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun LivvingAvatar(
    name: String,
    modifier: Modifier = Modifier,
    tone: LivvingTone = LivvingTone.Coral,
) {
    val colors = tone.colors()
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(colors.first),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name.take(1),
            color = colors.second,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
fun LivvingBadge(text: String, tone: LivvingTone) {
    val colors = tone.colors()
    Text(
        modifier = Modifier
            .clip(CircleShape)
            .background(colors.first)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        text = text,
        color = colors.second,
        fontSize = 12.sp,
        fontWeight = FontWeight.Black,
    )
}

@Composable
fun LivvingInfoBox(
    text: String,
    modifier: Modifier = Modifier,
    tone: LivvingTone = LivvingTone.Neutral,
) {
    val colors = tone.colors()
    Text(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(colors.first)
            .padding(16.dp),
        text = text,
        color = colors.second,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun LivvingDataRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, color = LivvingMuted)
        Text(text = value, color = LivvingText, fontWeight = FontWeight.Black)
    }
}

@Composable
fun LivvingMenuRow(
    title: String,
    desc: String? = null,
    leading: String,
    trailing: String = "›",
    onClick: () -> Unit,
) {
    LivvingCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(text = leading, color = LivvingCoral, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Black, color = LivvingText)
                if (desc != null) {
                    Text(text = desc, color = LivvingMuted, fontSize = 13.sp)
                }
            }
            Text(text = trailing, color = LivvingMuted, fontSize = 24.sp)
        }
    }
}

@Composable
fun LivvingCheckDot(
    checked: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .then(
                if (checked) Modifier.background(LivvingGradient)
                else Modifier.border(1.dp, Color(0xFFD4D4D4), CircleShape)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Text(text = "✓", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun LivvingBottomBar(
    items: List<Pair<String, String>>,
    active: String,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .background(Color.White)
            .border(1.dp, LivvingLine),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { (key, label) ->
            val selected = key == active
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClick(key) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = when (key) {
                        "home" -> "⌂"
                        "relations" -> "◎"
                        "notifications" -> "!"
                        else -> "⚙"
                    },
                    color = if (selected) LivvingCoral else Color(0xFFA3A3A3),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                )
                Text(
                    text = label,
                    color = if (selected) LivvingCoral else Color(0xFFA3A3A3),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun LivvingSegmented(
    left: String,
    right: String,
    selectedLeft: Boolean,
    onLeft: () -> Unit,
    onRight: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFF5F5F5))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        LivvingSegmentButton(left, selectedLeft, Modifier.weight(1f), onLeft)
        LivvingSegmentButton(right, !selectedLeft, Modifier.weight(1f), onRight)
    }
}

@Composable
private fun LivvingSegmentButton(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) Color.White else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (selected) LivvingCoral else LivvingMuted,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
fun LivvingBodyText(text: String) {
    Text(text = text, color = LivvingText)
}

@Composable
fun LivvingCenterText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 16,
    color: Color = LivvingText,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Center,
        fontSize = fontSize.sp,
        lineHeight = (fontSize + 7).sp,
        color = color,
    )
}

@Composable
fun LivvingTwoColumnButtons(
    items: List<String>,
    selected: String,
    onClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { item ->
                    val isSelected = selected == item
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isSelected) LivvingCoral else Color.White)
                            .border(1.dp, if (isSelected) LivvingCoral else Color(0xFFE5E5E5), RoundedCornerShape(18.dp))
                            .clickable { onClick(item) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = item,
                            color = if (isSelected) Color.White else LivvingText,
                            fontWeight = FontWeight.Black,
                        )
                    }
                }
                if (row.size == 1) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun LivvingScheduleCard(
    deadline: String,
    delayMinutes: Int,
    modifier: Modifier = Modifier,
) {
    LivvingGradientCard(modifier.fillMaxWidth()) {
        Column {
            LivvingFlowLine(deadline, "안부 확인 마감 시간")
            Box(
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, bottom = 8.dp)
                    .width(1.dp)
                    .height(28.dp)
                    .background(Color(0xFFE5E5E5)),
            )
            val alertText = if (delayMinutes == 0) "즉시" else "${delayMinutes}분 후"
            LivvingFlowLine(addLivvingMinutes(deadline, delayMinutes), "$alertText 보호자 알림 발송")
        }
    }
}

@Composable
fun LivvingFlowLine(
    title: String,
    desc: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Text("•", color = LivvingCoral, fontSize = 24.sp, fontWeight = FontWeight.Black)
        }
        Column {
            Text(title, fontWeight = FontWeight.Black)
            Text(desc, color = LivvingMuted, fontSize = 13.sp)
        }
    }
}

@Composable
fun LivvingCheckInButton(
    checked: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(176.dp)
            .clip(RoundedCornerShape(32.dp))
            .then(if (checked) Modifier.background(Color(0xFFECFDF5)) else Modifier.background(LivvingGradient))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                Text("✓", color = if (checked) LivvingSuccess else LivvingCoral, fontSize = 38.sp, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = if (checked) "확인 완료" else "안부 확인하기",
                color = if (checked) LivvingSuccess else Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
            )
        }
    }
}

@Composable
fun LivvingSummaryCard(
    label: String,
    value: String,
    sub: String,
    color: Color,
    modifier: Modifier,
) {
    LivvingCard(modifier) {
        Column(Modifier.padding(16.dp)) {
            Text("•", color = color, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text(label, color = LivvingMuted, fontSize = 13.sp)
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.Black)
            Text(sub, color = Color(0xFFA3A3A3), fontSize = 12.sp)
        }
    }
}

@Composable
fun LivvingPersonHeader(
    name: String,
    desc: String,
    tone: LivvingTone,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LivvingAvatar(name, tone = tone)
        Spacer(Modifier.height(18.dp))
        Text(name, fontSize = 30.sp, fontWeight = FontWeight.Black)
        Text(desc, color = LivvingMuted)
    }
}

@Composable
fun LivvingFlowCard(
    firstTitle: String,
    firstDesc: String,
    secondTitle: String,
    secondDesc: String,
) {
    LivvingCard {
        Column(Modifier.padding(18.dp)) {
            LivvingFlowLine(firstTitle, firstDesc)
            Box(
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, bottom = 8.dp)
                    .width(1.dp)
                    .height(28.dp)
                    .background(Color(0xFFE5E5E5)),
            )
            LivvingFlowLine(secondTitle, secondDesc)
        }
    }
}

@Composable
fun LivvingNoticeRow(
    title: String,
    time: String,
    desc: String,
    tone: LivvingTone,
    onClick: () -> Unit,
) {
    LivvingCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            LivvingAvatar(title, tone = tone)
            Column(Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(title, fontWeight = FontWeight.Black)
                    Text(time, color = Color(0xFFA3A3A3), fontSize = 12.sp)
                }
                Text(desc, color = LivvingMuted, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun LivvingSettingGroup(
    title: String,
    content: @Composable () -> Unit,
) {
    Text(
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
        text = title,
        color = Color(0xFFA3A3A3),
        fontSize = 12.sp,
        fontWeight = FontWeight.Black,
    )
    LivvingCard {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            content()
        }
    }
    Spacer(Modifier.height(20.dp))
}

@Composable
fun LivvingSettingRow(
    title: String,
    value: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = if (title == "로그아웃") LivvingDanger else LivvingText)
        if (value.isNotBlank()) {
            Text(value, color = LivvingText, fontSize = 13.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.width(6.dp))
        }
        Text("›", color = LivvingMuted, fontSize = 22.sp)
    }
}

@Composable
fun LivvingToggleRow(
    title: String,
    value: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(title, fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .size(width = 48.dp, height = 28.dp)
                .clip(CircleShape)
                .background(if (value) LivvingCoral else Color(0xFFE5E5E5))
                .clickable(onClick = onClick)
                .padding(4.dp),
            contentAlignment = if (value) Alignment.CenterEnd else Alignment.CenterStart,
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White),
            )
        }
    }
}

@Composable
fun LivvingPolicyItem(
    title: String,
    desc: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("i", color = LivvingCoral, fontWeight = FontWeight.Black)
        Column {
            Text(title, fontWeight = FontWeight.Black)
            Text(desc, color = LivvingMuted, fontSize = 13.sp)
        }
    }
}

fun addLivvingMinutes(time: String, minutes: Int): String {
    val parts = time.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
    val total = hour * 60 + minute + minutes
    val normalized = ((total % 1440) + 1440) % 1440
    val nextHour = normalized / 60
    val nextMinute = normalized % 60
    return nextHour.toString().padStart(2, '0') + ":" + nextMinute.toString().padStart(2, '0')
}

enum class LivvingTone {
    Coral,
    Purple,
    Green,
    Orange,
    Red,
    Neutral,
}

private fun LivvingTone.colors(): Pair<Color, Color> = when (this) {
    LivvingTone.Coral -> Color(0xFFFFF1F0) to LivvingCoral
    LivvingTone.Purple -> Color(0xFFF4EFFF) to LivvingPurple
    LivvingTone.Green -> Color(0xFFECFDF5) to LivvingSuccess
    LivvingTone.Orange -> Color(0xFFFFF7ED) to LivvingWarning
    LivvingTone.Red -> Color(0xFFFEF2F2) to LivvingDanger
    LivvingTone.Neutral -> Color(0xFFFAFAFA) to LivvingMuted
}
