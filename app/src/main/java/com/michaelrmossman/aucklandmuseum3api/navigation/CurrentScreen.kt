package com.michaelrmossman.aucklandmuseum3api.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.data.FaveEntity
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.objects.ImageObjects
import kotlinx.serialization.Serializable

sealed interface CurrentScreen: NavKey {

    val drawableId: Int
    val introStringId: Int
    val listIndex: Int
    val titleStringId: Int

    /* COLLECTION ... not actually used as a
     * NavDisplay entry, but instead for the
     * Main button, and HelpScreen resources */
    @Serializable
    data object CollectionDummy: CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.outline_database_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_0
        override val listIndex: Int = 0
        @StringRes override val titleStringId: Int =
            R.string.nav_collection_status
    }

    // HOME
    @Serializable
    data object MainScreen: CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.outline_home_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_1
        override val listIndex: Int = -1
        @StringRes override val titleStringId: Int =
            R.string.app_name
    }

    // OBJECTS
    @Serializable
    data object ObjectsSearch: CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.outline_category_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_2
        override val listIndex: Int = 1
        @StringRes override val titleStringId: Int =
            R.string.nav_objects_1
    }
    @Serializable
    data class ObjectDetails(
        val `object`: OpacObject
    ) : CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.outline_text_snippet_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_3
        override val listIndex: Int = -1
        @StringRes override val titleStringId: Int =
            R.string.nav_objects_3
    }

    // PERSONS
    @Serializable
    data object PersonsSearch: CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.outline_person_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_4
        override val listIndex: Int = 2
        @StringRes override val titleStringId: Int =
            R.string.nav_persons_1
    }
    @Serializable
    data class PersonDetails(
        val person: OpacPerson
    ) : CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.outline_text_snippet_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_5
        override val listIndex: Int = -1
        @StringRes override val titleStringId: Int =
            R.string.nav_persons_3
    }

    // IMAGES
    data class ImagesScreen(
        val imageObjects: ImageObjects
    ) : CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.outline_imagesmode_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_10
        override val listIndex: Int = -1
        @StringRes override val titleStringId: Int =
            R.string.nav_images
    }

    // FAVOURITES
    @Serializable
    data object FavesScreen: CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.outline_bookmark_stacks_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_6
        override val listIndex: Int = 3
        @StringRes override val titleStringId: Int =
            R.string.nav_faves_1
    }
    @Serializable
    data class FaveObject(
        val fave: FaveEntity
    ) : CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.outline_text_snippet_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_7
        override val listIndex: Int = -1
        @StringRes override val titleStringId: Int =
            R.string.nav_faves_3
    }
    data class FavePerson(
        val fave: FaveEntity
    ) : CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.outline_text_snippet_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_8
        override val listIndex: Int = -1
        @StringRes override val titleStringId: Int =
            R.string.nav_faves_3
    }

    // HELP
    data object HelpScreen: CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.icons_lib_help_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_9
        override val listIndex: Int = 4
        @StringRes override val titleStringId: Int =
            R.string.nav_help
    }

    // SETTINGS
    @Serializable
    data object SettingsScreen: CurrentScreen {
        @DrawableRes override val drawableId: Int =
            R.drawable.outline_settings_24
        @StringRes override val introStringId: Int =
            R.string.help_section_main_11
        override val listIndex: Int = 5
        @StringRes override val titleStringId: Int =
            R.string.nav_settings
    }
}