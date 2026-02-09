package com.michaelrmossman.aucklandmuseum3api.enum

/* Override reqd for search queries. Refer okHttpClient
   in AppContainer regarding NOT encoding the + sign */
enum class CollectionNatSciences: Collection { // 7

    Birds,
    Botany,
    Entomology,
    Geology,
    LandMammals {
        override fun toString() = "Land+Mammals"
    },
    Marine,
    ReptilesAndAmphibians {
        override fun toString() = "Reptiles+and+Amphibians"
    };

    override val parent = Collection.CollectionNatSciences
}