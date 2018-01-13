package mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.view.MainFrame;

public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		log.info("Start application");
		
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}
}
