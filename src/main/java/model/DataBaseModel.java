package model;

import dyds.tvseriesinfo.fulllogic.DataBase;
import utils.WikiPage;

import java.util.ArrayList;

public class DataBaseModel {

    private DataBase dataBase;
    private Object[] savedTitles;
    private String extract;
    private ArrayList<WikiPage> scoredSeries;

    private int id;
    private ArrayList<DataBaseModelListener> listeners;
    public DataBaseModel(){
        try{
            listeners = new ArrayList<>();
            dataBase = new DataBase();
            dataBase.loadDatabase();

        } catch (Exception e) {
            notifyListeners(e.getMessage());
        }
    }

    private void notifyListeners(String message) {
        for (DataBaseModelListener listener: listeners) {
            listener.errorOnDataBase(message);
        }
    }

    public void getSavedTitles(){

        try{
            savedTitles = dataBase.getTitles().stream().sorted().toArray();
            notifyFinishedTitlesSearch();
        } catch (Exception e){
            notifyListeners(e.getMessage());}

    }

    private void notifyFinishedTitlesSearch() {
        for (DataBaseModelListener listener: listeners) {
            listener.SeriesListFound();
        }
    }

    public void deleteSavedPage(String selectedTitle) {
        try{
            dataBase.deleteEntry(selectedTitle);
        } catch (Exception e){
            notifyListeners(e.getMessage());
        }

    }
    public void getSavedExtract(String selectedTitle) {
        try{
            extract = dataBase.getExtract(selectedTitle);
            id = dataBase.getID(selectedTitle);
            notifyExtractFound();
        } catch (Exception e){
            notifyListeners(e.getMessage());
        }

    }

    private void notifyExtractFound() {
        for (DataBaseModelListener listener: listeners) {
            listener.extractFound();
        }
    }


    public void updateSavedPage(String pageTitle,String pageID, String pageExtract) {
        try {
            dataBase.saveInfo(pageTitle, pageID, pageExtract);
        } catch (Exception e){
            notifyListeners(e.getMessage());
        }

    }

    public void setScore(WikiPage series){
        try{
            dataBase.saveScore(Integer.parseInt(series.getPageID()),series.getTitle(), series.getScore());
        } catch (Exception e){
            notifyListeners(e.getMessage());
        }

    }

    public int getScore(String pageTitle){
        try{
            return dataBase.getScore(pageTitle);

        } catch (Exception e){
            notifyListeners(e.getMessage());
        }
    return -1;
    }

    public void getScoredSeries()  {
        try{
            scoredSeries= dataBase.getScoredSeries();
            notifyScoredSeriesFound();
        } catch (Exception e){
            notifyListeners(e.getMessage());
            }

    }

    private void notifyScoredSeriesFound() {
        for (DataBaseModelListener listener: listeners) {
            listener.ScoredSeriesFound();
        }
    }

    public void addListener(DataBaseModelListener dataBaseModelListener) {
        listeners.add(dataBaseModelListener);
    }

    public Object[] getSavedTitlesList() {
        return savedTitles;
    }

    public String getExtract() {
        return extract;
    }

    public ArrayList<WikiPage> getScoredSeriesList() {
        return scoredSeries;
    }

    public int getID() {
        return id;
    }
}
