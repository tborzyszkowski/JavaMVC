package mvc.view.toolbar.button;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.model.Category;
import mvc.model.Subcategory;
import mvc.model.Url;
import mvc.observer.category.edit.CategoryEditListener;
import mvc.observer.category.edit.CategoryEditSubject;
import mvc.observer.url.edit.UrlEditListener;
import mvc.observer.url.edit.UrlEditSubject;
import mvc.view.toolbar.panel.CategoryPanel;
import mvc.view.toolbar.panel.SubcategoryPanel;
import mvc.view.toolbar.panel.UrlPanel;

public final class AddNewButton extends JButton implements ActionListener, CategoryEditSubject, UrlEditSubject {
	private static final long serialVersionUID = 3821107696744652502L;
	private static final Logger log = LoggerFactory.getLogger(AddNewButton.class);
	private static final String iconName = "toolbar_addnew.png";
	
	private List<CategoryEditListener> categoryEditListeners = new LinkedList<>();
	private List<UrlEditListener> urlEditListeners = new LinkedList<>();
	private JPopupMenu popup = null;
	
	public AddNewButton(Dimension size) {
		log.info("Initialize add new button");
		
		initializeSize(size);
		initializeIcon();
		initializePopup();
		
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
	
	private void initializePopup() {
		log.debug("Initialize popup");
		
		ActionListener optionSelectedAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String source = e.getActionCommand();
				log.debug("Selected option: {}", source);
				
				if(source.equalsIgnoreCase("cat")) addNewCategory();
				else if(source.equalsIgnoreCase("subcat")) addNewSubcategory();
				else if(source.equalsIgnoreCase("url")) addNewUrl();
			}
		};
		
		JMenuItem cat = new JMenuItem("Category");
		cat.setActionCommand("cat");
		cat.addActionListener(optionSelectedAction);
		
		JMenuItem subcat = new JMenuItem("Subcategory");
		subcat.setActionCommand("subcat");
		subcat.addActionListener(optionSelectedAction);
		
		JMenuItem url = new JMenuItem("Url");
		url.setActionCommand("url");
		url.addActionListener(optionSelectedAction);
		
		popup = new JPopupMenu();
		popup.add(cat);
		popup.add(subcat);
		popup.addSeparator();
		popup.add(url);
	}
	
	private void addNewCategory() {
		log.debug("Add new category");
		
		CategoryPanel panel = new CategoryPanel();
		int result = JOptionPane.showConfirmDialog(this, panel, "Add new category", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if(result == JOptionPane.OK_OPTION) {
			if(!panel.isNameEmpty()) {
				log.debug("Add new category with name: {}", panel.getName());
				addCategory(new Category(panel.getName()));
			}
			else {
				log.warn("Add new category aborted, empty name value");
			}
		}
		else {
			log.debug("Add new category canceled");
		}
	}
	
	private void addNewSubcategory() {
		log.debug("Add new subcategory");
		
		SubcategoryPanel panel = new SubcategoryPanel();
		int result = JOptionPane.showConfirmDialog(this, panel, "Add new subcategory", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if(result == JOptionPane.OK_OPTION) {
			if(!panel.isNameEmpty()) {
				log.debug("Add new subcategory with name: {}", panel.getName());
				addSubcategory(new Subcategory(panel.getName(), panel.getCategory()));
			}
			else {
				log.warn("Add new subcategory aborted, empty name value");
			}			
		}
		else {
			log.debug("Add new subcategory canceled");
		}
	}
	
	private void addNewUrl() {
		log.debug("Add new url");
		
		UrlPanel panel = new UrlPanel();
		int result = JOptionPane.showConfirmDialog(this, panel, "Add new url", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if(result == JOptionPane.OK_OPTION) {
			if(!panel.isTitleOrUrlEmpty()) {
				log.debug("Add new url: title={} url={} subcategory: name={}", panel.getUrl(), panel.getSubcategory().getName());
				
				if(!panel.isDescriptionEmpty()) addUrl(new Url(panel.getUrl(), panel.getTitle(), panel.getDescription(), panel.getSubcategory())); 
				else addUrl(new Url(panel.getUrl(), panel.getTitle(), panel.getSubcategory()));
			}
			else {
				log.warn("Add new url aborted, empty title/url value");
			}
		}
		else {
			log.debug("Add new url canceled");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.debug("Pressed button");	
		popup.show(this, 0, 25);
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
	public void addCategory(Category category) {
		log.debug("Add new category with name: {}", category.getName());
		for(CategoryEditListener listener : categoryEditListeners) listener.onCategoryAdd(category);
	}

	@Override
	public void addSubcategory(Subcategory subcategory) {
		log.debug("Add new subcategory with name: {}", subcategory.getName());
		for(CategoryEditListener listener : categoryEditListeners) listener.onSubcategoryAdd(subcategory);
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
	public void addUrl(Url url) {
		log.debug("Add new url: title={} url={}", url.getTitle(), url.getUrl());
		for(UrlEditListener listener : urlEditListeners) listener.onUrlAdd(url);
	}
	
	@Override public void editCategories(List<Category> categories) { log.warn("Wrong reference, this button cannot edit categories."); }
	@Override public void editSubcategories(List<Subcategory> subcategories) { log.warn("Wrong reference, this button cannot edit subcategories"); }
	@Override public void deleteCategories(List<Category> categories) { log.warn("Wrong reference, this button cannot delete categories"); }
	@Override public void deleteSubcategories(List<Subcategory> subcategories) { log.warn("Wrong reference, this button cannot delete subcategories"); }
	@Override public void editUrls(List<Url> urls) { log.warn("Wrong reference, this button cannot edit urls"); }
	@Override public void deleteUrls(List<Url> urls) { log.warn("Wrong reference, this button cannot delete urls"); }
}
