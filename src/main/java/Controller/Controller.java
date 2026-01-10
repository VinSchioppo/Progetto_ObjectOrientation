package Controller;
import ClassModel.*;
import DAO.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

//Questo enum serve a determinare la specializzazione che un utente assume.
enum Role{
    PARTECIPANTE, ORGANIZZATORE, GIUDICE
}

public class Controller {

    private Utente UtenteCorrente = null;
    private Partecipante PartecipanteCorrente = null;
    private Organizzatore OrganizzatoreCorrente = null;
    private Giudice GiudiceCorrente = null;
    private Role RuoloCorrente = null;
    private static final ImplementazioneDAO DAO = new ImplementazioneDAO();

    //Questo metodo verifica i dati di login inseriti e restituisce true se va a buon fine, altrimenti false.

    public boolean logInUtente(String NomeUtente, String Password) {
        boolean success = false;
        try {
            success = DAO.checkLoginDB(NomeUtente, Password);
            if(success) UtenteCorrente = DAO.getUtenteDB(NomeUtente);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    //Questo metodo verifica che il nome utente scelto durante la registrazione non sia già presente nel Database.
    //Restituisce true se la registrazione viene completata con successo, altrimenti restituisce false.

    public boolean registerUtente(String NomeUtente, String Password) {
        try {
            DAO.addUtenteDB(NomeUtente, Password);
            UtenteCorrente = new Utente(NomeUtente, Password);
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Questo metodo restiuisce una lista contenente l'id e il titolo di tutti gli eventi in cui l'utente ha un ruolo.
    //Ogni elemento della lista segue il formato: IdEvento Titolo

    public ArrayList<String> listaEventiUtente() {
        ArrayList<String> listaEventi = null;
        try {
            if(PartecipanteCorrente == null)
                PartecipanteCorrente = DAO.getPartecipanteDB(UtenteCorrente.getNomeUtente());
            if(OrganizzatoreCorrente == null)
                OrganizzatoreCorrente = DAO.getOrganizzatoreDB(UtenteCorrente.getNomeUtente());
            if(GiudiceCorrente == null)
                GiudiceCorrente = DAO.getGiudiceDB(UtenteCorrente.getNomeUtente());
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        if(PartecipanteCorrente != null){
            listaEventi = new ArrayList<String>();
            PartecipanteCorrente.firstEvento();
            while(PartecipanteCorrente.getEvento() != null){
                String Evento = PartecipanteCorrente.getEvento().getIdEvento() + " " + PartecipanteCorrente.getEvento().getTitolo();
                listaEventi.add(Evento);
                PartecipanteCorrente.nextEvento();
            }
        }
        if(OrganizzatoreCorrente != null){
            if(listaEventi == null){
                listaEventi = new ArrayList<String>();
            }
            OrganizzatoreCorrente.firstEvento();
            while(OrganizzatoreCorrente.getEvento() != null){
                String Evento = OrganizzatoreCorrente.getEvento().getIdEvento() + " " + OrganizzatoreCorrente.getEvento().getTitolo();
                listaEventi.add(Evento);
                OrganizzatoreCorrente.nextEvento();
            }
        }
        if(GiudiceCorrente != null){
            if(listaEventi == null){
                listaEventi = new ArrayList<String>();
            }
            GiudiceCorrente.firstEvento();
            while(GiudiceCorrente.getEvento() != null){
                String Evento = GiudiceCorrente.getEvento().getIdEvento() + " " + GiudiceCorrente.getEvento().getTitolo();
                listaEventi.add(Evento);
                GiudiceCorrente.nextEvento();
            }
        }
        return listaEventi;
    }

    //Questo metodo riceve un input l'id di un evento e determina il ruolo dell'utente.
    //Restituisce il ruolo alla funzione chiamante.

    public Role selectEvento(int idEvento) {

        if(PartecipanteCorrente != null){
            if(PartecipanteCorrente.seekEvento(idEvento) != null){
                RuoloCorrente = Role.PARTECIPANTE;
            }
        }
        if(OrganizzatoreCorrente != null){
            if(OrganizzatoreCorrente.seekEvento(idEvento) != null){
                RuoloCorrente = Role.ORGANIZZATORE;
            }
        }
        if(GiudiceCorrente != null){
            if(GiudiceCorrente.seekEvento(idEvento) != null){
                RuoloCorrente = Role.GIUDICE;
            }
        }
        return RuoloCorrente;
    }

    //Questo metodo restituisce una stringa contenente i dati personali dell'utente.
    //Nel caso in cui non sia salvato nessun dato restituisce null.
    //La stringa segue il formato: FNome MNome LNome DataNascita

    public String datiUtente(){
        if(UtenteCorrente != null){
            if(UtenteCorrente.getFNome() == null && UtenteCorrente.getMNome() == null &&
                    UtenteCorrente.getLNome() == null && UtenteCorrente.getDataNascita() == null)
                return null;
            else
                return UtenteCorrente.getFNome() + " " + UtenteCorrente.getMNome() + " "
                    + UtenteCorrente.getLNome() + " " + UtenteCorrente.getDataNascita();
        }
        return null;
    }

    //Questo metodo permette di aggiornare i dati personali di un utente.
    //Restituisce un boolean e accetta solo una data col formato yyyy-mm-dd, altrimenti lancia una eccezione.

    public boolean inserisciDatiUtente(String FNome, String MNome, String LNome, LocalDate DataNascita){
        try{
                if(UtenteCorrente != null) {
                    UtenteCorrente.setDati(FNome, MNome, LNome, DataNascita);
                    DAO.updatePartecipanteDB(UtenteCorrente.getNomeUtente(), FNome, MNome, LNome, DataNascita);
                }
                if(PartecipanteCorrente != null)
                    PartecipanteCorrente.setDati(FNome, MNome, LNome, DataNascita);
                if(OrganizzatoreCorrente != null) {
                    OrganizzatoreCorrente.setDati(FNome, MNome, LNome, DataNascita);
                    DAO.updateOrganizzatoreDB(OrganizzatoreCorrente.getNomeUtente(), FNome, MNome, LNome, DataNascita);
                }
                if(GiudiceCorrente != null) {
                    GiudiceCorrente.setDati(FNome, MNome, LNome, DataNascita);
                    DAO.updateGiudiceDB(GiudiceCorrente.getNomeUtente(), FNome, MNome, LNome, DataNascita);
                }
        }
        catch (DateTimeParseException e) {
            System.out.println("Errore nel tentativo di copiare la data di nascita.");
            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Questo metodo restiuisce una lista contenente i dati di tutti gli eventi le cui prenotazioni sono attualmente aperte.
    //Ogni elemento della lista segue il formato: IdEvento Titolo IndirizzoSede NCivicoSede DataInizio DataFine
    // MaxIscritti MaxTeam DataInzioReg DataFineReg DescrizioneProblema

    public ArrayList<String> listaEventiAperti(){
        ArrayList<String> listaEventi = null;
        if(UtenteCorrente != null) {
            try {
                ArrayList<Evento> eventi = DAO.getEventiApertiDB(UtenteCorrente.getNomeUtente());
                if (eventi != null) {
                    listaEventi = new ArrayList<String>();
                    for (Evento evento : eventi) {
                        String datiEvento = evento.getIdEvento() + " " + evento.getTitolo()
                                + " " + evento.getIndirizzoSede() + " " + evento.getNCivicoSede()
                                + " " + evento.getDataInizio() + " " + evento.getDataFine()
                                + " " + evento.getMaxIscritti() + " " + evento.getMaxTeam()
                                + " " + evento.getDataInizioReg() + " " + evento.getDataFineReg()
                                + " " + evento.getDescrizioneProblema();
                        listaEventi.add(datiEvento);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaEventi;
    }

    //Questo metodo aggiunge un evento alla lista di eventi a cui è iscritto un partecipante.
    //Restituisce false se fallisce.

    public boolean iscriviEvento(int idEvento){
        Evento evento = null;
        try{
            evento = DAO.getEventoDB(idEvento);
            if(evento != null){
                if(PartecipanteCorrente == null)
                    PartecipanteCorrente = UtenteCorrente.becomePartecipante();
                PartecipanteCorrente.addEvento(evento);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*******************************************************************************************************/

    //Operazioni Partecipante

    //Questo metodo restituisce una stringa con l'id e il nome del team con cui il partecipante è iscritto all'evento.
    //La stringa è fromattata come: IdTeam Nome

    public String teamPartecipante() {
        String infoTeam = null;
        if(PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.seekTeamEvento(PartecipanteCorrente.getEvento().getIdEvento());
            if (team != null) {
                infoTeam = team.getIdTeam() + " " + team.getNome();
            }
        }
        return infoTeam;
    }

    //Questo metodo restiuisce una lista contenente l'id e il testo di ogni progresso pubblicato da un team.
    //Ogni elemento della lista segue il formato: IdProgresso Testo

    public ArrayList<String> listaProgressiTeam(){
        ArrayList<String> listaProgressi = null;
        if(PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getTeam();
            Progresso progresso = team.firstProgresso();
            if (progresso != null) {
                listaProgressi = new ArrayList<String>();
                while (progresso != null) {
                    listaProgressi.add(progresso.getIdProgresso() + " " + progresso.getTestoDocumeto());
                    progresso = team.nextProgresso();
                }
            }
        }
        return listaProgressi;
    }

    //Questo metodo permette di pubblicare un progresso.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean pubblicaProgresso(String Testo){
        Progresso progresso = null;
        if(PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getTeam();
            if (team.getTeamLeader().equals(PartecipanteCorrente.getNomeUtente())) {
                try {
                    progresso = new Progresso(Testo, team.getIdTeam());
                    progresso.setIdProgresso(DAO.addProgressoDB(team.getIdTeam(), Testo));
                    team.addProgresso(progresso);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    //Questo metodo permette al partecipante di creare un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean creaTeamPartecipante(String nome){
        if(PartecipanteCorrente != null) {
            Team team = new Team(-1, nome, PartecipanteCorrente.getNomeUtente());
            team.setEventoIscritto(PartecipanteCorrente.getEvento());
            team.addMembroTeam(PartecipanteCorrente);
            try {
                team.setIdTeam(DAO.addTeamDB(team));
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    //Questo metodo restiuisce una lista contenente l'id e il nome di tutti i team iscritti all'evento.
    //Ogni elemento della lista segue il formato: IdTeam Nome

    public ArrayList<String> listaTeamEvento(){
        ArrayList<String> listaTeam = null;
        if(PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getEvento().firstTeam();
            if (team != null) {
                listaTeam = new ArrayList<String>();
                while (team != null) {
                    listaTeam.add(team.getIdTeam() + " " + team.getNome());
                    team = PartecipanteCorrente.getEvento().nextTeam();
                }
            }
        }
        return listaTeam;
    }

    //Questo metodo fa aggiungere un partecipante alla lista di attesa per un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean joinTeam(int idTeam){
        if(PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getEvento().seekTeam(idTeam);
            team.addRichiesta(PartecipanteCorrente);
            return true;
        }
        return false;
    }

    //Questo metodo restituisce una lista contenente il nome utente di tutti coloro che
    //hanno fatto richiesta di entrare nel team.

    public ArrayList<String> requestTeamNotifications(){
        ArrayList<String> notifications = null;
        if(PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getTeam();
            if (PartecipanteCorrente != null) {
                if (PartecipanteCorrente.getNomeUtente().equals(team.getTeamLeader())) {
                    Partecipante partecipante = team.firstRichiesta();
                    while (partecipante != null) {
                        if (notifications == null)
                            notifications = new ArrayList<String>();
                        notifications.add(partecipante.getNomeUtente());
                        partecipante = team.nextRichiesta();
                    }
                }
            }
        }
        return notifications;
    }

    //Questo metodo riceve in input gli utenti che sono stati accettati e rifiutati dal team.
    //Aggiunge ai membri team, laddove necessario, e li rimuove tutti dalla lista di attesa.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean teamAcceptance(ArrayList<String> accepted, ArrayList<String> refused){
        if(PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getTeam();
            if (accepted != null) {
                for (String s : accepted) {
                    Partecipante partecipante = team.seekRichiesta(s);
                    if (partecipante != null) {
                        team.addMembroTeam(partecipante);
                        team.setRichiestaAnswer(true);
                    }
                }
            }
            if (refused != null) {
                for (String s : refused) {
                    if (team.seekRichiesta(s) != null) {
                        team.setRichiestaAnswer(false);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /*******************************************************************************************************/

    //Operazioni Organizzatore

    //Questo metodo restituisce una stringa contenente tutti i dati dell'evento.
    //La stringa segue il formato: Titolo IndirizzoSede NCivicoSede DataInizio DataFine MaxIscritti MaxTeam
    // DataInizioReg DataFineReg DescrizioneProblema

    public String datiEvento(){
        if(OrganizzatoreCorrente != null) {
            Evento evento = OrganizzatoreCorrente.getEvento();
            if (evento != null) {
                return evento.getTitolo() + " "
                        + evento.getIndirizzoSede() + " " + evento.getNCivicoSede()
                        + " " + evento.getDataInizio() + " " + evento.getDataFine()
                        + " " + evento.getMaxIscritti() + " " + evento.getMaxTeam()
                        + " " + evento.getDataInizioReg() + " " + evento.getDataFineReg()
                        + " " + evento.getDescrizioneProblema();
            }
        }
        return null;
    }

    //Questo metodo permette di impostare i dati dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean inserisciDatiEvento(String indirizzoSede, int nCivico, int maxIscritti, int maxTeam){
        if(OrganizzatoreCorrente != null) {
            Evento evento = OrganizzatoreCorrente.getEvento();
            evento.setIndirizzoSede(indirizzoSede);
            evento.setNCivicoSede(nCivico);
            evento.setMaxIscritti(maxIscritti);
            evento.setMaxTeam(maxTeam);
            return true;
        }
        return false;
    }

    //Questo metodo permette di impostare le date dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean setDateEvento(LocalDate dataInizio, LocalDate dataFine){
        if(OrganizzatoreCorrente != null) {
            try {
                OrganizzatoreCorrente.getEvento().setDate(dataInizio, dataFine);
            } catch (NullPointerException e) {
                System.out.println("Errore nel tentativo di accedere ai dati dell'evento.");
                return false;
            } catch (DateTimeParseException e) {
                System.out.println("Errore nel tentativo di copiare le date dell'evento.");
                return false;
            }
            return true;
        }
        return false;
    }

    //Questo metodo permette di impostare le date di registrazione dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean setRegistrazioniEvento(LocalDate dataInizio, LocalDate dataFine){
        if(OrganizzatoreCorrente != null) {
            try {
                OrganizzatoreCorrente.getEvento().setDateReg(dataInizio, dataFine);
            }
            catch (NullPointerException e) {
                System.out.println("Errore nel tentativo di accedere ai dati dell'evento.");
                return false;
            }
            catch (DateTimeParseException e) {
                System.out.println("Errore nel tentativo di copiare le date registrazione evento.");
                return false;
            }
            return true;
        }
        return false;
    }

    //Questo metodo restituisce una lista contenente il nome utente di tutti i partecipanti all'evento.
    //Ogni elemento della lista segue il formato: NomeUtente

    public ArrayList<String> listaPartecipantiEvento(){
        ArrayList<String> listaPartecipanti = null;
        if(OrganizzatoreCorrente != null) {
            Evento evento = OrganizzatoreCorrente.getEvento();
            if (evento != null) {
                evento.firstPartecipante();
                while (evento.getPartecipante() != null) {
                    if (listaPartecipanti == null)
                        listaPartecipanti = new ArrayList<String>();
                    listaPartecipanti.add(evento.getPartecipante().getNomeUtente());
                    evento.nextPartecipante();
                }
            }
        }
        return listaPartecipanti;
    }

    //Questo metodo aggiunge un partecipante alla lista di coloro che sono stati invitati a essere giudici.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean invitaGiudice(String NomeUtente){
        if(OrganizzatoreCorrente != null){
            Evento evento = OrganizzatoreCorrente.getEvento();
            if(evento != null){
                Partecipante partecipante = evento.seekPartecipante(NomeUtente);
                evento.addInvitoGiudice(partecipante);
                return true;
            }
        }
        return false;
    }

    /*******************************************************************************************************/

    //Operazioni Giudice

    //Questo metodo permette a un giudice di pubblicare la descrizione del problema dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean pubblicaProblema(String problema){
        if(GiudiceCorrente != null){
            Evento evento = GiudiceCorrente.getEvento();
            if(evento != null){
                evento.setDescrizioneProblema(problema);
                return true;
            }
        }
        return false;
    }

    //Questo metodo restituisce un lista con tutti i team iscritti ad un evento giudicato dall'utente.
    //Ogni elemento della lista segue il formato: IdTeam Nome

    public ArrayList<String> listaTeamGiudicati(){
        ArrayList<String> listaTeam = null;
        if(GiudiceCorrente != null){
            Evento evento = GiudiceCorrente.getEvento();
            if(evento != null){
                Team team = evento.firstTeam();
                while(team != null){
                    if(listaTeam == null)
                        listaTeam = new ArrayList<String>();
                    listaTeam.add(team.getIdTeam() + " " + team.getNome());
                    team = evento.nextTeam();
                }
            }
        }
        return listaTeam;
    }

    //Questo metodo permette a un giudice di attribuire un voto ad un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean giveVotoTeam(int idTeam, int valore){
        if(GiudiceCorrente != null) {
            Evento evento = GiudiceCorrente.getEvento();
            if (evento != null) {
                Team team = evento.seekTeam(idTeam);
                if (team != null) {
                    team.addVoto(new Voto(team.getIdTeam(), valore, GiudiceCorrente.getNomeUtente()));
                    return true;
                }
            }
        }
        return false;
    }

    //Questo metodo restituisce una lista contenente tutti i voti attribuire dall'utente in qualità di giudice.
    //Ogni elemento della lista segue il formato: IdTeam Valore

    public ArrayList<String> listaVotiTeam(){
        ArrayList<String> listaVoti = null;
        if(GiudiceCorrente != null) {
            try {
                ArrayList<Voto> voti = DAO.getAllVotiDB(GiudiceCorrente.getNomeUtente());
                if (voti != null) {
                    for (Voto voto : voti) {
                        if (listaVoti == null)
                            listaVoti = new ArrayList<String>();
                        listaVoti.add(voto.getIdTeam() + " " + voto.getValore());
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaVoti;
    }

    //Questo metodo restituisce un lista con tutti i progressi di un team giudicato dell'utente.
    //Ogni elemento della lista segue il formato: IdProgresso TestoDocumento

    public ArrayList<String> listaProgressiTeamGiudicato(int idTeam){
        ArrayList<String> listaProgressi = null;
        if(GiudiceCorrente != null) {
            Evento evento = GiudiceCorrente.getEvento();
            if(evento != null){
                Team team = evento.seekTeam(idTeam);
                if(team != null){
                    Progresso progresso = team.firstProgresso();
                    while(progresso != null) {
                        if (listaProgressi == null)
                            listaProgressi = new ArrayList<String>();
                        listaProgressi.add(progresso.getIdProgresso() + " " + progresso.getTestoDocumeto());
                        progresso = team.nextProgresso();
                    }
                }
            }
        }
        return listaProgressi;
    }

    //Questo metodo permette a un giudice di commentare il progresso di un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean commentaProgresso(int idProgresso, String commento){
        if(GiudiceCorrente != null) {
            Evento evento = GiudiceCorrente.getEvento();
            if(evento != null){
                Team team = evento.getTeam();
                if(team != null){
                    Progresso progresso = team.seekProgresso(idProgresso);
                    if(progresso != null) {
                        progresso.addCommento(new Commento(progresso.getIdProgresso(), commento, GiudiceCorrente.getNomeUtente()));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Questo metodo restituisce un lista con tutti i commenti pubblicati da un giudice.
    //Ogni elemento della lista segue il formato: IdProgresso Testo

    public ArrayList<String> listaCommenti(){
        ArrayList<String> listaCommenti = null;
        if(GiudiceCorrente != null) {
            try{
                ArrayList<Commento> commenti = DAO.getAllCommentiDB(GiudiceCorrente.getNomeUtente());
                if (commenti != null) {
                    for (Commento commento : commenti) {
                        if (listaCommenti == null)
                            listaCommenti = new ArrayList<String>();
                        listaCommenti.add(commento.getIdProgresso() + " " + commento.getTesto());
                    }
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return listaCommenti;
    }
}