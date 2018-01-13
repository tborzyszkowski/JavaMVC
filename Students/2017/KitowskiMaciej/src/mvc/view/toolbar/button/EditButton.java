package mvc.view.toolbar.button;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
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
import mvc.view.toolbar.panel.CategoryPanel;
import mvc.view.toolbar.panel.SubcategoryPanel;
import mvc.view.toolbar.panel.UrlPanel;

public final class EditButton extends JButton implements ActionListener, CategoryEditSubject, UrlEditSubject, CategorySelectListener, UrlSelectListener {
	private static final long serialVersionUID = 2452055567420326318L;
	private static final Logger log = LoggerFactory.getLogger(EditButton.class);
	private static final String iconName = "toolbar_edit.png";
	
	private List<CategoryEditListener> categoryEditListeners = new LinkedList<>();
	private List<UrlEditListener> urlEditListeners = new LinkedList<>();
	private List<Category> selectedCategories = null;
	private List<Subcategory> selectedSubcategories = null;
	private List<Url> selectedUrls = null;
	
	public EditButton(Dimension size) {
		log.info("Initialize edit button");
		
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
		
		if(selectedUrls != null) editUrls(selectedUrls);
		else if(selectedSubcategories != null) editSubcategories(selectedSubcategories);
		else if(selectedCategories != null) editCategories(selectedCategories);
		else {
			log.warn("Unwanted behaviour, edit button should be disabled if everything is unselected");
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
	public void editCategories(List<Category> categories) {
		log.debug("Edit categories");
			
		for(Category category : categories) {
			log.debug("Edit category: ID={}, name={}", category.getID(), category.getName());
			
			CategoryPanel panel = new CategoryPanel(category);
			int result = JOptionPane.showConfirmDialog(this, panel, "Edit category", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(result == JOptionPane.OK_OPTION) {
				if(!panel.isNameEmpty()) {
					category.setName(panel.getName());
				}
				else {
					log.warn("Edit category aborted, empty name value");
				}
			}
			else {
				log.debug("Edit category canceled");
			}
		}
		
		for(CategoryEditListener listener : categoryEditListeners) listener.onCategoryEdit(categories);
	}

	@Override
	public void editSubcategories(List<Subcategory> subcategories) {
		log.debug("Edit subcategories");
		
		for(Subcategory subcategory : subcategories) {
			log.debug("Edit subcategory: ID={}, name={}", subcategory.getID(), subcategory.getName());
						
			SubcategoryPanel panel = new SubcategoryPanel(subcategory);
			int result = JOptionPane.showConfirmDialog(this, panel, "Edit subcategory", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(result == JOptionPane.OK_OPTION) {
				if(!panel.isNameEmpty()) {
					subcategory.setName(panel.getName());
					subcategory.setParent(panel.getCategory());
				}
				else {
					log.warn("Edit subcategory aborted, empty name value");
				}
			}
			else {
				log.debug("Edit subcategory canceled");
			}
		}
		
		for(CategoryEditListener listener : categoryEditListeners) listener.onSubcategoryEdit(subcategories);
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
	public void editUrls(List<Url> urls) {
		log.debug("Edit urls");
		
		for(Url url : urls) {
			log.debug("Edit url: ID={}, title={}, url={}", url.getID(), url.getTitle(), url.getUrl());
						
			UrlPanel panel = new UrlPanel(url);
			int result = JOptionPane.showConfirmDialog(this, panel, "Edit url", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(result == JOptionPane.OK_OPTION) {
				if(!panel.isTitleOrUrlEmpty()) {
					url.setTitle(panel.getTitle());
					url.setUrl(panel.getUrl());
					
					if(!panel.isDescriptionEmpty()) url.setDescription(panel.getDescription());
					else url.setDescription(null);
					
					url.setCategory(panel.getSubcategory());
				}
				else {
					log.warn("Edit url aborted, empty title/url value");
				}
			}
			else {
				log.debug("Edit url canceled");
			}
		}
		
		for(UrlEditListener listener : urlEditListeners) listener.onUrlEdit(urls);
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

	@Override public void addCategory(Category category) { log.warn("Wrong reference, this button cannot add new categories"); }
	@Override public void addSubcategory(Subcategory subcategory) { log.warn("Wrong reference, this button cannot delete subcategories"); }
	@Override public void deleteCategories(List<Category> categories) { log.warn("Wrong reference, this button cannot delete categories"); }
	@Override public void deleteSubcategories(List<Subcategory> subcategories) { log.warn("Wrong reference, this button cannot delete subcategories"); }
	@Override public void addUrl(Url url) { log.warn("Wrong reference, this button cannot add new urls"); }	
	@Override public void deleteUrls(List<Url> urls) { log.warn("Wrong reference, this button cannot delete urls"); }
}
