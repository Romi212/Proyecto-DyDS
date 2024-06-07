package model;

import database.DataBaseInterface;
import utils.WikiPage;
import java.util.ArrayList;

public class DataBaseModel {
    private DataBaseInterface dataBase;
    private Object[] savedTitles;
    private String extract;
    private ArrayList<WikiPage> scoredSeries;
    private int id;
    private ArrayList<DataBaseModelListener> listeners;
    public DataBaseModel(DataBaseInterface dataBase){
        try{
            listeners = new ArrayList<>();
            this.dataBase = dataBase;
            dataBase.loadDatabase();

        } catch (Exception e) {
            NotifyErrorListeners(e.getMessage());
        }
    }
    public void addListener(DataBaseModelListener dataBaseModelListener) {
        listeners.add(dataBaseModelListener);
    }
    private void NotifyErrorListeners(String message) {
        for (DataBaseModelListener listener: listeners) {
            listener.errorOnDataBase(message);
        }
    }
    public void getSavedTitles(){
        try{
            savedTitles = dataBase.getTitles().stream().sorted().toArray();
            notifyFinishedTitlesSearch();
        } catch (Exception e){
            NotifyErrorListeners(e.getMessage());}

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
            NotifyErrorListeners(e.getMessage());
        }
    }
    public void getSavedExtract(String selectedTitle) {
        try{
            extract = dataBase.getExtract(selectedTitle);
            id = dataBase.getID(selectedTitle);
            notifyExtractFound();
        } catch (Exception e){
            NotifyErrorListeners(e.getMessage() );
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
            NotifyErrorListeners(e.getMessage());
        }
    }
    public void setScore(WikiPage series){
        try{
            dataBase.saveScore(Integer.parseInt(series.getPageID()),series.getTitle(), series.getScore());
        } catch (Exception e){
            NotifyErrorListeners(e.getMessage());
        }
    }
    public int getScore(String pageTitle){
        try{
            return dataBase.getScore(pageTitle);
        } catch (Exception e){
            NotifyErrorListeners(e.getMessage());
        }
    return -1;
    }
    public void getScoredSeries()  {
        try{
            scoredSeries= dataBase.getScoredSeries();
            notifyScoredSeriesFound();
        } catch (Exception e){
            NotifyErrorListeners(e.getMessage());
        }
    }
    private void notifyScoredSeriesFound() {
        for (DataBaseModelListener listener: listeners) {
            listener.ScoredSeriesFound();
        }
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

    public void closeConnection() {
        try {
            dataBase.closeConnection();
        } catch (Exception e) {
            NotifyErrorListeners(e.getMessage());
        }

    }
}
