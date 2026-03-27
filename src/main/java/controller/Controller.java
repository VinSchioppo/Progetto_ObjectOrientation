package controller;
import classmodel.*;
import dao.*;

import java.sql.SQLException;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Questo enum serve a determinare la specializzazione che un utente assume.

public class Controller {

    private Utente utenteCorrente = null;
    private Partecipante partecipanteCorrente = null;
    private Organizzatore organizzatoreCorrente = null;
    private Giudice giudiceCorrente = null;
    private static ImplementazioneDAO dao;

    private boolean addOrganizzatore = false;
    private List<Evento> updateInvitiGiudice = null;
    private List<Evento> updateOrganizzatoreEvento = null;
    private List<Evento> updatePartecipanteEvento = null;
    private List<Team> updateRichiesteTeam = null;
    private List<Team> leaveTeam = null;
    private List<Evento> pubblicaProblema = null;
    private List<Voto> addVoti = null;
    private List<Commento> addCommenti = null;

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    //Questo metodo stabilisce una nuova connessione col database.

    private static void connect(){
        try{
            dao = new ImplementazioneDAO();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
    }

    //Questo metodo verifica i dati di login inseriti e restituisce true se va a buon fine, altrimenti false.

    public boolean logInUtente(String nomeUtente, String password) {
        try {
            connect();
            boolean success = dao.checkLoginDB(nomeUtente, password);
            if (success) {
                utenteCorrente = dao.getUtenteDB(nomeUtente);

                // ===== RICOSTRUZIONE RUOLI =====
                partecipanteCorrente = dao.getPartecipanteDB(nomeUtente);
                organizzatoreCorrente = dao.getOrganizzatoreDB(nomeUtente);
                giudiceCorrente = dao.getGiudiceDB(nomeUtente);

                return true;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
        return false;
    }
    
    //Questo metodo verifica che il nome utente scelto durante la registrazione non sia già presente nel Database.
    //Restituisce true se la registrazione viene completata con successo, altrimenti restituisce false.

    public boolean registerUtente(String nomeUtente, String password) {
        try {
            dao.addUtenteDB(nomeUtente, password);
            utenteCorrente = new Utente(nomeUtente, password);
        }
        catch(SQLException e){
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            return false;
        }
        return true;
    }

    //Metodi di verifica del ruolo

    public boolean isPartecipante() {return partecipanteCorrente != null;}
    public boolean isOrganizzatore() {return organizzatoreCorrente != null;}
    public boolean isGiudice() {return giudiceCorrente != null;}

    //Questo metodo restiuisce una lista contenente l'id e il titolo di tutti gli eventi a cui l'utente partecipa.
    //Ogni elemento della lista segue il formato: IdEvento Titolo

    public List<String> listaEventiPartecipante() {
        List<String> listaEventi = null;
        if (partecipanteCorrente != null) {
            listaEventi = new ArrayList<>();
            partecipanteCorrente.firstEvento();
            while(partecipanteCorrente.getEvento() != null) {
                String evento = partecipanteCorrente.getEvento().getIdEvento() + " " + partecipanteCorrente.getEvento().getTitolo();
                listaEventi.add(evento);
                partecipanteCorrente.nextEvento();
            }
        }
        return listaEventi;
    }

    //Questo metodo restiuisce una lista contenente l'id e il titolo di tutti gli eventi che l'utente organizza.
    //Ogni elemento della lista segue il formato: IdEvento Titolo

    public List<String> listaEventiOrganizzatore() {
        List<String> listaEventi = null;
        if (organizzatoreCorrente != null) {
            listaEventi = new ArrayList<>();
            organizzatoreCorrente.firstEvento();
            while(organizzatoreCorrente.getEvento() != null) {
                String evento = organizzatoreCorrente.getEvento().getIdEvento() + " " + organizzatoreCorrente.getEvento().getTitolo();                listaEventi.add(evento);
                listaEventi.add(evento);
                organizzatoreCorrente.nextEvento();
            }
        }
        return listaEventi;
    }

    //Questo metodo restiuisce una lista contenente l'id e il titolo di tutti gli eventi che l'utente giudica.
    //Ogni elemento della lista segue il formato: IdEvento Titolo

    public List<String> listaEventiGiudice() {
        List<String> listaEventi = null;
        if(giudiceCorrente != null){
            listaEventi = new ArrayList<>();
            giudiceCorrente.firstEvento();
            while(giudiceCorrente.getEvento() != null){
                String evento = giudiceCorrente.getEvento().getIdEvento() + " " + giudiceCorrente.getEvento().getTitolo();
                listaEventi.add(evento);
                giudiceCorrente.nextEvento();
            }
        }
        return listaEventi;
    }

    //Questo metodo restituisce i dati di un evento a cui partecipa un utente.
    //La stringa segue il formato: Titolo IndirizzoSede NCivicoSede DataInizio DataFine MaxIscritti MaxTeam
    // DataInizioReg DataFineReg DescrizioneProblema

    public String datiEventoPreSelezione(int idEvento) {
        if(partecipanteCorrente != null){
            Evento evento = partecipanteCorrente.seekEvento(idEvento);
            if (evento != null) {
                return evento.getTitolo() + " "
                        + evento.getIndirizzoSede() + " " + evento.getnCivicoSede()
                        + " " + evento.getDataInizio() + " " + evento.getDataFine()
                        + " " + evento.getMaxIscritti() + " " + evento.getMaxTeam()
                        + " " + evento.getDataInizioReg() + " " + evento.getDataFineReg()
                        + " " + evento.getDescrizioneProblema();
            }
        }
        return null;
    }

    //Questo metodo riceve un input l'id di un evento e il ruolo del utente in quell'evento e selezione l'evento in memoria.
    //Restituisce true se l'operazione viene completata con successo, altrimenti restituisce false.

    public boolean selectEvento(int idEvento, Role ruolo) {

        if(partecipanteCorrente != null && ruolo == Role.PARTECIPANTE && partecipanteCorrente.seekEvento(idEvento) != null) {
            return checkMembroTeam();
        }

        if(organizzatoreCorrente != null && ruolo == Role.ORGANIZZATORE && organizzatoreCorrente.seekEvento(idEvento) != null)
                return true;

        return giudiceCorrente != null && ruolo == Role.GIUDICE && giudiceCorrente.seekEvento(idEvento) != null;
    }

    //Questo metodo verifica che il partecipante sia in un team all'inizio dell'evento.
    //Se il partecipante non fa parte di un team, viene creato un team con il solo partecipante.
    //Tuttavia, se l'evento ha raggiunto il numero massimo di team viene negato l'accesso al partecipante.
    //Restituisce true se l'operazione viene completata con successo, altrimenti restituisce false.

    private boolean checkMembroTeam() {
        if(partecipanteCorrente.getEvento().getDataFineReg().isBefore(LocalDate.now())
        && partecipanteCorrente.getEvento().sizeTeamIscritti() + 1 <= partecipanteCorrente.getEvento().getMaxTeam()) {
            String nomeTeam = "Team " + partecipanteCorrente.getNomeUtente();
            Team team = new Team(-1, nomeTeam, partecipanteCorrente.getNomeUtente());
            team.setEventoIscritto(partecipanteCorrente.getEvento());
            team.addMembroTeam(partecipanteCorrente);
            try {
                team.setIdTeam(dao.addTeamDB(team));
                return true;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
                return false;
            }
        }
        else return partecipanteCorrente.getEvento().sizeTeamIscritti() + 1 <= partecipanteCorrente.getEvento().getMaxTeam();
    }

    //Questo metodo restituisce una stringa contenente i dati personali dell'utente.
    //Nel caso in cui non sia salvato nessun dato restituisce null.
    //La stringa segue il formato: FNome MNome LNome DataNascita

    public String datiUtente(){
        if(utenteCorrente != null){
            if(utenteCorrente.getFNome() == null && utenteCorrente.getMNome() == null &&
                    utenteCorrente.getLNome() == null && utenteCorrente.getDataNascita() == null)
                return null;
            else
                return utenteCorrente.getFNome() + " " + utenteCorrente.getMNome() + " "
                    + utenteCorrente.getLNome() + " " + utenteCorrente.getDataNascita();
        }
        return null;
    }

    //Questo metodo permette di aggiornare i dati personali di un utente.
    //Restituisce un boolean e accetta solo una data col formato yyyy-mm-dd, altrimenti lancia una eccezione.

    public boolean inserisciDatiUtente(String fNome, String mNome, String lNome, LocalDate dataNascita){
        try{
                if(utenteCorrente != null) {
                    utenteCorrente.setDati(fNome, mNome, lNome, dataNascita);
                    dao.updatePartecipanteDB(utenteCorrente.getNomeUtente(), fNome, mNome, lNome, dataNascita);
                }
                if(partecipanteCorrente != null)
                    partecipanteCorrente.setDati(fNome, mNome, lNome, dataNascita);
                if(organizzatoreCorrente != null) {
                    organizzatoreCorrente.setDati(fNome, mNome, lNome, dataNascita);
                    dao.updateOrganizzatoreDB(organizzatoreCorrente.getNomeUtente(), fNome, mNome, lNome, dataNascita);
                }
                if(giudiceCorrente != null) {
                    giudiceCorrente.setDati(fNome, mNome, lNome, dataNascita);
                    dao.updateGiudiceDB(giudiceCorrente.getNomeUtente(), fNome, mNome, lNome, dataNascita);
                }
        }
        catch (DateTimeParseException _) {
            logger.log(Level.SEVERE, "Errore nel tentativo di copiare la data di nascita.");
            return false;
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            return false;
        }
        return true;
    }

    //Questo metodo restiuisce una lista contenente i dati di tutti gli eventi le cui prenotazioni sono attualmente aperte.
    //Ogni elemento della lista segue il formato: IdEvento Titolo IndirizzoSede NCivicoSede DataInizio DataFine
    // MaxIscritti MaxTeam DataInzioReg DataFineReg DescrizioneProblema

    public List<String> listaEventiAperti(){
        List<String> listaEventi = null;
        if(utenteCorrente != null) {
            try {
                List<Evento> eventi = dao.getEventiApertiDB(utenteCorrente.getNomeUtente());
                if (eventi != null) {
                    listaEventi = new ArrayList<>();
                    for (Evento evento : eventi) {
                        String datiEvento = evento.getIdEvento() + " " + evento.getTitolo()
                                + " " + evento.getIndirizzoSede() + " " + evento.getnCivicoSede()
                                + " " + evento.getDataInizio() + " " + evento.getDataFine()
                                + " " + evento.getMaxIscritti() + " " + evento.getMaxTeam()
                                + " " + evento.getDataInizioReg() + " " + evento.getDataFineReg()
                                + " " + evento.getDescrizioneProblema();
                        listaEventi.add(datiEvento);
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
        return listaEventi;
    }

    //Questo metodo aggiunge un evento alla lista di eventi a cui è iscritto un partecipante.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean iscriviEvento(int idEvento){
        Evento evento;
        try{
            evento = dao.getEventoDB(idEvento);
            if(evento != null && evento.sizePartecipanti() > evento.getMaxIscritti()) {
                if (partecipanteCorrente == null)
                    partecipanteCorrente = utenteCorrente.becomePartecipante();
                partecipanteCorrente.addEvento(evento);
                if(updatePartecipanteEvento == null)
                    updatePartecipanteEvento = new ArrayList<>();
                updatePartecipanteEvento.add(evento);
                return true;
            }
        }
        catch(SQLException e){
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
        return false;
    }

    //Questo metodo permette ad un utente di creare un evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean creaEvento(String titolo, String indirizzo, int nCivico){
        if(utenteCorrente != null) {
            Evento evento = new Evento(titolo, indirizzo, nCivico);
            Organizzatore organizzatore;
            if(organizzatoreCorrente == null) {
                organizzatore = utenteCorrente.becomeOrganizzatore();
                addOrganizzatore = true;
            }
            else organizzatore = organizzatoreCorrente;
            evento.setOrganizzatore(organizzatore);
            try {
                dao.addEventoDB(evento);
                organizzatore.addEvento(evento);
                organizzatoreCorrente = organizzatore;
                if(updateOrganizzatoreEvento == null)
                    updateOrganizzatoreEvento = new ArrayList<>();
                updateOrganizzatoreEvento.add(evento);
                return true;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
        return false;
    }

    //Questo metodo restiuisce una lista contenente i dati di tutti gli eventi
    //per cui un utente è stato invitato a diventare un giudice.
    //Ogni elemento della lista segue il formato: IdEvento Titolo IndirizzoSede NCivicoSede DataInizio DataFine
    // MaxIscritti MaxTeam DataInzioReg DataFineReg

    public List<String> listaInvitiGiudice() {
        List<String> invitiGiudice = null;
        if(utenteCorrente != null && partecipanteCorrente != null) {
                try {
                    dao.getAllInvitiGiudiceDB(utenteCorrente, partecipanteCorrente);
                    Evento evento = utenteCorrente.firstInvitoGiudiceEvento();
                    while (evento != null) {
                        if (invitiGiudice == null)
                            invitiGiudice = new ArrayList<>();
                        String datiEvento = evento.getIdEvento() + " " + evento.getTitolo()
                                + " " + evento.getIndirizzoSede() + " " + evento.getnCivicoSede()
                                + " " + evento.getDataInizio() + " " + evento.getDataFine()
                                + " " + evento.getMaxIscritti() + " " + evento.getMaxTeam()
                                + " " + evento.getDataInizioReg() + " " + evento.getDataFineReg();
                        invitiGiudice.add(datiEvento);
                        evento = utenteCorrente.nextInvitoGiudiceEvento();
                    }
                }
                catch(SQLException e){
                    logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
                }
            }

        return invitiGiudice;
    }

    //Questo metodo permette ad un utente di accettare un invito ad essere giudice di un evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean acceptInvitoGiudiceEvento(int idEvento){
        if(utenteCorrente != null) {
            Evento evento = utenteCorrente.seekInvitoGiudiceEvento(idEvento);
            if (evento != null && utenteCorrente.getInvitoGiudiceEventoAnswer() == null) {
                    utenteCorrente.setInvitoGiudiceEventoAnswer(true);
                    evento.seekInvitoGiudice(utenteCorrente.getNomeUtente());
                    evento.setInvitoGiudiceAnswer(true);
                    if(giudiceCorrente == null)
                        giudiceCorrente = utenteCorrente.becomeGiudice();
                    evento.addGiudice(giudiceCorrente);
                    giudiceCorrente.addEvento(evento);
                    if(updateInvitiGiudice == null)
                        updateInvitiGiudice = new ArrayList<>();
                    updateInvitiGiudice.add(evento);
                    return true;
                }

        }
        return false;
    }

    //Questo metodo permette ad un utente di rifiutare un invito ad essere giudice di un evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean refuseInvitoGiudiceEvento(int idEvento){
        if(utenteCorrente != null) {
            Evento evento = utenteCorrente.seekInvitoGiudiceEvento(idEvento);
            if (evento != null && utenteCorrente.getInvitoGiudiceEventoAnswer() == null) {
                    utenteCorrente.setInvitoGiudiceEventoAnswer(false);
                    evento.seekInvitoGiudice(utenteCorrente.getNomeUtente());
                    evento.setInvitoGiudiceAnswer(false);
                    if(updateInvitiGiudice == null)
                        updateInvitiGiudice = new ArrayList<>();
                    updateInvitiGiudice.add(evento);
                    return true;
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
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.seekTeamEvento(partecipanteCorrente.getEvento().getIdEvento());
            if (team != null) {
                infoTeam = team.getIdTeam() + " " + team.getNome() + " " + team.getTeamLeader();
            }
        }
        return infoTeam;
    }

    //Questo metodo restituisce l'id del team in esame.

    public int idTeamCorrente(){
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null) {
                return team.getIdTeam();
            }
        }
        return -1;
    }

    //Questo metodo restituisce il nome del team in esame.

    public String nomeTeamCorrente(){
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null) {
                return team.getNome();
            }
        }
        return null;
    }

    //Questo metodo restituisce il leader del team in esame.

    public String teamLeaderCorrente(){
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null) {
                return team.getTeamLeader();
            }
        }
        return null;
    }

    //Questo metodo permette al partecipante di creare un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean creaTeamPartecipante(String nome){
        if(partecipanteCorrente != null && partecipanteCorrente.getEvento().getDataFineReg() != null
        && partecipanteCorrente.getEvento().getDataFineReg().isAfter(LocalDate.now())
        && partecipanteCorrente.getEvento().sizeTeamIscritti() + 1 <= partecipanteCorrente.getEvento().getMaxTeam()){
            Team team = new Team(-1, nome, partecipanteCorrente.getNomeUtente());
            team.setEventoIscritto(partecipanteCorrente.getEvento());
            team.addMembroTeam(partecipanteCorrente);
            try {
                team.setIdTeam(dao.addTeamDB(team));
                return true;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
        return false;
    }

    //Questo metodo restiuisce una lista contenente l'id e il nome di tutti i team iscritti all'evento.
    //Ogni elemento della lista segue il formato: IdTeam Nome

    public List<String> listaTeamEvento(){
        List<String> listaTeam = null;
        if(partecipanteCorrente != null && partecipanteCorrente.getEvento() != null) {
            Team team = partecipanteCorrente.getEvento().firstTeam();
            if (team != null) {
                listaTeam = new ArrayList<>();
                while (team != null) {
                    listaTeam.add(team.getIdTeam() + " " + team.getNome());
                    team = partecipanteCorrente.getEvento().nextTeam();
                }
            }
        }
        return listaTeam;
    }

    //Questo metodo fa aggiungere un partecipante alla lista di attesa per un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean joinTeam(int idTeam){
        if(partecipanteCorrente != null && partecipanteCorrente.getEvento().getDataFineReg() != null && partecipanteCorrente.getEvento().getDataFineReg().isAfter(LocalDate.now())) {
                Team team = partecipanteCorrente.getEvento().seekTeam(idTeam);
                if (team != null) {
                    team.addRichiesta(partecipanteCorrente);
                    if (updateRichiesteTeam == null)
                        updateRichiesteTeam = new ArrayList<>();
                    updateRichiesteTeam.add(team);
                    return true;
                }
            }

        return false;
    }

    //Questo metodo restiuisce una lista contenente tutti i membri di un team.
    //Ogni elemento della lista segue il formato: NomeUtente FNome MNome LNome

    public List<String> listaMembriTeam(){
        List<String> listaMembri = null;
        if(partecipanteCorrente != null && partecipanteCorrente.getTeam() != null) {
            Partecipante membro = partecipanteCorrente.getTeam().firstMembroTeam();
            if (membro != null) {
                listaMembri = new ArrayList<>();
                while (membro != null) {
                    listaMembri.add(membro.getNomeUtente() + " " + membro.getFNome() + " " + membro.getMNome() + " " + membro.getLNome());
                    membro = partecipanteCorrente.getTeam().nextMembroTeam();
                }
            }
        }
        return listaMembri;
    }

    //Questo metodo permette a un partecipante di lasciare un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean leaveTeam(int idTeam){
        if(partecipanteCorrente != null && partecipanteCorrente.getEvento() != null && partecipanteCorrente.getEvento().getDataFineReg() != null && partecipanteCorrente.getEvento().getDataFineReg().isAfter(LocalDate.now())) {
            Team team = partecipanteCorrente.seekAndRemoveTeam(idTeam);
            if (team != null) {
                team.removeMembroTeam(partecipanteCorrente.getNomeUtente());
                if(leaveTeam == null)
                    leaveTeam = new ArrayList<>();
                leaveTeam.add(team);
                if(team.sizeMembriTeam() > 0)
                    team.setTeamLeader(team.firstMembroTeam().getNomeUtente());
                else {
                    team.setTeamLeader(null);
                    partecipanteCorrente.getEvento().seekAndRemoveTeam(idTeam);
                }
                return true;
            }
        }
        return false;
    }

    //Questo metodo restituisce una lista contenente il nome utente di tutti coloro che
    //hanno fatto richiesta di entrare nel team.

    public List<String> requestTeamNotifications(){
        List<String> notifications = null;
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null && partecipanteCorrente.getNomeUtente().equals(team.getTeamLeader())) {
                    Partecipante partecipante = team.firstRichiesta();
                    while (partecipante != null) {
                        if (notifications == null)
                            notifications = new ArrayList<>();
                        notifications.add(partecipante.getNomeUtente());
                        partecipante = team.nextRichiesta();
                    }
                }

        }
        return notifications;
    }

    //Questo metodo riceve in input un utente che è stato accettato nel team e lo aggiunge.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean acceptRichiestaTeam(String nomeUtente){
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if(team != null && team.getTeamLeader().equals(partecipanteCorrente.getNomeUtente())) {
                Partecipante partecipante = team.seekRichiesta(nomeUtente);
                if (partecipante != null) {
                    team.addMembroTeam(partecipante);
                    team.setRichiestaAnswer(true);
                }
                if(updateRichiesteTeam == null)
                    updateRichiesteTeam = new ArrayList<>();
                updateRichiesteTeam.add(team);
                return true;
            }
        }
        return false;
    }

    //Questo metodo riceve in input un utente che è stato rifiutato dal team e e imposta la sua richiesta a falso.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean refuseRichiestaTeam(String nomeUtente){
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if(team != null && team.getTeamLeader().equals(partecipanteCorrente.getNomeUtente())) {
                Partecipante partecipante = team.seekRichiesta(nomeUtente);
                if (partecipante != null) {
                    team.setRichiestaAnswer(false);
                }
                if(updateRichiesteTeam == null)
                    updateRichiesteTeam = new ArrayList<>();
                updateRichiesteTeam.add(team);
                return true;
            }
        }
        return false;
    }

    //Questo metodo restiuisce una lista contenente l'id e il testo di ogni progresso pubblicato da un team.
    //Ogni elemento della lista segue il formato: IdProgresso Testo

    public List<String> listaProgressiTeam(){
        List<String> listaProgressi = null;
        if (partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null) {
                Progresso progresso = team.firstProgresso();
                if (progresso != null) {
                    listaProgressi = new ArrayList<>();
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

    public boolean pubblicaProgresso(String testo){
        Progresso progresso = null;
        if (partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null && team.getTeamLeader().equals(partecipanteCorrente.getNomeUtente())) {
                    try {
                        progresso = new Progresso(testo, team.getIdTeam());
                        dao.addProgressoDB(progresso);
                        team.addProgresso(progresso);
                        return true;
                    } catch (SQLException e) {
                        logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
                    }
                }

        }
        return false;
    }

    //Questo metodo restutuisce una lista contenente tutti i commenti ricevuti da un progresso.
    //Ogni elemento della lista segue il formato: Giudice Testo

    public List<String> listaCommentiProgresso(int idProgresso)
    {
        List<String> listaCommenti = null;
        if (partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null) {
                Progresso progresso = team.seekProgresso(idProgresso);
                if (progresso != null) {
                    Commento commento = progresso.firstCommento();
                    while (commento != null) {
                        if (listaCommenti == null)
                            listaCommenti = new ArrayList<>();
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

    public List<String> listaVotiTeam()
    {
        List<String> listaVoti = null;
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null) {
                Voto voto = team.firstVoto();
                while (voto != null) {
                    if(listaVoti == null)
                        listaVoti = new ArrayList<>();
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
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
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
        if(organizzatoreCorrente != null) {
            Evento evento = organizzatoreCorrente.getEvento();
            if (evento != null) {
                return evento.getTitolo() + " "
                        + evento.getIndirizzoSede() + " " + evento.getnCivicoSede()
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
        if(organizzatoreCorrente != null) {
            try {
                dao.updateEventoDB(organizzatoreCorrente.getEvento().getIdEvento(), indirizzoSede, nCivico, maxIscritti, maxTeam);
                Evento evento = organizzatoreCorrente.getEvento();
                evento.setIndirizzoSede(indirizzoSede);
                evento.setnCivicoSede(nCivico);
                evento.setMaxIscritti(maxIscritti);
                evento.setMaxTeam(maxTeam);
                return true;
            }
            catch (SQLException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
        return false;
    }

    //Questo metodo permette di impostare le date dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean setDateEvento(LocalDate dataInizio, LocalDate dataFine){
        if(organizzatoreCorrente != null) {
            try {
                dao.updateDateEventoDB(organizzatoreCorrente.getEvento().getIdEvento(), dataInizio, dataFine);
                organizzatoreCorrente.getEvento().setDate(dataInizio, dataFine);
                return true;
            }catch(SQLException e){
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            } catch (NullPointerException _) {
                logger.log(Level.SEVERE, "Errore nel tentativo di accedere ai dati dell'evento.");
            } catch (DateTimeParseException _) {
                logger.log(Level.SEVERE, "Errore nel tentativo di copiare le date dell'evento.");
            }
        }
        return false;
    }

    //Questo metodo permette di impostare le date di registrazione dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean setRegistrazioniEvento(LocalDate dataInizio, LocalDate dataFine){
        if(organizzatoreCorrente != null) {
            try {
                dao.updateDateRegEventoDB(organizzatoreCorrente.getEvento().getIdEvento(), dataInizio, dataFine);
                organizzatoreCorrente.getEvento().setDateReg(dataInizio, dataFine);
                return true;
            }catch(SQLException e){
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            } catch (NullPointerException _) {
                logger.log(Level.SEVERE, "Errore nel tentativo di accedere ai dati dell'evento.");
            } catch (DateTimeParseException _) {
                logger.log(Level.SEVERE, "Errore nel tentativo di copiare le date registrazione evento.");
            }
        }
        return false;
    }

    //Questo metodo restituisce una lista contenente il nome utente di tutti i partecipanti all'evento.
    //Ogni elemento della lista segue il formato: NomeUtente

    public List<String> listaPartecipantiEvento(){
        List<String> listaPartecipanti = null;
        if(organizzatoreCorrente != null) {
            Evento evento = organizzatoreCorrente.getEvento();
            if (evento != null) {
                evento.firstPartecipante();
                while (evento.getPartecipante() != null) {
                    if (listaPartecipanti == null)
                        listaPartecipanti = new ArrayList<>();
                    listaPartecipanti.add(evento.getPartecipante().getNomeUtente());
                    evento.nextPartecipante();
                }
            }
        }
        return listaPartecipanti;
    }

    //Questo metodo aggiunge un partecipante alla lista di coloro che sono stati invitati a essere giudici.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean invitaGiudice(String nomeUtente){
        if(organizzatoreCorrente != null){
            Evento evento = organizzatoreCorrente.getEvento();
            if(evento != null){
                Partecipante partecipante = evento.seekPartecipante(nomeUtente);
                evento.addInvitoGiudice(partecipante);
                if(updateInvitiGiudice == null)
                    updateInvitiGiudice = new ArrayList<>();
                updateInvitiGiudice.add(evento);
                return true;
            }
        }
        return false;
    }

    public List<String> listaGiudiciEvento(){
        List<String> listaGiudici = null;
        if(organizzatoreCorrente != null) {
            Evento evento = organizzatoreCorrente.getEvento();
            if (evento != null) {
                evento.firstGiudice();
                while (evento.getGiudice() != null) {
                    if (listaGiudici == null)
                        listaGiudici = new ArrayList<>();
                    listaGiudici.add(evento.getGiudice().getNomeUtente());
                    evento.nextGiudice();
                }
            }
        }
        return listaGiudici;
    }

    /*******************************************************************************************************/

    //Operazioni Giudice

    //Questo metodo permette a un giudice di pubblicare la descrizione del problema dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean pubblicaProblema(String problema){
        if(giudiceCorrente != null){
            Evento evento = giudiceCorrente.getEvento();
            if(evento != null){
                evento.setDescrizioneProblema(problema);
                if(pubblicaProblema == null)
                    pubblicaProblema = new ArrayList<>();
                pubblicaProblema.add(evento);
                return true;
            }
        }
        return false;
    }

    //Questo metodo restituisce un lista con tutti i team iscritti ad un evento giudicato dall'utente.
    //Ogni elemento della lista segue il formato: IdTeam Nome

    public List<String> listaTeamGiudicati(){
        List<String> listaTeam = null;
        if(giudiceCorrente != null){
            Evento evento = giudiceCorrente.getEvento();
            if(evento != null){
                Team team = evento.firstTeam();
                while(team != null){
                    if(listaTeam == null)
                        listaTeam = new ArrayList<>();
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
        if(giudiceCorrente != null) {
            Evento evento = giudiceCorrente.getEvento();
            if (evento != null) {
                Team team = evento.seekTeam(idTeam);
                if (team != null) {
                    Voto voto = new Voto(team.getIdTeam(), valore, giudiceCorrente.getNomeUtente());
                    team.addVoto(voto);
                    if(addVoti == null)
                        addVoti = new ArrayList<>();
                    addVoti.add(voto);
                    return true;
                }
            }
        }
        return false;
    }

    //Questo metodo restituisce una lista contenente tutti i voti attribuire dall'utente in qualità di giudice.
    //Ogni elemento della lista segue il formato: IdTeam Valore

    public List<String> listaVotiTeamGiudicati(){
        List<String> listaVoti = null;
        if(giudiceCorrente != null) {
            try {
                List<Voto> voti = dao.getAllVotiDB(giudiceCorrente.getNomeUtente());
                if (voti != null) {
                    for (Voto voto : voti) {
                        if (listaVoti == null)
                            listaVoti = new ArrayList<>();
                        listaVoti.add(voto.getIdTeam() + " " + voto.getValore());
                    }
                }
            }
            catch (SQLException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
        return listaVoti;
    }

    //Questo metodo restituisce un lista con tutti i progressi di un team giudicato dell'utente.
    //Ogni elemento della lista segue il formato: IdProgresso TestoDocumento

    public List<String> listaProgressiTeamGiudicato(int idTeam){
        List<String> listaProgressi = null;
        if(giudiceCorrente != null) {
            Evento evento = giudiceCorrente.getEvento();
            if(evento != null){
                Team team = evento.seekTeam(idTeam);
                if(team != null){
                    Progresso progresso = team.firstProgresso();
                    while(progresso != null) {
                        if (listaProgressi == null)
                            listaProgressi = new ArrayList<>();
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
        if(giudiceCorrente != null) {
            Evento evento = giudiceCorrente.getEvento();
            if(evento != null){
                Team team = evento.getTeam();
                if(team != null){
                    Progresso progresso = team.seekProgresso(idProgresso);
                    if(progresso != null) {
                        Commento commento = new Commento(progresso.getIdProgresso(), testoCommento, giudiceCorrente.getNomeUtente());
                        progresso.addCommento(commento);
                        if(addCommenti == null)
                            addCommenti = new ArrayList<>();
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

    public List<String> listaCommenti(){
        List<String> listaCommenti = null;
        if(giudiceCorrente != null) {
            try{
                List<Commento> commenti = dao.getAllCommentiDB(giudiceCorrente.getNomeUtente());
                if (commenti != null) {
                    for (Commento commento : commenti) {
                        if (listaCommenti == null)
                            listaCommenti = new ArrayList<>();
                        listaCommenti.add(commento.getIdProgresso() + " " + commento.getTesto());
                    }
                }
            }
            catch(SQLException e){
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
        return listaCommenti;
    }

    //Questo metodo serve a riportare tutti i cambiamenti avvenuti sul database all'uscita dall'applicazione.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean exitApplication(){
        try {
            updateInviti();
            updatePartecipante();
            updateOrganizzatore();
            updateGiudice();
            dao.disconnect();
            return true;
        }catch(SQLException e){
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
        return false;
    }

    private void updateInviti() throws SQLException{
        if(utenteCorrente != null && updateInvitiGiudice != null)
            dao.addInvitiGiudiceDB(updateInvitiGiudice);
    }

    private void updatePartecipante() throws SQLException{
        if(partecipanteCorrente != null) {
            if(updatePartecipanteEvento != null)
                dao.addPartecipanteEventoDB(partecipanteCorrente.getNomeUtente(), updatePartecipanteEvento);
            if(updateRichiesteTeam != null)
                dao.addRichiesteTeamDB(updateRichiesteTeam);
            if(leaveTeam != null)
                dao.leaveTeamsDB(partecipanteCorrente.getNomeUtente(), leaveTeam);
        }
    }

    private void updateOrganizzatore() throws SQLException{
        if(organizzatoreCorrente != null) {
            if (addOrganizzatore)
                dao.addOrganizzatoreDB(organizzatoreCorrente);
            if(updateOrganizzatoreEvento != null)
                dao.addOrganizzatoreEventoDB(organizzatoreCorrente.getNomeUtente(), updateOrganizzatoreEvento);
        }
    }

    private void updateGiudice() throws  SQLException{
        if(giudiceCorrente != null) {
            if(pubblicaProblema != null)
                dao.updateProblemaDB(pubblicaProblema);
            if(addVoti != null)
                dao.addAllVotiDB(addVoti);
            if(addCommenti != null)
                dao.addAllCommentiDB(addCommenti);
        }
    }
}
