package test.dao.model;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import mvc.dao.DAOFactory;

class Utilities {
	private Utilities() {}
	
	public static List<String> getTableNames(int database) throws Exception {
		Connection connection = DAOFactory.get(database).getConnection();
		DatabaseMetaData meta = connection.getMetaData();
		List<String> buffer = new ArrayList<>();
		
		ResultSet rs = meta.getTables(null, null, "%", null);
		while(rs.next()) buffer.add(rs.getString(3));
		
		rs.close();
		connection.close();
		
		return buffer;
	}
	
	public static int count(String tableName, int database) throws Exception {
		String sql = "SELECT COUNT(*) FROM " + tableName;
		int result = 0;
		Connection connection = DAOFactory.get(database).getConnection();
		Statement statement = connection.createStatement();
		
		ResultSet rs = statement.executeQuery(sql);
		if(rs != null && rs.next()) result = rs.getInt(1);
		
		rs.close();
		statement.close();
		connection.close();
		
		return result;
	}
	
	public static JSONObject getJsonQuery(String filename, int database) {
		FileInputStream jsonFile = null;
		JSONObject object = null;
		
		try {
			jsonFile = new FileInputStream(new File(filename));
			String rawJson = IOUtils.toString(jsonFile);
			object = new JSONObject(rawJson).getJSONObject(DAOFactory.get(database).getName());
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return object;
	}
	
	public static String getCreateTable(JSONArray array) {
		String[] raw = array.toList().toArray(new String[array.length()]);
		return String.join("\n", raw);
	}
}
