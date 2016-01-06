package mvc.dao;

import java.util.List;
import mvc.model.Klient;

public interface IKlientDAO {
    
    public int create(Klient klient);
    
    public void createTable();
 
    public Klient read(int id);

    public List<Klient> readAll();
 
    public boolean update(Klient klient);
 
    public boolean delete(int id);
}
