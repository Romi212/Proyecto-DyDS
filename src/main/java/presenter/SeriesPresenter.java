package presenter;

import model.*;
import utils.WikiPage;
import view.ScoredView;
import view.SearcherView;
import view.StoredView;
import view.TVSeriesSearcherWindow;

import javax.xml.crypto.Data;
import java.util.ArrayList;

import static utils.TextProcessing.generateURL;
import static utils.TextProcessing.textToHtml;

public class SeriesPresenter {

    SearchSeriesModel seriesSearchModel;
    SearchWikiPageModel wikiPageModel;
    DataBaseModel dataBaseModel;
    SearcherView searchView;
    StoredView storedView;
    ScoredView scoredView;
    Thread taskThread;
    TVSeriesSearcherWindow mainWindow;

    public SeriesPresenter(SearchSeriesModel seriesSearchModel, SearchWikiPageModel wikiPageModel, DataBaseModel dataBaseModel) {
        this.seriesSearchModel = seriesSearchModel;
        this.wikiPageModel = wikiPageModel;
        this.dataBaseModel = dataBaseModel;
        seriesSearchModel.setPresenter(this);
    }

    public void start(){
        mainWindow = new TVSeriesSearcherWindow(this);
        searchView = mainWindow.getSearchView();
        searchView.setUpView();

        storedView = mainWindow.getStoredView();
        storedView.setUpView();

        scoredView = mainWindow.getScoredView();
        scoredView.setUpView();

        mainWindow.showView();

        initListeners();
    }

    private void initListeners(){
        seriesSearchModel.addListener(this::showSeriesSearchResults);
        dataBaseModel.addListener(new DataBaseModelListener() {
                                      @Override
                                      public void errorOnDataBase(String message) {
                                            mainWindow.showErrorMessage(searchView.getContentPane(), message);
                                      }

                                      @Override
                                      public void SeriesListFound() {
                                          showSavedSeries();
                                      }

                                      @Override
                                        public void extractFound() {
                                            showExtract();
                                      }


                                      @Override
                                        public void ScoredSeriesFound() {
                                            showScoredSeries();
                                        }
                                  }
        );
        wikiPageModel.addListener(this::showExtractSearchResults);
    }

    private void showSeriesSearchResults(){
        ArrayList<WikiPage> results = seriesSearchModel.getSearchResults();
        EditScoredSeries(results);
        searchView.showResults(results);
        mainWindow.setWatingStatus();
    }

    private ArrayList<WikiPage> EditScoredSeries(ArrayList<WikiPage> results) {
        for (WikiPage series : results) {
            if(hasScore(series.getTitle())) series.getGraphicMenuItem().changeScoredText();;
        }
        return results;
    }

    private void showExtractSearchResults(){
        searchView.setLasSearchedExtract(wikiPageModel.getExtract());
        searchView.setSearchResultTextPane(wikiPageModel.getExtract() +"<a href='"+wikiPageModel.getUrl()+"'>"+wikiPageModel.getUrl()+"</a>"  );
        mainWindow.setWatingStatus();
    }

    //SEARCH VIEW SEARCH BUTTON
    public void searchSeries() {
        taskThread = new Thread(() -> {
            mainWindow.setWorkingStatus();
            String seriesName = searchView.getSeriesName();

            if(seriesName.isEmpty()) {
                mainWindow.setWatingStatus();
                mainWindow.showErrorMessage(searchView.getContentPane(), "Search field is empty!");
            } else{
                seriesSearchModel.searchSeries(seriesName);
            }
        });
        //TODO: TERMINAR HILO???
        taskThread.start();

    }

    public void getSelectedExtract(){
        taskThread = new Thread(() -> {
            mainWindow.setWatingStatus();
            WikiPage selectedResult = searchView.getLastSearchedSeries();
            getExtract(selectedResult);
        });
        taskThread.start();
    }

    private void getExtract(WikiPage selectedResult){
        wikiPageModel.searchPageExtract(selectedResult);
        if(hasScore(selectedResult.getTitle())) searchScore(selectedResult.getTitle());
        else searchView.showNoScore();
    }

    //STORED VIEW
    public void initializeSavedPanel(){
        dataBaseModel.getSavedTitles();
    }

    private void showSavedSeries(){
        storedView.setSelectSavedComboBox(dataBaseModel.getSavedTitlesList());
    }

    public void getSavedExtract(){
        taskThread = new Thread(() -> {
            mainWindow.setWorkingStatus();
            String selectedTitle = storedView.getSeletedSavedTitle();
            dataBaseModel.getSavedExtract(selectedTitle);
        });
        //TODO: TERMINAR HILO???
        taskThread.start();
    }
    private void showExtract(){
        String selectedExtract = dataBaseModel.getExtract();
        int id = dataBaseModel.getID();
        storedView.setSelectedExtract(textToHtml(selectedExtract));
        storedView.setURL(generateURL(id));
        mainWindow.setWatingStatus();
    }

    public void deleteSelectedExtract() {
        if(storedView.existSelectedEntry()){
            String selectedTitle = storedView.getSeletedSavedTitle();
            if(mainWindow.askConfirmation(storedView.getContentPane(), "Are you sure you want to delete the selected entry?")){
            //TODO: Best con listener o cone xception?
            dataBaseModel.deleteSavedPage(selectedTitle);
            dataBaseModel.getSavedTitles();
            storedView.emptySavedTextPane();
            }
        }
    }
    public void recordScore() {
        int score = searchView.getScore();
        WikiPage series = searchView.getLastSearchedSeries();
        series.setScore(score);
        dataBaseModel.setScore(series);
    }

    private boolean hasScore(String seriesName){
        return dataBaseModel.getScore(seriesName) != -1;
    }
    public void searchScore( String seriesName) {
        searchView.showScore();
        int score = dataBaseModel.getScore(seriesName);
        searchView.setScore(score);
    }

    public void getSavedScores() {
        dataBaseModel.getScoredSeries();

    }

    private void showScoredSeries(){
        scoredView.showSavedScores(dataBaseModel.getScoredSeriesList());
    }

    public void onRowSelected() {
        WikiPage selectedSeries = scoredView.getSelectedSeries();
        searchView.setLastSearchedSeries(selectedSeries);
        getExtract(selectedSeries);
       // scoredView.deselectRows();
        mainWindow.showSearchPanel();

    }

    public void setScore() {
        searchView.showScore();
    }

    public void saveExtractChanges() {
        updateStoredExtract(storedView.getSeletedSavedTitle(), String.valueOf(dataBaseModel.getID()),storedView.getSelectedSavedExtract());
    }
    public void saveLocally() {

        try{
            WikiPage pageToSave = searchView.getLastSearchedSeries();
            if(pageToSave != null) updateStoredExtract(pageToSave.getTitle(), pageToSave.getPageID(), pageToSave.getExtract());
            mainWindow.showSuccessMessage(searchView.getContentPane(), "The series was correctly saved!");

        } catch(Exception e){
            mainWindow.showErrorMessage(searchView.getContentPane(), e.getMessage());

        }


    }
    private void updateStoredExtract(String title, String pageid,String extract){
        dataBaseModel.updateSavedPage(title.replace("'", "`"), pageid,extract);

    }

    public void changedTabs() {
        int selectedTab = mainWindow.getSelectedTab();
        switch (selectedTab){
            case 0:
                mainWindow.setWatingStatus();
                break;
            case 1:
                initializeSavedPanel();
                break;
            case 2:
                getSavedScores();
                break;
        }
    }


}
