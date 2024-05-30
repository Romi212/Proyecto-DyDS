package model;

import dyds.tvseriesinfo.fulllogic.WikipediaPageAPI;
import dyds.tvseriesinfo.fulllogic.WikipediaSearchAPI;
import presenter.SeriesPresenter;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SearchModel {
    private SeriesPresenter presenter;

    public SearchModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/w/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        WikipediaSearchAPI searchAPI = retrofit.create(WikipediaSearchAPI.class);
        WikipediaPageAPI pageAPI = retrofit.create(WikipediaPageAPI.class);
    }

    public void setPresenter(SeriesPresenter presenter) {
        this.presenter = presenter;
    }
}
