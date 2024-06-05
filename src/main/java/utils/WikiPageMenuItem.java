package utils;

import javax.swing.*;

public class WikiPageMenuItem extends JMenuItem {

    private String itemText;
    public WikiPageMenuItem(String title, String snippet){
        setSeriesPreview(title,snippet);
    }

    private void setSeriesPreview(String title, String snippet){
        itemText =  title + ": " + snippet;
        itemText = itemText.replace("<span class=\"searchmatch\">", "")
                .replace("</span>", "");
        String text = "<html><font face=\"arial\">" + itemText;

        this.setText(text);
    }

    public void changeScoredText(){
        this.setText("<html><font face=\"arial\">" + "\u2605" + itemText);
    }
}
