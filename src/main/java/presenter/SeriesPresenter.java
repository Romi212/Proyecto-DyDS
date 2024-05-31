package presenter;

import dyds.tvseriesinfo.fulllogic.DataBase;
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
    }

    public void searchSeries() {

        taskThread = new Thread(() -> {
            view.setWorkingStatus();
            String seriesName = view.getSeriesName();

            //TODO: Controlar vacia
            ArrayList<WikiPage> PagesFound = model.searchSeries(seriesName);

            view.showResults(PagesFound);
            view.setWatingStatus();
        });
        //TODO: TERMINAR HILO???
        taskThread.start();

    }

    public void getSelectedExtract(WikiPage selectedResult){
        String extract = model.searchPageExtract(selectedResult);
        view.setSearchResultTextPane(extract);
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
