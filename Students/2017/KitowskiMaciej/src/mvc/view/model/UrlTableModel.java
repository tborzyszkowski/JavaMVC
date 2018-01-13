package mvc.view.model;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.model.Url;

public final class UrlTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 3938608858574512836L;
	private static final Logger log = LoggerFactory.getLogger(UrlTableModel.class);
	private static final String[] columnNames = {"ID", "Title", "Url", "Description"};
	
	private List<Url> urls = new LinkedList<>();
	
	public UrlTableModel() {
		log.info("Initialize model");
		
		fireTableStructureChanged();
	}
	
	public void setList(List<Url> urls) {
		log.debug("Set new list with {} values", urls.size());
		this.urls = urls;
		
		fireTableDataChanged();
	}
	
	public void removeAll() {
		log.debug("Remove all values");
		this.urls = new LinkedList<>();
		
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return urls.size();
	}
	
	@Override
	public String getColumnName(int col) {
	    return columnNames[col];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	}
	
	public Url getValue(int row) {
		if(urls == null || row >= urls.size() || row < 0) {
			log.warn("Wrong getValue row index: {}", row);
			return null;
		}
		else return urls.get(row);
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object toReturn = Object.class;
		
		if(urls.size() > 0) {
			Url url = urls.get(row);
			
			if(col == 0) toReturn = url.getID();
			else if(col == 1) toReturn = url.getTitle();
			else if(col == 2) toReturn = url.getUrl();
			else if(col == 3) {
				if(url.getDescription() == null) toReturn = "";
				else toReturn = url.getDescription();
			}
		}
		
		return toReturn;
	}

}
