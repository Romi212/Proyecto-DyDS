package presenter;

import model.*;
import utils.WikiPage;
import view.ScoredView;
import view.SearcherView;
import view.StoredView;
import view.TVSeriesSearcherWindow;

import javax.swing.*;
import java.util.ArrayList;

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
        seriesSearchModel.addListener(new SearchSeriesModelListener() {
            @Override public void seriesSearchFinished() {
                showSeriesSearchResults();
            }
        });

        wikiPageModel.addListener(new SearchWikiPageModelListener() {
            @Override public void extractSearchFinished() {
                showExtractSearchResults();
            }
        });
    }

    private void showSeriesSearchResults(){
        searchView.showResults(seriesSearchModel.getSearchResults());
        searchView.setWatingStatus();
    }

    private void showExtractSearchResults(){
        searchView.setSearchResultTextPane(wikiPageModel.getExtract() + wikiPageModel.getUrl() );

    }
    public void searchSeries() {

        taskThread = new Thread(() -> {
            searchView.setWorkingStatus();
            String seriesName = searchView.getSeriesName();

            //TODO: Controlar vacia
            seriesSearchModel.searchSeries(seriesName);



        });
        //TODO: TERMINAR HILO???
        taskThread.start();

    }

    public void getSelectedExtract(WikiPage selectedResult){
        wikiPageModel.searchPageExtract(selectedResult);
        if(hasScore(selectedResult.getTitle())) searchScore(selectedResult.getTitle());
        else searchView.showNoScore();

    }

    public void initializeSavedPanel(){
        Object[] seriesTitles = dataBaseModel.getSavedTitles();
        Object[] seriesTItlesScored = new Object[seriesTitles.length];
        for(int i = 0; i < seriesTitles.length; i++){
            if(hasScore((String) seriesTitles[i])) seriesTItlesScored[i] = seriesTitles[i] + " - Scored";
            else
            seriesTItlesScored[i] = seriesTitles[i];
        }
        storedView.setSelectSavedComboBox(seriesTItlesScored);
    }

    public void showSelectedExtract(){
        taskThread = new Thread(() -> {
            searchView.setWorkingStatus();
            System.out.println("ENTRE");
            String selectedTitle = storedView.getSeletedSavedTitle();
            String selectedExtract = dataBaseModel.getSavedExtract(selectedTitle);
            System.out.println(selectedTitle+selectedExtract);
            storedView.setSelectedExtract(textToHtml(selectedExtract));
            searchView.setWatingStatus();
        });
        //TODO: TERMINAR HILO???
        taskThread.start();


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

    public void deleteSelectedExtract() {
        if(storedView.existSelectedEntry()){
            String selectedTitle = storedView.getSeletedSavedTitle();
            dataBaseModel.deleteSavedPage(selectedTitle);
            storedView.setSelectSavedComboBox(dataBaseModel.getSavedTitles());
            storedView.emptySavedTextPane();
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
        getSelectedExtract(selectedSeries);

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
            mainWindow.showSuccessMessage(searchView.getContentPane(), "The series was correctly saved!","Success");

        } catch(Exception e){
            mainWindow.showErrorMessage(searchView.getContentPane(), e.getMessage(),"Error");

        }


    }
    private void updateStoredExtract(String title, String extract){
        dataBaseModel.updateSavedPage(title.replace("'", "`"), extract);

    }

    public void changedTabs() {
        int selectedTab = mainWindow.getSelectedTab();
        switch (selectedTab){
            case 0:
                searchView.setWatingStatus();
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
