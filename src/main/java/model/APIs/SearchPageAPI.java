package model.APIs;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dyds.tvseriesinfo.fulllogic.WikipediaPageAPI;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import utils.WikiPage;

import java.util.Map;
import java.util.Set;

import static utils.TextProcessing.textToHtml;

public class SearchPageAPI implements SearchPageAPInterface {
    private WikipediaPageAPI pageAPI;
    private Gson gson;

    public SearchPageAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/w/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        pageAPI = retrofit.create(WikipediaPageAPI.class);

        gson = new Gson();
    }

    @Override
    public WikiPage getExtract(WikiPage wikiPage){
        String extract = "";
        try {
            //This may take some time, dear user be patient in the meanwhile!
            //setWorkingStatus();
            //Now fetch the info of the select page
            Response<String> callForPageResponse = pageAPI.getExtractByPageID(wikiPage.getPageID()).execute();

            System.out.println("JSON " + callForPageResponse.body());

            //toAlberto: This is similar to the code above, but here we parse the wikipage answer.
            //For more details on Gson look for very important coment 1, or just google it :P
            JsonObject jsonPageResponse = gson.fromJson(callForPageResponse.body(), JsonObject.class);
            JsonObject jsonPageQuery = jsonPageResponse.get("query").getAsJsonObject();
            JsonObject jsonPagesFound = jsonPageQuery.get("pages").getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> pagesFoundSet = jsonPagesFound.entrySet();
            Map.Entry<String, JsonElement> firstPageFound = pagesFoundSet.iterator().next();
            JsonObject pageFound = firstPageFound.getValue().getAsJsonObject();
            JsonElement jsonPageExtract = pageFound.get("extract");
            JsonElement jsonPageUrl = pageFound.get("fullurl");
            if (jsonPageUrl != null) {
                wikiPage.setUrl(jsonPageUrl.getAsString());
            }
            if (jsonPageExtract == null) {
                extract = "No Results";
            } else {
                extract = "<h1>" + wikiPage.getTitle() + "</h1>";
                extract += jsonPageExtract.getAsString().replace("\\n", "\n");
                extract = textToHtml(extract);
            }


        } catch (Exception e12) {
            System.out.println(e12.getMessage());
        }

        wikiPage.setExtract(extract);
        return wikiPage;
    }

}
