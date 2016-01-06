package mvc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class SqliteDAOFactory extends DAOFactory {
	private static final Logger logger = Logger
            .getLogger(MysqlDAOFactory.class);

    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DBURL = "jdbc:sqlite:test.db";

    /**
     * Method to create a Connection on the sqlite-database.
     * 
     * @return the Connection.
     */
    public static Connection createConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DBURL);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        return conn;
    }
    @Override
	public IKlientDAO getKlientDAO() {
		return new SqliteKlientDAO();
	}

}
