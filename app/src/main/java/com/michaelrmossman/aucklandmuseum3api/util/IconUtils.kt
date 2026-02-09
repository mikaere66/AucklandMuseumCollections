package com.michaelrmossman.aucklandmuseum3api.util

import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionDocHeritage
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionHumanHistory
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionNatSciences

/**
 * Icon utility functions used throughout the app
 */
object IconUtils {

    fun getCollectionIconId(collection: Collection): Int {
        return when (collection) {
            Collection.CollectionDocHeritage -> R.drawable.outline_snippet_folder_24
            Collection.CollectionNatSciences -> R.drawable.outline_emoji_nature_24
            Collection.CollectionHumanHistory -> R.drawable.icons_lib_human_24

            CollectionDocHeritage.Art -> R.drawable.icons_lib_art_24
            CollectionDocHeritage.Ephemera -> R.drawable.icons_lib_ticket_24
            CollectionDocHeritage.Manuscripts -> R.drawable.outline_book_4_24
            CollectionDocHeritage.MuseumArchives -> R.drawable.outline_archive_24
            CollectionDocHeritage.Photography -> R.drawable.outline_photo_camera_24
            CollectionDocHeritage.Publications -> R.drawable.icons_lib_publication_24

            CollectionNatSciences.Birds -> R.drawable.icons_lib_bird_24
            CollectionNatSciences.Botany -> R.drawable.icons_lib_plant_24
            CollectionNatSciences.Entomology -> R.drawable.icons_lib_bug_24
            CollectionNatSciences.Geology -> R.drawable.outline_interests_24
            CollectionNatSciences.LandMammals -> R.drawable.outline_landscape_2_24
            CollectionNatSciences.Marine -> R.drawable.icons_lib_fish_24
            CollectionNatSciences.ReptilesAndAmphibians -> R.drawable.icons_lib_reptile_24

            CollectionHumanHistory.AppliedArtsAndDesign -> R.drawable.icons_lib_organisation_24
            CollectionHumanHistory.Archaeology -> R.drawable.outline_humerus_24
            CollectionHumanHistory.History -> R.drawable.outline_history_edu_24
            CollectionHumanHistory.Māori -> R.drawable.icons_lib_prized_24
            CollectionHumanHistory.Pacific -> R.drawable.outline_water_24
            CollectionHumanHistory.SocialHistory -> R.drawable.outline_family_history_24
            CollectionHumanHistory.WarHistory -> R.drawable.icons_lib_medal_24
            CollectionHumanHistory.World -> R.drawable.icons_lib_earth_24
        }
    }
}