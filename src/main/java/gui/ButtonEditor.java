package gui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ButtonEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    private JButton button;
    private JTable table;
    private List<String> eventi;

    public ButtonEditor(JTable table, List<String> eventi) {
        this.table = table;
        this.eventi = eventi;

        button = new JButton("Dettagli");
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value,
            boolean isSelected, int row, int column) {

        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "Dettagli";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int row = table.getSelectedRow();

        if (row < 0 || row >= eventi.size()) {
            JOptionPane.showMessageDialog(
                    table,
                    "Errore nella selezione dell'evento",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            fireEditingStopped();
            return;
        }

        String evento = eventi.get(row);

        // Parsing della stringa
        String[] dati = evento.split(" ");

        // La descrizione è l'ultimo campo
        String descrizione = dati[dati.length - 1];

        if (descrizione == null || descrizione.equalsIgnoreCase("null")) {
            descrizione = "Dettagli non disponibili";
        }

        JOptionPane.showMessageDialog(
                table,
                descrizione,
                "Descrizione dell'evento",
                JOptionPane.INFORMATION_MESSAGE
        );

        fireEditingStopped();
    }
}