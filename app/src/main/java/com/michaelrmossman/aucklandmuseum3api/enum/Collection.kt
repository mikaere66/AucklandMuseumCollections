package com.michaelrmossman.aucklandmuseum3api.enum

/* Okay, so it's not an enum, but
   enums do implement this =) */
sealed interface Collection {

    val parent: Collection

    /* Override reqd for search queries ...
       refer to OkHttpClient in AppContainer
       regarding NOT encoding the + sign */

    data object CollectionDocHeritage : Collection {
        override val parent = CollectionDocHeritage
        override fun toString() = "Documentary+Heritage"
    }

    data object CollectionHumanHistory: Collection {
        override val parent = CollectionHumanHistory
        override fun toString() = "Human+History"
    }

    data object CollectionNatSciences : Collection {
        override val parent = CollectionNatSciences
        override fun toString() = "Natural+Sciences"
    }
}