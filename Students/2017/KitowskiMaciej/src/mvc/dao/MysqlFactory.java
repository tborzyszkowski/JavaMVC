package mvc.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.dao.model.SubcategoryDAO;
import mvc.dao.model.ISubcategoryDAO;
import mvc.dao.model.ICategoryDAO;
import mvc.dao.model.IUrlDAO;
import mvc.dao.model.CategoryDAO;
import mvc.dao.model.UrlDAO;

public final class MysqlFactory extends DAOFactory {
	private static final Logger log = LoggerFactory.getLogger(MysqlFactory.class);
	
	private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/bookmarker?autoReconnect=true&useSSL=false";
    private static final String USERNAME = "bookmarker";
    private static final String USERPASSWORD = "admin1";
        
    @Override
	public ICategoryDAO getCategory() {
    	log.trace("Get CategoryDAO");
		return new CategoryDAO(MYSQL);
	}
    
    @Override
	public ISubcategoryDAO getSubcategory() {
    	log.trace("Get SubcategoryDAO");
		return new SubcategoryDAO(MYSQL);
	}
    
    @Override
	public IUrlDAO getUrl() {
    	log.trace("Get UrlDAO");
		return new UrlDAO(MYSQL);
	}

	@Override
	public String getName() {
		log.trace("Get name");
		return "MYSQL";
	}

	@Override
	public Connection getConnection() {
		log.debug("Get connection");
		
		Connection connection = null;
		
		try {
			Class.forName(DRIVER);
    		connection = DriverManager.getConnection(URL, USERNAME, USERPASSWORD);
		}
		catch(Exception ex) {
			log.error("Get connection error", ex);
		}
		
		return connection;
	}
}
