package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import mvc.model.Klient;

public class SqliteKlientDAO implements IKlientDAO {

	private static final Logger logger = Logger.getLogger(SqliteKlientDAO.class);
    
	private static final String CREATE_QUERY = 
			"INSERT INTO klient(imie, nazwisko, miasto, rok_urodzenia)  VALUES (?,?,?,?)";
    private static final String READ_QUERY = 
    		"SELECT id, imie, nazwisko, miasto, rok_urodzenia FROM klient WHERE id = ?";
    private static final String READALL_QUERY = 
    		"SELECT id, imie, nazwisko, miasto, rok_urodzenia FROM klient";
    private static final String UPDATE_QUERY = 
    		"UPDATE klient SET imie=?, nazwisko=?, miasto=?,rok_urodzenia=?  WHERE id = ?";
    private static final String DELETE_QUERY = 
    		"DELETE FROM klient WHERE id = ?";
	private static final String CREATE_TABLE = 
			"CREATE TABLE klient(id INTEGER PRIMARY KEY AUTOINCREMENT,imie CHAR(50), nazwisko CHAR(50), miasto CHAR(50), rok_urodzenia INT)";
 
	@Override
	public int create(Klient klient) {
		Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            conn = SqliteDAOFactory.createConnection();
            preparedStatement = conn.prepareStatement(CREATE_QUERY,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, klient.getImie());
            preparedStatement.setString(2, klient.getNazwisko());
            preparedStatement.setString(3, klient.getMiasto());
            preparedStatement.setInt(4, klient.getRokUrodzenia());
            preparedStatement.execute();
            result = preparedStatement.getGeneratedKeys();
 
            if (result.next() && result != null) {
                return result.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                logger.error(rse.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return -1;
	}

	@Override
	public Klient read(int id) {
		Klient klient = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
        	conn = SqliteDAOFactory.createConnection();
            preparedStatement = conn.prepareStatement(READ_QUERY);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
 
            if (result.next() && result != null) {
            	klient = new Klient(result.getInt(1), 
            						result.getString(2), 
            						result.getString(3), 
            						result.getString(4), 
            						result.getInt(5));
            } else {
                // TODO
            	logger.info("Brak klienta o ID = "+ id);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                logger.error(rse.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return klient;
	}

	@Override
	public List<Klient> readAll() {
		List<Klient> lista_klientow = new ArrayList<Klient>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
        	conn = SqliteDAOFactory.createConnection();
            preparedStatement = conn.prepareStatement(READALL_QUERY);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
            
            if (result != null){
            while(result.next())
            	lista_klientow.add(
            					new Klient(
            							result.getInt(1), 
            							result.getString(2), 
            							result.getString(3), 
            							result.getString(4), 
            							result.getInt(5)
            							)
            					);
            } else {
                // TODO
            	logger.info("Brak klientów");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                logger.error(rse.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return lista_klientow;
	}

	@Override
	public boolean update(Klient klient) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
        	conn = SqliteDAOFactory.createConnection();
            preparedStatement = conn.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, klient.getImie());
            preparedStatement.setString(2, klient.getNazwisko());
            preparedStatement.setString(3, klient.getMiasto());
            preparedStatement.setInt(4, klient.getRokUrodzenia());
            preparedStatement.setInt(5, klient.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return false;
	}

	@Override
	public boolean delete(int id) {
		Connection conn = null;
	    PreparedStatement preparedStatement = null;
	    try {
	    	conn = SqliteDAOFactory.createConnection();
	    	preparedStatement = conn.prepareStatement(DELETE_QUERY);
	    	preparedStatement.setInt(1, id);
	    	preparedStatement.execute();
	    	return true;
	    } catch (SQLException e) {
	    	logger.error(e.getMessage());
	    } finally {
	    	try {
	    		preparedStatement.close();
	    	} catch (Exception sse) {
	    		logger.error(sse.getMessage());
	    	}
	    	try {
	    		conn.close();
	        } catch (Exception cse) {
	        	logger.error(cse.getMessage());
	        }
	    }
	    return false;
	}
	
	@Override
	public void createTable(){
		Connection conn = null;
		Statement stmt = null;
		try {
	    	conn = SqliteDAOFactory.createConnection();
	    	stmt = conn.createStatement();
	    	stmt.executeUpdate(CREATE_TABLE);
		} catch (SQLException e) {
	    	logger.error(e.getMessage());
		} finally {
	    	try {
	    		stmt.close();
	    	} catch (Exception sse) {
	    		logger.error(sse.getMessage());
	    	}
	    	try {
	    		conn.close();
	        } catch (Exception cse) {
	        	logger.error(cse.getMessage());
	        }
		}
	}
}
