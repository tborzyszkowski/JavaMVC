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

public final class PostgresFactory extends DAOFactory {
	private static final Logger log = LoggerFactory.getLogger(PostgresFactory.class);
	
	private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost/bookmarker";
    private static final String USERNAME = "bookmarker";
    private static final String USERPASSWORD = "admin1";
        
    @Override
	public ICategoryDAO getCategory() {
    	log.trace("Get CategoryDAO");
		return new CategoryDAO(POSTGRES);
	}
    
    @Override
	public ISubcategoryDAO getSubcategory() {
    	log.trace("Get SubcategoryDAO");
		return new SubcategoryDAO(POSTGRES);
	}
    
    @Override
	public IUrlDAO getUrl() {
    	log.trace("Get UrlDAO");
		return new UrlDAO(POSTGRES);
	}

	@Override
	public String getName() {
		log.trace("Get name");
		return "POSTGRES";
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
