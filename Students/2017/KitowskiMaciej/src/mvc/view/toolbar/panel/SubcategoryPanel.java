package mvc.view.toolbar.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.dao.DAOFactory;
import mvc.model.Category;
import mvc.model.Subcategory;

public final class SubcategoryPanel extends JPanel {
	private static final long serialVersionUID = 1294392808012937642L;
	private static final Logger log = LoggerFactory.getLogger(SubcategoryPanel.class);
	
	private boolean displayID = false;
	private Subcategory subcategory = null;
	private GridBagConstraints constraints = new GridBagConstraints();
	private JLabel idLabel = new JLabel("ID");
	private JLabel nameLabel = new JLabel("Name");
	private JLabel categoryLabel = new JLabel("Category");
	private JTextField idField = new JTextField();
	private JTextField nameField = new JTextField();
	private JComboBox<Category> categoryField = null;

	public SubcategoryPanel() {
		log.info("Initialize subcategory panel");
		
		displayID = false;
		idField.setEnabled(false);
		initializePanel();
	}
	
	public SubcategoryPanel(Subcategory subcategory) {
		log.info("Initialize subcategory panel with values");
		
		displayID = true;
		idField.setEnabled(false);
		this.subcategory = subcategory;
		initializePanel();
	}
	
	private void initializePanel() {
		log.debug("Initialize panel");
		
		setLayout(new GridBagLayout());
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 5, 0);
		
		List<Category> categories = DAOFactory.get().getCategory().getAll();
		categoryField = new JComboBox<>(categories.toArray(new Category[categories.size()]));
		if(subcategory != null) setFieldValues(categories);
		
		if(displayID) addFormItem(0, idLabel, idField);
		addFormItem(1, categoryLabel, categoryField);
		addFormItem(2, nameLabel, nameField);
	}
	
	private void setFieldValues(List<Category> categories) {
		log.debug("Set default values");
		
		idField.setText(String.valueOf(subcategory.getID()));
		nameField.setText(subcategory.getName());
		
		Category current = null;
		for(Category cat : categories) {
			if(subcategory.getParent().getID() == cat.getID()) {
				current = cat;
				break;
			}
		}
		
		categoryField.setSelectedItem(current);
	}
	
	private void addFormItem(int posY, JLabel label, Component component) {
		constraints.gridx = 0;
		constraints.gridy = posY;
		constraints.weightx = 0.05;
		add(label, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = posY;
		constraints.weightx = 0.95;
		add(component, constraints);
	}
	
	public boolean isNameEmpty() {
		return nameField.getText() == null || nameField.getText().trim().isEmpty();
	}
	
	public String getName() {
		return nameField.getText();
	}
	
	public Category getCategory() {
		return (Category) categoryField.getSelectedItem();
	}
}
