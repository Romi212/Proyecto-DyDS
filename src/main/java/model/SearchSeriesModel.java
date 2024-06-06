package model;

import model.APIs.SearchSeriesAPInterface;
import utils.WikiPage;
import presenter.SeriesPresenter;

import java.util.ArrayList;

public class SearchSeriesModel {
    private SeriesPresenter presenter;
    private SearchSeriesAPInterface searchAPI;
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
