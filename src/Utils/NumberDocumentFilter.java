package Utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumberDocumentFilter extends DocumentFilter {
    @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            StringBuilder sb = new StringBuilder();
            for (char c : string.toCharArray()) {
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
            super.insertString(fb, offset, sb.toString(), attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
            if (string == null) return;
            StringBuilder sb = new StringBuilder();
            for (char c : string.toCharArray()) {
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
            super.replace(fb, offset, length, sb.toString(), attrs);
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
}
