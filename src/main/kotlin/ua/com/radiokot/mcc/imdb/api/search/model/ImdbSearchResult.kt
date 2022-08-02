package ua.com.radiokot.mcc.imdb.api.search.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ImdbSearchResult(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("l")
    val name: String,
    @JsonProperty("y")
    val releaseYear: Int,
)