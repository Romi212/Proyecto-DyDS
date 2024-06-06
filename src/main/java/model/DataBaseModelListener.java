package model;

public interface DataBaseModelListener {
    void errorOnDataBase(String message);
    void SeriesListFound();
    void extractFound();
    void ScoredSeriesFound();
}
