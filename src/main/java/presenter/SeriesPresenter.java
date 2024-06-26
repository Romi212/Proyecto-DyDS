package presenter;

import model.*;
import utils.WikiPage;
import view.*;

public class SeriesPresenter {

    SearchSeriesModel seriesSearchModel;
    SearchWikiPageModel wikiPageModel;
    DataBaseModel dataBaseModel;
    TVSeriesSearcherWindow mainWindow;
    ScorePresenter scorePresenter;
    SearchPresenter searchPresenter;
    StorePresenter storePresenter;

    public SeriesPresenter(SearchSeriesModel seriesSearchModel, SearchWikiPageModel wikiPageModel, DataBaseModel dataBaseModel) {

        this.seriesSearchModel = seriesSearchModel;
        this.wikiPageModel = wikiPageModel;
        this.dataBaseModel = dataBaseModel;
        searchPresenter = new SearchPresenter(dataBaseModel,seriesSearchModel, wikiPageModel, this);
        storePresenter = new StorePresenter( dataBaseModel, this);
        scorePresenter = new ScorePresenter(dataBaseModel, this);
    }

    public void start(TVSeriesSearcherWindow mainWindow){
        this.mainWindow = mainWindow;
        SearcherViewInterface searchView = mainWindow.getSearchView();
        searchPresenter.setView(searchView);
        searchView.setUpView();
        StoredViewInterface storedViewInterface = mainWindow.getStoredView();
        storePresenter.setView(storedViewInterface);
        storedViewInterface.setUpView();
        ScoredViewInterface scoredViewInterface = mainWindow.getScoredView();
        scorePresenter.setView(scoredViewInterface);
        scoredViewInterface.setUpView();

        mainWindow.showView();

        initListeners();
    }

    private void initListeners(){
        seriesSearchModel.addListener(() -> searchPresenter.showSeriesSearchResults());
        dataBaseModel.addListener(new DataBaseModelListener() {
                                      @Override
                                      public void errorOnDataBase(String message) {
                                            mainWindow.showErrorMessage(message);
                                      }

                                      @Override
                                      public void SeriesListFound() {
                                          storePresenter.showSavedSeries();
                                      }

                                      @Override
                                        public void extractFound() {
                                            storePresenter.showExtract();
                                      }


                                      @Override
                                        public void ScoredSeriesFound() {
                                            scorePresenter.showScoredSeries();
                                        }
                                  }
        );
        wikiPageModel.addListener(() -> searchPresenter.showExtractSearchResults());
    }
    public void showSeries(WikiPage series) {
        searchPresenter.showSeries(series);
    }
    public void changedTabs() {
        int selectedTab = mainWindow.getSelectedTab();
        switch (selectedTab){
            case 0:
                mainWindow.setWatingStatus();
                break;
            case 1:
                storePresenter.initializeSavedPanel();
                break;
            case 2:
                scorePresenter.getSavedScores();
                break;
        }
    }

    public boolean hasScore(String title){
        return scorePresenter.hasScore(title);
    }

    public void setWindowWatingStatus() {
        mainWindow.setWatingStatus();
    }

    public void setWindowWorkingStatus() {
        mainWindow.setWorkingStatus();
    }

    public void showErrorMessage(String message) {
        mainWindow.showErrorMessage(message);
    }

    public void showSuccessMessage(String message) {
        mainWindow.showSuccessMessage(message);
    }

    public SearchPresenter getSearchPresenter() {
        return searchPresenter;
    }

    public StorePresenter getStorePresenter() {
        return storePresenter;
    }

    public boolean askDeleteConfirmation() {
       return  mainWindow.askConfirmation("Are you sure you want to delete the selected entry?");
    }

    public ScorePresenter getScorePresenter() {
        return scorePresenter;
    }

    public void showSearchPanel() {
        mainWindow.showSearchPanel();
    }

    public void closeWindow() {
        dataBaseModel.closeConnection();
    }
}
