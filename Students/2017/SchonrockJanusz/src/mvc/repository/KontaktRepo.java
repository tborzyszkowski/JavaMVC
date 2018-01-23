package mvc.repository;

import java.util.List;

import mvc.dao.DAOFactory;
import mvc.dao.IKontaktDAO;
import mvc.model.Kontakt;

public class KontaktRepo implements IKontaktRepo {
    DAOFactory sqlFactory;
    IKontaktDAO kontaktDAO;

    public void refreshDB(DAOFactory factory) {
        this.sqlFactory = factory;
        this.kontaktDAO = sqlFactory.getKontaktDAO();
    }

    @Override
    public List<Kontakt> getKontakty() {
        return kontaktDAO.readAll();
    }


    @Override
    public void insertKontakt(Kontakt kontakt) {
        kontaktDAO.create(kontakt);
    }

    @Override
    public void createTableKontakt() {
        kontaktDAO.createTable();
    }

    @Override
    public void updateKontakt(Kontakt kontakt) {
        kontaktDAO.update(kontakt);
    }

    @Override
    public void deleteKontakt(Integer kontaktId) {
        kontaktDAO.delete(kontaktId);
    }

    @Override
    public void insertNumer(Kontakt numer) {
        kontaktDAO.createNumer(numer);
    }

    @Override
    public List<Kontakt> getNumery(Integer kontaktId) {
        return kontaktDAO.readNumery(kontaktId);

    }

    @Override
    public void deleteNumer(Integer kontaktId) {
        kontaktDAO.deleteNumber(kontaktId);
    }

    @Override
    public void updateNumer(Kontakt numer) {
        kontaktDAO.updateNumber(numer);
    }



}
