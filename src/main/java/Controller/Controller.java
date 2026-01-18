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
    private static final ImplementazioneDAO DAO = new ImplementazioneDAO();

    private boolean addOrganizzatore = false;
    private ArrayList<Evento> updateInvitiGiudice = null;
    private ArrayList<Evento> updateOrganizzatoreEvento = null;
    private ArrayList<Evento> updatePartecipanteEvento = null;
    private ArrayList<Team> updateRichiesteTeam = null;
    private ArrayList<Evento> pubblicaProblema = null;
    private ArrayList<Voto> addVoti = null;
    private ArrayList<Commento> addCommenti = null;

    //Questo metodo verifica i dati di login inseriti e restituisce true se va a buon fine, altrimenti false.

    public boolean logInUtente(String NomeUtente, String Password) {
        boolean success = false;
        try {
            success = DAO.checkLoginDB(NomeUtente, Password);
            if(success)
                UtenteCorrente = DAO.getUtenteDB(NomeUtente);
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
    //Ogni elemento della lista segue il formato: Ruolo IdEvento Titolo

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
                String Evento = "Partecipante " + PartecipanteCorrente.getEvento().getIdEvento() + " " + PartecipanteCorrente.getEvento().getTitolo();
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
                String Evento = "Organizzatore " + OrganizzatoreCorrente.getEvento().getIdEvento() + " " + OrganizzatoreCorrente.getEvento().getTitolo();
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
                String Evento = "Giudice " + GiudiceCorrente.getEvento().getIdEvento() + " " + GiudiceCorrente.getEvento().getTitolo();
                listaEventi.add(Evento);
                GiudiceCorrente.nextEvento();
            }
        }
        return listaEventi;
    }

    //Questo metodo riceve un input l'id di un evento e il ruolo del utente in quell'evento e selezione l'evento in memoria.
    //Restituisce true se l'operazione viene completata con successo, altrimenti restituisce false.

    public boolean selectEvento(int idEvento, Role ruolo) {

        if(PartecipanteCorrente != null && ruolo == Role.PARTECIPANTE){
            if(PartecipanteCorrente.seekEvento(idEvento) != null)
                return true;
        }
        if(OrganizzatoreCorrente != null && ruolo == Role.ORGANIZZATORE){
            if(OrganizzatoreCorrente.seekEvento(idEvento) != null)
                return true;
        }
        if(GiudiceCorrente != null && ruolo == Role.GIUDICE){
            if(GiudiceCorrente.seekEvento(idEvento) != null)
                return true;
        }
        return false;
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
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean iscriviEvento(int idEvento){
        Evento evento = null;
        try{
            evento = DAO.getEventoDB(idEvento);
            if(evento != null){
                if(evento.sizePartecipanti() > evento.getMaxIscritti()) {
                    if (PartecipanteCorrente == null)
                        PartecipanteCorrente = UtenteCorrente.becomePartecipante();
                    PartecipanteCorrente.addEvento(evento);
                    if(updatePartecipanteEvento == null)
                        updatePartecipanteEvento = new ArrayList<Evento>();
                    updatePartecipanteEvento.add(evento);
                    return true;
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    //Questo metodo permette ad un utente di creare un evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean creaEvento(String Titolo, String Indirizzo, int NCivico){
        if(UtenteCorrente != null) {
            Evento evento = new Evento(Titolo, Indirizzo, NCivico);
            Organizzatore organizzatore = null;
            if(OrganizzatoreCorrente == null) {
                organizzatore = UtenteCorrente.becomeOrganizzatore();
                addOrganizzatore = true;
            }
            else
                organizzatore = OrganizzatoreCorrente;
            evento.setOrganizzatore(organizzatore);
            try {
                DAO.addEventoDB(evento);
                organizzatore.addEvento(evento);
                OrganizzatoreCorrente = organizzatore;
                if(updateOrganizzatoreEvento == null)
                    updateOrganizzatoreEvento = new ArrayList<Evento>();
                updateOrganizzatoreEvento.add(evento);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //Questo metodo restiuisce una lista contenente i dati di tutti gli eventi
    //per cui un utente è stato invitato a diventare un giudice.
    //Ogni elemento della lista segue il formato: IdEvento Titolo IndirizzoSede NCivicoSede DataInizio DataFine
    // MaxIscritti MaxTeam DataInzioReg DataFineReg

    public ArrayList<String> listaInvitiGiudice() {
        ArrayList<String> invitiGiudice = null;
        if(UtenteCorrente != null) {
            if(PartecipanteCorrente != null) {
                try {
                    DAO.getAllInvitiGiudiceDB(UtenteCorrente, PartecipanteCorrente);
                    Evento evento = UtenteCorrente.firstInvitoGiudiceEvento();
                    while (evento != null) {
                        if (invitiGiudice == null)
                            invitiGiudice = new ArrayList<String>();
                        String datiEvento = evento.getIdEvento() + " " + evento.getTitolo()
                                + " " + evento.getIndirizzoSede() + " " + evento.getNCivicoSede()
                                + " " + evento.getDataInizio() + " " + evento.getDataFine()
                                + " " + evento.getMaxIscritti() + " " + evento.getMaxTeam()
                                + " " + evento.getDataInizioReg() + " " + evento.getDataFineReg();
                        invitiGiudice.add(datiEvento);
                        evento = UtenteCorrente.nextInvitoGiudiceEvento();
                    }
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return invitiGiudice;
    }

    //Questo metodo permette ad un utente di accettare un invito ad essere giudice di un evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean acceptInvitoGiudiceEvento(int idEvento){
        if(UtenteCorrente != null) {
            Evento evento = UtenteCorrente.seekInvitoGiudiceEvento(idEvento);
            if (evento != null) {
                if(UtenteCorrente.getInvitoGiudiceEventoAnswer() == null) {
                    UtenteCorrente.setInvitoGiudiceEventoAnswer(true);
                    evento.seekInvitoGiudice(UtenteCorrente.getNomeUtente());
                    evento.setInvitoGiudiceAnswer(true);
                    if(GiudiceCorrente == null)
                        GiudiceCorrente = UtenteCorrente.becomeGiudice();
                    evento.addGiudice(GiudiceCorrente);
                    GiudiceCorrente.addEvento(evento);
                    if(updateInvitiGiudice == null)
                        updateInvitiGiudice = new ArrayList<Evento>();
                    updateInvitiGiudice.add(evento);
                    return true;
                }
            }
        }
        return false;
    }

    //Questo metodo permette ad un utente di rifiutare un invito ad essere giudice di un evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean refuseInvitoGiudiceEvento(int idEvento){
        if(UtenteCorrente != null) {
            Evento evento = UtenteCorrente.seekInvitoGiudiceEvento(idEvento);
            if (evento != null) {
                if(UtenteCorrente.getInvitoGiudiceEventoAnswer() == null) {
                    UtenteCorrente.setInvitoGiudiceEventoAnswer(false);
                    evento.seekInvitoGiudice(UtenteCorrente.getNomeUtente());
                    evento.setInvitoGiudiceAnswer(false);
                    if(updateInvitiGiudice == null)
                        updateInvitiGiudice = new ArrayList<Evento>();
                    updateInvitiGiudice.add(evento);
                    return true;
                }
            }
        }
        return false;
    }

    /*******************************************************************************************************/

    //Operazioni Partecipante

    //Questo metodo restituisce una stringa con l'id e il nome del team con cui il partecipante è iscritto all'evento.
    //La stringa è fromattata come: IdTeam Nome TeamLeader

    public String teamPartecipante() {
        String infoTeam = null;
        if(PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.seekTeamEvento(PartecipanteCorrente.getEvento().getIdEvento());
            if (team != null) {
                infoTeam = team.getIdTeam() + " " + team.getNome() + " " + team.getTeamLeader();
            }
        }
        return infoTeam;
    }

    //Questo metodo permette al partecipante di creare un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean creaTeamPartecipante(String nome){
        if(PartecipanteCorrente != null) {
            if(PartecipanteCorrente.getEvento().getDataFineReg() != null && PartecipanteCorrente.getEvento().getDataFineReg().isAfter(LocalDate.now())){
                Team team = new Team(-1, nome, PartecipanteCorrente.getNomeUtente());
                team.setEventoIscritto(PartecipanteCorrente.getEvento());
                team.addMembroTeam(PartecipanteCorrente);
                try {
                    team.setIdTeam(DAO.addTeamDB(team));
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
            if(PartecipanteCorrente.getEvento().getDataFineReg() != null && PartecipanteCorrente.getEvento().getDataFineReg().isAfter(LocalDate.now())) {
                Team team = PartecipanteCorrente.getEvento().seekTeam(idTeam);
                if (team != null) {
                    team.addRichiesta(PartecipanteCorrente);
                    if (updateRichiesteTeam == null)
                        updateRichiesteTeam = new ArrayList<Team>();
                    updateRichiesteTeam.add(team);
                    return true;
                }
            }
        }
        return false;
    }

    //Questo metodo restituisce una lista contenente il nome utente di tutti coloro che
    //hanno fatto richiesta di entrare nel team.

    public ArrayList<String> requestTeamNotifications(){
        ArrayList<String> notifications = null;
        if(PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getTeam();
            if (team != null) {
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
            if(team != null) {
                if(accepted != null || refused != null) {
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
                    if(updateRichiesteTeam == null)
                        updateRichiesteTeam = new ArrayList<Team>();
                    updateRichiesteTeam.add(team);
                }
                return true;
            }
        }
        return false;
    }

    //Questo metodo restiuisce una lista contenente l'id e il testo di ogni progresso pubblicato da un team.
    //Ogni elemento della lista segue il formato: IdProgresso Testo

    public ArrayList<String> listaProgressiTeam(){
        ArrayList<String> listaProgressi = null;
        if (PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getTeam();
            if (team != null) {
                Progresso progresso = team.firstProgresso();
                if (progresso != null) {
                    listaProgressi = new ArrayList<String>();
                    while (progresso != null) {
                        listaProgressi.add(progresso.getIdProgresso() + " " + progresso.getTestoDocumeto());
                        progresso = team.nextProgresso();
                    }
                }
            }
        }
        return listaProgressi;
    }

    //Questo metodo permette di pubblicare un progresso.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean pubblicaProgresso(String Testo){
        Progresso progresso = null;
        if (PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getTeam();
            if (team != null) {
                if (team.getTeamLeader().equals(PartecipanteCorrente.getNomeUtente())) {
                    try {
                        progresso = new Progresso(Testo, team.getIdTeam());
                        DAO.addProgressoDB(progresso);
                        team.addProgresso(progresso);
                        return true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    //Questo metodo restutuisce una lista contenente tutti i commenti ricevuti da un progresso.
    //Ogni elemento della lista segue il formato: Giudice Testo

    public ArrayList<String> listaCommentiProgresso(int idProgresso)
    {
        ArrayList<String> listaCommenti = null;
        if (PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getTeam();
            if (team != null) {
                Progresso progresso = team.seekProgresso(idProgresso);
                if (progresso != null) {
                    Commento commento = progresso.firstCommento();
                    while (commento != null) {
                        if (listaCommenti == null)
                            listaCommenti = new ArrayList<String>();
                        listaCommenti.add(commento.getGiudice() + " " + commento.getTesto());
                        commento = progresso.nextCommento();
                    }
                }
            }
        }
        return listaCommenti;
    }


    //Questo metodo restutuisce una lista contenente tutti i voti ricevuti da un team.
    //Ogni elemento della lista segue il formato: Giudice Valore

    public ArrayList<String> listaVotiTeam()
    {
        ArrayList<String> listaVoti = null;
        if(PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getTeam();
            if (team != null) {
                Voto voto = team.firstVoto();
                while (voto != null) {
                    if(listaVoti == null)
                        listaVoti = new ArrayList<String>();
                    listaVoti.add(voto.getGiudice() + " " + voto.getValore());
                    voto = team.nextVoto();
                }
            }
        }
        return listaVoti;
    }

    //Questo metodo restituisce la media di tutti i voti ricevuti da un team.

    public int mediaVotiTeam(){
        int mediaVoti = 0;
        if(PartecipanteCorrente != null) {
            Team team = PartecipanteCorrente.getTeam();
            if (team != null) {
                mediaVoti = team.mediaVoti();
            }
        }
        return mediaVoti;
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
            try {
                DAO.updateEventoDB(OrganizzatoreCorrente.getEvento().getIdEvento(), indirizzoSede, nCivico, maxIscritti, maxTeam);
                Evento evento = OrganizzatoreCorrente.getEvento();
                evento.setIndirizzoSede(indirizzoSede);
                evento.setNCivicoSede(nCivico);
                evento.setMaxIscritti(maxIscritti);
                evento.setMaxTeam(maxTeam);
                return true;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //Questo metodo permette di impostare le date dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean setDateEvento(LocalDate dataInizio, LocalDate dataFine){
        if(OrganizzatoreCorrente != null) {
            try {
                DAO.updateDateEventoDB(OrganizzatoreCorrente.getEvento().getIdEvento(), dataInizio, dataFine);
                OrganizzatoreCorrente.getEvento().setDate(dataInizio, dataFine);
                return true;
            }catch(SQLException e){
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("Errore nel tentativo di accedere ai dati dell'evento.");
            } catch (DateTimeParseException e) {
                System.out.println("Errore nel tentativo di copiare le date dell'evento.");
            }
        }
        return false;
    }

    //Questo metodo permette di impostare le date di registrazione dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean setRegistrazioniEvento(LocalDate dataInizio, LocalDate dataFine){
        if(OrganizzatoreCorrente != null) {
            try {
                DAO.updateDateRegEventoDB(OrganizzatoreCorrente.getEvento().getIdEvento(), dataInizio, dataFine);
                OrganizzatoreCorrente.getEvento().setDateReg(dataInizio, dataFine);
                return true;
            }catch(SQLException e){
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("Errore nel tentativo di accedere ai dati dell'evento.");
            } catch (DateTimeParseException e) {
                System.out.println("Errore nel tentativo di copiare le date registrazione evento.");
            }
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
                if(updateInvitiGiudice == null)
                    updateInvitiGiudice = new ArrayList<Evento>();
                updateInvitiGiudice.add(evento);
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
                if(pubblicaProblema == null)
                    pubblicaProblema = new ArrayList<Evento>();
                pubblicaProblema.add(evento);
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
                    Voto voto = new Voto(team.getIdTeam(), valore, GiudiceCorrente.getNomeUtente());
                    team.addVoto(voto);
                    if(addVoti == null)
                        addVoti = new ArrayList<Voto>();
                    addVoti.add(voto);
                    return true;
                }
            }
        }
        return false;
    }

    //Questo metodo restituisce una lista contenente tutti i voti attribuire dall'utente in qualità di giudice.
    //Ogni elemento della lista segue il formato: IdTeam Valore

    public ArrayList<String> listaVotiTeamGiudicati(){
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

    public boolean commentaProgresso(int idProgresso, String testoCommento){
        if(GiudiceCorrente != null) {
            Evento evento = GiudiceCorrente.getEvento();
            if(evento != null){
                Team team = evento.getTeam();
                if(team != null){
                    Progresso progresso = team.seekProgresso(idProgresso);
                    if(progresso != null) {
                        Commento commento = new Commento(progresso.getIdProgresso(), testoCommento, GiudiceCorrente.getNomeUtente());
                        progresso.addCommento(commento);
                        if(addCommenti == null)
                            addCommenti = new ArrayList<Commento>();
                        addCommenti.add(commento);
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

    //Questo metodo serve a riportare tutti i cambiamenti avvenuti sul database all'uscita dall'applicazione.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean exitApplication(){
        try {
            if(UtenteCorrente != null) {
                if (updateInvitiGiudice != null)
                    DAO.addInvitiGiudiceDB(updateInvitiGiudice);
            }

            if(PartecipanteCorrente != null) {
                if(updatePartecipanteEvento != null)
                    DAO.addPartecipanteEventoDB(PartecipanteCorrente.getNomeUtente(), updatePartecipanteEvento);
                if(updateRichiesteTeam != null)
                    DAO.addRichiesteTeamDB(updateRichiesteTeam);
            }

            if(OrganizzatoreCorrente != null) {
                if (addOrganizzatore)
                    DAO.addOrganizzatoreDB(OrganizzatoreCorrente);
                if(updateOrganizzatoreEvento != null)
                    DAO.addOrganizzatoreEventoDB(OrganizzatoreCorrente.getNomeUtente(), updateOrganizzatoreEvento);
            }

            if(GiudiceCorrente != null) {
                if(pubblicaProblema != null)
                    DAO.updateProblemaDB(pubblicaProblema);
                if(addVoti != null)
                    DAO.addVotiDB(addVoti);
                if(addCommenti != null)
                    DAO.addCommentiDB(addCommenti);
            }

            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}