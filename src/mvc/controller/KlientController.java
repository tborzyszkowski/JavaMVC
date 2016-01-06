package mvc.controller;

import java.util.List;

import mvc.model.Klient;
import mvc.repository.KlientRepo;
import mvc.view.KlientView;

public class KlientController {

	private KlientRepo model;
	private KlientView view;
	
	public KlientController(KlientRepo model, KlientView view) {
		this.model = model;
		this.view = view;
		
		view.setController(this);
		
		refreshKlienci();
	}
	
	public void insertKlient(Klient klient) {
		model.insertKlient(klient);
		refreshKlienci();
	}
	
	public void updateKlient(Klient klient) {
		model.updateKlient(klient);
		refreshKlienci();
	}
	
	public void deleteKlient(Integer klientId) {
		model.deleteKlient(klientId);
		refreshKlienci();
	}
	
	private void refreshKlienci() {
		List<Klient> klienci = model.getKlienci();
		view.refreshKlienci(klienci);
	}
}

