package model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dyds.tvseriesinfo.fulllogic.DataBase;
import utils.WikiPage;
import dyds.tvseriesinfo.fulllogic.WikipediaSearchAPI;
import presenter.SeriesPresenter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class SearchSeriesModel {
    private SeriesPresenter presenter;

    private WikipediaSearchAPI searchAPI;



    private Gson gson;

    private ArrayList<SearchSeriesModelListener> listeners = new ArrayList<>();

    private ArrayList<WikiPage> searchResultsArray;




    public SearchSeriesModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/w/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        searchAPI = retrofit.create(WikipediaSearchAPI.class);

        gson = new Gson();
    }

    public void setPresenter(SeriesPresenter presenter) {
        this.presenter = presenter;
    }

    private void notifySeiesSearchFinishedListener() {
        for (SearchSeriesModelListener listener: listeners) {
            listener.seriesSearchFinished();
        }
    }

    public void searchSeries(String seriesName) {

        Response<String> searchResponse;
        searchResultsArray = new ArrayList<>();
        try {

            //ToAlberto: First, lets search for the term in Wikipedia
            searchResponse = searchAPI.searchForTerm(seriesName + " (Tv series) articletopic:\"television\"").execute();

            //Show the result for testing reasons, if it works, dont forget to delete!
            System.out.println("JSON " + searchResponse.body());

            //ToAlberto: Very Important Comment 1
            //This is the code parses the string with the search results for the query
            //The string uses the JSON format to the describe the query and the results
            //So we will use the Google library for JSONs (Gson) for its parsing and manipulation
            //Basically, we will turn the string into a JSON object,
            //With such object we can acceses to its fields using get(fieldname) method provided by Gson

            JsonObject jsonSearchResult = gson.fromJson(searchResponse.body(), JsonObject.class);
            JsonObject jsonResultQuery = jsonSearchResult.get("query").getAsJsonObject();
            Iterator<JsonElement> resultIterator = jsonResultQuery.get("search").getAsJsonArray().iterator();
            JsonArray jsonResults = jsonResultQuery.get("search").getAsJsonArray();

            //toAlberto: shows each result in the JSonArry in a Popupmenu
           // JPopupMenu searchOptionsMenu = new JPopupMenu("Search Results");

            for (JsonElement jsonResult : jsonResults) {
                JsonObject searchResultElement = jsonResult.getAsJsonObject();
                String searchResultTitle = searchResultElement.get("title").getAsString();
                String searchResultPageId = searchResultElement.get("pageid").getAsString();
                String searchResultSnippet = searchResultElement.get("snippet").getAsString();

                WikiPage wikiPage = new WikiPage(searchResultTitle, searchResultPageId, searchResultSnippet);
                searchResultsArray.add(wikiPage);

                //toAlberto: Adding an event to retrive the wikipage when the user clicks an item in the Popupmenu

            }
            //searchOptionsMenu.show(textField1, textField1.getX(), textField1.getY());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        notifySeiesSearchFinishedListener();

    }

    public ArrayList<WikiPage> getSearchResults() {
        return searchResultsArray;
    }






    public void addListener(SearchSeriesModelListener listener) { this.listeners.add(listener);    }
}