package model.APIs;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import utils.WikiPage;
import java.io.IOException;
import java.util.ArrayList;

public class SearchSeriesAPI implements SearchSeriesAPInterface {

    private WikipediaSearchAPI searchAPI;
    private Gson gson;

    //TODO UNIR LAS 2API???
    public SearchSeriesAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/w/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        searchAPI = retrofit.create(WikipediaSearchAPI.class);
        gson = new Gson();
    }

    @Override
    public ArrayList<WikiPage> searchSeries(String seriesName)throws IOException {
        Response<String> searchResponse;
        ArrayList<WikiPage> searchResultsArray = new ArrayList<>();

        searchResponse = searchAPI.searchForTerm(seriesName + " (Tv series) articletopic:\"television\"").execute();

        JsonObject jsonSearchResult = gson.fromJson(searchResponse.body(), JsonObject.class);
        JsonObject jsonResultQuery = jsonSearchResult.get("query").getAsJsonObject();
        JsonArray jsonResults = jsonResultQuery.get("search").getAsJsonArray();

        for (JsonElement jsonResult : jsonResults) {
            JsonObject searchResultElement = jsonResult.getAsJsonObject();
            String searchResultTitle = searchResultElement.get("title").getAsString();
            String searchResultPageId = searchResultElement.get("pageid").getAsString();
            String searchResultSnippet = searchResultElement.get("snippet").getAsString();

            WikiPage wikiPage = new WikiPage(searchResultTitle, searchResultPageId, searchResultSnippet);
            searchResultsArray.add(wikiPage);
        }

        return searchResultsArray;
    }
}
