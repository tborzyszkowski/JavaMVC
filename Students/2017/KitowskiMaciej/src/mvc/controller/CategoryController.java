package mvc.controller;


import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.dao.DAOFactory;
import mvc.dao.model.ISubcategoryDAO;
import mvc.dao.model.ICategoryDAO;
import mvc.model.Subcategory;
import mvc.model.Category;
import mvc.observer.category.edit.CategoryEditListener;
import mvc.observer.category.update.CategoryUpdateListener;
import mvc.observer.category.update.CategoryUpdateSubject;
import mvc.observer.toolbar.DatabaseChangeListener;

public final class CategoryController implements CategoryUpdateSubject, DatabaseChangeListener, CategoryEditListener {
	private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
	
	private List<CategoryUpdateListener> catUpdateListeners = new LinkedList<>();
	private ICategoryDAO mainDao = null;
	private ISubcategoryDAO catDao = null;
	
	public CategoryController() {
		log.info("Initialize subcategory controller");
		
		initializeDAO();
	}
	
	private void initializeDAO() {
		log.debug("Initialize dao");
		
		mainDao = DAOFactory.get().getCategory();
		catDao = DAOFactory.get().getSubcategory();
	}
			
	private Map<Category, List<Subcategory>> getCategories() {
		log.debug("Load categories to map");
		
		Map<Category, List<Subcategory>> categories = new LinkedHashMap<>();
		List<Category> mainCategories = mainDao.getAll();
		
		for(Category category : mainCategories) {
			log.debug("Create list for main category(ID={} name={})", category.getID(), category.getName());
			
			List<Subcategory> subcategories = catDao.getWithCategory(category);
			categories.put(category, subcategories);
			
			log.debug("Added {} subcategories", subcategories.size());
		}
		
		return categories;
	}

	@Override
	public void addCategoryUpdateListener(CategoryUpdateListener listener) {
		log.debug("Add new listener");
		catUpdateListeners.add(listener);
	}

	@Override
	public void removeCategoryUpdateListener(CategoryUpdateListener listener) {
		log.debug("Remove listener");
		catUpdateListeners.remove(listener);
	}

	@Override
	public void updateCategories() {
		log.debug("Call {} category updated listeners", catUpdateListeners.size());
		
		Map<Category, List<Subcategory>> categories = getCategories();
		for(CategoryUpdateListener listener : catUpdateListeners) listener.onCategoryUpdate(categories);
	}

	@Override
	public void onDatabaseChange() {
		log.debug("Database changed");
		initializeDAO();
		updateCategories();
	}

	@Override
	public void onCategoryAdd(Category category) {
		log.debug("Add new category: name={}", category.getName());
		
		if(mainDao.insert(category) == ICategoryDAO.INSERT_FAIL) {
			log.warn("Failed to add new category");
		}
		else updateCategories();
	}

	@Override
	public void onSubcategoryAdd(Subcategory subcategory) {
		log.debug("Add new subcategory: name={}, category: ID={}, name={}", subcategory.getName(), subcategory.getParent().getID(), subcategory.getParent().getName());
		
		if(catDao.insert(subcategory) == ISubcategoryDAO.INSERT_FAIL) {
			log.warn("Failed to add new subcategory");
		}
		else updateCategories();
	}

	@Override
	public void onCategoryEdit(List<Category> categories) {
		log.debug("Edit {} categories", categories.size());
		
		for(Category cat : categories) {
			if(!mainDao.update(cat)) {
				log.warn("Failed to edit category");
			}
		}
		
		updateCategories();
	}

	@Override
	public void onSubcategoryEdit(List<Subcategory> subcategories) {
		log.debug("Edit {} subcategories", subcategories.size());
		
		for(Subcategory cat : subcategories) {
			if(!catDao.update(cat)) {
				log.warn("Failed to edit subcategory");
			}
		}
		
		updateCategories();
	}
	
	@Override
	public void onCategoryDelete(List<Category> categories) {
		log.debug("Delete {} categories", categories.size());
		
		for(Category cat : categories) {
			log.debug("Delete category: ID={} name={}", cat.getID(), cat.getName());
			
			if(!mainDao.delete(cat.getID())) {
				log.warn("Failed to delete category");
			}
		}
		
		updateCategories();
	}

	@Override
	public void onSubcategoryDelete(List<Subcategory> subcategories) {
		log.debug("Delete {} subcategories", subcategories.size());
		
		for(Subcategory cat : subcategories) {
			log.debug("Delete category: ID={} name={}", cat.getID(), cat.getName());
			
			if(!catDao.delete(cat.getID())) {
				log.warn("Failed to delete category");
			}
		}
		
		updateCategories();
	}
}
