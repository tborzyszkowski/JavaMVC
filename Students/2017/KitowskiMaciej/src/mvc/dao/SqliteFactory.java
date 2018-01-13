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

public final class SqliteFactory extends DAOFactory {
	private static final Logger log = LoggerFactory.getLogger(SqliteFactory.class);
	
	private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:sqlite.db";
        
    @Override
	public ICategoryDAO getCategory() {
    	log.trace("Get CategoryDAO");
		return new CategoryDAO(SQLITE);
	}
    
    @Override
	public ISubcategoryDAO getSubcategory() {
    	log.trace("Get SubcategoryDAO");
		return new SubcategoryDAO(SQLITE);
	}
    
    @Override
	public IUrlDAO getUrl() {
    	log.trace("Get UrlDAO");
		return new UrlDAO(SQLITE);
	}

	@Override
	public String getName() {
		log.trace("Get name");
		return "SQLITE";
	}

	@Override
	public Connection getConnection() {
		log.debug("Get connection");
		
		Connection connection = null;
		
		try {
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(URL);
		}
		catch(Exception ex) {
			log.error("Get connection error", ex);
		}
		
		return connection;
	}
}
