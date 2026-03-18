package GUI;

import ClassModel.Evento;
import Controller.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class OrganizzatoreGUI {

    private JPanel mainPanel;
    private JTable table1;
    private JButton setDatiEventoButton;
    private JButton invitaGiudiceButton;
    private JButton backButton;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    private ArrayList<Evento> eventiOrganizzatore;
    private Evento eventoSelezionato = null;

    public OrganizzatoreGUI(SelectEventoFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaTabella();
        inizializzaBottoni();
    }

    /* ============================================================
       ================== TABELLA EVENTI ==========================
       ============================================================ */
    private void inizializzaTabella() {

        String[] colonne = {
                "Titolo",
                "Tipo",
                "Max squadre"
        };

        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        eventiOrganizzatore = controller.listaEventiOrganizzatore();

        if (eventiOrganizzatore != null) {
            for (Evento e : eventiOrganizzatore) {
                String tipo = (e.getMaxTeam() > 1) ? "Team" : "Singolo";
                model.addRow(new Object[]{
                        e.getTitolo(),
                        tipo,
                        e.getMaxIscritti()
                });
            }
        }

        table1.setModel(model);
        table1.setRowHeight(32);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.setRowSelectionAllowed(true);
        table1.setColumnSelectionAllowed(false);

        // ===== CENTRATURA TESTO =====
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table1.getColumnCount(); i++) {
            table1.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        table1.getTableHeader().setReorderingAllowed(false);

        // ===== SELEZIONE RIGA =====
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table1.getSelectedRow();
                if (row != -1) {
                    eventoSelezionato = eventiOrganizzatore.get(row);

                    controller.selezionaEvento(eventoSelezionato); //QUI CON eventoSelezionato nelle parentesi

                    setDatiEventoButton.setEnabled(true);
                    invitaGiudiceButton.setEnabled(true);
                }
            }
        });

        // ===== DOPPIO CLICK → DETTAGLI =====
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table1.getSelectedRow();
                    if (row != -1) {
                        mostraDettagliEvento(eventiOrganizzatore.get(row));
                    }
                }
            }
        });
    }

    /* ============================================================
       ================= DETTAGLI EVENTO ==========================
       ============================================================ */
    private void mostraDettagliEvento(Evento evento) {

        String testo =
                "Titolo: " + evento.getTitolo() + "\n\n" +
                        "Luogo: " + evento.getIndirizzoSede() + " " + evento.getNCivicoSede() + "\n\n" +
                        "Date evento: " + evento.getDataInizio() + " → " + evento.getDataFine() + "\n\n" +
                        "Registrazioni: " + evento.getDataInizioReg() + " → " + evento.getDataFineReg() + "\n\n" +
                        "Max squadre: " + evento.getMaxIscritti() + "\n" +
                        "Membri per squadra: " + evento.getMaxTeam() + "\n\n" +
                        "Descrizione problema:\n" + evento.getDescrizioneProblema();

        JOptionPane.showMessageDialog(
                mainPanel,
                testo,
                "Dettagli evento",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /* ============================================================
       ===================== BOTTONI ==============================
       ============================================================ */
    private void inizializzaBottoni() {

        // Disabilitati finché non selezioni un evento
        setDatiEventoButton.setEnabled(false);
        invitaGiudiceButton.setEnabled(false);

        backButton.addActionListener(e -> parentFrame.showHome());

        setDatiEventoButton.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row != -1) {
                Evento evento = eventiOrganizzatore.get(row);
                controller.selezionaEvento(evento);
                parentFrame.showSetDatiEvento();
            }
        });

        invitaGiudiceButton.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row != -1) {
                Evento evento = eventiOrganizzatore.get(row);
                controller.selezionaEvento(evento);
                parentFrame.showInvitaGiudice();
            }
        });

        // Abilita i bottoni solo quando selezioni una riga
        table1.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table1.getSelectedRow() != -1;
            setDatiEventoButton.setEnabled(selected);
            invitaGiudiceButton.setEnabled(selected);
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}