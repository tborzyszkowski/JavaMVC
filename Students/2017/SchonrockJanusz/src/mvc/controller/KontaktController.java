package mvc.controller;

import java.util.List;

import mvc.dao.DAOFactory;
import mvc.model.Kontakt;

import mvc.repository.IKontaktRepo;
import mvc.repository.KontaktRepo;
import mvc.view.KontaktView;

public class KontaktController {

    private KontaktRepo model;
    private KontaktView view;

    public KontaktController(KontaktRepo model, KontaktView view) {
        this.model = model;
        this.view = view;

        view.setController(this);

        // Domyslna baza danych
        chooseDatabase(0);

        //refreshKontakty();

        // URUCHAMIA REFRESH TABELI Z NUMERAMI
        //refreshNumery();
    }

    public void insertKontakt(Kontakt kontakt) {
        model.insertKontakt(kontakt);
        refreshKontakty();

    }

    public void updateKontakt(Kontakt kontakt) {
        model.updateKontakt(kontakt);
        refreshKontakty();
    }

    public void deleteKontakt(Integer kontaktId) {
        model.deleteKontakt(kontaktId);
        refreshKontakty();
    }

    private void refreshKontakty() {
        List<Kontakt> kontakty = model.getKontakty();
        view.refreshKontakty(kontakty);
    }

    public void insertNumer(Kontakt numer) {
        model.insertNumer(numer);
        refreshKontakty();
    }


    public void viewAllKontaktNumbers(Integer kontaktId) {
        List<Kontakt> numery = model.getNumery(kontaktId);
        view.refreshNumery(numery);
    }

    public void deleteNumer(Integer numerId) {
        model.deleteNumer(numerId);
    }

    public void updateNumer(Kontakt numer) {
        model.updateNumer(numer);
    }

    public void chooseDatabase(Integer baza) {
        DAOFactory dao = DAOFactory.getDAOFactory(baza);
        model.refreshDB(dao);
        refreshKontakty();
    }

    public void utworzTabele() {
        model.createTableKontakt();
        refreshKontakty();
    }

}
