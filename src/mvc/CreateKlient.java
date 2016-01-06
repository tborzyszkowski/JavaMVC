package mvc;
/**   
 * @author 
 * Joanna Kowalska: 
 * laboratorium z Programowania obiektowego, studia zaoczne UG, 2014/15
 * 
 * DAO, Repository & log4j by Tomasz Borzyszkowski
 */  
import org.apache.log4j.PropertyConfigurator;

import mvc.controller.KlientController;
import mvc.repository.KlientRepo;
import mvc.view.KlientView;

public class CreateKlient {

	public static void main(String[] args) {
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		KlientRepo model = new KlientRepo();
		KlientView view = new KlientView();
		new KlientController(model, view);
		
		view.setVisible(true);
	}
}
