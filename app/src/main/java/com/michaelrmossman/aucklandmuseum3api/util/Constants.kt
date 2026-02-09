package com.michaelrmossman.aucklandmuseum3api.util

const val DEBUG_JSON_ADDITIONAL_MESSAGES = false
const val DEBUG_SHOW_ADDITIONAL_MESSAGES = true

const val DOWNLOAD_COMPLETE_DELAY = 2_250L
const val EMOJI_ERROR_DELAY = 2_000L

const val IDENTIFIER_ASSOC_EVENT = "assoc_event"
const val IDENTIFIER_ASSOC_NOTES = "assoc_notes"
const val IDENTIFIER_ASSOC_PLACE = "assoc_place"
const val IDENTIFIER_COPYRIGHT = "copyright"
const val IDENTIFIER_DATE_OF_BIRTH = "date_of_birth"
const val IDENTIFIER_DATE_OF_DEATH = "date_of_death"
const val IDENTIFIER_DEPARTMENT = "department"
const val IDENTIFIER_DESCRIPTION = "description"
const val IDENTIFIER_DOCUMENTATION = "documentation"
const val IDENTIFIER_IMAGE_DERIVATIVE_MED = "MEDIUM"
const val IDENTIFIER_INDIVIDUAL = "individual"
const val IDENTIFIER_LAST_UPDATE = "last_update"
const val IDENTIFIER_NAME_FIRST_LAST = "name_f_l"
const val IDENTIFIER_OBJECT_TYPE = "object_type"
const val IDENTIFIER_PERSON_CORP_TYPE = "person_corp_type"
const val IDENTIFIER_SUBJECT_CATEGORY = "subject_category"
const val IDENTIFIER_TAXONOMIC = "taxonomic_classification"
const val IDENTIFIER_TYPE_TITLE = "type_title"
const val IDENTIFIER_VALUE_UNKNOWN = "unknown"

const val KIWI_UPDATE_FORMAT = "EEE, dd MMM yyyy h:mma"

const val MUSEUM_SEARCH_URL = "https://collection-publicapi.aucklandmuseum.com/api/v3/"
const val MUSEUM_FACET_COLLECTION = "collection_area:%s"
const val MUSEUM_FACET_DEPARTMENT = "department:%s"
const val MUSEUM_DATA_AUTH = "Authorization"
const val MUSEUM_DATA_TYPE = "application/json"
// https://collection-publicapi.aucklandmuseum.com/api/v3/opacobjects/?query=&limit=0
const val MUSEUM_INDEX_OBJECTS = "opacobjects" /* 1,032,927 */
const val MUSEUM_INDEX_OBJECT = "opacobjects/{objectId}"
// https://collection-publicapi.aucklandmuseum.com/api/v3/opacpersons/?query=&limit=0
const val MUSEUM_INDEX_PERSONS = "opacpersons" /* 97,989 */
const val MUSEUM_INDEX_PERSON = "opacpersons/{personId}"
const val MUSEUM_RELATED_OBJECT = "record"
const val MUSEUM_RELATED_PERSON = "person"
// i.e. .../collections/person/8473 OR .../collections/record/671988
const val MUSEUM_RELATED_URL = "https://www.aucklandmuseum.com/discover/collections/%s/%s"
const val MUSEUM_TYPE_KEY = "Accept"
const val MUSEUM_VIEW_DETAIL = "detail"

const val SEPARATOR_DOUBLE_PIPE = "||"
const val SEPARATOR_ITEM = ", "
const val SEPARATOR_PIPE = " | "
const val SEPARATOR_RECORD = "\u001E" //  (Record Separator)
const val SEPARATOR_SEMI_COLON = ";"

const val SETTING_COMMON_NUM_RESULTS   = "num_results"
const val SETTING_COMMON_SAVE_HISTORY  = "save_history"
const val SETTING_COMMON_SORT_ORDER    = "sort_order"
const val SETTING_SAVE_FAVOURITES_DATE = "save_fav_date"
const val SETTING_SORT_FAVOURITES_BY   = "sort_faves_by"
