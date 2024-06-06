package model;

import model.APIs.SearchPageAPInterface;
import utils.WikiPage;
import java.util.ArrayList;

public class SearchWikiPageModel {
    private WikiPage page;
    private ArrayList<SearchWikiPageModelListener> listeners = new ArrayList<>();
    private SearchPageAPInterface pageAPI;

    public SearchWikiPageModel(SearchPageAPInterface pageAPI) {  this.pageAPI = pageAPI;  }
    public void addListener(SearchWikiPageModelListener listener) { this.listeners.add(listener);}
    public void searchPageExtract(WikiPage wikiPage) throws Exception{
        page = pageAPI.getExtract(wikiPage);
        notifyExtractSearchFinishedListener();
    }
    private void notifyExtractSearchFinishedListener() {
        for (SearchWikiPageModelListener listener: listeners) {
            listener.extractSearchFinished();
        }
    }
    public String getExtract() {
        return page.getExtract();
    }

    public String getUrl(){
        return page.getUrl();
    }


}
