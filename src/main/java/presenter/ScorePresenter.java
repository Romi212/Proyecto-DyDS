package presenter;

import model.DataBaseModel;
import utils.WikiPage;
import view.ScoredViewInterface;

public class ScorePresenter {
    private DataBaseModel dataBaseModel;
    private ScoredViewInterface scoredViewInterface;
    private SeriesPresenter presenter;

    public ScorePresenter(DataBaseModel dataBaseModel, SeriesPresenter presenter) {
        this.dataBaseModel = dataBaseModel;
        this.presenter = presenter;
    }
    public void setView(ScoredViewInterface scoredViewInterface) {
        this.scoredViewInterface = scoredViewInterface;
    }
    boolean hasScore(String seriesName){
        return dataBaseModel.getScore(seriesName) != -1;
    }
    public void getSavedScores() { dataBaseModel.getScoredSeries(); }
    public void showScoredSeries(){ scoredViewInterface.showSavedScores(dataBaseModel.getScoredSeriesList());  }
    public void onRowSelected() {
        WikiPage selectedSeries = scoredViewInterface.getSelectedSeries();
        presenter.showSeries(selectedSeries);

    }

}
