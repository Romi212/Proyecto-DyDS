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

    public void setScore(String pageTitle, int score){
        DataBase.saveScore(pageTitle, score);
    }

    public int getScore(String pageTitle){
        return DataBase.getScore(pageTitle);
    }
}
