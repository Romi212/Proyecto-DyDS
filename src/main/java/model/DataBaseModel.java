package model;

import dyds.tvseriesinfo.fulllogic.DataBase;
import utils.WikiPage;

import java.sql.SQLException;
import java.util.ArrayList;

public class DataBaseModel {

    private DataBase dataBase;


    public DataBaseModel(){
        try{
            dataBase = new DataBase();
            dataBase.loadDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object[] getSavedTitles(){
        try{
            return dataBase.getTitles().stream().sorted().toArray();
        } catch (Exception e){
            e.printStackTrace();}
        return null;
    }

    public void deleteSavedPage(String selectedTitle) {
        try{
            dataBase.deleteEntry(selectedTitle);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public String getSavedExtract(String selectedTitle) {
        try{
            return dataBase.getExtract(selectedTitle);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void updateSavedPage(String pageTitle, String pageExtract) {
        try {
            dataBase.saveInfo(pageTitle, pageExtract);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setScore(WikiPage series){
        try{
            dataBase.saveScore(Integer.parseInt(series.getPageID()),series.getTitle(), series.getScore());
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public int getScore(String pageTitle){
        try{
            return dataBase.getScore(pageTitle);
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<WikiPage> getScoredSeries()  {
        try{
            return dataBase.getScoredSeries();
        } catch (Exception e){
            e.printStackTrace();
            }
        return null;
    }}
