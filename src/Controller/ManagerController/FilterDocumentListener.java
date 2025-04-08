package Controller.ManagerController;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class FilterDocumentListener<T> implements DocumentListener  {

    private JComboBox<String> comboBox;
    private JTable table;
    private JTextField textField;
    private List<T> dataList;
    private String[] columnName;
    private RowDataMapper<T> mapper;
    
    public FilterDocumentListener(JComboBox<String> comboBox, JTable table, JTextField textField, List<T> dataList, String[] columnName, RowDataMapper<T> mapper) {
		this.comboBox = comboBox;
		this.table = table;
		this.textField = textField;
		this.dataList = dataList;
		this.columnName = columnName;
		this.mapper = mapper;
	}

    public interface RowDataMapper<T>{
        Object[] mapRow(T item);
    }

	@Override
    public void insertUpdate(DocumentEvent e) {
        triggerFilter();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        triggerFilter();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        triggerFilter();
    }

    public void triggerFilter(){
        String atb = comboBox.getSelectedItem().toString();
        String value = textField.getText().trim().toLowerCase();

        int column = 0;
        for (int i=0; i<columnName.length; i++){
            if (columnName[i].equals(atb)){
                column = i;
                break;
            }
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear data

        for (T item : dataList){
            Object [] rowObjects = mapper.mapRow(item);
            if (value.isEmpty() || (rowObjects[column] != null && rowObjects[column].toString().toLowerCase().contains(value)))
            model.addRow(rowObjects);
        }
    }
    
}
