package mvc.view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.controller.CategoryController;
import mvc.controller.UrlController;
import mvc.view.model.CategoryView;
import mvc.view.model.UrlView;
import mvc.view.toolbar.ToolbarPanel;
import mvc.view.toolbar.button.AddNewButton;
import mvc.view.toolbar.button.DeleteButton;
import mvc.view.toolbar.button.EditButton;
import mvc.view.toolbar.button.SelectDatabaseButton;

public final class MainFrame extends JFrame {
	private static final long serialVersionUID = 4785143357028575468L;
	private static final Logger log = LoggerFactory.getLogger(MainFrame.class);
	
	private final String windowTitle = "Bookmarker";
	private final int windowWidth = 800;
	private final int windowHeight = 600;
	
	private ToolbarPanel toolbarPanel = null;
	private CategoryView categoryView = null;
	private CategoryController categoryController = null;
	private UrlView urlView = null;
	private UrlController urlController = null;
	
	public MainFrame() {
		log.info("Initialize Main Frame with title: {} and size: {}x{}", windowTitle, windowWidth, windowHeight);
		
		setTitle(windowTitle);
		setSize(windowWidth, windowHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0, 0));
		
		initializeViews();
		initializeControllers();
		initializeObservers();
		initializeToolbar();
		
		addViewsToFrame();
	}
		
	private void initializeViews() {
		log.info("Initialize views");
		
		categoryView = new CategoryView(150, windowHeight);
		urlView = new UrlView(400,  windowHeight);
	}
	
	private void initializeControllers() {
		log.info("Initialize controllers");
		
		categoryController = new CategoryController();
		urlController = new UrlController();
	}
	
	private void initializeObservers() {
		log.debug("Initalize observers");
		
		categoryController.addCategoryUpdateListener(categoryView);
		categoryView.addCategorySelectListener(urlController);
		urlController.addUrlUpdateListener(urlView);
		
		categoryController.updateCategories();
	}
	
	private void initializeToolbar() {
		log.info("Initialize toolbar");
		
		toolbarPanel = new ToolbarPanel(windowWidth, 30);
		
		AddNewButton add = toolbarPanel.getAddNewButton();
		add.addCategoryEditListener(categoryController);
		add.addUrlEditListener(urlController);
		
		EditButton edit = toolbarPanel.getEditButton();
		edit.addCategoryEditListener(categoryController);
		edit.addUrlEditListener(urlController);
		categoryView.addCategorySelectListener(edit);
		urlView.addUrlSelectListener(edit);
		
		DeleteButton delete = toolbarPanel.getDeleteButton();
		delete.addCategoryEditListener(categoryController);
		delete.addUrlEditListener(urlController);
		categoryView.addCategorySelectListener(delete);
		urlView.addUrlSelectListener(delete);
		
		SelectDatabaseButton dbSelect = toolbarPanel.getSelectDatabaseButton();
		dbSelect.addDatabaseChangeListener(categoryController);
		dbSelect.addDatabaseChangeListener(urlController);		
	}
	
	private void addViewsToFrame() {
		log.info("Add views to frame");
		
		add(toolbarPanel, BorderLayout.PAGE_START);
		add(categoryView, BorderLayout.LINE_START);
		add(urlView, BorderLayout.CENTER);
	}
}
