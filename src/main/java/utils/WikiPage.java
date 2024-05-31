package utils;

import javax.swing.*;

public class WikiPage  {


    private String title;
    private String pageID;
    private String snippet;

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
}
