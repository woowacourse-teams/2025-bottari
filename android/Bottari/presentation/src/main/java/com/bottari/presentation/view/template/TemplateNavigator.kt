package com.bottari.presentation.view.template

interface TemplateNavigator {
    fun navigateToDetail(
        bottariTemplateId: Long,
        isMyTemplate: Boolean = false,
    )

    fun navigateToMyTemplate()
}
