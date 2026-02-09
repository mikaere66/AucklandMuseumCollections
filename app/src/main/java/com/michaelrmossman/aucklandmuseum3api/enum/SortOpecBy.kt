package com.michaelrmossman.aucklandmuseum3api.enum

enum class SortOpecBy {

    Asc,
    Desc;

    override fun toString(): String {

        return name.lowercase()
    }
}