package com.bottari.presentation.common.extension

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <T> Fragment.safeArgument(block: Bundle.() -> T): T? = runCatching { requireArguments().block() }.getOrNull()
