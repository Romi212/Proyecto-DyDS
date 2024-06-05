package utils;

import javax.swing.*;
import java.util.Date;

public class WikiPage  {
    private String title;
    private String pageID;
    private String snippet;
    private Date lastUpdated;
    private String extract;
    private int score;
    private WikiPageMenuItem graphicMenuItem;

    public WikiPage(String title, String pageID, String snippet) {
        this.title = title;
        this.pageID = pageID;
        this.snippet = snippet;
        createMenuItem();
    }

    public String getTitle() { return title; }

    public String getPageID() { return pageID; }

    public String getSnippet() { return snippet; }

    public String getUrl() { return "https://en.wikipedia.org/?curid="+pageID; }

    private void createMenuItem() {
        //TODO: Format text somewhere else
        graphicMenuItem = new WikiPageMenuItem(title, snippet);


    }

    public WikiPageMenuItem getGraphicMenuItem() { return graphicMenuItem; }
    public void setScore(int score){ this.score = score;  }
    public int getScore() { return score; }
    public Date getLastUpdated() {  return lastUpdated;}
    public void setLastUpdated(Date date) { this.lastUpdated = date;}
    public void setExtract(String extract) {this.extract = extract;}
    public String getExtract() {return extract;}
}
