package Backend;

import Fontend.Staff_Sign;
import Fontend.WelcomeScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Listen_WelcomScreen implements ActionListener{
	private WelcomeScreen WS;
	
	public Listen_WelcomScreen(WelcomeScreen lWS) {
		this.WS = lWS;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cm = e.getActionCommand();
		if (cm.equals("")) {
			WS.dispose();
			new Staff_Sign();
		}
	}
	
}
