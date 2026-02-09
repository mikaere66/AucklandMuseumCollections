package com.michaelrmossman.aucklandmuseum3api.objects

import android.graphics.drawable.Drawable

data class HelpSectionObject(

    val helpSectionDrawables: List<Drawable?>,
    val helpSectionHeader: String,
    val helpSectionStrings: List<String>
)