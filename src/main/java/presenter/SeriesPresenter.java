package presenter;

import model.*;
import utils.WikiPage;
import view.ScoredView;
import view.SearcherView;
import view.StoredView;
import view.TVSeriesSearcherWindow;
import java.util.ArrayList;

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

        wikiPageModel.addListener(this::showExtractSearchResults);
    }

    private void showSeriesSearchResults(){
        searchView.showResults(seriesSearchModel.getSearchResults());
        mainWindow.setWatingStatus();
    }

    private void showExtractSearchResults(){
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
        storedView.setSelectSavedComboBox(dataBaseModel.getSavedTitles());
    }

    public void getSavedExtract(){
        taskThread = new Thread(() -> {
            mainWindow.setWorkingStatus();
            String selectedTitle = storedView.getSeletedSavedTitle();
            String selectedExtract = dataBaseModel.getSavedExtract(selectedTitle);
            storedView.setSelectedExtract(textToHtml(selectedExtract));
            mainWindow.setWatingStatus();
        });
        //TODO: TERMINAR HILO???
        taskThread.start();
    }

    public void deleteSelectedExtract() {
        if(storedView.existSelectedEntry()){
            String selectedTitle = storedView.getSeletedSavedTitle();
            if(mainWindow.askConfirmation(storedView.getContentPane(), "Are you sure you want to delete the selected entry?")){
            //TODO: Best con listener o cone xception?
            dataBaseModel.deleteSavedPage(selectedTitle);
            storedView.setSelectSavedComboBox(dataBaseModel.getSavedTitles());
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
        ArrayList<WikiPage> scoredSeries = dataBaseModel.getScoredSeries();
        scoredView.showSavedScores(scoredSeries);
    }

    public void onRowSelected() {
        WikiPage selectedSeries = scoredView.getSelectedSeries();
        searchView.setLastSearchedSeries(selectedSeries);
        getExtract(selectedSeries);

        mainWindow.showSearchPanel();

    }

    public void setScore() {
        searchView.showScore();
    }

    public void saveExtractChanges() {
        updateStoredExtract(storedView.getSeletedSavedTitle(), storedView.getSelectedSavedExtract());


    }
    public void saveLocally() {

        try{
            WikiPage pageToSave = searchView.getLastSearchedSeries();
            if(pageToSave != null) updateStoredExtract(pageToSave.getTitle(), pageToSave.getExtract());
            mainWindow.showSuccessMessage(searchView.getContentPane(), "The series was correctly saved!");

        } catch(Exception e){
            mainWindow.showErrorMessage(searchView.getContentPane(), e.getMessage());

        }


    }
    private void updateStoredExtract(String title, String extract){
        dataBaseModel.updateSavedPage(title.replace("'", "`"), extract);

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
