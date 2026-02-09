package com.michaelrmossman.aucklandmuseum3api.network

import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.model.OpacObjects
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.model.OpacPersons
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_INDEX_OBJECT
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_INDEX_OBJECTS
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_INDEX_PERSON
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_INDEX_PERSONS
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_SEARCH_PATH
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_TYPE_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface MuseumApiService {

    @GET(MUSEUM_INDEX_OBJECT)
    fun getObjectSearchResultById(
        @Path("objectId") objectId: String
    ) : Call<OpacObject>

    @GET(MUSEUM_INDEX_OBJECTS)
    fun getObjectSearchResultsByFacet(
        @Query("view") view: String,
        @Query("query") query: String,
        @Query("direction") sortOrder : String,
        @Query("facet") facets: String,
        @Query("offset") from: Int
    ) : Call<OpacObjects>

    @GET(MUSEUM_INDEX_OBJECTS)
    fun getObjectSearchResults(
        @Query("view") view: String,
        @Query("query") query: String,
        @Query("direction") sortOrder : String,
        @Query("offset") from: Int
    ) : Call<OpacObjects>

    @GET(MUSEUM_INDEX_PERSON)
    fun getPersonSearchResultById(
        @Path("personId") personId: String
    ) : Call<OpacPerson>

    @GET(MUSEUM_INDEX_PERSONS)
    fun getPersonSearchResults(
        @Query("view") view: String,
        @Query("query") query: String,
        @Query("direction") sortOrder : String,
        @Query("offset") from: Int
    ) : Call<OpacPersons>
}