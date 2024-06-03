package model.APIs;

import utils.WikiPage;

import java.util.ArrayList;

public interface SearchSeriesAPInterface {
    ArrayList<WikiPage> searchSeries(String seriesName);
}
