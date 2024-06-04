package model;

import dyds.tvseriesinfo.fulllogic.DataBase;
import utils.WikiPage;

import java.util.ArrayList;

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

    public void setScore(WikiPage series){
        DataBase.saveScore(Integer.parseInt(series.getPageID()),series.getTitle(), series.getScore());
    }

    public int getScore(String pageTitle){
        return DataBase.getScore(pageTitle);
    }

    public ArrayList<WikiPage> getScoredSeries() { return DataBase.getScoredSeries();    }
}
