package com.bottari.presentation.compose.home.team

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun AddBottariButton(
    buttonSize: Dp,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    onCodeClick: () -> Unit,
    onTeamClick: () -> Unit,
    onPersonalClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
    )

    Column(
        modifier = modifier.padding(BottariTheme.spacing.spaceMedium),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceMedium),
        horizontalAlignment = Alignment.End,
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(bottom = BottariTheme.spacing.spaceXSmall)
                        .padding(horizontal = BottariTheme.spacing.spaceXSmall),
                verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceMedium),
                horizontalAlignment = Alignment.End,
            ) {
                ExtendedFloatingActionButton(
                    onClick = onCodeClick,
                    modifier = Modifier.height(buttonSize),
                    shape = RoundedCornerShape(BottariTheme.spacing.spaceMedium),
                    containerColor = BottariTheme.colors.white,
                    contentColor = BottariTheme.colors.black,
                    elevation =
                        FloatingActionButtonDefaults.elevation(
                            defaultElevation = 4.dp,
                        ),
                ) {
                    Text(
                        text = stringResource(R.string.team_bottari_join_btn_text),
                        style = BottariTheme.typography.semiBold20.toTextStyle(),
                        modifier = Modifier.padding(horizontal = BottariTheme.spacing.space2xSmall),
                    )
                }

                LargeFloatingActionButton(
                    onClick = onTeamClick,
                    modifier = Modifier.size(buttonSize),
                    shape = CircleShape,
                    containerColor = BottariTheme.colors.white,
                    elevation =
                        FloatingActionButtonDefaults.elevation(
                            defaultElevation = 4.dp,
                        ),
                ) {
                    Text(
                        text = stringResource(R.string.team_bottari_join_btn_text),
                        style = BottariTheme.typography.semiBold20.toTextStyle(),
                        modifier = Modifier.padding(horizontal = BottariTheme.spacing.space2xSmall),
                    )
                }

                LargeFloatingActionButton(
                    onClick = onPersonalClick,
                    modifier = Modifier.size(buttonSize),
                    shape = CircleShape,
                    containerColor = BottariTheme.colors.white,
                    elevation =
                        FloatingActionButtonDefaults.elevation(
                            defaultElevation = 4.dp,
                        ),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_person_filled),
                        modifier = Modifier.size(40.dp),
                        contentDescription = stringResource(R.string.personal_bottari_btn_create_description),
                    )
                }
            }
        }

        LargeFloatingActionButton(
            onClick = onExpandClick,
            modifier =
                Modifier.padding(end = BottariTheme.spacing.spaceXSmall).size(buttonSize),
            shape = CircleShape,
            containerColor = BottariTheme.colors.primary,
            elevation =
                FloatingActionButtonDefaults.elevation(
                    defaultElevation = 4.dp,
                ),
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.bottari_btn_create_description),
                modifier = Modifier.graphicsLayer { rotationZ = rotation }.size(50.dp),
                tint = BottariTheme.colors.white,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun AddBottariButtonPreview() {
    AddBottariButton(
        buttonSize = 60.dp,
        isExpanded = true,
        onExpandClick = {},
        onCodeClick = {},
        onTeamClick = {},
        onPersonalClick = {},
    )
}
