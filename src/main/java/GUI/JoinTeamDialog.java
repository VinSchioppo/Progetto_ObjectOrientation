package GUI;

import ClassModel.Evento;
import ClassModel.Team;
import Controller.Controller;

import javax.swing.*;
import java.util.ArrayList;

public class JoinTeamDialog extends JDialog {

    private JPanel mainPanel;
    private JList<String> listEventi;
    private JList<String> listTeam;
    private JButton saveButton;
    private JButton backButton;

    private Controller controller;

    private ArrayList<Evento> eventi;
    private ArrayList<Team> teamEvento;

    public JoinTeamDialog(JFrame owner, Controller controller) {

        super(owner, "Join Team", true);

        this.controller = controller;

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(owner);

        caricaEventi();

        listEventi.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                caricaTeamEvento();
            }
        });

        saveButton.addActionListener(e -> joinTeam());

        backButton.addActionListener(e -> dispose());
    }

    //NON PUÒ USARE EVENTO
    private void caricaEventi() {

        eventi = controller.listaEventiUtenteCompleti();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (eventi != null) {
            for (Evento e : eventi) {
                model.addElement(e.getTitolo());
            }
        }

        listEventi.setModel(model);
    }

    //NON SI PUÒ USARE TEAM
    private void caricaTeamEvento() {

        int index = listEventi.getSelectedIndex();

        System.out.println("Index evento: " + index);

        if (index == -1)
            return;

        Evento evento = eventi.get(index);

        System.out.println("Evento selezionato: " + evento.getTitolo());
        System.out.println("ID evento GUI: " + evento.getIdEvento());

        teamEvento = controller.listaTeamDisponibili(evento.getIdEvento());

        System.out.println("Team ricevuti: " +
                (teamEvento == null ? "null" : teamEvento.size()));

        DefaultListModel<String> model = new DefaultListModel<>();

        if (teamEvento != null) {
            for (Team t : teamEvento) {
                System.out.println("Team: " + t.getNome());
                model.addElement(t.getNome());
            }
        }

        System.out.println("Team nel model: " + model.size());

        listTeam.setModel(model);
    }

    private void joinTeam() {

        int indexTeam = listTeam.getSelectedIndex();

        if (indexTeam == -1) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Seleziona un team",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Team team = teamEvento.get(indexTeam);

        boolean ok = controller.joinTeam(team.getIdTeam());

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Entrato nel team",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore durante l'operazione",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}