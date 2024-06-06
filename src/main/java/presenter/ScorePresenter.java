package presenter;

import model.DataBaseModel;
import utils.WikiPage;
import view.ScoredView;

public class ScorePresenter {
    private DataBaseModel dataBaseModel;
    private ScoredView scoredView;
    private SeriesPresenter presenter;

    public ScorePresenter(DataBaseModel dataBaseModel, SeriesPresenter presenter) {
        this.dataBaseModel = dataBaseModel;
        this.presenter = presenter;
    }
    public void setView(ScoredView scoredView) {
        this.scoredView = scoredView;
    }
    boolean hasScore(String seriesName){
        return dataBaseModel.getScore(seriesName) != -1;
    }
    public void getSavedScores() { dataBaseModel.getScoredSeries(); }
    public void showScoredSeries(){ scoredView.showSavedScores(dataBaseModel.getScoredSeriesList());  }
    public void onRowSelected() {
        WikiPage selectedSeries = scoredView.getSelectedSeries();
        presenter.showSeries(selectedSeries);

    }

}
