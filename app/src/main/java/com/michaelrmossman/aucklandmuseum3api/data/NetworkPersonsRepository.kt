package com.michaelrmossman.aucklandmuseum3api.data

import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.model.OpacPersons
import com.michaelrmossman.aucklandmuseum3api.network.MuseumApiService
import com.michaelrmossman.aucklandmuseum3api.network.PersonResponse
import com.michaelrmossman.aucklandmuseum3api.network.PersonsResponse
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_VIEW_DETAIL
import com.michaelrmossman.aucklandmuseum3api.util.errorMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkPersonsRepository(
    private val apiService: MuseumApiService
) {

    fun getPersonSearchResults(
        callback: (PersonsResponse) -> Unit,
        query: String,
        sortOrder: String,
        startFrom: Int
    ) {
        val call = apiService.getPersonSearchResults(
            view = MUSEUM_VIEW_DETAIL,
            query = query,
            sortOrder = sortOrder,
            from = startFrom
        )
        call.enqueue(object: Callback<OpacPersons> {
            override fun onResponse(
                call:     Call<OpacPersons>,
                response: Response<OpacPersons>
            ) {
                if (response.errorBody() != null) {
                    callback(
                        PersonsResponse(
                            responseCode = response.code(),
                            responseMessage =
                                response.errorBody()?.errorMessage()
                        )
                    )

                } else if (response.isSuccessful) {
                    response.body()?.let { searchResponse ->
                        callback(
                            PersonsResponse(
                                searchResults = searchResponse,
                                responseCode = response.code()
                            )
                        )
                    }

                } else {
                    callback(
                        PersonsResponse(
                            responseCode = response.code()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<OpacPersons>, throwable: Throwable
            ) {
                callback(PersonsResponse())
                println(throwable)
            }
        })
    }

    fun getPersonSearchResultById(
        callback: (PersonResponse) -> Unit,
        id: String
    ) {
        val call = apiService.getPersonSearchResultById(
            personId = id
        )
        call.enqueue(object: Callback<OpacPerson> {
            override fun onResponse(
                call:     Call<OpacPerson>,
                response: Response<OpacPerson>
            ) {
                if (response.errorBody() != null) {
                    callback(
                        PersonResponse(
                            responseCode = response.code(),
                            responseMessage =
                                response.errorBody()?.errorMessage()
                        )
                    )

                } else if (response.isSuccessful) {
                    response.body()?.let { searchResponse ->
                        callback(
                            PersonResponse(
                                searchResult = searchResponse,
                                responseCode = response.code()
                            )
                        )
                    }

                } else {
                    callback(
                        PersonResponse(
                            responseCode = response.code()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<OpacPerson>, throwable: Throwable
            ) {
                callback(PersonResponse())
                println(throwable)
            }
        })
    }
}