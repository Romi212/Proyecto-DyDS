package model;

import model.APIs.SearchSeriesAPInterface;
import utils.WikiPage;
import java.util.ArrayList;

public class SearchSeriesModel {
    private SearchSeriesAPInterface searchAPI;
    private ArrayList<SearchSeriesModelListener> listeners = new ArrayList<>();
    private ArrayList<WikiPage> searchResultsArray;

    public SearchSeriesModel(SearchSeriesAPInterface searchAPI) {
        this.searchAPI = searchAPI;
    }
    public void addListener(SearchSeriesModelListener listener) { this.listeners.add(listener);    }
    public void searchSeries(String seriesName) throws Exception {
        searchResultsArray = searchAPI.searchSeries(seriesName);
        notifySeiesSearchFinishedListener();
    }
    private void notifySeiesSearchFinishedListener() {
        for (SearchSeriesModelListener listener: listeners) {
            listener.seriesSearchFinished();
        }
    }
    public ArrayList<WikiPage> getSearchResults() {
        return searchResultsArray;
    }
}
