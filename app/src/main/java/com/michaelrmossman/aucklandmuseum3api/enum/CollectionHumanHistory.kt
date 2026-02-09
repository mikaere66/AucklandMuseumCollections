package com.michaelrmossman.aucklandmuseum3api.enum

/* Override reqd for search queries. Refer okHttpClient
   in AppContainer regarding NOT encoding the + sign */
enum class CollectionHumanHistory: Collection { // 8

    AppliedArtsAndDesign {
        override fun toString() = "Applied+Arts+and+Design"
    },
    Archaeology,
    History,
    Māori,
    Pacific,
    SocialHistory {
        override fun toString() = "Social+History"
    },
    WarHistory {
        override fun toString() = "War+History"
    },
    World;

    override val parent = Collection.CollectionHumanHistory
}