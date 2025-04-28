package Utils;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class file {
    // Hàm loại bỏ thẻ HTML và định dạng lại tiêu đề cột
    private static String cleanColumnName(String columnName) {
        // Loại bỏ thẻ HTML
        String cleaned = columnName.replaceAll("<[^>]+>", "");
        // Thay thế các ký tự xuống dòng HTML bằng ký tự xuống dòng thực tế
        cleaned = cleaned.replaceAll("<br>", "\n");
        return cleaned;
    }

    // Hàm xuất JTable ra PDF
    public static void exportToPDF(JTable table, Component parent) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF File");
            fileChooser.setSelectedFile(new File("table_output.pdf"));
            int userSelection = fileChooser.showSaveDialog(parent);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                Table pdfTable = new Table(table.getColumnCount());
                for (int col = 0; col < table.getColumnCount(); col++) {
                    String columnName = cleanColumnName(table.getColumnName(col));
                    pdfTable.addHeaderCell(new Cell().add(new Paragraph(columnName)));
                }
                for (int row = 0; row < table.getRowCount(); row++) {
                    for (int col = 0; col < table.getColumnCount(); col++) {
                        Object value = table.getValueAt(row, col);
                        pdfTable.addCell(new Cell().add(new Paragraph(value != null ? value.toString() : "")));
                    }
                }

                document.add(pdfTable);
                document.close();

                JOptionPane.showMessageDialog(parent, "PDF exported successfully!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error exporting PDF: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Hàm xuất JTable ra Excel
    public static void exportToExcel(JTable table, Component parent) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Excel File");
            fileChooser.setSelectedFile(new File("table_output.xlsx"));
            int userSelection = fileChooser.showSaveDialog(parent);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Table Data");

                Row headerRow = sheet.createRow(0);
                for (int col = 0; col < table.getColumnCount(); col++) {
                    org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(col);
                    String columnName = cleanColumnName(table.getColumnName(col));
                    cell.setCellValue(columnName);
                }

                for (int row = 0; row < table.getRowCount(); row++) {
                    Row excelRow = sheet.createRow(row + 1);
                    for (int col = 0; col < table.getColumnCount(); col++) {
                        org.apache.poi.ss.usermodel.Cell cell = excelRow.createCell(col);
                        Object value = table.getValueAt(row, col);
                        if (value != null) {
                            if (value instanceof Number) {
                                cell.setCellValue(((Number) value).doubleValue());
                            } else {
                                cell.setCellValue(value.toString());
                            }
                        }
                    }
                }

                try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                    workbook.write(fileOut);
                }
                workbook.close();

                JOptionPane.showMessageDialog(parent, "Excel exported successfully!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error exporting Excel: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}