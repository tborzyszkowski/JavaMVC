package mvc.dao.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.dao.DAOFactory;
import mvc.model.Subcategory;
import mvc.model.Url;

public final class UrlDAO implements IUrlDAO {
	private static final Logger log = LoggerFactory.getLogger(UrlDAO.class);
	private static final String queryPath = "resources/sql/Url.json";
	
	private DAOFactory database = null;
	
	private static int lastDatabaseType = 0;
	private static String CREATE_TABLE = null;
	private static String INSERT = null;
	private static String GET = null;
	private static String GET_CATEGORY = null;
	private static String GET_ALL = null;
	private static String UPDATE = null;
	private static String DELETE = null;
	
	public UrlDAO(int databaseType) {
		database = DAOFactory.get(databaseType);
		log.debug("Create UrlDAO with database: {}", database.getName());
		
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
	public int insert(Url url) {
		log.debug("Insert url: ID={} url={} title={}", url.getID(), url.getUrl(), url.getTitle());
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		int resultBuffer = 0;
			
		try {
			connection = database.getConnection();
			statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, url.getTitle());
			statement.setString(2, url.getUrl());
			if(url.getDescription() != null) statement.setString(3, url.getDescription());
			else statement.setNull(3, Types.VARBINARY);
			if(url.getCategory() != null) statement.setInt(4, url.getCategory().getID());
			else statement.setNull(4, Types.INTEGER);
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
				resultBuffer = insert(url);
			}
		}

		return resultBuffer;
	}

	@Override
	public Url get(int ID) {
		log.debug("Get url: ID={}", ID);
		
		Url url = null;
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
				String foundTitle = result.getString(2);
				String foundUrl = result.getString(3);
				String foundDescription = result.getString(4);
				int foundCatID = result.getInt(5);
				url = new Url(foundID, foundUrl, foundTitle);
				
				if(foundDescription != null) url.setDescription(foundDescription);
				if(foundCatID != 0) {
					ISubcategoryDAO category = database.getSubcategory();
					url.setCategory(category.get(foundCatID));
				}
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
		
		if(url == null) {
			log.debug("Try to create table and get again");
			
			if(createTable()) {
				log.debug("Create dable succeed");
				url = get(ID);
			}
		}
		
		return url;
	}

	@Override
	public List<Url> getAllWithSubcategory(Subcategory subcategory) {
		log.debug("Get all urls with subcategory: ID={} name={}", subcategory.getID(), subcategory.getName());
		
		List<Url> urls = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			connection = database.getConnection();
			statement = connection.prepareStatement(GET_CATEGORY);
			
			statement.setInt(1, subcategory.getID());
			
			result = statement.executeQuery();
			urls = new ArrayList<>();
					
			if(result != null) {
				while(result.next()) {
					int foundID = result.getInt(1);
					String foundTitle = result.getString(2);
					String foundUrl = result.getString(3);
					String foundDescription = result.getString(4);
					
					Url url = new Url(foundID, foundUrl, foundTitle, subcategory);
					
					if(foundDescription != null) url.setDescription(foundDescription);
					
					urls.add(url);
				}
			}
		}
		catch(Exception ex) {
			log.error("Get all with subcategory failed", ex);
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
		
		if(urls == null) {
			log.debug("Try to create table and get with subcategory again");
			
			if(createTable()) {
				log.debug("Create dable succeed");
				urls = getAllWithSubcategory(subcategory);
			}
		}
		
		return urls;
	}

	@Override
	public List<Url> getAll() {
		log.debug("Get all urls");
		
		List<Url> urls = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			
			result = statement.executeQuery(GET_ALL);
			urls = new ArrayList<>();
					
			if(result != null) {
				while(result.next()) {
					int foundID = result.getInt(1);
					String foundTitle = result.getString(2);
					String foundUrl = result.getString(3);
					String foundDescription = result.getString(4);
					int foundCatID = result.getInt(5);
					Url url = new Url(foundID, foundUrl, foundTitle);
					
					if(foundDescription != null) url.setDescription(foundDescription);
					if(foundCatID != 0) {
						ISubcategoryDAO category = database.getSubcategory();
						url.setCategory(category.get(foundCatID));
					}
					
					urls.add(url);
				}
			}
		}
		catch(Exception ex) {
			log.error("Get all urls failed", ex);
			ex.printStackTrace();
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
		
		if(urls == null) {
			log.debug("Try to create table and get all again");
			
			if(createTable()) {
				log.debug("Create dable succeed");
				urls = getAll();
			}
		}
		
		return urls;
	}

	@Override
	public boolean update(Url url) {
		log.debug("Update subcategory: ID={} url={} title={}", url.getID(), url.getUrl(), url.getTitle());
		
		Connection connection = null;
		PreparedStatement statement = null;
		boolean result = false;
		
		try {
			connection = database.getConnection();
			statement = connection.prepareStatement(UPDATE);
			
			statement.setString(1, url.getTitle());
			statement.setString(2, url.getUrl());
			statement.setString(3, url.getDescription());
			statement.setInt(4, url.getCategory().getID());
			statement.setInt(5, url.getID());
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
				result = update(url);
			}
		}
		
		return result;
	}

	@Override
	public boolean delete(int ID) {
		log.debug("Delete url: ID={}", ID);
		
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
			GET_CATEGORY = obj.getString("GET_CATEGORY");
			UPDATE = obj.getString("UPDATE");
			DELETE = obj.getString("DELETE");
			
			log.debug("CREATE_TABLE: {}", CREATE_TABLE);
			log.debug("INSERT: {}", INSERT);
			log.debug("GET: {}", GET);
			log.debug("GET_ALL: {}", GET_ALL);
			log.debug("GET_CATEGORY: {}", GET_CATEGORY);
			log.debug("UPDATE: {}", UPDATE);
			log.debug("DELETE: {}", DELETE);
		}
		catch(Exception ex) {
			log.error("Load JSON file failed", ex);
		}
	}
}
