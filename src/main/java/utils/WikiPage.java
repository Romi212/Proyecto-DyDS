package utils;

import javax.swing.*;
import java.util.Date;

public class WikiPage  {


    private String title;
    private String pageID;
    private String snippet;
    private Date lastUpdated;
    private String extract;

    private String url;
    private int score;

    private JMenuItem graphicMenuItem;

    public WikiPage(String title, String pageID, String snippet) {

        this.title = title;
        this.pageID = pageID;
        this.snippet = snippet;

        createMenuItem();
    }

    public String getTitle() {
        return title;
    }

    public String getPageID() {
        return pageID;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    private void createMenuItem() {
        graphicMenuItem = new JMenuItem();
        String itemText = "<html><font face=\"arial\">" + title + ": " + snippet;
        itemText =itemText.replace("<span class=\"searchmatch\">", "")
                .replace("</span>", "");
        graphicMenuItem.setText(itemText);
    }

    public JMenuItem getGraphicMenuItem() {
        return graphicMenuItem;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date date) {
        this.lastUpdated = date;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getExtract() {
        return extract;
    }
}
