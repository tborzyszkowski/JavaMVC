package mvc.view.toolbar.button;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.model.Category;
import mvc.model.Subcategory;
import mvc.model.Url;
import mvc.observer.category.edit.CategoryEditListener;
import mvc.observer.category.edit.CategoryEditSubject;
import mvc.observer.category.select.CategorySelectListener;
import mvc.observer.url.edit.UrlEditListener;
import mvc.observer.url.edit.UrlEditSubject;
import mvc.observer.url.select.UrlSelectListener;

public final class DeleteButton extends JButton implements ActionListener, CategoryEditSubject, UrlEditSubject, CategorySelectListener, UrlSelectListener {
	private static final long serialVersionUID = 5739651433521986611L;
	private static final Logger log = LoggerFactory.getLogger(DeleteButton.class);
	private static final String iconName = "toolbar_delete.png";
	
	private List<CategoryEditListener> categoryEditListeners = new LinkedList<>();
	private List<UrlEditListener> urlEditListeners = new LinkedList<>();
	private List<Category> selectedCategories = null;
	private List<Subcategory> selectedSubcategories = null;
	private List<Url> selectedUrls = null;
	
	public DeleteButton(Dimension size) {
		log.info("Initialize delete button");
		
		initializeSize(size);
		initializeIcon();
		
		setEnabled(false);
		addActionListener(this);
	}
	
	private void initializeSize(Dimension size) {
		log.debug("Set size: {}", size.toString());
		
		setSize(size);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
	}
	
	private void initializeIcon() {
		log.debug("Set icon");
		
		try {
			URL icon = getClass().getResource(String.format("/icons/%s", iconName));
			setIcon(new ImageIcon(icon));
		}
		catch(Exception ex) {
			log.error("Failed to set icon", ex);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.debug("Pressed button");
		
		if(selectedUrls != null) deleteUrls(selectedUrls);
		else if(selectedSubcategories != null) deleteSubcategories(selectedSubcategories);
		else if(selectedCategories != null) deleteCategories(selectedCategories);
		else {
			log.warn("Unwanted behaviour, delete button should be disabled if everything is unselected");
		}
	}
	
	@Override
	public void addCategoryEditListener(CategoryEditListener listener) {
		log.debug("Add new listener");
		categoryEditListeners.add(listener);
	}

	@Override
	public void removeCategoryEditListener(CategoryEditListener listener) {
		log.debug("Remove listener");
		categoryEditListeners.remove(listener);
	}
	
	@Override
	public void deleteCategories(List<Category> categories) {
		log.debug("Delete {} categories", categories.size());
		for(CategoryEditListener listener : categoryEditListeners) listener.onCategoryDelete(categories);
	}

	@Override
	public void deleteSubcategories(List<Subcategory> subcategories) {
		log.debug("Delete {} subcategories", subcategories.size());
		for(CategoryEditListener listener : categoryEditListeners) listener.onSubcategoryDelete(subcategories);
	}

	@Override
	public void addUrlEditListener(UrlEditListener listener) {
		log.debug("Add new listener");
		urlEditListeners.add(listener);
	}

	@Override
	public void removeUrlEditListener(UrlEditListener listener) {
		log.debug("Remove listener");
		urlEditListeners.remove(listener);
	}
	
	@Override
	public void deleteUrls(List<Url> urls) {
		log.debug("Delete {} urls", urls.size());
		for(UrlEditListener listener : urlEditListeners) listener.onUrlDelete(urls);
	}

	@Override
	public void onSelectCategory(List<Category> categories) {
		log.debug("Categories selected");
		setEnabled(true);
		selectedCategories = categories;
	}

	@Override
	public void onSelectSubcategory(List<Subcategory> subcategories) {
		log.debug("Subcategories selected");
		setEnabled(true);
		selectedSubcategories = subcategories;
	}

	@Override
	public void onUnselectAllCategories() {
		log.debug("All categories unselected");
		selectedCategories = null;
		selectedSubcategories = null;
		selectedUrls = null;
		
		setEnabled(false);
	}

	@Override
	public void onSelectUrl(List<Url> urls) {
		log.debug("Urls selected");
		setEnabled(true);
		selectedUrls = urls;
	}

	@Override
	public void onUnselectAllUrls() {
		log.debug("All urls unselected");
		selectedUrls = null;
		
		if(selectedCategories == null && selectedSubcategories == null) setEnabled(false);
	}
	
	@Override public void addCategory(Category category) { log.warn("Wrong reference, this button cannot add new category"); }
	@Override public void addSubcategory(Subcategory subcategory) { log.warn("Wrong reference, this button cannot add new subcategory"); }
	@Override public void editCategories(List<Category> categories) { log.warn("Wrong reference, this button cannot edit categories"); }
	@Override public void editSubcategories(List<Subcategory> subcategories) { log.warn("Wrong reference, this button cannot edit subcategories"); }
	@Override public void addUrl(Url url) { log.warn("Wrong reference, this button cannot add new url"); }
	@Override public void editUrls(List<Url> urls) { log.warn("Wrong reference, this button cannot edit urls"); }
}
