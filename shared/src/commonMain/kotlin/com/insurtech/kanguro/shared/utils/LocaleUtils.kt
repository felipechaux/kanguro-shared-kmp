package com.insurtech.kanguro.shared.utils

import dev.icerock.moko.resources.desc.StringDesc

fun forceLocale(locale: String) {
    StringDesc.localeType = StringDesc.LocaleType.Custom(locale)
}
