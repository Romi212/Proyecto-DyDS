package presenter;

import dyds.tvseriesinfo.fulllogic.DataBase;
import model.SearchModel;
import view.SearchView;

import javax.swing.*;
import java.awt.*;

public class SeriesPresenter {

    SearchModel model;
    SearchView view;

    public SeriesPresenter(SearchModel model) {
        this.model = model;
        model.setPresenter(this);
    }

    public void start(){
        view = new SearchView(this);
        view.showView();


        DataBase.loadDatabase();
        //DataBase.saveInfo("test", "sarasa");


        //System.out.println(DataBase.getExtract("test"));
        //System.out.println(DataBase.getExtract("nada"));
    }

}
