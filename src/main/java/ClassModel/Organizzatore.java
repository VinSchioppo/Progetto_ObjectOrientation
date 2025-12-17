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

    public void apriPrenotazioni() {

        Evento EventoOrganizzato = getEvento();
        if (EventoOrganizzato != null) {
            EventoOrganizzato.setPrenotazioni(true);
        }
    }

    public void chiudiPrenotazioni() {

        Evento EventoOrganizzato = getEvento();
        if (EventoOrganizzato != null) {
            EventoOrganizzato.setPrenotazioni(false);
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

        giudice.setFnome(utente.getFNome());
        giudice.setMnome(utente.getMNome());
        giudice.setLnome(utente.getLNome());
        giudice.setDataNascita(utente.getDataNascita());
        return giudice;

    }
}