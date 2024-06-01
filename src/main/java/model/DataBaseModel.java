package model;

import dyds.tvseriesinfo.fulllogic.DataBase;

public class DataBaseModel {



    public DataBaseModel(){
        DataBase.loadDatabase();
    }

    public Object[] getSavedTitles(){
        return DataBase.getTitles().stream().sorted().toArray();
    }

    public void deleteSavedPage(String selectedTitle) {
        DataBase.deleteEntry(selectedTitle);
    }
    public String getSavedExtract(String selectedTitle) {
        return DataBase.getExtract(selectedTitle);
    }

    public void updateSavedPage(String pageTitle, String pageExtract) {
        DataBase.saveInfo(pageTitle, pageExtract);
    }
}
