package com.bottari.presentation.compose.template

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.bottari.designsystem.theme.LocalBottariBgColor
import com.bottari.presentation.R
import com.bottari.presentation.compose.template.component.CreateBottariSelector
import com.bottari.presentation.compose.template.component.CreateTemplateTopApp
import com.bottari.presentation.compose.template.component.SelectedBottariSection
import com.bottari.presentation.compose.template.component.TemplateDescriptionSection
import com.bottari.presentation.compose.template.component.TemplateHashtagSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTemplateScreen(
    navigateBack: () -> Unit,
    viewModel: CreateTemplateViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isOpenSelector by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is CreateTemplateUiEvent.FetchMyBottariesFailure -> {
                    snackbarHostState.showSnackbar(context.getString(R.string.bottari_home_fetch_failure_text))
                }

                is CreateTemplateUiEvent.CreateTemplateSuccess -> {
                    Toast
                        .makeText(
                            context,
                            R.string.template_create_success_text,
                            Toast.LENGTH_SHORT,
                        ).show()
                    navigateBack()
                }

                is CreateTemplateUiEvent.CreateTemplateFailure -> {
                    snackbarHostState.showSnackbar(context.getString(R.string.template_create_failure_text))
                }
            }
        }
    }

    if (isOpenSelector) {
        CreateBottariSelector(
            bottaries = uiState.value.myTemplates,
            bottomSheetState = bottomSheetState,
            onClickBottari = { bottariId ->
                viewModel.updateSelectedBottari(bottariId)
                isOpenSelector = false
            },
            onClickClose = { isOpenSelector = false },
            onDismissRequest = { isOpenSelector = false },
        )
    }

    CreateTemplateScreen(
        uiState = uiState.value,
        snackbarHostState = snackbarHostState,
        onDescriptionChange = viewModel::updateDescription,
        onWritingHashtagChange = viewModel::updateHashtag,
        onAddHashtag = viewModel::addHashtag,
        onUpdateHashtags = viewModel::updateHashtags,
        onClickCreate = viewModel::createTemplate,
        onClickSelect = { isOpenSelector = true },
        onClickBack = navigateBack,
    )
}

@Composable
private fun CreateTemplateScreen(
    uiState: CreateTemplateUiState,
    snackbarHostState: SnackbarHostState,
    onDescriptionChange: (String) -> Unit,
    onWritingHashtagChange: (String) -> Unit,
    onAddHashtag: () -> Unit,
    onUpdateHashtags: (List<String>) -> Unit,
    onClickCreate: () -> Unit,
    onClickSelect: () -> Unit,
    onClickBack: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.imePadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = LocalBottariBgColor.current,
        topBar = { CreateTemplateTopApp(onClickBack) },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = BottariTheme.spacing.spaceMedium)
                    .verticalScroll(scrollState),
        ) {
            CreateTemplateScreenContent(
                uiState = uiState,
                onDescriptionChange = onDescriptionChange,
                onWritingHashtagChange = onWritingHashtagChange,
                onAddHashtag = onAddHashtag,
                onUpdateHashtags = onUpdateHashtags,
                onClickSelect = onClickSelect,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceLarge))

            CreateTemplateButton(
                enabled = uiState.canCreate,
                onClickCreate = onClickCreate,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = BottariTheme.spacing.spaceMedium),
            )
        }
    }
}

@Composable
private fun CreateTemplateScreenContent(
    uiState: CreateTemplateUiState,
    onDescriptionChange: (String) -> Unit,
    onWritingHashtagChange: (String) -> Unit,
    onAddHashtag: () -> Unit,
    onUpdateHashtags: (List<String>) -> Unit,
    onClickSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
    ) {
        SelectedBottariSection(
            selectedBottariTitle = uiState.selectedBottariTitle,
            selectedBottariItems = uiState.selectedBottariItems,
            isSelected = uiState.isSelected,
            onClickSelect = onClickSelect,
        )
        TemplateDescriptionSection(
            description = uiState.description,
            onDescriptionChange = onDescriptionChange,
        )
        TemplateHashtagSection(
            hashtags = uiState.hashtags,
            writingHashtag = uiState.writingHashtag,
            onWritingHashtagChange = onWritingHashtagChange,
            canAddHashtag = uiState.canAddHashtag,
            onClickAdd = onAddHashtag,
            onUpdateHashtags = onUpdateHashtags,
        )
    }
}

@Composable
private fun CreateTemplateButton(
    enabled: Boolean,
    onClickCreate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        TextButton(
            enabled = enabled,
            onClick = onClickCreate,
            shape = RoundedCornerShape(12.dp),
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceMedium),
            colors =
                ButtonDefaults.textButtonColors(
                    containerColor = BottariTheme.colors.primary,
                    contentColor = BottariTheme.colors.white,
                    disabledContainerColor = BottariTheme.colors.gray200,
                    disabledContentColor = BottariTheme.colors.gray500,
                ),
        ) {
            Text(
                text = "보따리 템플릿 등록하기",
                style = BottariTheme.typography.bold18.toTextStyle(),
            )
        }

        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))
    }
}

@Preview
@Composable
private fun CreateTemplateScreenPreview() {
    var uiState =
        remember {
            CreateTemplateUiState(
                description = "신입 사원 온보딩 가이드입니다.",
                hashtags = List(10) { "해시태그$it" },
                selectedBottariTitle = "신입 사원 온보딩 가이드",
                selectedBottariItems = List(10) { "아이템 $it" },
            )
        }

    BottariTheme {
        CreateTemplateScreen(
            snackbarHostState = SnackbarHostState(),
            uiState = uiState,
            onClickSelect = {},
            onClickCreate = {},
            onClickBack = {},
            onDescriptionChange = { uiState = uiState.copy(description = it) },
            onWritingHashtagChange = { uiState = uiState.copy(writingHashtag = it) },
            onUpdateHashtags = { uiState = uiState.copy(hashtags = uiState.hashtags - it) },
            onAddHashtag = {
                uiState =
                    uiState.copy(
                        writingHashtag = "",
                        hashtags = uiState.hashtags + uiState.writingHashtag,
                    )
            },
        )
    }
}
