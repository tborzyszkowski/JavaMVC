package mvc.view.toolbar.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.model.Category;

public final class CategoryPanel extends JPanel {
	private static final long serialVersionUID = -4079172705587516541L;
	private static final Logger log = LoggerFactory.getLogger(CategoryPanel.class);
	
	private boolean displayID = false;
	private Category category = null;
	private GridBagConstraints constraints = new GridBagConstraints();
	private JLabel idLabel = new JLabel("ID");
	private JLabel nameLabel = new JLabel("Name");
	private JTextField idField = new JTextField();
	private JTextField nameField = new JTextField();

	public CategoryPanel() {
		log.info("Initialize category panel");
		
		displayID = false;
		idField.setEnabled(false);
		initializePanel();
	}
	
	public CategoryPanel(Category category) {
		log.info("Initialize category panel with values");
		
		displayID = true;
		this.category = category;
		idField.setEnabled(false);
		initializePanel();
	}
	
	private void initializePanel() {
		log.debug("Initialize panel");
		
		setLayout(new GridBagLayout());
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 5, 0);
		
		if(category != null) {
			idField.setText(String.valueOf(category.getID()));
			nameField.setText(category.getName());
		}
		
		if(displayID) addFormItem(0, idLabel, idField);
		addFormItem(1, nameLabel, nameField);
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
}
