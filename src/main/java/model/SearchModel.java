package model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dyds.tvseriesinfo.fulllogic.DataBase;
import utils.WikiPage;
import dyds.tvseriesinfo.fulllogic.WikipediaPageAPI;
import dyds.tvseriesinfo.fulllogic.WikipediaSearchAPI;
import presenter.SeriesPresenter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SearchModel {
    private SeriesPresenter presenter;

    private WikipediaSearchAPI searchAPI;

    private WikipediaPageAPI pageAPI;

    private Gson gson;

    private ArrayList<SearchModelListener> listeners = new ArrayList<>();

    private ArrayList<WikiPage> searchResultsArray;

    private String extract;


    public SearchModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/w/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        searchAPI = retrofit.create(WikipediaSearchAPI.class);
        pageAPI = retrofit.create(WikipediaPageAPI.class);

        gson = new Gson();
    }

    public void setPresenter(SeriesPresenter presenter) {
        this.presenter = presenter;
    }

    private void notifySeiesSearchFinishedListener() {
        for (SearchModelListener listener: listeners) {
            listener.seriesSearchFinished();
        }
    }
    private void notifyExtractSearchFinishedListener() {
        for (SearchModelListener listener: listeners) {
            listener.extractSearchFinished();
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

    public void searchPageExtract(WikiPage wikiPage){
            extract = "";
            try {
                //This may take some time, dear user be patient in the meanwhile!
                //setWorkingStatus();
                //Now fetch the info of the select page
                Response<String> callForPageResponse = pageAPI.getExtractByPageID(wikiPage.getPageID()).execute();

                System.out.println("JSON " + callForPageResponse.body());

                //toAlberto: This is similar to the code above, but here we parse the wikipage answer.
                //For more details on Gson look for very important coment 1, or just google it :P
                JsonObject jsonPageResponse = gson.fromJson(callForPageResponse.body(), JsonObject.class);
                JsonObject jsonPageQuery = jsonPageResponse.get("query").getAsJsonObject();
                JsonObject jsonPagesFound = jsonPageQuery.get("pages").getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> pagesFoundSet = jsonPagesFound.entrySet();
                Map.Entry<String, JsonElement> firstPageFound = pagesFoundSet.iterator().next();
                JsonObject pageFound = firstPageFound.getValue().getAsJsonObject();
                JsonElement jsonPageExtract = pageFound.get("extract");
                if (jsonPageExtract == null) {
                    extract = "No Results";
                } else {
                    extract = "<h1>" + wikiPage.getTitle() + "</h1>";
                    extract += jsonPageExtract.getAsString().replace("\\n", "\n");
                    extract = textToHtml(extract);
                }


            } catch (Exception e12) {
                System.out.println(e12.getMessage());
            }

            notifyExtractSearchFinishedListener();


    }

    public String getExtract(){
        return extract;
    }


    public Object[] getSavedTitles(){
        return DataBase.getTitles().stream().sorted().toArray();
    }

    public static String textToHtml(String text) {

        StringBuilder builder = new StringBuilder();

        builder.append("<font face=\"arial\">");

        String fixedText = text
                .replace("'", "`"); //Replace to avoid SQL errors, we will have to find a workaround..

        builder.append(fixedText);

        builder.append("</font>");

        return builder.toString();
    }

    public String getSavedExtract(String selectedTitle) {
        return DataBase.getExtract(selectedTitle);
    }

    public void updateSavedPage(String pageTitle, String pageExtract) {
        DataBase.saveInfo(pageTitle, pageExtract);
    }

    public void addListener(SearchModelListener listener) { this.listeners.add(listener);    }
}
