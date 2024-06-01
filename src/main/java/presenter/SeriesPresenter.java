package presenter;

import dyds.tvseriesinfo.fulllogic.DataBase;
import model.SearchModelListener;
import utils.WikiPage;
import model.SearchModel;
import view.SearchView;

import java.util.ArrayList;

public class SeriesPresenter {

    SearchModel model;
    SearchView view;

    Thread taskThread;

    public SeriesPresenter(SearchModel model) {
        this.model = model;
        model.setPresenter(this);
    }

    public void start(){
        view = new SearchView(this);
        view.showView();

        DataBase.loadDatabase();
        initListeners();
    }

    private void initListeners(){
        model.addListener(new SearchModelListener() {
            @Override public void seriesSearchFinished() {
                showSeriesSearchResults();
            }

            @Override public void extractSearchFinished() {
                showExtractSearchResults();
            }
        });
    }

    private void showSeriesSearchResults(){
        view.showResults(model.getSearchResults());
        view.setWatingStatus();
    }

    private void showExtractSearchResults(){
        view.setSearchResultTextPane(model.getExtract());
    }
    public void searchSeries() {

        taskThread = new Thread(() -> {
            view.setWorkingStatus();
            String seriesName = view.getSeriesName();

            //TODO: Controlar vacia
            model.searchSeries(seriesName);



        });
        //TODO: TERMINAR HILO???
        taskThread.start();

    }

    public void getSelectedExtract(WikiPage selectedResult){
        model.searchPageExtract(selectedResult);

    }

    public void initializeSavedPanel(){
        view.setSelectSavedComboBox(model.getSavedTitles());
    }

    public void showSelectedExtract(){
        taskThread = new Thread(() -> {
            view.setWorkingStatus();
            System.out.println("ENTRE");
            String selectedTitle = view.getSeletedSavedTitle();
            String selectedExtract = model.getSavedExtract(selectedTitle);
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
            DataBase.deleteEntry(selectedTitle);
            view.setSelectSavedComboBox(model.getSavedTitles());
            view.emptySavedTextPane();
        }
    }

    public void saveExtractChanges() {
        model.updateSavedPage(view.getSeletedSavedTitle().replace("'", "`"), view.getSelectedSavedExtract());


    }
}
