package presenter;

import model.DataBaseModel;
import view.StoredView;
import static utils.TextProcessing.generateURL;
import static utils.TextProcessing.textToHtml;

public class StorePresenter {
    StoredView storedView;
    DataBaseModel dataBaseModel;
    Thread taskThread;
    SeriesPresenter presenter;
    public StorePresenter( DataBaseModel dataBaseModel, SeriesPresenter presenter){
        this.dataBaseModel = dataBaseModel;
        this.presenter = presenter;
    }
    public void setView(StoredView storedView){
        this.storedView = storedView;
    }
    public void initializeSavedPanel(){
        dataBaseModel.getSavedTitles();
    }
    public void showSavedSeries(){
        storedView.setSelectSavedComboBox(dataBaseModel.getSavedTitlesList());
    }
    public void getSavedExtract(){
        taskThread = new Thread(() -> {
            presenter.setWindowWorkingStatus();
            String selectedTitle = storedView.getSeletedSavedTitle();
            dataBaseModel.getSavedExtract(selectedTitle);
        });
        taskThread.start();
    }
    public void showExtract(){
        String selectedExtract = dataBaseModel.getExtract();
        int id = dataBaseModel.getID();
        storedView.setSelectedExtract(textToHtml(selectedExtract));
        storedView.setURL(generateURL(id));
        presenter.setWindowWatingStatus();
    }
    public void deleteSelectedExtract() {
        if(storedView.existSelectedEntry()){
            String selectedTitle = storedView.getSeletedSavedTitle();
            if(presenter.askDeleteConfirmation()){
                dataBaseModel.deleteSavedPage(selectedTitle);
                dataBaseModel.getSavedTitles();
                storedView.emptySavedTextPane();
            }
        }
    }

    public void saveExtractChanges() {
        dataBaseModel.updateSavedPage(storedView.getSeletedSavedTitle().replace("'", "`"), String.valueOf(dataBaseModel.getID()),storedView.getSelectedSavedExtract());
    }
}
