package presenter;

import model.SearchModel;

public class Main {
    public static void main(String[] args) {
        SearchModel model = new SearchModel();
        SeriesPresenter presenter = new SeriesPresenter(model);
        presenter.start();
    }
}
