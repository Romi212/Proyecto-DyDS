package model.APIs;

import utils.WikiPage;

import java.io.IOException;

public interface SearchPageAPInterface {
    WikiPage getExtract(WikiPage wikiPage) throws IOException;
}
