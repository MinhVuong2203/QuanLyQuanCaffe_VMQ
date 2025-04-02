package Controller;

import View.Window.Login;
import View.Window.WelcomeScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomScreenController implements ActionListener{
	private WelcomeScreen WS;
	
	public WelcomScreenController(WelcomeScreen lWS) {
		this.WS = lWS;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cm = e.getActionCommand();
		if (cm.equals("")) {
			WS.dispose();
			new Login();
		}
	}
	
}
