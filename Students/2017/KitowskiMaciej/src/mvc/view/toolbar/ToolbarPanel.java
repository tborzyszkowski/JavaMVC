package mvc.view.toolbar;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.view.toolbar.button.AddNewButton;
import mvc.view.toolbar.button.DeleteButton;
import mvc.view.toolbar.button.EditButton;
import mvc.view.toolbar.button.SelectDatabaseButton;

public final class ToolbarPanel extends JToolBar {
	private static final long serialVersionUID = 1071937589387532800L;
	private static final Logger log = LoggerFactory.getLogger(ToolbarPanel.class);
	
	private final Color backgroundColor = new Color(220, 220, 220);
	
	private SelectDatabaseButton selectDb = null;
	private DeleteButton delete = null;
	private AddNewButton addNew = null;
	private EditButton edit = null;
	
	public ToolbarPanel(int width, int height) {
		log.info("Initialize toolbar");
		
		setBackground(backgroundColor);
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createEmptyBorder());
		setFloatable(false);
		
		Dimension buttonSize = new Dimension(height - 5, height - 5);
		
		addNew = new AddNewButton(buttonSize);
		edit = new EditButton(buttonSize);
		delete = new DeleteButton(buttonSize);
		selectDb = new SelectDatabaseButton(buttonSize);

		addButtonsToPanel();
	}
	
	private void addButtonsToPanel() {
		log.debug("Add buttons to panel");
		
		add(Box.createHorizontalStrut(5));
		add(addNew);
		
		add(Box.createHorizontalStrut(5));
		add(edit);
		
		add(Box.createHorizontalStrut(5));
		add(delete);
		
		add(Box.createHorizontalStrut(30));
		add(selectDb);
	}
			
	public SelectDatabaseButton getSelectDatabaseButton() {
		return selectDb;
	}
	
	public DeleteButton getDeleteButton() {
		return delete;
	}
	
	public AddNewButton getAddNewButton() {
		return addNew;
	}
	
	public EditButton getEditButton() {
		return edit;
	}
}
