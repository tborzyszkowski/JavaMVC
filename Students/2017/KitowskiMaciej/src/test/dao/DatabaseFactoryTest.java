package test.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.dao.DAOFactory;
import mvc.dao.model.ISubcategoryDAO;
import mvc.dao.model.ICategoryDAO;
import mvc.dao.model.IUrlDAO;

@RunWith(Parameterized.class)
public class DatabaseFactoryTest {
	private static final Logger log = LoggerFactory.getLogger(DatabaseFactoryTest.class);
	
	private int databaseType;
	
	@Parameters
    public static List<Object> data() {
		log.debug("Prepare data");
		
        return Arrays.asList(new Object[] {     
                 DAOFactory.SQLITE, DAOFactory.MYSQL, DAOFactory.POSTGRES  
           });
    }
	
	public DatabaseFactoryTest(int database) {
		databaseType = database;
		log.debug("Initialize test for {} database", DAOFactory.get(database).getName());
	}
	
	@Test
	public void getConnectionTest() throws Exception {
		log.debug("Get connection test");
		
		Connection connection = DAOFactory.get(databaseType).getConnection();
		
		assertNotNull(connection);
		assertFalse(connection.isClosed());
		
		connection.close();
		assertTrue(connection.isClosed());
	}
	
	@Test
	public void getMainCategoryTest() {
		log.debug("Get CategoryDAO test");
		
		ICategoryDAO dao = DAOFactory.get(databaseType).getCategory();
		
		assertNotNull(dao);
	}
	
	@Test
	public void getCategoryTest() {
		log.debug("Get SubcategoryDAO test");
		
		ISubcategoryDAO dao = DAOFactory.get(databaseType).getSubcategory();
		
		assertNotNull(dao);
	}
	
	@Test
	public void getUrlTest() {
		log.debug("Get UrlDAO test");
		
		IUrlDAO dao = DAOFactory.get(databaseType).getUrl();
		
		assertNotNull(dao);
	}
}
