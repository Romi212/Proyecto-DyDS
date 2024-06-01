package presenter;

import model.*;
import utils.WikiPage;
import view.SearchView;
import view.SearcherView;
import view.StoredView;
import view.TVSeriesSearcherView;

public class SeriesPresenter {

    SearchSeriesModel seriesSearchModel;

    SearchWikiPageModel wikiPageModel;

    DataBaseModel dataBaseModel;
    SearcherView searchView;

    StoredView storedView;

    Thread taskThread;

    public SeriesPresenter(SearchSeriesModel seriesSearchModel, SearchWikiPageModel wikiPageModel, DataBaseModel dataBaseModel) {
        this.seriesSearchModel = seriesSearchModel;
        this.wikiPageModel = wikiPageModel;
        this.dataBaseModel = dataBaseModel;
        seriesSearchModel.setPresenter(this);
    }

    public void start(){
        TVSeriesSearcherView view = new TVSeriesSearcherView(this);

        searchView = view.getSearchView();
        searchView.setUpView();

        storedView = view.getStoredView();
        storedView.setUpView();
        view.showView();


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
        searchView.setSearchResultTextPane(wikiPageModel.getExtract());
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

    }

    public void initializeSavedPanel(){
        storedView.setSelectSavedComboBox(dataBaseModel.getSavedTitles());
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

    public void saveExtractChanges() {
        dataBaseModel.updateSavedPage(storedView.getSeletedSavedTitle().replace("'", "`"), storedView.getSelectedSavedExtract());


    }
}
