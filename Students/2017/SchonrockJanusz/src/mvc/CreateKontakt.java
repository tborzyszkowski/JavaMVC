package mvc;

import org.apache.log4j.PropertyConfigurator;

import mvc.controller.KontaktController;
import mvc.repository.KontaktRepo;
import mvc.view.KontaktView;


public class CreateKontakt {

    public static void main(String[] args) {
        String log4jconfPath = "log4j.properties";
        PropertyConfigurator.configure(log4jconfPath);

        KontaktRepo model = new KontaktRepo();
        KontaktView view = new KontaktView();
        new KontaktController(model, view);

        view.setVisible(true);
    }

}
