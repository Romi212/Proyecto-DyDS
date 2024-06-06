package database;

import utils.WikiPage;

import javax.naming.directory.SearchResult;
import java.sql.*;
import java.util.ArrayList;

public class DataBase implements DataBaseInterface {

  private Connection connection;
  @Override
  public void loadDatabase() throws SQLException {

    String url = "jdbc:sqlite:./dictionary.db";

    connection = DriverManager.getConnection(url);
      if (connection != null) {
        DatabaseMetaData meta = connection.getMetaData();
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        ResultSet tables = meta.getTables(null, null, "catalog", null);
        if (!tables.next()) {
          createCatalogTable(statement);
        }

        tables = meta.getTables(null, null, "scores", null);
        if (!tables.next()) {
          createScoresTable(statement);
        }
      }
  }

  private void createCatalogTable(Statement statement) throws SQLException {
    statement.executeUpdate("create table catalog (id INTEGER PRIMARY KEY, title string, extract string, source integer)");
  }
  private void createScoresTable(Statement statement) throws SQLException {
      statement.executeUpdate("create table scores (id INTEGER, title string PRIMARY KEY, score integer, lastUpdated DATETIME DEFAULT CURRENT_TIMESTAMP)");
  }
  private  ResultSet searchQuery(String query) throws SQLException{
    Statement statement = connection.createStatement();
    statement.setQueryTimeout(30);
    return statement.executeQuery(query);
  }
  private void updateQuery(String query)throws SQLException{
    Statement statement = connection.createStatement();
    statement.setQueryTimeout(30);
    statement.executeUpdate(query);
  }
  public void closeConnection () throws SQLException{
    if(connection != null)
      connection.close();
  }

  @Override
  public void saveScore(int id, String title, int score) throws SQLException{
      updateQuery("replace into scores values( "+id+", '"+ title + "', "+ score + ", datetime('now'))");
  }
  @Override
  public int getScore(String title) throws SQLException {
    int score = -1;
    ResultSet rs = searchQuery("select * from scores WHERE title = '" + title + "'" );
    if (rs.next()){
      score = rs.getInt("score");
    }
    return score;
  }

  @Override
  public ArrayList<String> getTitles() throws SQLException {
    ArrayList<String> titles = new ArrayList<>();
    ResultSet rs = searchQuery("select * from catalog");
    while(rs.next()) titles.add(rs.getString("title"));
    return titles;
  }

  @Override
  public void saveInfo(String title, String id, String extract) throws SQLException {
    updateQuery("replace into catalog values( "+id+", '"+ title + "', '"+ extract + "', 1)");
  }

  @Override
  public String getExtract(String title) throws SQLException {
      ResultSet rs = searchQuery("select * from catalog WHERE title = '" + title + "'" );
      rs.next();
      String extract = rs.getString("extract");
      return extract;
  }

  @Override
  public void deleteEntry(String title) throws SQLException
  {
    updateQuery("DELETE FROM catalog WHERE title = '" + title + "'" );
  }
  @Override
  public ArrayList<WikiPage> getScoredSeries() throws SQLException{

    ArrayList<WikiPage> scoredSeries = new ArrayList<>();
      ResultSet rs = searchQuery("select * from scores ORDER BY score ASC" );
      while (rs.next()) {
        WikiPage series = new WikiPage( rs.getString("title"), String.valueOf(rs.getInt("id")),"");
        series.setScore(rs.getInt("score"));
        Timestamp timestamp = rs.getTimestamp("lastUpdated");
        series.setLastUpdated(new java.util.Date(timestamp.getTime()));
        scoredSeries.add(series);
      }
    return scoredSeries;
  }

  @Override
  public int getID(String selectedTitle) throws SQLException {
    int id = -1;
      ResultSet rs = searchQuery("select * from catalog WHERE title = '" + selectedTitle + "'");
      rs.next();
      id = rs.getInt("id");
    return id;
  }
}
