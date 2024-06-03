package model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dyds.tvseriesinfo.fulllogic.WikipediaPageAPI;
import model.APIs.SearchPageAPI;
import model.APIs.SearchPageAPInterface;
import model.APIs.SearchSeriesAPI;
import presenter.SeriesPresenter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import utils.WikiPage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static utils.TextProcessing.textToHtml;

public class SearchWikiPageModel {



    private String extract;


    private ArrayList<SearchWikiPageModelListener> listeners = new ArrayList<>();
    private SearchPageAPInterface pageAPI;

    private SeriesPresenter presenter;

    public SearchWikiPageModel(SearchPageAPInterface pageAPI

        ) {
            this.pageAPI = pageAPI;
    }

    public void setPresenter(SeriesPresenter presenter) {
        this.presenter = presenter;
    }

    private void notifyExtractSearchFinishedListener() {
        for (SearchWikiPageModelListener listener: listeners) {
            listener.extractSearchFinished();
        }
    }

    public void searchPageExtract(WikiPage wikiPage){
        extract = pageAPI.getExtract(wikiPage);


        notifyExtractSearchFinishedListener();


    }

    public String getExtract(){
        return extract;
    }

    public void addListener(SearchWikiPageModelListener listener) { this.listeners.add(listener);    }

}
