package test.dao.model;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.dao.DAOFactory;
import mvc.dao.model.SubcategoryDAO;
import mvc.dao.model.UrlDAO;
import mvc.model.Subcategory;
import mvc.model.Url;

@RunWith(Parameterized.class)
public class UrlDAOTest {
	private static final Logger log = LoggerFactory.getLogger(UrlDAOTest.class);
	
	private UrlDAO dao = null;
	private int databaseType;
	
	@Parameters
    public static List<Object> data() {
		log.debug("Prepare data");
		
        return Arrays.asList(new Object[] {     
                 DAOFactory.SQLITE, DAOFactory.MYSQL, DAOFactory.POSTGRES  
           });
    }
	
	public UrlDAOTest(int database) {
		databaseType = database;
		log.debug("Initialize test for {} database", DAOFactory.get(database).getName());
	}
	
	@Before
	public void initialize() {
		log.debug("Initialize UrlDAO");
		dao = new UrlDAO(databaseType);
	}
	
	@Test
	public void loadQueriesTest() {
		log.debug("Load queries from JSON test");
		String[] fieldNames = {"CREATE_TABLE", "INSERT", "GET", "GET_CATEGORY", "GET_ALL", "UPDATE", "DELETE"};
		
		try {
			JSONObject json = Utilities.getJsonQuery("resources/sql/Url.json", databaseType);
			Class<?> cls = dao.getClass();
			
			for(String name : fieldNames) {
				String sql = null;
				Field field = cls.getDeclaredField(name);
				field.setAccessible(true);
				
				if(name == "CREATE_TABLE") sql = Utilities.getCreateTable(json.getJSONArray(name));
				else sql = json.getString(name);
				
				log.debug("{} - {}", name, field.get(dao));
				
				assertNotNull(field.get(dao));
				assertEquals(field.get(dao), sql);
			}
		}
		catch(Exception ex) {
			log.error("Failed test - load queries from JSON", ex);
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void createTableTest() {
		log.debug("Create table test");
		
		try {
			dao.createTable();
			List<String> tableList = Utilities.getTableNames(databaseType);
			
			assertTrue(tableList.contains("Url") || tableList.contains("url"));
		}
		catch(Exception ex) {
			log.error("Failed test - create table", ex);
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void insertTest() {
		log.debug("Insert test");
		
		int ID = 3;
		Subcategory subcategory = DAOFactory.get(databaseType).getSubcategory().get(ID);
		Url url = new Url("http://test", "SingleinsertTest", "Single", subcategory);
		
		int result = dao.insert(url);
		
		assertNotEquals(SubcategoryDAO.INSERT_FAIL, result);
	}
	
	@Test
	public void insertNullDescriptionTest() {
		log.debug("Insert with null description value test");
		
		int ID = 3;
		Subcategory subcategory = DAOFactory.get(databaseType).getSubcategory().get(ID);
		Url url = new Url("http://test", "SingleinsertNullDescriptionTest", subcategory);
		
		int result = dao.insert(url);
		
		assertNotEquals(SubcategoryDAO.INSERT_FAIL, result);
	}
	
	@Test
	public void insertNullCategoryTest() {
		log.debug("Insert with null subcategory value test");
		
		Url url = new Url("http://test", "SingleinsertNullCategoryTest", "Null Subcategory");
		
		int result = dao.insert(url);
		
		assertNotEquals(SubcategoryDAO.INSERT_FAIL, result);
	}
	
	@Test
	public void insertNullDescriptionAndCategoryTest() {
		log.debug("Insert with null description and subcategory values test");
		
		Url url = new Url("http://test", "SingleinsertNullAllTest");
		
		int result = dao.insert(url);
		
		assertNotEquals(SubcategoryDAO.INSERT_FAIL, result);
	}
	
	@Test
	public void insertMultipleTest() {
		log.debug("Multiple insert test");
		
		String pattern = "MultipleInsertTest_%d";
		int insertCount = 10;
		int ID = 4;
		Subcategory subcategory = DAOFactory.get(databaseType).getSubcategory().get(ID);
		
		for(int i = 0; i < insertCount; ++i) {
			Url url = new Url("http://test", String.format(pattern, i + 1), "Single", subcategory);
			
			int result = dao.insert(url);
			
			assertNotEquals(SubcategoryDAO.INSERT_FAIL, result);
		}
	}
	
	@Test
	public void insertMultipleNullDescriptionTest() {
		log.debug("Multiple insert with null description value test");
		
		String pattern = "MultipleInsertNullDescriptionTest_%d";
		int insertCount = 10;
		int ID = 4;
		Subcategory subcategory = DAOFactory.get(databaseType).getSubcategory().get(ID);
		
		for(int i = 0; i < insertCount; ++i) {
			Url url = new Url("http://test", String.format(pattern, i + 1), subcategory);
			
			int result = dao.insert(url);
			
			assertNotEquals(SubcategoryDAO.INSERT_FAIL, result);
		}
	}
	
	@Test
	public void insertMultipleNullCategoryTest() {
		log.debug("Multiple insert with null subcategory value test");
		
		String pattern = "MultipleInsertNullCategoryTest_%d";
		int insertCount = 10;
		
		for(int i = 0; i < insertCount; ++i) {
			Url url = new Url("http://test", String.format(pattern, i + 1), "Null Subcategory");
			
			int result = dao.insert(url);
			
			assertNotEquals(SubcategoryDAO.INSERT_FAIL, result);
		}
	}
	
	@Test
	public void insertMultipleNullDescriptionAndCategoryTest() {
		log.debug("Multiple insert with null description and subcategory values test");
		
		String pattern = "MultipleInsertNullAllTest_%d";
		int insertCount = 10;
		
		for(int i = 0; i < insertCount; ++i) {
			Url url = new Url("http://test", String.format(pattern, i + 1));
			
			int result = dao.insert(url);
			
			assertNotEquals(SubcategoryDAO.INSERT_FAIL, result);
		}
	}
	
	@Test
	public void getSingleTest() {
		log.debug("Get test");
		
		int ID = 1;
		
		Url result = dao.get(ID);
		
		assertTrue(result != null && result.getID() == ID);
	}
	
	@Test
	public void getWithCategoryTest() {
		log.debug("Get with subcategory test");
		
		int ID = 4;
		Subcategory subcategory = DAOFactory.get(databaseType).getSubcategory().get(ID);
		
		List<Url> result = dao.getAllWithSubcategory(subcategory);
		
		assertTrue(result != null);
	}
	
	@Test
	public void getAllTest() throws Exception {
		log.debug("Get all test");
		
		List<Url> result = dao.getAll();
		
		assertTrue(result != null && result.size() == Utilities.count("Url", databaseType));
	}
	
	@Test
	public void updateTest() {
		log.debug("Update test");
		
		Url toUpdate = dao.get(1);
		toUpdate.setTitle(toUpdate.getTitle() + "-UPDATED");
		
		boolean result = dao.update(toUpdate);
		
		assertTrue(result);
	}
	
	@Test
	public void deleteTest() {
		log.debug("Delete test");
		
		int ID = 10;
		
		boolean result = dao.delete(ID);
		
		assertTrue(result);
	}
}
