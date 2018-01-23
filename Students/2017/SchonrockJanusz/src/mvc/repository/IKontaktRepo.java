package mvc.repository;

import java.util.List;

import mvc.model.Kontakt;

public interface IKontaktRepo {

    public List<Kontakt> getKontakty();
    public void insertKontakt(Kontakt kontakt);
    //public void
    public void updateKontakt(Kontakt kontakt);
    public void deleteKontakt(Integer kontaktId);

    public void createTableKontakt();


    public void insertNumer(Kontakt numer);

    public List<Kontakt> getNumery(Integer kontaktId);

    public void deleteNumer(Integer kontaktId);

    public void updateNumer(Kontakt numer);

}
