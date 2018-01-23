package mvc.view;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import mvc.controller.KontaktController;
import mvc.model.Kontakt;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class KontaktView extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Object[] COLUMN_NAMES = {"ID","Imie", "Nazwisko", "Adres", "Kraj", "Numer", "Operator"};
    private static final Object[] COLUMN_NAMES_NUMBER = {"ID", "Kontakt ID", "Numer", "Operator"};

    private KontaktController controller;

    // Etykiety w oknie glownym
    private JLabel lId, lImie, lNazwisko, lAdres, lKraj, lNumer, lOperator, lDodajId, lEdytujKontaktId, lDodajNumer, lDodajOperator, lEdytujNumer, lEdytujOperator;
    // Pola tekstowe w oknie edycji
    private JTextField tfId, tfImie, tfNazwisko, tfAdres, tfKraj, tfNumer, tfOperator, tfDodajId, tfEdytujKontaktId, tfDodajNumer, tfDodajOperator, tfEdytujNumer, tfEdytujOperator;
    // Tabela w oknie glownym
    private JTable table, tableNumber;
    private DefaultTableModel tableModel, tableModelNumber;
    //PANEL
    private JPanel tablePanel, menuPanel;
    private JButton dodajKontakt, edytujKontakt, usunKontakt, dodajNumer, edytujNumer, usunNumer, zapisz_edycje, utworz_tabele;
    int location_x = 1500;
    int location_y = 50;

    public KontaktView() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 900);
        this.setTitle("Kontakty");
        this.setLocation(location_x,location_y);
        this.setResizable(false);


        tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createTitledBorder("Lista"));
        // Parametry tabeli
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 22));
        table.setRowHeight(table.getRowHeight() + 20);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(165);
        table.getColumnModel().getColumn(3).setMinWidth(0);
        table.getColumnModel().getColumn(3).setMaxWidth(0);
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);
        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setMaxWidth(0);
        table.getColumnModel().getColumn(6).setMinWidth(0);
        table.getColumnModel().getColumn(6).setMaxWidth(0);

        JScrollPane scrollPane = new JScrollPane(table, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(380, 650));
        tablePanel.add(scrollPane);
        this.add(tablePanel);

        // MENU PRZYCISKI
        menuPanel = new JPanel();
        menuPanel.setBorder(BorderFactory.createTitledBorder("Narzędzia"));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(220,180));
        this.add(menuPanel, BorderLayout.SOUTH);

        menu_przycisk();
        szczegoly_przycisk();
        baza_przycisk();


        /**Set all the Components Visible.
         * If it is set to "false", the components in the container will not be visible.
         */
        setVisible(true);
    }

    public void menu_przycisk() {
        JButton menu_btn = new JButton("Menu");
        menu_btn.setMaximumSize(new Dimension(150,50));
        menu_btn.setAlignmentX(CENTER_ALIGNMENT);
        menu_btn.setFont(new Font("Arial", Font.BOLD,16));
        menuPanel.add(Box.createRigidArea(new Dimension(0,15)));
        menuPanel.add(menu_btn);

        menu_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu(true);
            }
        });

    }

    public void menu(boolean aktywator) {
        // Okno menu
        JFrame oknoMenu = new JFrame("Menu");
        oknoMenu.setSize(400,350);
        oknoMenu.setLocation(location_x-880,location_y);
        oknoMenu.setVisible(aktywator);
        oknoMenu.setResizable(false);

        // Panel okna menu
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(300,200));
        panel.setBorder(BorderFactory.createTitledBorder("Menu"));

        // Przycisk Dodaj Kontakt
        dodajKontakt = new JButton("Dodaj kontakt");
        dodajKontakt.setMaximumSize(new Dimension(200,50));
        dodajKontakt.setFont(new Font("Arial", Font.BOLD,16));
        dodajKontakt.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0,15)));
        panel.add(dodajKontakt);

        dodajKontakt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dodaj(true);
            }
        });

        // Przycisk Edytuj Kontakt
        edytujKontakt = new JButton("Edytuj kontakt");
        edytujKontakt.setMaximumSize(new Dimension(200,50));
        edytujKontakt.setFont(new Font("Arial", Font.BOLD,16));
        edytujKontakt.setAlignmentX(CENTER_ALIGNMENT);
        //edytujKontakt.setEnabled(false);
        panel.add(Box.createRigidArea(new Dimension(0,15)));
        panel.add(edytujKontakt);

        edytujKontakt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                edytuj(true);
                zaznaczenie_edycja_kontaktu();
            }
        });

        // Przycisk Usun Kontakt
        usunKontakt = new JButton("Usuń kontakt");
        usunKontakt.setMaximumSize(new Dimension(200,50));
        usunKontakt.setFont(new Font("Arial", Font.BOLD,16));
        usunKontakt.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0,15)));
        panel.add(usunKontakt);

        usunKontakt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >=0 ) {
                    Integer kontaktId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    controller.deleteKontakt(kontaktId);
                }
            }
        });

        // Utwórz tabele
        utworz_tabele = new JButton("Utwórz tabele");
        utworz_tabele.setMaximumSize(new Dimension(200,50));
        utworz_tabele.setFont(new Font("Arial", Font.BOLD,16));
        utworz_tabele.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0,15)));
        panel.add(utworz_tabele);

        utworz_tabele.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.utworzTabele();
                utworz_tabele.setEnabled(false);
            }
        });

        oknoMenu.add(panel, BorderLayout.CENTER);
    }

    public void szczegoly_przycisk() {
        JButton szczegoly = new JButton("Szczegóły");
        szczegoly.setFont(new Font("Arial", Font.BOLD,16));
        szczegoly.setMaximumSize(new Dimension(150,50));
        szczegoly.setAlignmentX(CENTER_ALIGNMENT);
        menuPanel.add(Box.createRigidArea(new Dimension(0,15)));
        menuPanel.add(szczegoly);

        szczegoly.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                szczegoly_tabela(true);
                zaznaczenie_szczegoly();
            }
        });

    }

    public void szczegoly_tabela(boolean aktywator) {
        JFrame oknoSzczegoly = new JFrame("Szczegóły");
        oknoSzczegoly.setSize(480,650);
        oknoSzczegoly.setLocation(location_x-480,location_y);
        oknoSzczegoly.setResizable(false);
        oknoSzczegoly.setVisible(aktywator);

        JPanel panelSzczegoly1 = new JPanel();
        panelSzczegoly1.setBorder(BorderFactory.createTitledBorder("Tabela"));

        tableModelNumber = new DefaultTableModel(COLUMN_NAMES_NUMBER,0);
        tableNumber = new JTable(tableModelNumber);
        tableNumber.setFont(new Font("Arial", Font.PLAIN, 18));
        tableNumber.setRowHeight(table.getRowHeight() + 10);
        tableNumber.setAutoCreateRowSorter(true);
        //tableNumber.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane suwak = new JScrollPane(tableNumber);
        suwak.setPreferredSize(new Dimension(450,250));
        panelSzczegoly1.add(suwak);

        JPanel panelSzczegoly2 = new JPanel();
        panelSzczegoly2.setBorder(BorderFactory.createTitledBorder("Adres"));

        lKraj = new JLabel("Kraj", SwingConstants.CENTER);
        panelSzczegoly2.add(lKraj);
        lAdres = new JLabel("Adres", SwingConstants.CENTER);
        panelSzczegoly2.add(lAdres);

        JPanel panelSzczegoly3 = new JPanel();
        panelSzczegoly3.setBorder(BorderFactory.createTitledBorder("Narzędzia"));

        // Przycisk Dodaj Numer
        dodajNumer = new JButton("Dodaj numer");
        dodajNumer.setMaximumSize(new Dimension(200,50));
        dodajNumer.setFont(new Font("Arial", Font.BOLD,16));
        dodajNumer.setAlignmentX(CENTER_ALIGNMENT);
        dodajNumer.setEnabled(false);
        //panelSzczegoly3.add(Box.createRigidArea(new Dimension(0,15)));
        panelSzczegoly3.add(dodajNumer);


        dodajNumer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dodaj_numer(true);

            }
        });

        // Przycisk Edytuj Numer
        edytujNumer = new JButton("Edytuj numer");
        edytujNumer.setMaximumSize(new Dimension(200,50));
        edytujNumer.setFont(new Font("Arial", Font.BOLD,16));
        edytujNumer.setAlignmentX(CENTER_ALIGNMENT);
        edytujNumer.setEnabled(false);
        //panelSzczegoly3.add(Box.createRigidArea(new Dimension(0,15)));
        panelSzczegoly3.add(edytujNumer);

        edytujNumer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                edytuj_numer(true);
                zaznaczenie_edycja_numeru();
            }
        });

        // Przycisk Usuń Numer
        usunNumer = new JButton("Usuń numer");
        usunNumer.setMaximumSize(new Dimension(200,50));
        usunNumer.setFont(new Font("Arial", Font.BOLD,16));
        usunNumer.setAlignmentX(CENTER_ALIGNMENT);
        usunNumer.setEnabled(false);
        //panelSzczegoly3.add(Box.createRigidArea(new Dimension(0,15)));
        panelSzczegoly3.add(usunNumer);

        usunNumer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableNumber.getSelectedRow();
                if (selectedRow >=0 ) {
                    Integer numerId = (Integer) tableModelNumber.getValueAt(selectedRow, 0);
                    Integer kontaktId = (Integer) tableModelNumber.getValueAt(selectedRow,1);
                    controller.deleteNumer(numerId);
                    controller.viewAllKontaktNumbers(kontaktId);
                }
            }
        });



        oknoSzczegoly.add(panelSzczegoly1,BorderLayout.NORTH);
        oknoSzczegoly.add(panelSzczegoly2);
        oknoSzczegoly.add(panelSzczegoly3,BorderLayout.SOUTH);
    }

    public void baza_przycisk() {
        String[] lista_baz = {"MYSQL", "SQLITE"};
        JComboBox comboBox = new JComboBox(lista_baz);
        comboBox.setMaximumSize(new Dimension(200,50));
        comboBox.setFont(new Font("Arial", Font.BOLD,16));
        comboBox.setAlignmentX(CENTER_ALIGNMENT);
        menuPanel.add(Box.createRigidArea(new Dimension(0,15)));
        menuPanel.add(comboBox);

        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String wybrana_baza = (String) comboBox.getSelectedItem();
                if (wybrana_baza == "MYSQL") {
                    controller.chooseDatabase(0);
                } else if (wybrana_baza == "SQLITE") {
                    controller.chooseDatabase(1);
                } else {
                    System.out.println("Blad wyboru bazy danych");
                }
            }
        });
    }

    public void zaznaczenie_edycja_kontaktu() {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    //tfId.setText("" + (Integer) tableModel.getValueAt(selectedRow,0));

                    tfImie.setText((String) tableModel.getValueAt(selectedRow, 1));
                    tfNazwisko.setText((String) tableModel.getValueAt(selectedRow, 2));
                    tfAdres.setText((String) tableModel.getValueAt(selectedRow, 3));
                    tfKraj.setText((String) tableModel.getValueAt(selectedRow,4));
                    //tfNumer.setText("" + (Integer) tableModel.getValueAt(selectedRow,5));
                    //tfOperator.setText((String) tableModel.getValueAt(selectedRow,6));

                }
                zapisz_edycje.setEnabled(true);

            }
        });
    }

    public void zaznaczenie_dodanie_numeru() {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    tfDodajId.setText("" + (Integer) tableModel.getValueAt(selectedRow,0));
                }
            }
        });
    }

    public void zaznaczenie_edycja_numeru() {
        tableNumber.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tableNumber.getSelectedRow();
                if (selectedRow >= 0) {
                    tfId.setText("" + (Integer) tableModelNumber.getValueAt(selectedRow,0));
                    tfEdytujKontaktId.setText("" + (Integer) tableModelNumber.getValueAt(selectedRow, 1));
                    tfEdytujNumer.setText("" + (Integer) tableModelNumber.getValueAt(selectedRow, 2));
                    tfEdytujOperator.setText((String) tableModelNumber.getValueAt(selectedRow, 3));

                }
            }
        });
    }

    public void zaznaczenie_szczegoly() {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    lAdres.setText((String) tableModel.getValueAt(selectedRow,3));
                    lKraj.setText((String) tableModel.getValueAt(selectedRow, 4) + ",");
                    Integer kontaktId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    controller.viewAllKontaktNumbers(kontaktId);
                    dodajNumer.setEnabled(true);
                    edytujNumer.setEnabled(true);
                    usunNumer.setEnabled(true);

                }
            }
        });
    }


    public void dodaj(boolean aktywator) {
        JFrame okno=new JFrame("Dodaj kontakt");
        okno.setSize(400,550);
        okno.setLocation(location_x-880,400);
        okno.setVisible(aktywator);
        okno.setResizable(false);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.setBorder(BorderFactory.createTitledBorder("Dodaj kontakt"));
        //panel1.setLayout(new GridLayout(6,1));
        tfImie = new JTextField(20);
        tfImie.setFont(new Font("Arial", Font.BOLD,14));
        tfImie.setPreferredSize(new Dimension(100,50));
        lImie = new JLabel("Imie", SwingConstants.RIGHT);
        panel1.add(lImie);
        panel1.add(tfImie);

        tfNazwisko = new JTextField(20);
        tfNazwisko.setFont(new Font("Arial", Font.BOLD,14));
        tfNazwisko.setPreferredSize(new Dimension(100,50));
        lNazwisko = new JLabel("Nazwisko", SwingConstants.RIGHT);
        panel1.add(lNazwisko);
        panel1.add(tfNazwisko);

        tfAdres = new JTextField(20);
        tfAdres.setFont(new Font("Arial", Font.BOLD,14));
        tfAdres.setPreferredSize(new Dimension(100,50));
        lAdres = new JLabel("Adres", SwingConstants.RIGHT);
        panel1.add(lAdres);
        panel1.add(tfAdres);

        tfKraj = new JTextField(20);
        tfKraj.setFont(new Font("Arial", Font.BOLD,14));
        tfKraj.setPreferredSize(new Dimension(100,50));
        lKraj = new JLabel("Kraj", SwingConstants.RIGHT);
        panel1.add(lKraj);
        panel1.add(tfKraj);

        tfNumer = new JTextField(20);
        tfNumer.setFont(new Font("Arial", Font.BOLD,14));
        tfNumer.setPreferredSize(new Dimension(100,50));
        lNumer = new JLabel("Numer", SwingConstants.RIGHT);
        panel1.add(lNumer);
        panel1.add(tfNumer);

        tfOperator = new JTextField(20);
        tfOperator.setFont(new Font("Arial", Font.BOLD,14));
        tfOperator.setPreferredSize(new Dimension(100,50));
        lOperator = new JLabel("Operator", SwingConstants.RIGHT);
        panel1.add(lOperator);
        panel1.add(tfOperator);

        okno.add(panel1, BorderLayout.CENTER);

        JButton zapisz = new JButton("Zapisz");
        zapisz.setFont(new Font("Arial", Font.BOLD,14));
        JPanel panel2 = new JPanel();
        panel2.add(zapisz);
        okno.add(panel2, BorderLayout.SOUTH);

        zapisz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Kontakt kontakt = new Kontakt(
                        tfImie.getText(),
                        tfNazwisko.getText(),
                        tfAdres.getText(),
                        tfKraj.getText(),
                        Integer.parseInt(tfNumer.getText()),
                        tfOperator.getText());
                controller.insertKontakt(kontakt);

                // Zamknie okno tuz po zapisaniu
                okno.dispose();
            }
        });

    }

    public void edytuj(boolean aktywator) {
        JFrame okno=new JFrame("Edytuj kontakt");
        okno.setSize(400,550);
        okno.setLocation(location_x-880,400);
        okno.setVisible(aktywator);
        okno.setResizable(false);


        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.setBorder(BorderFactory.createTitledBorder("Edytuj kontakt"));
        //panel1.setLayout(new GridLayout(3,3));
        tfImie = new JTextField(20);
        tfImie.setFont(new Font("Arial", Font.BOLD,14));
        tfImie.setPreferredSize(new Dimension(100,50));
        panel1.add(new JLabel("Imie", SwingConstants.RIGHT));
        panel1.add(tfImie);

        tfNazwisko = new JTextField(20);
        tfNazwisko.setFont(new Font("Arial", Font.BOLD,14));
        tfNazwisko.setPreferredSize(new Dimension(100,50));
        panel1.add(new JLabel("Nazwisko", SwingConstants.RIGHT));
        panel1.add(tfNazwisko);

        tfAdres = new JTextField(20);
        tfAdres.setFont(new Font("Arial", Font.BOLD,14));
        tfAdres.setPreferredSize(new Dimension(100,50));
        panel1.add(new JLabel("Adres", SwingConstants.RIGHT));
        panel1.add(tfAdres);

        tfKraj = new JTextField(20);
        tfKraj.setFont(new Font("Arial", Font.BOLD,14));
        tfKraj.setPreferredSize(new Dimension(100,50));
        panel1.add(new JLabel("Kraj", SwingConstants.RIGHT));
        panel1.add(tfKraj);

        okno.add(panel1, BorderLayout.CENTER);

        zapisz_edycje = new JButton("Zapisz");
        zapisz_edycje.setFont(new Font("Arial", Font.BOLD,14));
        zapisz_edycje.setEnabled(false);
        JPanel panel2 = new JPanel();
        panel2.add(zapisz_edycje);
        okno.add(panel2, BorderLayout.SOUTH);

        zapisz_edycje.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Integer kontaktId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    Kontakt kontakt = new Kontakt(
                            kontaktId,
                            tfImie.getText(),
                            tfNazwisko.getText(),
                            tfAdres.getText(),
                            tfKraj.getText());
                    controller.updateKontakt(kontakt);
                    System.out.println(kontaktId);
                }

                // Zamknie okno tuz po zapisaniu
                okno.dispose();
            }
        });

    }


    public void dodaj_numer(boolean aktywator) {
        JFrame okno=new JFrame("Dodaj numer");
        okno.setSize(480,250);
        okno.setLocation(location_x-480,700);
        okno.setVisible(aktywator);
        okno.setResizable(false);


        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        //panel1.setPreferredSize(new Dimension(100,200));
        panel1.setBorder(BorderFactory.createTitledBorder("Dodaj numer"));
        //panel1.setLayout(new GridLayout(3,3));

        tfDodajId = new JTextField(20);
        tfDodajId.setFont(new Font("Arial", Font.BOLD,14));
        tfDodajId.setPreferredSize(new Dimension(100,50));
        lDodajId = new JLabel("Id", SwingConstants.RIGHT);
        panel1.add(lDodajId);
        panel1.add(tfDodajId);

        tfDodajNumer = new JTextField(20);
        tfDodajNumer.setFont(new Font("Arial", Font.BOLD,14));
        tfDodajNumer.setPreferredSize(new Dimension(100,50));
        lDodajNumer = new JLabel("Numer", SwingConstants.RIGHT);
        panel1.add(lDodajNumer);
        panel1.add(tfDodajNumer);

        tfDodajOperator = new JTextField(20);
        tfDodajOperator.setFont(new Font("Arial", Font.BOLD,14));
        tfDodajOperator.setPreferredSize(new Dimension(100,50));
        lDodajOperator = new JLabel("Operator", SwingConstants.RIGHT);
        panel1.add(lDodajOperator);
        panel1.add(tfDodajOperator);

        okno.add(panel1);

        JButton zapisz = new JButton("Zapisz");
        zapisz.setFont(new Font("Arial", Font.BOLD,14));
        JPanel panel2 = new JPanel();
        panel2.add(zapisz);
        okno.add(panel2, BorderLayout.SOUTH);

        zapisz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Kontakt numer = new Kontakt(
                        Integer.parseInt(tfDodajId.getText()),
                        Integer.parseInt(tfDodajNumer.getText()),
                        tfDodajOperator.getText());
                controller.insertNumer(numer);
                controller.viewAllKontaktNumbers(Integer.parseInt(tfDodajId.getText()));
                // Zamknie okno tuz po zapisaniu
                okno.dispose();
            }
        });


        zaznaczenie_dodanie_numeru();
    }

    public void edytuj_numer(boolean aktywator) {
        JFrame okno=new JFrame("Edytuj numer");
        okno.setSize(480,250);
        okno.setLocation(location_x-480,700);
        okno.setVisible(aktywator);
        okno.setResizable(false);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.setBorder(BorderFactory.createTitledBorder("Edytuj numer"));
        tfId = new JTextField(20);
        tfId.setFont(new Font("Arial", Font.BOLD,14));
        tfId.setPreferredSize(new Dimension(100,50));
        lId = new JLabel("Id", SwingConstants.RIGHT);
        panel1.add(lId);
        panel1.add(tfId);

        tfEdytujKontaktId = new JTextField(20);
        tfEdytujKontaktId.setFont(new Font("Arial", Font.BOLD,14));
        tfEdytujKontaktId.setPreferredSize(new Dimension(100,50));
        lEdytujKontaktId = new JLabel("Kontakt ID", SwingConstants.RIGHT);
        panel1.add(lEdytujKontaktId);
        panel1.add(tfEdytujKontaktId);

        tfEdytujNumer = new JTextField(20);
        tfEdytujNumer.setFont(new Font("Arial", Font.BOLD,14));
        tfEdytujNumer.setPreferredSize(new Dimension(100,50));
        lEdytujNumer = new JLabel("Numer", SwingConstants.RIGHT);
        panel1.add(lEdytujNumer);
        panel1.add(tfEdytujNumer);

        tfEdytujOperator = new JTextField(20);
        tfEdytujOperator.setFont(new Font("Arial", Font.BOLD,14));
        tfEdytujOperator.setPreferredSize(new Dimension(100,50));
        lEdytujOperator = new JLabel("Operator", SwingConstants.RIGHT);
        panel1.add(lEdytujOperator);
        panel1.add(tfEdytujOperator);

        okno.add(panel1, BorderLayout.CENTER);

        JButton zapisz = new JButton("Zapisz");
        zapisz.setFont(new Font("Arial", Font.BOLD,14));
        JPanel panel2 = new JPanel();
        panel2.add(zapisz);
        okno.add(panel2, BorderLayout.SOUTH);

        zapisz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Kontakt numer = new Kontakt(
                        Integer.parseInt(tfId.getText()),
                        Integer.parseInt(tfEdytujKontaktId.getText()),
                        Integer.parseInt(tfEdytujNumer.getText()),
                        tfEdytujOperator.getText());
                controller.updateNumer(numer);
                controller.viewAllKontaktNumbers(Integer.parseInt(tfEdytujKontaktId.getText()));

                // Zamknie okno tuz po zapisaniu
                okno.dispose();
            }
        });



    }

    public void setController(KontaktController controller) {
        this.controller = controller;
    }

    public void refreshKontakty(List<Kontakt> kontakty) {

        if (kontakty.size() > 0) {
            tableModel.getDataVector().clear();
            for (Kontakt kontakt : kontakty) {
                tableModel.addRow(new Object[] {
                        kontakt.getId(),
                        kontakt.getImie(),
                        kontakt.getNazwisko(),
                        kontakt.getAdres(),
                        kontakt.getKraj(),
                        kontakt.getNumer(),
                        kontakt.getOperator()
                });
            }
        } else {
            tableModel.setRowCount(0);
        }
    }

    public void refreshNumery(List<Kontakt> numery) {

        if (numery.size() > 0) {
            tableModelNumber.getDataVector().clear();
            for (Kontakt numer : numery) {
                tableModelNumber.addRow(new Object[] {
                        numer.getNumer_id(),
                        numer.getKontakt_id(),
                        numer.getNumer(),
                        numer.getOperator()
                });
            }
        } else {
            tableModelNumber.setRowCount(0);
        }
    }

}
