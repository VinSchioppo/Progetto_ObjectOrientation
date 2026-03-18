package GUI;

import Controller.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.DefaultCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProgressiGUI {

    private JPanel mainPanel;
    private JTable TabellaProgresso;
    private JLabel voto;
    private JButton backButton;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    private ArrayList<Object[]> progressi;

    public ProgressiGUI(SelectEventoFrame parentFrame, Controller controller, int idTeam) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaTabella(idTeam);
        caricaVoto(idTeam);

        backButton.addActionListener(e -> parentFrame.showTeamGUI());

    }

    /* ========================= TABELLA ========================= */

    //NON PUÒ USARE PROGRESSO
    private void inizializzaTabella(int idTeam) {

        String[] colonne = {
                "Giudice",
                "Data pubblicazione",
                "Dettagli"
        };

        DefaultTableModel model = new DefaultTableModel(colonne, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        progressi = controller.listaProgressiTeam(idTeam);

        if (progressi != null) {

            for (Object[] r : progressi) {

                model.addRow(new Object[]{
                        r[0],
                        r[1],
                        "Dettagli"
                });
            }
        }

        TabellaProgresso.setModel(model);

        TabellaProgresso.setRowHeight(30);
        TabellaProgresso.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        TabellaProgresso.getColumnModel().getColumn(0).setCellRenderer(center);
        TabellaProgresso.getColumnModel().getColumn(1).setCellRenderer(center);

        TabellaProgresso.getColumn("Dettagli")
                .setCellRenderer(new ButtonRenderer());

        TabellaProgresso.getColumn("Dettagli")
                .setCellEditor(new ButtonEditor(new JCheckBox(), progressi));
    }

    /* ========================= VOTO ========================= */

    //I VOTI NON FUNZIONANO COSÌ
    private void caricaVoto(int idTeam) {

        Integer v = controller.getVotoTeam(idTeam);

        if (v != null) {
            voto.setText("Voto: " + v);
        } else {
            voto.setText("Nessun voto assegnato");
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    /* ===================== BUTTON RENDERER =================== */


    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setText("Dettagli");
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            return this;
        }
    }

    /* ====================== BUTTON EDITOR ==================== */

    class ButtonEditor extends DefaultCellEditor {

        private JButton button;
        private ArrayList<Object[]> progressi;
        private int row;

        public ButtonEditor(JCheckBox checkBox, ArrayList<Object[]> progressi) {

            super(checkBox);

            this.progressi = progressi;

            button = new JButton("Dettagli");

            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    String testo = (String) progressi.get(row)[2];

                    JTextArea area = new JTextArea(testo);
                    area.setLineWrap(true);
                    area.setWrapStyleWord(true);
                    area.setEditable(false);

                    JScrollPane scroll = new JScrollPane(area);
                    scroll.setPreferredSize(new Dimension(400,200));

                    JOptionPane.showMessageDialog(
                            button,
                            scroll,
                            "Commento del giudice",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {

            this.row = row;
            return button;
        }
    }
}