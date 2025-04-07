package Controller.ManagerController;

import View.ManagerView.ManagerTable.AddTableJDialog;
import View.ManagerView.ManagerTable.FixTableDialog;
import View.ManagerView.ManagerTable.MaintenanceDialog;
import View.ManagerView.ManagerTable.TablePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TableManagerController implements ActionListener {
	private TablePanel tablePanel;

	public TableManagerController(TablePanel tablePanel) {
		this.tablePanel = tablePanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Thêm bàn")){
			System.out.println(command);
			AddTableJDialog addTableDialog = new AddTableJDialog(tablePanel.listTable, tablePanel);
			addTableDialog.setVisible(true);

		} else if (command.equals("Sửa bàn")){
			System.out.println(command);
			FixTableDialog fixTableDialog = new FixTableDialog(tablePanel.listTable, tablePanel);
			fixTableDialog.setVisible(true);
		} else if (command.equals("Bảo trì")){
			System.out.println(command);
			MaintenanceDialog maintenanceDialog = new MaintenanceDialog(tablePanel.listTable, tablePanel);
			maintenanceDialog.setVisible(true);
		} 
	}
	
}
