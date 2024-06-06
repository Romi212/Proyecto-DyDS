package database;

import utils.WikiPage;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DataBaseInterface {
    void loadDatabase() throws SQLException;
    void saveScore(int id, String title, int score) throws SQLException;
    int getScore(String title) throws SQLException;
    ArrayList<String> getTitles() throws SQLException;
    void saveInfo(String title, String id, String extract) throws SQLException;
    String getExtract(String title) throws SQLException;
    void deleteEntry(String title) throws SQLException;
    ArrayList<WikiPage> getScoredSeries() throws SQLException;
    int getID(String selectedTitle) throws SQLException;
}
