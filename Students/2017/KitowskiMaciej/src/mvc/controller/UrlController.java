package mvc.controller;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.dao.DAOFactory;
import mvc.dao.model.ISubcategoryDAO;
import mvc.dao.model.IUrlDAO;
import mvc.model.Subcategory;
import mvc.model.Category;
import mvc.model.Url;
import mvc.observer.category.select.CategorySelectListener;
import mvc.observer.toolbar.DatabaseChangeListener;
import mvc.observer.url.edit.UrlEditListener;
import mvc.observer.url.update.UrlUpdateListener;
import mvc.observer.url.update.UrlUpdateSubject;

public final class UrlController implements UrlUpdateSubject, DatabaseChangeListener, CategorySelectListener, UrlEditListener {
	private static final Logger log = LoggerFactory.getLogger(UrlController.class);
	
	private List<UrlUpdateListener> urlUpdateListeners = new LinkedList<>();
	private List<Category> selectedCategories = null;
	private List<Subcategory> selectedSubcategories = null;
	private IUrlDAO urlDao = null;
	private ISubcategoryDAO catDao = null;
	
	public UrlController() {
		log.info("Initialize url controller");
		
		initializeDAO();
	}
	
	private void initializeDAO() {
		log.debug("Initialize dao");
		
		urlDao = DAOFactory.get().getUrl();
		catDao = DAOFactory.get().getSubcategory();
	}
	
	@Override
	public void addUrlUpdateListener(UrlUpdateListener listener) {
		log.debug("Add new listener");
		urlUpdateListeners.add(listener);
	}

	@Override
	public void removeUrlUpdateListener(UrlUpdateListener listener) {
		log.debug("Remove listener");
		urlUpdateListeners.remove(listener);
	}

	@Override
	public void updateUrls(List<Url> urls) {
		log.debug("Call {} url update listeners", urlUpdateListeners.size());
		for(UrlUpdateListener listener : urlUpdateListeners) listener.onUrlUpdate(urls);
	}
	
	@Override
	public void onDatabaseChange() {
		log.debug("Database changed");
		initializeDAO();
		updateUrls(null);
	}

	@Override
	public void onSelectCategory(List<Category> categories) {
		log.debug("Categories selected");
		
		selectedCategories = categories;
		selectedSubcategories = null;
		List<Url> urls = new LinkedList<>();
		
		for(Category cat : categories) {
			log.debug("Get all subcategories from {} category", cat.toString());
			
			List<Subcategory> subcategories = catDao.getWithCategory(cat);
			log.debug("Found {} subcategories", subcategories.size());
			
			for(Subcategory subcat : subcategories) {
				log.debug("Get all urls from {} subcategory", subcat.toString());
				
				urls.addAll(urlDao.getAllWithSubcategory(subcat));
			}
		}
		
		log.debug("Found {} urls", urls.size());
		updateUrls(urls);
	}

	@Override
	public void onSelectSubcategory(List<Subcategory> subcategories) {
		log.debug("Subcategories selected");
		
		selectedCategories = null;
		selectedSubcategories = subcategories;
		List<Url> urls = new LinkedList<>();
		
		for(Subcategory subcat : subcategories) {
			log.debug("Get all urls from {} subcategory", subcat.toString());
			
			urls.addAll(urlDao.getAllWithSubcategory(subcat));
		}
		
		log.debug("Found {} urls", urls.size());
		updateUrls(urls);
	}
	
	@Override
	public void onUnselectAllCategories() {
		log.debug("All categories unselected");
		selectedCategories = null;
		selectedSubcategories = null;
		updateUrls(null);
	}

	@Override
	public void onUrlAdd(Url url) {
		log.debug("Add new url: title={}, url={}", url.getTitle(), url.getUrl());
		
		if(urlDao.insert(url) == IUrlDAO.INSERT_FAIL) {
			log.warn("Failed to add new subcategory");
		}
		else {
			if(selectedSubcategories != null) onSelectSubcategory(selectedSubcategories);
			else if(selectedCategories != null) onSelectCategory(selectedCategories);
		}
	}

	@Override
	public void onUrlEdit(List<Url> urls) {
		log.debug("Edit {} urls", urls.size());
		
		for(Url url : urls) {
			if(!urlDao.update(url)) {
				log.warn("Failed to edit url");
			}
		}
		
		if(selectedSubcategories != null) onSelectSubcategory(selectedSubcategories);
		else if(selectedCategories != null) onSelectCategory(selectedCategories);
	}
	
	@Override
	public void onUrlDelete(List<Url> urls) {
		log.debug("Delete {} urls", urls.size());
		
		for(Url url : urls) {
			log.debug("Delete url: ID={} title={} url={}", url.getID(), url.getTitle(), url.getUrl());
			
			if(urlDao.delete(url.getID())) {
				log.warn("Failed to delete url");
			}
		}
		
		if(selectedSubcategories != null) onSelectSubcategory(selectedSubcategories);
		else if(selectedCategories != null) onSelectCategory(selectedCategories);
		else {
			log.warn("Unwanted behaviour, delete button should be disabled if categories are unselected");
		}
	}
}
