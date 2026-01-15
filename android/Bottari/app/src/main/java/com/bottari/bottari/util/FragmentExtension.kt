package com.bottari.bottari.util

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <T> Fragment.safeArgument(block: Bundle.() -> T): T? = runCatching { requireArguments().block() }.getOrNull()
