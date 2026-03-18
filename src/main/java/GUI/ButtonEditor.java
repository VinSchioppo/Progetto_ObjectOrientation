package GUI;

import ClassModel.Evento;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ButtonEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    private JButton button;
    private JTable table;
    private ArrayList<Evento> eventi;

    public ButtonEditor(JTable table, ArrayList<Evento> eventi) {
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
        Evento evento = eventi.get(row);

        JOptionPane.showMessageDialog(
                table,
                evento.getDescrizioneProblema(),
                "Descrizione dell'evento",
                JOptionPane.INFORMATION_MESSAGE
        );

        fireEditingStopped();
    }
}