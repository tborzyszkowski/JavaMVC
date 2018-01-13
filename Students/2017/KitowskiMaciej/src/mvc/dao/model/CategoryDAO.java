package mvc.dao.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.dao.DAOFactory;
import mvc.model.Category;

public final class CategoryDAO implements ICategoryDAO {
	private static final Logger log = LoggerFactory.getLogger(CategoryDAO.class);
	private static final String queryPath = "resources/sql/Category.json";
		
	private DAOFactory database = null;
	
	private static int lastDatabaseType = 0;
	private static String CREATE_TABLE = null;
	private static String INSERT = null;
	private static String GET = null;
	private static String GET_ALL = null;
	private static String UPDATE = null;
	private static String DELETE = null;
	
	public CategoryDAO(int databaseType) {
		database = DAOFactory.get(databaseType);
		log.debug("Create CategoryDAO with database: {}", database.getName());
		
		if(CREATE_TABLE == null || databaseType != lastDatabaseType) {
			lastDatabaseType = databaseType;
			loadSql();
		}
	}

	@Override
	public boolean createTable() {
		log.debug("Create table");
		
		Connection connection = null;
		Statement statement = null;
		boolean result = false;
		
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			
			statement.execute(CREATE_TABLE);
			result = true;
		}
		catch(Exception ex) {
			log.error("Create new table failed", ex);
			result = false;
		}
		finally {
			try {
				if(statement != null && !statement.isClosed()) statement.close();
				if(connection != null && !connection.isClosed()) connection.close();
			}
			catch(Exception ex) {
				log.error("Close connection failed", ex);
			}
		}
		
		return result;
	}
	
	@Override
	public int insert(Category category) {
		log.debug("Insert category: ID={} name={}", category.getID(), category.getName());
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		int resultBuffer = 0;
		
		try {
			connection = database.getConnection();
			statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, category.getName());
			statement.execute();
			
			result = statement.getGeneratedKeys();
			if(result != null && result.next()) resultBuffer = result.getInt(1);
			else resultBuffer = INSERT_FAIL;
		}
		catch(Exception ex) {
			log.error("Insert failed", ex);
			resultBuffer = INSERT_FAIL;
		}
		finally {
			try {
				if(result != null && !result.isClosed()) result.close();
				if(statement != null && !statement.isClosed()) statement.close();
				if(connection != null && !connection.isClosed()) connection.close();
			}
			catch(Exception ex) {
				log.error("Close connection failed", ex);
			}
		}
		
		if(resultBuffer == INSERT_FAIL) {
			log.debug("Try to create table and insert again");
			
			if(createTable()) {
				log.debug("Create dable succeed");
				resultBuffer = insert(category);
			}
		}

		return resultBuffer;
	}
	
	@Override
	public Category get(int ID) {
		log.debug("Get category: ID={}", ID);
		
		Category category = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			connection = database.getConnection();
			statement = connection.prepareStatement(GET);
			
			statement.setInt(1, ID);
			statement.execute();
			
			result = statement.getResultSet();
			if(result != null && result.next()) {
				int foundID = result.getInt(1);
				String foundName = result.getString(2);
				
				category = new Category(foundID, foundName);
			}
		}
		catch(Exception ex) {
			log.error("Get failed", ex);
		}
		finally {
			try {
				if(result != null && !result.isClosed()) result.close();
				if(statement != null && !statement.isClosed()) statement.close();
				if(connection != null && !connection.isClosed()) connection.close();
			}
			catch(Exception ex) {
				log.error("Close connection failed", ex);
			}
		}
		
		if(category == null) {
			log.debug("Try to create table and get again");
			
			if(createTable()) {
				log.debug("Create dable succeed");
				category = get(ID);
			}
		}

		return category;
	}
	
	@Override
	public List<Category> getAll() {
		log.debug("Get all categories");
		
		List<Category> categories = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			
			result = statement.executeQuery(GET_ALL);
			categories = new ArrayList<>();
			
			if(result != null) {
				while(result.next()) {
					int foundID = result.getInt(1);
					String foundName = result.getString(2);
					
					categories.add(new Category(foundID, foundName));
				}
			}
		}
		catch(Exception ex) {
			log.error("Get all categories failed", ex);
		}
		finally {
			try {
				if(result != null && !result.isClosed()) result.close();
				if(statement != null && !statement.isClosed()) statement.close();
				if(connection != null && !connection.isClosed()) connection.close();
			}
			catch(Exception ex) {
				log.error("Close connection failed", ex);
			}
		}
		
		if(categories == null) {
			log.debug("Try to create table and get all again");
			
			if(createTable()) {
				log.debug("Create dable succeed");
				categories = getAll();
			}
		}
		
		return categories;
	}
	
	@Override
	public boolean update(Category category) {
		log.debug("Update category: ID={} name={}", category.getID(), category.getName());
		
		Connection connection = null;
		PreparedStatement statement = null;
		boolean result = false;
		
		try {
			connection = database.getConnection();
			statement = connection.prepareStatement(UPDATE);
			
			statement.setString(1, category.getName());
			statement.setInt(2, category.getID());
			statement.execute();
			
			result = true;
		}
		catch(Exception ex) {
			log.error("Update failed", ex);
			result = false;
		}
		finally {
			try {
				if(statement != null && !statement.isClosed()) statement.close();
				if(connection != null && !connection.isClosed()) connection.close();
			}
			catch(Exception ex) {
				log.error("Close connection failed", ex);
			}
		}
		
		if(!result) {
			log.debug("Try to create table and update again");
			
			if(createTable()) {
				log.debug("Create dable succeed");
				result = update(category);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean delete(int ID) {
		log.debug("Delete category: ID={}", ID);
		
		Connection connection = null;
		PreparedStatement statement = null;
		boolean result = false;
		
		try {
			connection = database.getConnection();
			statement = connection.prepareStatement(DELETE);
			
			statement.setInt(1, ID);
			statement.execute();
			
			result = true;
		}
		catch(Exception ex) {
			log.error("Delete failed", ex);
			result = false;
		}
		finally {
			try {
				if(statement != null && !statement.isClosed()) statement.close();
				if(connection != null && !connection.isClosed()) connection.close();
			}
			catch(Exception ex) {
				log.error("Close connection failed", ex);
			}
		}
		
		if(!result) {
			log.debug("Try to create table and delete again");
			
			if(createTable()) {
				log.debug("Create dable succeed");
				result = delete(ID);
			}
		}
		
		return result;
	}
	
	private void loadSql() {
		log.info("Load sql queries");
		
		try {
			JSONObject obj = JsonLoader.getJson(queryPath, database.getName());
			
			CREATE_TABLE = JsonLoader.joinStringArray(obj.getJSONArray("CREATE_TABLE"));
			INSERT = obj.getString("INSERT");
			GET = obj.getString("GET");
			GET_ALL = obj.getString("GET_ALL");
			UPDATE = obj.getString("UPDATE");
			DELETE = obj.getString("DELETE");
			
			log.debug("CREATE_TABLE: {}", CREATE_TABLE);
			log.debug("INSERT: {}", INSERT);
			log.debug("GET: {}", GET);
			log.debug("GET_ALL: {}", GET_ALL);
			log.debug("UPDATE: {}", UPDATE);
			log.debug("DELETE: {}", DELETE);
		}
		catch(Exception ex) {
			log.error("Load JSON file failed", ex);
		}
	}
}
