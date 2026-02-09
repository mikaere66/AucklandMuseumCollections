package com.michaelrmossman.aucklandmuseum3api.util

import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionDocHeritage
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionHumanHistory
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionNatSciences

/**
 * Collection entries based on current selection
 */
object CollectionUtils {

    fun getCollectionEntries(
        collection: Collection?
    ) : Iterable<Collection> {
        return when (collection) {
            null -> listOf(
                Collection.CollectionDocHeritage,
                Collection.CollectionHumanHistory,
                Collection.CollectionNatSciences
            )
            CollectionDocHeritage.Art,
            CollectionDocHeritage.Ephemera,
            CollectionDocHeritage.Manuscripts,
            CollectionDocHeritage.MuseumArchives,
            CollectionDocHeritage.Photography,
            CollectionDocHeritage.Publications,
            Collection.CollectionDocHeritage -> {
                CollectionDocHeritage.entries
            }
            CollectionHumanHistory.AppliedArtsAndDesign,
            CollectionHumanHistory.Archaeology,
            CollectionHumanHistory.History,
            CollectionHumanHistory.Māori,
            CollectionHumanHistory.Pacific,
            CollectionHumanHistory.SocialHistory,
            CollectionHumanHistory.WarHistory,
            CollectionHumanHistory.World,
            Collection.CollectionHumanHistory -> {
                CollectionHumanHistory.entries
            }
            CollectionNatSciences.Birds,
            CollectionNatSciences.Botany,
            CollectionNatSciences.Entomology,
            CollectionNatSciences.Geology,
            CollectionNatSciences.LandMammals,
            CollectionNatSciences.Marine,
            CollectionNatSciences.ReptilesAndAmphibians,
            Collection.CollectionNatSciences -> {
                CollectionNatSciences.entries
            }
        }
    }
}