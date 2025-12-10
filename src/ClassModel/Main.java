package ClassModel;

import DAO.ImplementazioneDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ImplementazioneDAO d = new ImplementazioneDAO();
        ArrayList<Evento> eventi = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Evento evento = new Evento("Sans");
            evento.setIndirizzoSede("Sede");
            evento.setNCivicoSede(10);
            eventi.add(evento);
        }
        try {
            d.addAllEventi(eventi);
            d.printEventi();
            d.disconnect();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
