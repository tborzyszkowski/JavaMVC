package mvc.view.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.model.Subcategory;
import mvc.model.Category;
import mvc.observer.category.select.CategorySelectListener;
import mvc.observer.category.select.CategorySelectSubject;
import mvc.observer.category.update.CategoryUpdateListener;

public final class CategoryView extends JPanel implements CategorySelectSubject, CategoryUpdateListener {
	private static final long serialVersionUID = 8970054597563459574L;
	private static final Logger log = LoggerFactory.getLogger(CategoryView.class);
	
	private final boolean rootVisible = false;
	private final int toggleClickCount = 2;
	private final Color backgroundColor = new Color(248, 248, 248);
	private final Color selectedColor = new Color(230, 230, 230);
	
	private List<CategorySelectListener> listeners = new LinkedList<>();
	private DefaultMutableTreeNode treeRoot = null;
	private JTree treeList = null;
	private JScrollPane treeScrollbar = null;
	
	public CategoryView(int width, int height) {
		log.info("Initialize Category view");
		
		setBackground(backgroundColor);
		setPreferredSize(new Dimension(width, height));
		setBorder(new TitledBorder("Categories"));
		
		setLayout(new BorderLayout(5,5));
		
		initializeListTree();
		initializeTreeSelectionListener();
		setTreeListStyle();
		add(treeScrollbar);
	}
		
	private void initializeListTree() {
		log.debug("Initialize category list tree");
		
		treeRoot = new DefaultMutableTreeNode("Categories");
		treeList = new JTree(treeRoot);
		treeScrollbar = new JScrollPane(treeList);
		
		treeList.setRootVisible(rootVisible);
		treeList.setToggleClickCount(toggleClickCount);
	}
	
	private void initializeTreeSelectionListener() {
		log.debug("Initialize tree selection listener");
		
		treeList.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				log.debug("Selected category");
				
				TreePath[] paths = treeList.getSelectionPaths();
				List<Category> categories = new LinkedList<>();
				List<Subcategory> subcategories = new LinkedList<>();
				
				if(paths != null) {
					for(TreePath path : paths) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
						Object obj = node.getUserObject();
						
						if(obj instanceof Category) {
							log.debug("Selected category: {}", path.getLastPathComponent().toString());
							categories.add((Category) obj);
						}
						else if(obj instanceof Subcategory) {
							log.debug("Selected subcategory: {}", path.getLastPathComponent().toString());
							subcategories.add((Subcategory) obj);
						}
						else {
							log.warn("Unwanted object class type: {}", obj.getClass().getName());
						}
					}
				}
				
				if(subcategories.size() > 0) selectSubcategory(subcategories);
				else if(categories.size() > 0) selectCategory(categories);
				else selectNothing();
			}
		});
	}
	
	private void setTreeListStyle() {
		log.debug("Set tree list style");
		
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)treeList.getCellRenderer();
		
		renderer.setBackgroundNonSelectionColor(backgroundColor);
		renderer.setBackgroundSelectionColor(selectedColor);
		
		try {
			URL nodeClosedIcon = getClass().getResource("/icons/treelist_node_closed.png");
			URL nodeOpenedIcon = getClass().getResource("/icons/treelist_node_opened.png");
			URL nodeLeafIcon = getClass().getResource("/icons/treelist_leaf.png");
			
			renderer.setClosedIcon(new ImageIcon(nodeClosedIcon));
			renderer.setOpenIcon(new ImageIcon(nodeOpenedIcon));
			renderer.setLeafIcon(new ImageIcon(nodeLeafIcon));
		}
		catch(Exception ex) {
			log.error("Failed to load icons", ex);
		}
						
		treeList.setBackground(backgroundColor);
	}
	
	private void setTreeList(Map<Category, List<Subcategory>> categories) {
		log.debug("Add {} nodes to tree list", categories.size());
		
		treeRoot.removeAllChildren();
		
		for(Map.Entry<Category, List<Subcategory>> entry : categories.entrySet()) {
			log.debug("Add category (ID={} name={}) as node", entry.getKey().getID(), entry.getKey().getName());
			
			DefaultMutableTreeNode main = new DefaultMutableTreeNode(entry.getKey());
			
			for(Subcategory cat : entry.getValue()) {
				log.debug("Add subcategory (ID={} name={}) to category (ID={} name={})", cat.getID(), cat.getName(), entry.getKey().getID(), entry.getKey().getName());
				
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(cat);
				main.add(child);
			}
			
			treeRoot.add(main);
		}
		
		refreshTreeList();
	}
		
	private void refreshTreeList() {
		DefaultTreeModel model = (DefaultTreeModel)treeList.getModel();
		model.reload();
	}
	
	@Override
	public void addCategorySelectListener(CategorySelectListener listener) {
		log.debug("Add new listener");
		listeners.add(listener);
	}

	@Override
	public void removeCategorySelectListener(CategorySelectListener listener) {
		log.debug("Remove listener");
		listeners.remove(listener);
	}

	@Override
	public void selectCategory(List<Category> categories) {
		log.debug("Call listeners with {} categories", categories.size());
		for(CategorySelectListener listener : listeners) listener.onSelectCategory(categories);
	}

	@Override
	public void selectSubcategory(List<Subcategory> subcategories) {
		log.debug("Call listeners with {} subcategories", subcategories.size());
		for(CategorySelectListener listener : listeners) listener.onSelectSubcategory(subcategories);
	}
	
	@Override
	public void selectNothing() {
		log.debug("Call listeners that all categories are unselected");
		for(CategorySelectListener listener : listeners) listener.onUnselectAllCategories();
	}

	@Override
	public void onCategoryUpdate(Map<Category, List<Subcategory>> categories) {
		log.debug("Categories updated");
		setTreeList(categories);
	}
}
