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
import mvc.dao.model.CategoryDAO;
import mvc.model.Category;

@RunWith(Parameterized.class)
public class CategoryDAOTest {
	private static final Logger log = LoggerFactory.getLogger(CategoryDAOTest.class);
	
	private CategoryDAO dao = null;
	private int databaseType;
	
	@Parameters
    public static List<Object> data() {
		log.debug("Prepare data");
		
        return Arrays.asList(new Object[] {     
                 DAOFactory.SQLITE, DAOFactory.MYSQL, DAOFactory.POSTGRES  
           });
    }
	
	public CategoryDAOTest(int database) {
		databaseType = database;
		log.debug("Initialize test for {} database", DAOFactory.get(database).getName());
	}
	
	@Before
	public void initialize() {
		log.debug("Initialize CategoryDAO");
		dao = new CategoryDAO(databaseType);
	}
	
	@Test
	public void loadQueriesTest() {
		log.debug("Load queries from JSON test");
		String[] fieldNames = {"CREATE_TABLE", "INSERT", "GET", "GET_ALL", "UPDATE", "DELETE"};
		
		try {
			JSONObject json = Utilities.getJsonQuery("resources/sql/Category.json", databaseType);
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
			
			assertTrue(tableList.contains("Category") || tableList.contains("category"));
		}
		catch(Exception ex) {
			log.error("Failed test - create table", ex);
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void insertTest() {
		log.debug("Insert test");
		
		Category category = new Category("SingleInsertTest");
		
		int result = dao.insert(category);
		
		assertNotEquals(CategoryDAO.INSERT_FAIL, result);
	}
	
	@Test
	public void insertMultipleTest() {
		log.debug("Multiple insert test");
		
		String pattern = "MultipleInsertTest_%d";
		int insertCount = 20;
		
		for(int i = 0; i < insertCount; ++i) {
			Category category = new Category(String.format(pattern, i + 1));
			
			int result = dao.insert(category);
			
			assertNotEquals(CategoryDAO.INSERT_FAIL, result);
		}
	}
	
	@Test
	public void getSingleTest() {
		log.debug("Get test");
		
		int ID = 1;
		
		Category result = dao.get(ID);
		
		assertTrue(result != null && result.getID() == ID);
	}
	
	@Test
	public void getAllTest() {
		log.debug("Get all test");
		
		try {
			List<Category> result = dao.getAll();
			log.warn("{}", Utilities.count("category", databaseType));
			assertTrue(result != null && result.size() == Utilities.count("category", databaseType));
		}
		catch(Exception ex) {
			log.error("Failed test - Get all", ex);
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void updateTest() {
		log.debug("Update test");
		
		int ID = 1;
		Category toUpdate = dao.get(ID);

		toUpdate.setName(toUpdate.getName() + "-UPDATE");
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
