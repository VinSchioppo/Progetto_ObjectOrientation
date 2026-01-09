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

    public void finalizeDateReg(LocalDate Inizio, LocalDate Fine) {

        Evento EventoOrganizzato = getEvento();
        if (EventoOrganizzato != null) {
            EventoOrganizzato.setDateReg(Inizio, Fine);
        }
    }

    public Giudice selezionaGiudice(Utente utente) {

        Evento EventoOrganizzato = getEvento();
        Giudice giudice = null;
        if (EventoOrganizzato != null) {
            giudice = new Giudice(utente.getNomeUtente(), utente.getPasswordUtente());
            giudice = promuoviUtente(giudice, utente);
            EventoOrganizzato.addGiudice(giudice);
            giudice.addEvento(EventoOrganizzato);
        }
        return giudice;
    }

    public Giudice promuoviUtente(Giudice giudice, Utente utente) {

        giudice.setFNome(utente.getFNome());
        giudice.setMNome(utente.getMNome());
        giudice.setLNome(utente.getLNome());
        giudice.setDataNascita(utente.getDataNascita());
        return giudice;

    }
}