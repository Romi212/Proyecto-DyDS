package model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dyds.tvseriesinfo.fulllogic.DataBase;
import model.APIs.SearchSeriesAPI;
import model.APIs.SearchSeriesAPInterface;
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

    SearchSeriesAPInterface searchAPI;


    private ArrayList<SearchSeriesModelListener> listeners = new ArrayList<>();

    private ArrayList<WikiPage> searchResultsArray;




    public SearchSeriesModel(SearchSeriesAPInterface searchAPI

    ) {
        this.searchAPI = searchAPI;

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

        searchResultsArray = searchAPI.searchSeries(seriesName);
        notifySeiesSearchFinishedListener();

    }

    public ArrayList<WikiPage> getSearchResults() {
        return searchResultsArray;
    }






    public void addListener(SearchSeriesModelListener listener) { this.listeners.add(listener);    }
}
