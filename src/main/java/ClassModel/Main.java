package ClassModel;

import DAO.ImplementazioneDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;


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
        }*/
        try {
            /*
            Evento e = d.getEventoDB(1);
            Giudice g = d.getGiudiceDB("jud1", e);
            */
            int result = d.addEventoDB("Sans", "Sans Street", 34);
            d.updateEventoDB(20, LocalDate.parse("2025-12-11"), LocalDate.parse("2025-12-15"), 100, 10, LocalDate.parse("2025-10-11"), LocalDate.parse("2025-11-11"), "Inventare il viaggio nel tempo.");
            /*
            d.addAllEventiDB(eventi);
            d.printEventi();
            System.out.println(g.getNomeUtente() + "\n" + g.getPasswordUtente() + "\n" + g.getFNome() + "\n" + g.getMNome() + "\n" + g.getIdEventoGiudicato());
            Team t = d.getTeamDB(1);
            t.printMembri();
             */
            d.disconnect();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
