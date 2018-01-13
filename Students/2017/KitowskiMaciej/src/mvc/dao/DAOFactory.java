package mvc.dao;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.dao.model.ISubcategoryDAO;
import mvc.dao.model.ICategoryDAO;
import mvc.dao.model.IUrlDAO;

public abstract class DAOFactory {
	private static final Logger log = LoggerFactory.getLogger(DAOFactory.class);

	public static final int SQLITE = 1;
	public static final int MYSQL = 2;
	public static final int POSTGRES = 3;
	
	private static int defaultFactory = SQLITE;
	
	public abstract ICategoryDAO getCategory();
	public abstract ISubcategoryDAO getSubcategory();
	public abstract IUrlDAO getUrl();
	
	public abstract String getName();
	public abstract Connection getConnection();
	
	public static DAOFactory get() {
		log.debug("Get default database factory");
		return get(defaultFactory);
	}

	public static DAOFactory get(int database) {
		if(database == SQLITE) {
			log.debug("Get SQLite database factory");
			return new SqliteFactory();
		}
		else if(database == MYSQL) {
			log.debug("Get MySql database factory");
			return new MysqlFactory();
		}
		else if(database == POSTGRES) {
			log.debug("Get PostgreSql database factory");
			return new PostgresFactory();
		}
		else {
			log.warn("Wrong database selection");
			return null;
		}
	}
	
	public static void setDefaultFactory(int factory) {
		log.debug("Set new default factory: {}", get(factory).getName());
		defaultFactory = factory;
	}
}
