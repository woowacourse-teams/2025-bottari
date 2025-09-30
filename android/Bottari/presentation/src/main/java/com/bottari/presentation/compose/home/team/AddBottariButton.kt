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
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun AddBottariButton(
    buttonSize: Dp,
    onCodeClick: () -> Unit,
    onTeamClick: () -> Unit,
    onPersonalClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        label = "rotation",
    )
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ExtendedFloatingActionButton(
                    modifier = Modifier.height(buttonSize),
                    shape = RoundedCornerShape(16.dp),
                    onClick = onCodeClick,
                    containerColor = Color.White,
                    contentColor = Color.Black,
                ) {
                    Text(
                        text = "코드로 참여",
                        style = BottariTheme.typography.medium20.toTextStyle(),
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                }

                LargeFloatingActionButton(
                    modifier = Modifier.size(buttonSize),
                    shape = CircleShape,
                    onClick = onTeamClick,
                    containerColor = Color.White,
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(R.drawable.ic_people),
                        contentDescription = "Team",
                    )
                }

                LargeFloatingActionButton(
                    modifier = Modifier.size(buttonSize),
                    shape = CircleShape,
                    onClick = onPersonalClick,
                    containerColor = Color.White,
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(R.drawable.ic_person_filled),
                        contentDescription = "Personal",
                    )
                }
            }
        }

        LargeFloatingActionButton(
            modifier = Modifier.size(buttonSize),
            shape = CircleShape,
            containerColor = BottariTheme.colors.primary,
            onClick = { isExpanded = !isExpanded },
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add",
                modifier = Modifier.rotate(rotation).size(50.dp),
                tint = Color.White,
            )
        }
    }
}
