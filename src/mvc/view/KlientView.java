package mvc.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import mvc.controller.KlientController;
import mvc.model.Klient;

public class KlientView extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Object[] COLUMN_NAMES = {"ID", "Imie", "Nazwisko", "Miasto", "Rok urodzenia"};

	private KlientController controller;

	private JLabel lImie, lNazwisko, lMiasto, lRokUrodzenia;
	private JTextField tfImie, tfNazwisko, tfMiasto, tfRokUrodzenia;
	private JTable table;
	private DefaultTableModel tableModel;
	
	public KlientView() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(1200, 600);

	    tableModel = new DefaultTableModel(COLUMN_NAMES, 0);
	    table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 26));
        table.setRowHeight(table.getRowHeight() + 16);
	    table.setAutoCreateRowSorter(true);

	    JButton dodajKlienta = new JButton("Dodaj klienta");
	    dodajKlienta.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		Klient klient = new Klient(
	    				tfImie.getText(),
	    				tfNazwisko.getText(),
	    				tfMiasto.getText(),
	    				Integer.parseInt(tfRokUrodzenia.getText()));
	    		controller.insertKlient(klient);
	    	}
	    });
	    
	    JButton edytujKlienta = new JButton("Edytuj klienta");
	    edytujKlienta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					Integer klientId = (Integer) tableModel.getValueAt(selectedRow, 0);
					Klient klient = new Klient(
							klientId,
		    				tfImie.getText(),
		    				tfNazwisko.getText(),
		    				tfMiasto.getText(),
		    				Integer.parseInt(tfRokUrodzenia.getText()));
					controller.updateKlient(klient);
				}
			}
	    });

	    JButton usunKlienta = new JButton("Usuń klienta");
	    usunKlienta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					Integer klientId = (Integer) tableModel.getValueAt(selectedRow, 0);
					controller.deleteKlient(klientId);
				}
			}
	    });
	    
	    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
    				tfImie.setText((String) tableModel.getValueAt(selectedRow, 1));
    				tfNazwisko.setText((String) tableModel.getValueAt(selectedRow, 2));
    				tfMiasto.setText((String) tableModel.getValueAt(selectedRow, 3));
    				tfRokUrodzenia.setText("" + (Integer) tableModel.getValueAt(selectedRow, 4));
				}
			}
	    });
	    
	    JScrollPane scrollPane = new JScrollPane(table);

	    lImie = new JLabel("Imię");
	    lNazwisko = new JLabel("Nazwisko");
	    lMiasto = new JLabel("Miasto");
	    lRokUrodzenia = new JLabel("Rok urodzenia");
	    tfImie = new JTextField(15);
	    tfNazwisko = new JTextField(15);
	    tfMiasto = new JTextField(10);
	    tfRokUrodzenia = new JTextField(4);
	    
	    JPanel inputPanel = new JPanel();
	    inputPanel.add(lImie);
	    inputPanel.add(tfImie);
	    inputPanel.add(lNazwisko);
	    inputPanel.add(tfNazwisko);
	    inputPanel.add(lMiasto);
	    inputPanel.add(tfMiasto);
	    inputPanel.add(lRokUrodzenia);
	    inputPanel.add(tfRokUrodzenia);
	    inputPanel.add(dodajKlienta);
	    inputPanel.add(usunKlienta);
	    inputPanel.add(edytujKlienta);
	    
	    this.add(scrollPane, BorderLayout.CENTER);
	    this.add(inputPanel, BorderLayout.SOUTH);
	}
	
	public void setController(KlientController controller) {
		this.controller = controller;
	}
	
	public void refreshKlienci(List<Klient> klienci) {
		
		if (klienci.size() > 0){
			tableModel.getDataVector().clear();
			for (Klient klient : klienci) {
				tableModel.addRow(new Object[] {klient.getId(), klient.getImie(), klient.getNazwisko(),
						klient.getMiasto(), klient.getRokUrodzenia()});
			}
		} else {
			tableModel.setRowCount(0);
		}
	}
}

