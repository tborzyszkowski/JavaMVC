package mvc.dao;

import java.util.List;
import mvc.model.Kontakt;

public interface IKontaktDAO {

    public int create(Kontakt kontakt);

    public void createTable();

    public Kontakt read(int id);

    public List<Kontakt> readAll();

    public boolean update(Kontakt kontakt);

    public boolean delete(int id);


    public int createNumer(Kontakt numer);

    public List<Kontakt> readNumery(int id);

    public boolean deleteNumber(int id);

    public boolean updateNumber(Kontakt numer);

}
