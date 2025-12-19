package ClassModel;

import Controller.*;
import DAO.ImplementazioneDAO;

import java.sql.SQLException;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        ImplementazioneDAO d = new ImplementazioneDAO();
        /*Evento eventi = new Evento("Sans", "Sans Street", 34);
        ArrayList<Evento> eventi = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Evento evento = new Evento("Sans");
            evento.setIndirizzoSede("Sede");
            evento.setNCivicoSede(10);
            eventi.add(evento);
        }*//*
        try {

            Evento e = d.getEventoDB(1);
            Giudice g = d.getGiudiceDB("jud1", e);
            int result = d.addEventoDB("Sans", "Sans Street", 34);
            d.updateEventoDB(20, LocalDate.parse("2025-12-11"), LocalDate.parse("2025-12-15"), 100, 10, LocalDate.parse("2025-10-11"), LocalDate.parse("2025-11-11"), "Inventare il viaggio nel tempo.");
            d.addAllEventiDB(eventi);
            d.printEventi();
            System.out.println(g.getNomeUtente() + "\n" + g.getPasswordUtente() + "\n" + g.getFNome() + "\n" + g.getMNome() + "\n" + g.getIdCurrentEvento());
            Team t = d.getTeamDB(1);
            t.printMembri();

            Giudice g = new Giudice("gino", "esposito");
            g.addEvento(new Evento(1));
            g.addEvento(new Evento(2));
            System.out.println("Evento: " + g.getEvento().getIdEvento());
            g.nextEvento();
            System.out.println("Evento: " + g.getEvento().getIdEvento());
            g.previousEvento();
            System.out.println("Evento: " + g.getEvento().getIdEvento());
            if(g.previousEvento() == null) throw new SQLException();
            */
            /*
            Evento evento = d.getEventoDB(11);
            if (evento == null) System.out.println("Evento non trovato");
            d.disconnect();
            if(d.checkLogin("org1", "orgpass1")) System.out.println("Login correto.");
            else System.out.println("Login errato.");

            Utente u = new Utente("alice123", "pwd1");
            ArrayList<Utente> ruoli = d.getAllRuoliDB(u);
            if(ruoli.size() >= 1 && ruoli.get(0) != null){System.out.println("Partecipante.");}
            if(ruoli.size() >= 2 && ruoli.get(1) != null){System.out.println("Giudice.");}
            if(ruoli.size() >= 3 && ruoli.get(2) != null){System.out.println("Organizzatore.");}

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        */

        Controller controller = new Controller();
        if(controller.logInUtente("alice", "pwd1")) System.out.println("Utente loggato");
        ArrayList<String> list = controller.listaEventiUtente();
        for(String s : list){
            System.out.println(s);
        }
    }
}
