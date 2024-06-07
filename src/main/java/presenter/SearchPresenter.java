package presenter;

import model.DataBaseModel;
import model.SearchSeriesModel;
import model.SearchWikiPageModel;
import utils.WikiPage;
import view.SearcherViewInterface;

import java.util.ArrayList;
public class SearchPresenter {
    private SearchSeriesModel seriesSearchModel;
    private SearchWikiPageModel wikiPageModel;
    Thread taskThread;
    private SearcherViewInterface searchView;
    private DataBaseModel dataBaseModel;
    private SeriesPresenter presenter;
    public SearchPresenter(DataBaseModel dataBaseModel, SearchSeriesModel seriesSearchModel, SearchWikiPageModel wikiPageModel,SeriesPresenter presenter){
        this.seriesSearchModel = seriesSearchModel;
        this.wikiPageModel = wikiPageModel;
        this.presenter = presenter;
        this.dataBaseModel = dataBaseModel;
    }
    public void setView(SearcherViewInterface searchView){  this.searchView = searchView;  }
    public void showSeriesSearchResults(){
        ArrayList<WikiPage> results = seriesSearchModel.getSearchResults();
        EditScoredSeries(results);
        searchView.showResults(results);
        presenter.setWindowWatingStatus();
    }
    private ArrayList<WikiPage> EditScoredSeries(ArrayList<WikiPage> results) {
        for (WikiPage series : results) {
            if(presenter.hasScore(series.getTitle())) series.getGraphicMenuItem().changeScoredText();;
        }
        return results;
    }
    public void showExtractSearchResults(){
        searchView.setLasSearchedExtract(wikiPageModel.getExtract());
        searchView.setSearchResultTextPane(wikiPageModel.getExtract() +"<a href='"+wikiPageModel.getUrl()+"'>"+wikiPageModel.getUrl()+"</a>"  );
        presenter.setWindowWatingStatus();
    }
    public void searchSeries() {
        taskThread = new Thread(() -> {
            presenter.setWindowWorkingStatus();
            String seriesName = searchView.getSeriesName();
            if(seriesName.isEmpty()) {
                presenter.setWindowWatingStatus();
                presenter.showErrorMessage("Search field is empty");
            } else{
                try{
                    seriesSearchModel.searchSeries(seriesName);
                } catch(Exception e) {
                    presenter.setWindowWatingStatus();
                    presenter.showErrorMessage(e.getMessage());
                }
            }
        });
        taskThread.start();

    }
    public void getSelectedExtract(){
        taskThread = new Thread(() -> {
            presenter.setWindowWatingStatus();
            WikiPage selectedResult = searchView.getLastSearchedSeries();
            getExtract(selectedResult);
        });
        taskThread.start();
    }
    public void getExtract(WikiPage selectedResult){
        try{
            wikiPageModel.searchPageExtract(selectedResult);
            if(presenter.hasScore(selectedResult.getTitle())) searchScore(selectedResult.getTitle());
            else searchView.showNoScore();
        } catch(Exception e) {
            presenter.showErrorMessage(e.getMessage());
        }
    }
    public void searchScore( String seriesName) {
        searchView.showScore();
        int score = dataBaseModel.getScore(seriesName);
        searchView.setScore(score);
    }
    public void setScore(){ searchView.showScore(); }
    public void recordScore() {
        int score = searchView.getScore();
        WikiPage series = searchView.getLastSearchedSeries();
        series.setScore(score);
        dataBaseModel.setScore(series);
    }
    public void saveLocally() {
        try{
            WikiPage pageToSave = searchView.getLastSearchedSeries();
            if(pageToSave != null) storeNewExtract(pageToSave.getTitle(), pageToSave.getPageID(), pageToSave.getExtract());
            presenter.showSuccessMessage( "The series was correctly saved!");
        } catch(Exception e){
            presenter.showErrorMessage( e.getMessage());
        }
    }

    private void storeNewExtract(String title, String pageid, String extract){
        dataBaseModel.updateSavedPage(title.replace("'", "`"), pageid,extract);
    }
    public void showSeries(WikiPage selectedSeries) {
        searchView.setLastSearchedSeries(selectedSeries);
        getExtract(selectedSeries);
        presenter.showSearchPanel();
    }
}
