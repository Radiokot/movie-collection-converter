package ua.com.radiokot.mcc.imdb.api.search.service

import ua.com.radiokot.mcc.imdb.api.search.model.ImdbSearchResult

interface ImdbSearchService {
    /**
     * @return a few search results for the query as if it was entered
     * into the main IMDB search.
     */
    fun searchTitles(query: String): Collection<ImdbSearchResult>
}