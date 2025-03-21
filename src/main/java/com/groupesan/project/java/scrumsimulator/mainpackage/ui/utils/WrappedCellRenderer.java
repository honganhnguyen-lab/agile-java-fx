package com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class WrappedCellRenderer extends DefaultTableCellRenderer {
    private final JTextArea txtAr;
    private final JPanel ctnerPnl;

    // code reference for aligning center position ->  https://stackoverflow.com/questions/965023/how-to-wrap-lines-in-a-jtable-cell
    public WrappedCellRenderer() {
        txtAr = new JTextArea();
        txtAr.setLineWrap(true);
        txtAr.setWrapStyleWord(true);
        txtAr.setOpaque(true);
        txtAr.setFont(getFont());
        txtAr.setMargin(new Insets(5, 5, 5, 5));

        ctnerPnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        ctnerPnl.setOpaque(true);
        ctnerPnl.add(txtAr);
    }

    @Override
    public Component getTableCellRendererComponent(JTable tbl, Object val,
                                                   boolean isSelected, boolean hasFocus,
                                                   int r, int c) {
        if (val != null) {
            txtAr.setText(val.toString());
        } else {
            txtAr.setText("");
        }
        txtAr.setSize(tbl.getColumnModel().getColumn(c).getWidth(), Integer.MAX_VALUE);

        if (isSelected) {
            ctnerPnl.setBackground(tbl.getSelectionBackground());
            txtAr.setBackground(tbl.getSelectionBackground());
            txtAr.setForeground(tbl.getSelectionForeground());
        } else {
            ctnerPnl.setBackground(tbl.getBackground());
            txtAr.setBackground(tbl.getBackground());
            txtAr.setForeground(tbl.getForeground());
        }

        return ctnerPnl;
    }
}
