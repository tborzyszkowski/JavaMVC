package mvc.view.toolbar.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.dao.DAOFactory;
import mvc.model.Subcategory;
import mvc.model.Url;

//TODO Group subcategories using unselectable categories
public final class UrlPanel extends JPanel {
	private static final long serialVersionUID = -7033793384872165623L;
	private static final Logger log = LoggerFactory.getLogger(UrlPanel.class);
	
	private boolean displayID = false;
	private Url url = null;
	private GridBagConstraints constraints = new GridBagConstraints();
	private JLabel idLabel = new JLabel("ID");
	private JLabel titleLabel = new JLabel("Title");
	private JLabel urlLabel = new JLabel("Url");
	private JLabel descriptionLabel = new JLabel("Description");
	private JLabel subcategoryLabel = new JLabel("Subcategory");
	private JTextField idField = new JTextField();
	private JTextField titleField = new JTextField();
	private JTextField urlField = new JTextField();
	private JTextArea descriptionField = new JTextArea(4, 20);
	private JComboBox<Subcategory> subcategoryField = null;
	
	public UrlPanel() {
		log.info("Initialize url panel");
		
		displayID = false;
		idField.setEnabled(false);
		initializePanel();
	}
	
	public UrlPanel(Url url) {
		log.info("Initialize url panel with values");
		
		displayID = true;
		this.url = url;
		idField.setEnabled(false);
		initializePanel();
	}
	
	private void initializePanel() {
		log.debug("Initialize panel");
		
		setLayout(new GridBagLayout());
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 5, 0);
		
		List<Subcategory> subcategories = DAOFactory.get().getSubcategory().getAll();
		subcategoryField = new JComboBox<>(subcategories.toArray(new Subcategory[subcategories.size()]));
		if(url != null) setFieldValues(subcategories);

		if(displayID) addFormItem(0, idLabel, idField);
		addFormItem(1, titleLabel, titleField);
		addFormItem(2, urlLabel, urlField);
		addFormItem(3, descriptionLabel, new JScrollPane(descriptionField));
		addFormItem(4, subcategoryLabel, subcategoryField);
	}
	
	private void setFieldValues(List<Subcategory> subcategories) {
		log.debug("Set default values");
		
		idField.setText(String.valueOf(url.getID()));
		titleField.setText(url.getTitle());
		urlField.setText(url.getUrl());
		
		if(url.getDescription() != null) descriptionField.setText(url.getDescription());
		
		Subcategory current = null;
		for(Subcategory sub : subcategories) {
			if(url.getCategory().getID() == sub.getID()) {
				current = sub;
				break;
			}
		}
		
		subcategoryField.setSelectedItem(current);
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
	
	public boolean isTitleOrUrlEmpty() {
		String title = titleField.getText();
		String url = urlField.getText();
		
		return title == null || url == null || title.trim().isEmpty() || url.trim().isEmpty();
	}
	
	public boolean isDescriptionEmpty() {
		return descriptionField.getText() == null || descriptionField.getText().trim().isEmpty();
	}
	
	public String getTitle() {
		return titleField.getText();
	}
	
	public String getUrl() {
		return urlField.getText();
	}
	
	public String getDescription() {
		return descriptionField.getText();
	}
	
	public Subcategory getSubcategory() {
		return (Subcategory) subcategoryField.getSelectedItem();
	}
}
