package presenter;

import model.DataBaseModel;
import view.StoredViewInterface;

import static utils.TextProcessing.generateURL;
import static utils.TextProcessing.textToHtml;

public class StorePresenter {
    StoredViewInterface storedViewInterface;
    DataBaseModel dataBaseModel;
    Thread taskThread;
    SeriesPresenter presenter;
    public StorePresenter( DataBaseModel dataBaseModel, SeriesPresenter presenter){
        this.dataBaseModel = dataBaseModel;
        this.presenter = presenter;
    }
    public void setView(StoredViewInterface storedViewInterface){
        this.storedViewInterface = storedViewInterface;
    }
    public void initializeSavedPanel(){
        dataBaseModel.getSavedTitles();
    }
    public void showSavedSeries(){
        storedViewInterface.setSelectSavedComboBox(dataBaseModel.getSavedTitlesList());
    }
    public void getSavedExtract(){
        taskThread = new Thread(() -> {
            presenter.setWindowWorkingStatus();
            String selectedTitle = storedViewInterface.getSeletedSavedTitle();
            dataBaseModel.getSavedExtract(selectedTitle);
        });
        taskThread.start();
    }
    public void showExtract(){
        String selectedExtract = dataBaseModel.getExtract();
        int id = dataBaseModel.getID();
        storedViewInterface.setSelectedExtract(textToHtml(selectedExtract));
        storedViewInterface.setURL(generateURL(id));
        presenter.setWindowWatingStatus();
    }
    public void deleteSelectedExtract() {
        if(storedViewInterface.existSelectedEntry()){
            String selectedTitle = storedViewInterface.getSeletedSavedTitle();
            if(presenter.askDeleteConfirmation()){
                dataBaseModel.deleteSavedPage(selectedTitle);
                dataBaseModel.getSavedTitles();
                storedViewInterface.emptySavedTextPane();
            }
        }
    }

    public void saveExtractChanges() {
        dataBaseModel.updateSavedPage(storedViewInterface.getSeletedSavedTitle().replace("'", "`"), String.valueOf(dataBaseModel.getID()), storedViewInterface.getSelectedSavedExtract());
    }
}
