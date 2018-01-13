package mvc.view.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.model.Url;
import mvc.observer.url.select.UrlSelectListener;
import mvc.observer.url.select.UrlSelectSubject;
import mvc.observer.url.update.UrlUpdateListener;

public final class UrlView extends JPanel implements UrlSelectSubject, UrlUpdateListener {
	private static final long serialVersionUID = -4908801645938833417L;
	private static final Logger log = LoggerFactory.getLogger(UrlView.class);
	
	private final int rowHeight = 20;
	private final Color backgroundColor = new Color(248, 248, 248);
	
	private List<UrlSelectListener> urlSelectListeners = new LinkedList<>();
	private UrlTableModel tableModel = null;
	private JTable table = null;
	private JScrollPane tableScroll = null;
	
	public UrlView(int width, int height) {
		log.info("Initialize Url view");
		
		setBackground(backgroundColor);
		setPreferredSize(new Dimension(width, height));
		setBorder(new TitledBorder("Urls"));
		setLayout(new BorderLayout(5,5));
				
		initializeTable();
		initializeListSelectionListener();
		setTableStyle();
		setTableColumnsSize();
		add(tableScroll, BorderLayout.CENTER);
	}
	
	private void initializeTable() {
		log.debug("Initialize table");
		
		tableModel = new UrlTableModel();
		table = new JTable(tableModel);
		tableScroll = new JScrollPane(table);
	}
	
	private void initializeListSelectionListener() {
		log.debug("Initialize list selection listener");
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					log.debug("Selected url");
					
					int[] selectedRows = table.getSelectedRows();
					log.debug("Selected rows: {}", selectedRows.length);
					
					if(selectedRows.length > 0) {
						List<Url> selected = new LinkedList<>();
						
						for(int index : selectedRows) {
							log.debug("Selected index: {}", index);
							selected.add(tableModel.getValue(index));
						}
						
						selectUrl(selected);
					}
					else selectNothing();
				}
			}
		});
	}
	
	private void setTableStyle() {
		log.debug("Set table style");
		
		table.setFont(new Font("Arial", Font.PLAIN, rowHeight - 5));
		table.setRowHeight(rowHeight);
		table.setAutoCreateRowSorter(true);
		
		//tableScroll.getViewport().setBackground(Color.GREEN);
	}
	
	private void setTableColumnsSize() {
		log.debug("Set table columns size");

		setColumnWidth(tableModel.getColumnName(0), 30, 60);
		setColumnWidth(tableModel.getColumnName(1), 100, 300);
		setColumnWidth(tableModel.getColumnName(2), 100, 300);
		setColumnWidth(tableModel.getColumnName(3), 10, 200);
	}
	
	private void setColumnWidth(String name, int min, int prefered) {
		log.debug("Set width for column: {} (min={}, prefered={})", name, min, prefered);
		
		table.getColumn(name).setMinWidth(min);
		table.getColumn(name).setPreferredWidth(prefered);
	}
	
	@Override
	public void addUrlSelectListener(UrlSelectListener listener) {
		log.debug("Add new listener");
		urlSelectListeners.add(listener);
	}

	@Override
	public void removeUrlSelectListener(UrlSelectListener listener) {
		log.debug("Remove listener");
		urlSelectListeners.remove(listener);
	}

	@Override
	public void selectUrl(List<Url> urls) {
		log.debug("Call listeners with {} urls", urls.size());
		for(UrlSelectListener listener : urlSelectListeners) listener.onSelectUrl(urls);
	}

	@Override
	public void selectNothing() {
		log.debug("Call listeners that all urls are unselected");
		for(UrlSelectListener listener : urlSelectListeners) listener.onUnselectAllUrls();
	}
	
	@Override
	public void onUrlUpdate(List<Url> urls) {
		if(urls != null) {
			log.debug("Update url list, received {} values", urls.size());
			for(Url url : urls) log.debug("{} {} {} {}", url.getID(), url.getTitle(), url.getUrl(), url.getDescription());
			
			tableModel.setList(urls);
		}
		else tableModel.removeAll();
	}
}
