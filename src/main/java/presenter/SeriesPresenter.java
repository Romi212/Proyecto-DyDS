package presenter;

import dyds.tvseriesinfo.fulllogic.DataBase;
import model.*;
import utils.WikiPage;
import view.SearchView;

public class SeriesPresenter {

    SearchSeriesModel seriesSearchModel;

    SearchWikiPageModel wikiPageModel;

    DataBaseModel dataBaseModel;
    SearchView view;

    Thread taskThread;

    public SeriesPresenter(SearchSeriesModel seriesSearchModel, SearchWikiPageModel wikiPageModel, DataBaseModel dataBaseModel) {
        this.seriesSearchModel = seriesSearchModel;
        this.wikiPageModel = wikiPageModel;
        this.dataBaseModel = dataBaseModel;
        seriesSearchModel.setPresenter(this);
    }

    public void start(){
        view = new SearchView(this);
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
        view.showResults(seriesSearchModel.getSearchResults());
        view.setWatingStatus();
    }

    private void showExtractSearchResults(){
        view.setSearchResultTextPane(wikiPageModel.getExtract());
    }
    public void searchSeries() {

        taskThread = new Thread(() -> {
            view.setWorkingStatus();
            String seriesName = view.getSeriesName();

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
        view.setSelectSavedComboBox(dataBaseModel.getSavedTitles());
    }

    public void showSelectedExtract(){
        taskThread = new Thread(() -> {
            view.setWorkingStatus();
            System.out.println("ENTRE");
            String selectedTitle = view.getSeletedSavedTitle();
            String selectedExtract = dataBaseModel.getSavedExtract(selectedTitle);
            System.out.println(selectedTitle+selectedExtract);
            view.setSelectedExtract(textToHtml(selectedExtract));
            view.setWatingStatus();
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
        if(view.existSelectedEntry()){
            String selectedTitle = view.getSeletedSavedTitle();
            dataBaseModel.deleteSavedPage(selectedTitle);
            view.setSelectSavedComboBox(dataBaseModel.getSavedTitles());
            view.emptySavedTextPane();
        }
    }

    public void saveExtractChanges() {
        dataBaseModel.updateSavedPage(view.getSeletedSavedTitle().replace("'", "`"), view.getSelectedSavedExtract());


    }
}
