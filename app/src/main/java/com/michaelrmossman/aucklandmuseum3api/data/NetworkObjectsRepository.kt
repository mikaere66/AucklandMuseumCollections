package com.michaelrmossman.aucklandmuseum3api.data

import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.model.OpacObjects
import com.michaelrmossman.aucklandmuseum3api.network.MuseumApiService
import com.michaelrmossman.aucklandmuseum3api.network.ObjectResponse
import com.michaelrmossman.aucklandmuseum3api.network.ObjectsResponse
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_VIEW_DETAIL
import com.michaelrmossman.aucklandmuseum3api.util.errorMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkObjectsRepository(
    private val apiService: MuseumApiService
) {

    fun getObjectSearchResults(
        callback: (ObjectsResponse) -> Unit,
        facets: List<Pair<String, String>>?,
        query: String,
        sortOrder: String,
        startFrom: Int
    ) {
        val call = when (facets?.isNotEmpty()) {
            true -> {
                val sb = StringBuilder()
                facets.forEachIndexed { index, facet ->
                    sb.append(
                        String.format(
                            // MUSEUM_FACET_COLLECTION
                            facet.first,
                            // Collection.CollectionDocHeritage
                            facet.second
                        )
                    )
                    if (index < facets.lastIndex) {
                        sb.append(",")
                    }
                }
                val facetsFormatted = sb.toString()
                // android.util.Log.d("HEY",facetsFormatted)
                apiService.getObjectSearchResultsByFacet(
                    view = MUSEUM_VIEW_DETAIL,
                    query = query,
                    sortOrder = sortOrder,
                    from = startFrom,
                    facets = facetsFormatted
                )
            }
            else -> apiService.getObjectSearchResults(
                view = MUSEUM_VIEW_DETAIL,
                query = query,
                sortOrder = sortOrder,
                from = startFrom
            )
        }
        call.enqueue(object: Callback<OpacObjects> {
            override fun onResponse(
                call:     Call<OpacObjects>,
                response: Response<OpacObjects>
            ) {
                if (response.errorBody() != null) {
                    callback(
                        ObjectsResponse(
                            responseCode = response.code(),
                            responseMessage =
                                response.errorBody()?.errorMessage()
                        )
                    )

                } else if (response.isSuccessful) {
                    response.body()?.let { searchResponse ->
                        callback(
                            ObjectsResponse(
                                searchResults = searchResponse,
                                responseCode = response.code()
                            )
                        )
                    }

                } else {
                    callback(
                        ObjectsResponse(
                            responseCode = response.code()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<OpacObjects>, throwable: Throwable
            ) {
                callback(ObjectsResponse())
                println(throwable)
            }
        })
    }

    fun getObjectSearchResultById(
        callback: (ObjectResponse) -> Unit,
        id: String
    ) {
        val call = apiService.getObjectSearchResultById(
            objectId = id
        )
        call.enqueue(object: Callback<OpacObject> {
            override fun onResponse(
                call:     Call<OpacObject>,
                response: Response<OpacObject>
            ) {
                if (response.errorBody() != null) {
                    callback(
                        ObjectResponse(
                            responseCode = response.code(),
                            responseMessage =
                                response.errorBody()?.errorMessage()
                        )
                    )

                } else if (response.isSuccessful) {
                    response.body()?.let { searchResponse ->

                        callback(
                            ObjectResponse(
                                searchResult = searchResponse,
                                responseCode = response.code()
                            )
                        )
                    }

                } else {
                    callback(
                        ObjectResponse(
                            responseCode = response.code()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<OpacObject>, throwable: Throwable
            ) {
                callback(ObjectResponse())
                println(throwable)
            }
        })
    }
}