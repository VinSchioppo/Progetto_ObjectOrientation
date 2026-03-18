package GUI;

import ClassModel.Evento;
import ClassModel.Team;
import Controller.*;

import javax.swing.*;
import java.util.ArrayList;

public class GiudiceGUI {

    private JPanel mainPanel;
    private JButton backButton;
    private JButton saveButton;
    private JList<String> listEventi;
    private JList<String> listTeam;
    private JSpinner voto;
    private JTextPane giudizio;
    private JCheckBox abilitaVoto;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    private ArrayList<Evento> eventiGiudice;
    private Evento eventoSelezionato;
    private ArrayList<Team> teamEvento;

    public GiudiceGUI(SelectEventoFrame parentFrame, Controller controller) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        voto.setModel(new SpinnerNumberModel(1,1,10,1));

        abilitaVoto.addActionListener(e -> {
            voto.setEnabled(abilitaVoto.isSelected());
        });

        voto.setEnabled(false);

        caricaEventi();

        listEventi.addListSelectionListener(e -> {

            if(!e.getValueIsAdjusting()){

                int index = listEventi.getSelectedIndex();

                if(index != -1){

                    eventoSelezionato = eventiGiudice.get(index);

                    caricaTeam(eventoSelezionato.getIdEvento());
                }
            }
        });

        backButton.addActionListener(e -> parentFrame.showHome());

        saveButton.addActionListener(e -> salvaValutazione());

    }

    /* ========================= EVENTI ========================= */

    private void caricaEventi(){

        DefaultListModel<String> model = new DefaultListModel<>();

        eventiGiudice = controller.listaEventiGiudice();

        if(eventiGiudice != null){

            for(Evento e : eventiGiudice){

                model.addElement(e.getTitolo());
            }
        }

        listEventi.setModel(model);
    }

    /* ========================= TEAM ========================= */

    private void caricaTeam(int idEvento){

        DefaultListModel<String> model = new DefaultListModel<>();

        teamEvento = controller.listaTeamEvento(idEvento);

        if(teamEvento != null){

            for(Team t : teamEvento){

                model.addElement(t.getNome());
            }
        }

        listTeam.setModel(model);
    }

    //VA RIPENSATO SENZA IL COMMENTO
    private void salvaValutazione(){

        int index = listTeam.getSelectedIndex();

        if(index == -1){

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Seleziona un team",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Team team = teamEvento.get(index);

        String testo = giudizio.getText();

        Integer votoVal = null;

        if(abilitaVoto.isSelected()){
            votoVal = (Integer) voto.getValue();
        }

        boolean ok = controller.salvaValutazione(
                team.getIdTeam(),
                votoVal,
                testo
        );

        if(ok){

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Valutazione salvata",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

        }else{

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore durante il salvataggio",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}