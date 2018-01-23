package mvc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class MysqlDAOFactory extends DAOFactory {

    private static final Logger logger = Logger.getLogger(MysqlDAOFactory.class);

    public static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/test";
    private static final String USERNAME = "root";
    private static final String USERPASSWORD = "";


    public static Connection createConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DBURL, USERNAME, USERPASSWORD);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        return conn;
    }


    @Override
    public IKontaktDAO getKontaktDAO() {
        return new MysqlKontaktDAO();
    }
}
