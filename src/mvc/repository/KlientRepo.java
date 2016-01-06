package mvc.repository;

import java.util.List;

import mvc.dao.DAOFactory;
import mvc.dao.IKlientDAO;
import mvc.model.Klient;

public class KlientRepo implements IKlientRepo{

	DAOFactory sqliteFactory = DAOFactory.getDAOFactory(DAOFactory.SQLITE);
    IKlientDAO klientDAO = sqliteFactory.getKlientDAO();

	@Override
	public List<Klient> getKlienci() {
        return klientDAO.readAll();
	}
	
	@Override
	public void insertKlient(Klient klient) {
        klientDAO.create(klient);
	}

	@Override
	public void createTableKlient() {
		klientDAO.createTable();
	}
	
	
	public void updateKlient(Klient klient) {
		klientDAO.update(klient);	
	}
	
	public void deleteKlient(Integer klientId) {
		klientDAO.delete(klientId);
	}
//	
//	private void executeCommand(String command) {
//        try {
//            Class.forName("org.sqlite.JDBC");
//            Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
//            Statement stmt = conn.createStatement();
//            stmt.executeUpdate(command);
//            stmt.close();
//            conn.close();
//        }  catch (Exception ex) {
//        	ex.printStackTrace();
//        }
//	}
}

