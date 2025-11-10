package com.bottari.presentation.compose.edit.team.assigned.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.modifier.noRippleClickable
import com.bottari.presentation.compose.common.modifier.topBottomFadingEdge
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.edit.team.assigned.TeamAssignedEditUiState
import com.bottari.presentation.compose.edit.team.assigned.dummyMembers
import com.bottari.presentation.compose.team.TeamStateListBox
import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamAssignedBottomSheet(
    uiState: TeamAssignedEditUiState,
    sheetState: SheetState,
    onSaveItem: () -> Unit,
    onAllSelect: () -> Unit,
    onAllUnSelect: () -> Unit,
    onBottomSheetClose: () -> Unit,
    onSelectMember: (Long) -> Unit,
    onChangeInputText: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onBottomSheetClose,
        sheetState = sheetState,
        containerColor = BottariTheme.colors.white,
        contentWindowInsets = { WindowInsets.navigationBars },
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                modifier =
                    Modifier.noRippleClickable(
                        true,
                        {},
                    ),
            )
        },
        modifier = Modifier.statusBarsPadding(),
    ) {
        val focusManager = LocalFocusManager.current
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(BottariTheme.colors.white)
                    .padding(BottariTheme.spacing.spaceMedium)
                    .navigationBarsPadding()
                    .noRippleClickable(onClick = { focusManager.clearFocus() }),
        ) {
            BottomSheetHeader(
                canSend = uiState.canSend,
                onSaveItem = onSaveItem,
            )
            Spacer(Modifier.padding(vertical = BottariTheme.spacing.spaceSmall))
            BottomSheetHint("물건 이름을 입력하고 담당자를 지정해 주세요")

            Spacer(Modifier.padding(vertical = BottariTheme.spacing.spaceXSmall))

            SectionLabel(text = "물건 이름")
            Spacer(Modifier.padding(vertical = BottariTheme.spacing.spaceXSmall))
            OutlinedTextField(
                value = uiState.inputText,
                onValueChange = { currentText -> onChangeInputText(currentText) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = BottariTheme.colors.white,
                        focusedContainerColor = BottariTheme.colors.white,
                        unfocusedBorderColor = BottariTheme.colors.gray500,
                        focusedBorderColor = BottariTheme.colors.gray500,
                    ),
                textStyle = BottariTheme.typography.semiBold16.toTextStyle(),
                isError = uiState.isAlreadyExist,
            )

            Spacer(Modifier.padding(vertical = BottariTheme.spacing.spaceXSmall))

            MemberSelectionHeader(
                onAllSelect = onAllSelect,
                onAllUnSelect = onAllUnSelect,
            )
            Spacer(Modifier.padding(vertical = BottariTheme.spacing.space2xSmall))
            TeamStateListBox(
                items =
                    uiState.members
                        .filter { member -> member.isHost }
                        .map { member -> member.nickname },
                text = "${uiState.members.filter { member -> member.isHost }.size} 명이 선택되었습니다",
                painter = painterResource(R.drawable.ic_assigned),
                color = BottariTheme.colors.primary,
            )
            Spacer(Modifier.padding(vertical = BottariTheme.spacing.spaceMedium))
            LazyColumn(
                modifier =
                    Modifier
                        .topBottomFadingEdge(BottariTheme.colors.white)
                        .nestedScroll(ConsumeOverscrollConnection),
                verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
                contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceSmall),
            ) {
                items(uiState.members) { member ->
                    member.id?.let {
                        TeamAssignedMemberItem(
                            member = member,
                            onToggleMember = onSelectMember,
                        )
                    }
                }
            }
        }
    }
}

private val ConsumeOverscrollConnection =
    object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset = available
    }

@Composable
private fun BottomSheetHeader(
    canSend: Boolean,
    onSaveItem: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "새 물건 추가", style = BottariTheme.typography.semiBold20.toTextStyle())
        Spacer(Modifier.weight(1f))
        TextButton(
            onClick = onSaveItem,
            enabled = canSend,
            colors = ButtonDefaults.textButtonColors(contentColor = BottariTheme.colors.primary),
        ) {
            Text("저장", style = BottariTheme.typography.semiBold16.toTextStyle())
        }
    }
}

@Composable
private fun BottomSheetHint(text: String) {
    Text(
        text = text,
        style = BottariTheme.typography.regular14.toTextStyle(),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.padding(vertical = BottariTheme.spacing.spaceXSmall))
    HorizontalDivider(thickness = 2.dp, color = BottariTheme.colors.gray500)
}

@Composable
private fun SectionLabel(text: String) {
    Row {
        Text(text = text, style = BottariTheme.typography.semiBold16.toTextStyle())
        Spacer(Modifier.padding(horizontal = BottariTheme.spacing.space2xSmall))
        Text(
            text = "*",
            style = BottariTheme.typography.semiBold16.toTextStyle(),
            color = BottariTheme.colors.red,
        )
    }
}

@Composable
private fun MemberSelectionHeader(
    onAllSelect: () -> Unit,
    onAllUnSelect: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        SectionLabel(text = "담당자 선택")
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onAllSelect,
            modifier = Modifier.height(30.dp),
            contentPadding =
                PaddingValues(
                    horizontal = 12.dp,
                    vertical = 2.dp,
                ),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = BottariTheme.colors.primary.copy(alpha = 0.1f),
                    contentColor = BottariTheme.colors.primary,
                    disabledContainerColor = BottariTheme.colors.primary.copy(alpha = 0.1f),
                    disabledContentColor = BottariTheme.colors.primary,
                ),
        ) {
            Text("전체선택", style = BottariTheme.typography.medium14.toTextStyle())
        }
        Spacer(Modifier.width(BottariTheme.spacing.spaceXSmall))
        Button(
            onClick = onAllUnSelect,
            modifier = Modifier.height(30.dp),
            contentPadding =
                PaddingValues(
                    horizontal = 12.dp,
                    vertical = 2.dp,
                ),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = BottariTheme.colors.gray200,
                    contentColor = BottariTheme.colors.black,
                    disabledContainerColor = BottariTheme.colors.gray200,
                    disabledContentColor = BottariTheme.colors.black,
                ),
        ) {
            Text("전체해제", style = BottariTheme.typography.medium14.toTextStyle())
        }
    }
}

@Composable
private fun TeamAssignedMemberItem(
    member: TeamMemberUiModel,
    onToggleMember: (Long) -> Unit,
) {
    if (member.id == null) return

    val isSelected = member.isHost
    val backgroundColor =
        if (isSelected) BottariTheme.colors.primary.copy(0.1f) else BottariTheme.colors.gray100
    val borderColor = if (isSelected) BottariTheme.colors.primary else BottariTheme.colors.gray500
    val checkmarkColor =
        if (isSelected) BottariTheme.colors.primary else BottariTheme.colors.gray500

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor) // 변수 사용
                .noRippleClickable(onClick = { onToggleMember(member.id) })
                .border(
                    2.dp,
                    borderColor, // 변수 사용
                    RoundedCornerShape(16.dp),
                ).padding(BottariTheme.spacing.spaceXLarge),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = member.nickname,
            style = BottariTheme.typography.semiBold16.toTextStyle(),
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Box(
            modifier =
                Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(checkmarkColor)
                    .padding(4.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                tint = BottariTheme.colors.white,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TeamAssignedBottomSheetPreview() {
    val sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)

    TeamAssignedBottomSheet(
        uiState = TeamAssignedEditUiState(members = dummyMembers),
        onSaveItem = {},
        onAllSelect = {},
        onAllUnSelect = {},
        onSelectMember = {},
        onChangeInputText = {},
        onBottomSheetClose = {},
        sheetState = sheetState,
    )
}
