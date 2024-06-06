package stub;

import database.DataBaseInterface;
import utils.WikiPage;

import java.util.ArrayList;

public class DataBaseStub implements DataBaseInterface {
    @Override
    public void loadDatabase() {
        System.out.println("loadDatabase");
    }

    @Override
    public void saveScore(int id, String title, int score) {
        System.out.println("saveScore");
    }

    @Override
    public int getScore(String title) {
        System.out.println("getScore");
        return 0;
    }

    @Override
    public ArrayList<String> getTitles() {
        System.out.println("getTitles");
        ArrayList<String> titles = new ArrayList<>();
        titles.add("title");
        return titles;
    }

    @Override
    public void saveInfo(String title, String id, String extract) {
        System.out.println("saveInfo");
    }

    @Override
    public String getExtract(String title) {
        System.out.println("getExtract");
        return "EXAMPLE EXTRACT";
    }

    @Override
    public void deleteEntry(String title) {
        System.out.println("deleteEntry");
    }

    @Override
    public ArrayList<WikiPage> getScoredSeries() {
        System.out.println("getScoredSeries");
        ArrayList<WikiPage> wikiPages = new ArrayList<>();
        WikiPage wikiPage = new WikiPage("title", "id", "extract");
        wikiPage.setScore(1);
        wikiPages.add(wikiPage);
        return wikiPages;
    }

    @Override
    public int getID(String selectedTitle) {
        System.out.println("getID");
        return 777;
    }
}
