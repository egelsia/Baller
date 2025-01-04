package com.baller.service

import com.baller.model.Fixture
import com.baller.model.Stage
import com.baller.model.Standing
import com.baller.model.Team
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import java.io.IOException

const val baseUrl = "https://api.sportmonks.com/v3/football"
const val token = "api_token=OmVVL6nM5b2biT30RBExu4gaAHXGmlKlEbEe1S7LO4z0eBsx4lct02IGNczj"

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int, val message: String) : ApiResult<Nothing>()
}

class APIService {

    // if (league_id == 501) => Scottish Premiership
    // if (league_id == 271) => Danish Superliga

    // Danish Superliga season id = 23584
    // Scottish Premiership season id = 23690


    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getTeamSchedules(currentSeason: Int, teamId: Int) : ApiResult<List<Fixture>> {
        val responseUrl = "/schedules/seasons/$currentSeason/teams/$teamId?${token}"

        return try {
            val response: HttpResponse = client.get(baseUrl + responseUrl)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val json = Json {
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                    }
                    val responseData: JsonObject = response.body()
                    val stagesData = responseData["data"]?.jsonArray
                        ?: return ApiResult.Error(-1, "Invalid Response Format")

                    // Decode to list of stages
                    val stages = json.decodeFromJsonElement<List<Stage>>(stagesData)

                    // Flatten the structure: stages -> rounds -> fixtures
                    val allFixtures = stages.flatMap { stage ->
                        stage.rounds.flatMap { round ->
                            round.fixtures
                        }
                    }

                    ApiResult.Success(allFixtures)
                }
                HttpStatusCode.BadRequest -> ApiResult.Error(
                    400,
                    "Bad Request: The request was malformed"
                )
                HttpStatusCode.Unauthorized -> ApiResult.Error(
                    401,
                    "Unauthorized: Please check API credentials"
                )
                HttpStatusCode.Forbidden -> ApiResult.Error(
                    403,
                    "Forbidden: This resource is not available in your current plan"
                )
                HttpStatusCode.TooManyRequests -> ApiResult.Error(
                    429,
                    "Rate Limit Exceeded."
                )
                HttpStatusCode.InternalServerError -> ApiResult.Error(
                    500,
                    "Internal Server Error."
                )
                else -> ApiResult.Error(
                    response.status.value,
                    "Request failed with status: ${response.status.description}"
                )
            }
        } catch (e: IOException) {
            ApiResult.Error(-1, "Network Error. Please check your internet connection.")
        } catch (e: SerializationException) {
            ApiResult.Error(-1, "Data parsing error: ${e.message}")
        } catch (e: Exception) {
            ApiResult.Error(-1, "Unknown error: ${e.message}")
        }
    }

    suspend fun getAllTeams(): ApiResult<List<Team>> {
        val responseUrl = "/teams?${token}&include=activeSeasons&per_page=50"

        return try {
            val response: HttpResponse = client.get(baseUrl + responseUrl)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val teamsData: Map<String, JsonElement> = response.body()
                    val teams = Json.decodeFromJsonElement<List<Team>>(
                        teamsData["data"] ?: return ApiResult.Error(
                            -1,
                            "Invalid Response Format"
                        )
                    )
                    ApiResult.Success(teams)
                }
                HttpStatusCode.BadRequest -> ApiResult.Error(
                    400,
                    "Bad Request: The request was malformed"
                )

                HttpStatusCode.Unauthorized -> ApiResult.Error(
                    401,
                    "Unauthorized: Please check API credentials"
                )

                HttpStatusCode.Forbidden -> ApiResult.Error(
                    403,
                    "Forbidden: This resource is not available in your current plan"
                )

                HttpStatusCode.TooManyRequests -> ApiResult.Error(
                    429,
                    "Rate Limit Exceeded."
                )

                HttpStatusCode.InternalServerError -> ApiResult.Error(
                    500,
                    "Internal Server Error."
                )
                else -> ApiResult.Error(
                    response.status.value,
                    "Request failed with status: ${response.status.description}"
                )
            }

        } catch (e: IOException) {
            ApiResult.Error(-1, "Network Error. Please check your internet connection.")
        } catch (e: SerializationException) {
            ApiResult.Error(-1, "Data parsing error: ${e.message}")
        } catch (e: Exception) {
            ApiResult.Error(-1, "Unknown error: ${e.message}")
        }
    }

    suspend fun getStandingsBySeasonId(seasonId: Int): ApiResult<List<Standing>> {
        val responseUrl = "/standings/seasons/$seasonId?${token}&includes=participant"

        return try {
            val response: HttpResponse = client.get(baseUrl + responseUrl)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val standingsData: Map<String, JsonElement> = response.body()
                    val standings = Json.decodeFromJsonElement<List<Standing>>(
                        standingsData["data"] ?: return ApiResult.Error(
                            -1,
                            "Invalid Response Format"
                        )
                    )
                    ApiResult.Success(standings)
                }

                HttpStatusCode.BadRequest -> ApiResult.Error(
                    400,
                    "Bad Request: The request was malformed"
                )

                HttpStatusCode.Unauthorized -> ApiResult.Error(
                    401,
                    "Unauthorized: Please check API credentials"
                )

                HttpStatusCode.Forbidden -> ApiResult.Error(
                    403,
                   "Forbidden: This resource is not available in your current plan"
                )

                HttpStatusCode.TooManyRequests -> ApiResult.Error(
                    429,
                    "Rate Limit Exceeded."
                )

                HttpStatusCode.InternalServerError -> ApiResult.Error(
                    500,
                    "Internal Server Error."
                )
                else -> ApiResult.Error(
                    response.status.value,
                    "Request failed with status: ${response.status.description}"
                )
            }
        } catch (e: IOException) {
            ApiResult.Error(-1, "Network Error. Please check your internet connection.")
        } catch (e: SerializationException) {
            ApiResult.Error(-1, "Data parsing error: ${e.message}")
        } catch (e: Exception) {
            ApiResult.Error(-1, "Unknown error: ${e.message}")
        }
    }

}