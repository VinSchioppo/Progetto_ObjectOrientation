package GUI;

import ClassModel.Evento;
import Controller.Controller;

import javax.swing.*;
import java.util.ArrayList;

public class CreaTeam extends JDialog {

    private JPanel mainPanel;
    private JTextField NomeTeam;
    private JList<String> ListaEventi;
    private JButton backButton;
    private JButton saveButton;

    private Controller controller;

    private ArrayList<Evento> eventi;

    public CreaTeam(JFrame owner, Controller controller) {

        super(owner, "Crea Team", true);

        this.controller = controller;

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(owner);

        caricaEventi();

        backButton.addActionListener(e -> dispose());

        saveButton.addActionListener(e -> creaTeam());
    }

    /* ====================== EVENTI ====================== */

    private void caricaEventi() {

        DefaultListModel<String> model = new DefaultListModel<>();

        eventi = controller.listaEventiDisponibiliTeam();

        if (eventi != null) {

            for (Evento e : eventi) {

                model.addElement(e.getTitolo());
            }
        }

        ListaEventi.setModel(model);
    }

    /* ====================== CREA TEAM ====================== */

    private void creaTeam() {

        String nomeTeam = NomeTeam.getText().trim();

        int index = ListaEventi.getSelectedIndex();

        if (nomeTeam.isEmpty() || index == -1) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Inserisci nome team e seleziona evento",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Evento evento = eventi.get(index);

        boolean ok = controller.creaTeam(
                nomeTeam,
                evento.getIdEvento()
        );

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Team creato con successo",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore creazione team",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}