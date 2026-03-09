package ClassModel;

import java.time.LocalDate;

public class Organizzatore extends Utente {

    public Organizzatore(){}

    public Organizzatore(String NomeUtente, String PasswordUtente) {

        super(NomeUtente, PasswordUtente);

    }

    public void addEvento(Evento evento) {

        super.addEvento(evento);
        evento.setOrganizzatore(this);
    }

}