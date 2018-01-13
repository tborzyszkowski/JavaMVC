package mvc.dao.model;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JsonLoader {
	private static final Logger log = LoggerFactory.getLogger(JsonLoader.class);
	
	private JsonLoader() {}
	
	public static JSONObject getJson(String filename, String databaseName) throws Exception {
		log.debug("Load JSON from: {} for database: {}", filename, databaseName);
	
		JSONObject obj = null;
		FileInputStream file = new FileInputStream(new File(filename));
		String raw = IOUtils.toString(file);
		
		obj = new JSONObject(raw).getJSONObject(databaseName);
		file.close();
		
		log.trace(obj.toString());
		return obj;
	}
	
	public static String joinStringArray(JSONArray array) {
		log.debug("Join JSONArray: {}", array.toString());
		
		String[] str = array.toList().toArray(new String[array.length()]);
		return String.join("\n", str);
	}
}
