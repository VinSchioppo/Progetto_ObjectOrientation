package ClassModel;

import DAO.ImplementazioneDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {
        ImplementazioneDAO d = new ImplementazioneDAO();
        Evento eventi = new Evento("Sans");
        /*ArrayList<Evento> eventi = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Evento evento = new Evento("Sans");
            evento.setIndirizzoSede("Sede");
            evento.setNCivicoSede(10);
            eventi.add(evento);
        }*/
        try {
            d.addEventoDB("Sans", "Sans Street", 34, LocalDate.parse("2025-12-11"), LocalDate.parse("2025-12-15"), 100, 10, null, null, "Inventare il viaggio nel tempo.");
            //d.addAllEventiDB(eventi);
            d.printEventi();
            d.disconnect();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
