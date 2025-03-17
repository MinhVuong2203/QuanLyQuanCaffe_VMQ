package Backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Fontend.Staff_Sign;
import Fontend.WelcomeScreen;

public class Listen_WelcomScreen implements ActionListener{
	private WelcomeScreen WS;
	
	public Listen_WelcomScreen(WelcomeScreen lWS) {
		this.WS = lWS;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cm = e.getActionCommand();
		if (cm.equals("VÀO QUÁN")) {
			WS.dispose();
			new Staff_Sign();
		}
	}
	
}
