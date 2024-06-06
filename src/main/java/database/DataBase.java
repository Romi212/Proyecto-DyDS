package database;

import utils.WikiPage;

import javax.naming.directory.SearchResult;
import java.sql.*;
import java.util.ArrayList;

public class DataBase implements DataBaseInterface {

  private Connection connection;
  @Override
  public void loadDatabase() throws SQLException {
    //If the database doesnt exists we create it
    String url = "jdbc:sqlite:./dictionary.db";

    connection = DriverManager.getConnection(url);
      if (connection != null) {

        DatabaseMetaData meta = connection.getMetaData();
        System.out.println("The driver name is " + meta.getDriverName());

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);




        ResultSet tables = meta.getTables(null, null, "catalog", null);
        if (!tables.next()) {
          // Table does not exist, create it
          createCatalogTable(statement);
        }

        // Check if the 'scores' table exists
        tables = meta.getTables(null, null, "scores", null);
        if (!tables.next()) {
          // Table does not exist, create it
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

  private void closeConnection () throws SQLException{
    if(connection != null)
      connection.close();
  }

  @Override
  public void saveScore(int id, String title, int score) throws SQLException{

      updateQuery("replace into scores values( "+id+", '"+ title + "', "+ score + ", datetime('now'))");
     // closeConnection();

  }
  @Override
  public int getScore(String title) throws SQLException {

    int score = -1;


      ResultSet rs = searchQuery("select * from scores WHERE title = '" + title + "'" );
      if (!rs.next()) {
        System.out.println("No records found");
      } else {
        System.out.println(rs.getInt("score"));
        score = rs.getInt("score");
      }
    //closeConnection();
    return score;
  }

  @Override
  public ArrayList<String> getTitles() throws SQLException {
    ArrayList<String> titles = new ArrayList<>();
    ResultSet rs = searchQuery("select * from catalog");
    while(rs.next()) titles.add(rs.getString("title"));
   // closeConnection();

    return titles;

  }

  @Override
  public void saveInfo(String title, String id, String extract) throws SQLException {
    updateQuery("replace into catalog values( "+id+", '"+ title + "', '"+ extract + "', 1)");
   // closeConnection();
  }

  @Override
  public String getExtract(String title) throws SQLException {

      ResultSet rs = searchQuery("select * from catalog WHERE title = '" + title + "'" );
      rs.next();

      String extract = rs.getString("extract");

      //closeConnection();
      WikiPage page = new WikiPage(title, String.valueOf(rs.getInt("id")), "");
      page.setExtract(extract);
      //TODO: ACOMODAR
      return extract ;

  }

  @Override
  public void deleteEntry(String title) throws SQLException
  {
    updateQuery("DELETE FROM catalog WHERE title = '" + title + "'" );
    //closeConnection();

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
        System.out.println("Scored series: " + rs.getString("title"));
        scoredSeries.add(series);
      }
    //closeConnection();
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