package com.michaelrmossman.aucklandmuseum3api.enum

/* Override reqd for search queries. Refer okHttpClient
   in AppContainer regarding NOT encoding the + sign */
enum class CollectionDocHeritage(): Collection { // 6

    Art,
    Ephemera,
    Manuscripts,
    MuseumArchives {
        override fun toString() = "Museum+Archives"
    },
    Photography,
    Publications;

    override val parent = Collection.CollectionDocHeritage
}