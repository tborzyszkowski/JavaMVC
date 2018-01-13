package mvc.view.toolbar.button;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.dao.DAOFactory;
import mvc.observer.toolbar.DatabaseChangeListener;
import mvc.observer.toolbar.DatabaseChangeSubject;

public final class SelectDatabaseButton extends JButton implements ActionListener, DatabaseChangeSubject {
	private static final long serialVersionUID = -3802417046411311406L;
	private static final Logger log = LoggerFactory.getLogger(SelectDatabaseButton.class);
	private static final String iconName = "toolbar_database.png";
	
	private List<DatabaseChangeListener> dbChangedListeners = new LinkedList<>();
	private JPopupMenu popup = null;
	
	public SelectDatabaseButton(Dimension size) {
		log.info("Create select database button");
		
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
		
		ActionListener radioAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String source = e.getActionCommand();
				log.debug("Selected radio: {}", source);
				DAOFactory.setDefaultFactory(Integer.parseInt(source));
				databaseChanged();
			}
		};
		
		JRadioButton sqlite = new JRadioButton("SqLite", true);
		sqlite.setActionCommand(String.valueOf(DAOFactory.SQLITE));
		sqlite.addActionListener(radioAction);
		
		JRadioButton mysql = new JRadioButton("My-SQL");
		mysql.setActionCommand(String.valueOf(DAOFactory.MYSQL));
		mysql.addActionListener(radioAction);
		
		JRadioButton postg = new JRadioButton("PostgreSQL");
		postg.setActionCommand(String.valueOf(DAOFactory.POSTGRES));
		postg.addActionListener(radioAction);
		
		ButtonGroup group = new ButtonGroup();
		group.add(sqlite);
		group.add(mysql);
		group.add(postg);

		popup = new JPopupMenu();
		popup.add(sqlite);
		popup.add(mysql);
		popup.add(postg);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.debug("Pressed button");
		popup.show(this, 0, 25);
	}

	@Override
	public void addDatabaseChangeListener(DatabaseChangeListener listener) {
		log.debug("Add new listener");
		dbChangedListeners.add(listener);
	}

	@Override
	public void removeDatabaseChangeListener(DatabaseChangeListener listener) {
		log.debug("Remove listener");
		dbChangedListeners.remove(listener);
	}

	@Override
	public void databaseChanged() {
		log.debug("Call {} database changed listeners", dbChangedListeners.size());
		for(DatabaseChangeListener listener : dbChangedListeners) listener.onDatabaseChange();
	}
}
