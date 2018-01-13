package mvc.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Url {
	private Logger log = LoggerFactory.getLogger(Url.class);
	
	private int ID = 0;
	private String url = null;
	private String title = null;
	private String description = null;
	Subcategory subcategory = null;
		
	public Url(String url, String title, String description, Subcategory subcategory) {
		log.debug("New Url: ID={} url={} title={} description={} subcategory: ID={} name={}", null, url, title, description, subcategory.getID(), subcategory.getName());
		
		this.url = url;
		this.title = title;
		this.description = description;
		this.subcategory = subcategory;
	}
	
	public Url(int ID, String url, String title, Subcategory subcategory) {
		log.debug("New Url: ID={} url={} title={} description={} subcategory: ID={} name={}", ID, url, title, null, subcategory.getID(), subcategory.getName());
		
		this.ID = ID;
		this.url = url;
		this.title = title;
		this.subcategory = subcategory;
	}
	
	public Url(int ID, String url, String title) {
		log.debug("New Url: ID={} url={} title={} description={} subcategory: ID={} name={}", ID, url, title, null, null, null);
		
		this.ID = ID;
		this.url = url;
		this.title = title;
	}
	
	public Url(String url, String title, String description) {
		log.debug("New Url: ID={} url={} title={} description={} subcategory: ID={} name={}", null, url, title, description, null, null);
		
		this.url = url;
		this.title = title;
		this.description = description;
	}
	
	public Url(String url, String title, Subcategory subcategory) {
		log.debug("New Url: ID={} url={} title={} description={} subcategory: ID={} name={}", null, url, title, null, subcategory.getID(), subcategory.getName());
		
		this.url = url;
		this.title = title;
		this.subcategory = subcategory;
	}
	
	public Url(String url, String title) {
		log.debug("New Url: ID={} url={} title={} description={} subcategory: ID={} name={}", null, url, title, null, null, null);
		
		this.url = url;
		this.title = title;
	}
		
	public void setID(int ID) {
		log.debug("Set ID: {}", ID);
		this.ID = ID;
	}

	public void setUrl(String url) {
		log.debug("Set url: {}", url);
		this.url = url;
	}

	public void setTitle(String title) {
		log.debug("Set title: {}", title);
		this.title = title;
	}

	public void setDescription(String description) {
		if(description != null) log.debug("Set description: {}", description);
		this.description = description;
	}

	public void setCategory(Subcategory subcategory) {
		log.debug("Set subcategory: ID={} name={}", subcategory.getID(), subcategory.getName());
		this.subcategory = subcategory;
	}

	public int getID() {
		log.trace("Get ID: {}", ID);
		return ID;
	}
	
	public String getUrl() {
		log.trace("Get url: {}", url);
		return url;
	}
	
	public String getTitle() {
		log.trace("Get title: {}", title);
		return title;
	}
	
	public String getDescription() {
		log.trace("Get description: {}", description);
		return description;
	}
	
	public Subcategory getCategory() {
		if(subcategory != null) log.trace("Get subcategory: ID={} name={}", subcategory.getID(), subcategory.getName());
		return subcategory;
	}
}
