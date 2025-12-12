package ClassModel;

import java.util.ArrayList;
import java.time.LocalDate;

public class Organizzatore extends Utente {

    private Evento EventoOrganizzato = null;

    public Organizzatore(String NomeUtente, String PasswordUtente) {

        super(NomeUtente, PasswordUtente);

    }

    public void selezionaEvento(Evento evento) {

        EventoOrganizzato = evento;
        evento.setOrganizzatore(this);

    }

    public int getIdEventoOrganizzato() {return EventoOrganizzato.getIdEvento();}

    public void finalizeDateReg(LocalDate Inizio, LocalDate Fine) {

        EventoOrganizzato.setDateReg(Inizio, Fine);
    }

    public void apriPrenotazioni() {

        EventoOrganizzato.prenotazioni = true;

    }

    public void chiudiPrenotazioni() {

        EventoOrganizzato.prenotazioni = false;

    }

    public Giudice selezionaGiudice(Utente utente) {

        if(EventoOrganizzato.Giudici == null)
            EventoOrganizzato.Giudici = new ArrayList<Giudice>();

        Giudice giudice = new Giudice(utente.NomeUtente, utente.PasswordUtente);
        giudice = promuoviUtente(giudice, utente);
        EventoOrganizzato.Giudici.add(giudice);
        giudice.setEvento(EventoOrganizzato);
        return giudice;

    }

    public Giudice promuoviUtente(Giudice giudice, Utente utente) {

        giudice.FNome = utente.FNome;
        giudice.MNome = utente.MNome;
        giudice.LNome = utente.LNome;
        giudice.DataNascita = utente.DataNascita;
        return giudice;

    }
}