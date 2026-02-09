package com.michaelrmossman.aucklandmuseum3api.util

import android.text.InputFilter
import android.text.Spanned

/**
 * Exclude emojis from search query
 */
class EmojiInputFilter: InputFilter {

    companion object {
        /* Disallowed character types */
        val characterTypes = listOf(
            Character.SURROGATE.toInt(),
            Character.OTHER_SYMBOL.toInt(),
            Character.NON_SPACING_MARK.toInt(),
            Character.MODIFIER_SYMBOL.toInt()
        )

        fun containsEmoji(
            source: String,
            start: Int,
            end: Int
        ) : Boolean {
            for (i in start until end) {
                val type = Character.getType(source[i])
                if (characterTypes.contains(type)) {
                    return true
                }
            }
            return false
        }
    }

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ) : String {
        return when (source) {
            null -> String()
            else -> {
                val sb = StringBuilder()
                for (i in start until end) {
                    val type = Character.getType(source[i])
                    if (!characterTypes.contains(type)) {
                        sb.append(source[i])
                    }
                }
                sb.toString()
            }
        }
    }
}