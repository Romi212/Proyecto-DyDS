package dyds.tvseriesinfo.fulllogic;

import utils.WikiPage;

import javax.naming.directory.SearchResult;
import java.sql.*;
import java.util.ArrayList;

public class DataBase {

  private Connection connection;
  public void loadDatabase() throws SQLException {
    //If the database doesnt exists we create it
    String url = "jdbc:sqlite:./dictionary.db";

    Connection connection = DriverManager.getConnection(url);
      if (connection != null) {

        DatabaseMetaData meta = connection.getMetaData();
        System.out.println("The driver name is " + meta.getDriverName());

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        statement.executeUpdate("create table IF NOT EXIST catalog (id INTEGER, title string PRIMARY KEY, extract string, source integer)");

        statement.executeUpdate("create table IF NOT EXIST scores (id INTEGER, title string PRIMARY KEY, score integer, lastUpdated DATETIME DEFAULT CURRENT_TIMESTAMP)");
      }


  }

  private  ResultSet searchQuery(String query) throws SQLException{
    connection = null;
    connection = DriverManager.getConnection("jdbc:sqlite:./dictionary.db");
    Statement statement = connection.createStatement();
    statement.setQueryTimeout(30);

    return statement.executeQuery(query);

  }

  private void updateQuery(String query)throws SQLException{
    connection = null;
    connection = DriverManager.getConnection("jdbc:sqlite:./dictionary.db");
    Statement statement = connection.createStatement();
    statement.setQueryTimeout(30);

    statement.executeUpdate(query);
  }

  private void closeConnection () throws SQLException{
    if(connection != null)
      connection.close();
  }

  public static void testDB()
  {

    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:./dictionary.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.

      //statement.executeUpdate("drop table if exists person");
      //statement.executeUpdate("create table person (id integer, name string)");
      //statement.executeUpdate("insert into person values(1, 'leo')");
      //statement.executeUpdate("insert into person values(2, 'yui')");
      ResultSet rs = statement.executeQuery("select * from catalog");
      while(rs.next())
      {
        // read the result set
        System.out.println("id = " + rs.getInt("id"));
        System.out.println("title = " + rs.getString("title"));
        System.out.println("extract = " + rs.getString("extract"));
        System.out.println("source = " + rs.getString("source"));

      }
    }
    catch(SQLException e)
    {
      // if the error message is "out of memory",
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
  }

  public void saveScore(int id, String title, int score) throws SQLException{

      updateQuery("replace into scores values( "+id+", '"+ title + "', "+ score + ", datetime('now'))");
      closeConnection();

  }
  public int getScore(String title) throws SQLException {

    int score = -1;


      ResultSet rs = searchQuery("select * from scores WHERE title = '" + title + "'" );
      if (!rs.next()) {
        System.out.println("No records found");
      } else {
        System.out.println(rs.getInt("score"));
        score = rs.getInt("score");
      }
    closeConnection();
    return score;
  }

  public ArrayList<String> getTitles() throws SQLException {
    ArrayList<String> titles = new ArrayList<>();
    ResultSet rs = searchQuery("select * from catalog");
    while(rs.next()) titles.add(rs.getString("title"));
    closeConnection();

    return titles;

  }

  public void saveInfo(String title, String extract) throws SQLException {
    updateQuery("replace into catalog values(null, '"+ title + "', '"+ extract + "', 1)");
    closeConnection();
  }

  public String getExtract(String title) throws SQLException {

      ResultSet rs = searchQuery("select * from catalog WHERE title = '" + title + "'" );
      rs.next();

      String extract = rs.getString("extract");

      closeConnection();

      return extract;

  }

  public void deleteEntry(String title) throws SQLException
  {
    updateQuery("DELETE FROM catalog WHERE title = '" + title + "'" );
    closeConnection();

  }


  public ArrayList<WikiPage> getScoredSeries() throws SQLException{

    ArrayList<WikiPage> scoredSeries = new ArrayList<>();

      ResultSet rs = searchQuery("select * from scores" );
      while (rs.next()) {
        WikiPage series = new WikiPage( rs.getString("title"), String.valueOf(rs.getInt("id")),"");
        series.setScore(rs.getInt("score"));
        Timestamp timestamp = rs.getTimestamp("lastUpdated");
        series.setLastUpdated(new java.util.Date(timestamp.getTime()));
        System.out.println("Scored series: " + rs.getString("title"));
        scoredSeries.add(series);
      }
    closeConnection();
    return scoredSeries;
  }
}
