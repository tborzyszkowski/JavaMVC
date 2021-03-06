package controller;

import java.util.List;

import dao.DAOFactory;
import daoRepo.Repository;
import model.Company;
import model.Model;
import model.Order;
import view.View;


public class Controller {

	public static int activeDB = -1;
	private Repository repo;
	private View view;

	public Controller(View view, Repository model) {
		this.repo = model;
		this.view = view;
		view.setController(this);
	}	

	public boolean connectToDB(int choice) {
		Controller.activeDB = -1;
		DAOFactory dao = DAOFactory.getDatabase(choice);		
		repo.refreshDB(dao);
		Controller.activeDB = choice;
		refreshCompanyList();
		refreshOrdersList();
		return true;
	}
	
	public void insertObject(Model model) {
		repo.insertObject(model);
		if (model instanceof Company) {
			refreshCompanyList();
		} else {
			refreshOrdersList();
		}
	}
		
	public void updateObject(Model model) {
		repo.updateObject(model);
		if (model instanceof Company) {
			refreshCompanyList();
		} else {
			refreshOrdersList();
		}		
	}
	
	public void deleteCompany(int id) {
		repo.deleteCompany(id);
		refreshCompanyList();
		refreshOrdersList();
	}
	
	public void deleteOrder(int id) {
		repo.deleteOrder(id);
	}	

	public void refreshCompanyList() {
		List<Company> companies = repo.getCompanies();
		view.getCompanies(companies);
	}

	public void refreshOrdersList() {
		List<Order> orders = repo.getAllOrders();
		view.getOrders(orders);
	}

	public void getSpecOrders(int companyID) {
		List<Order> specOrders = repo.getSpecOrders(companyID);
		view.getSpecOrders(specOrders);
	}

}
