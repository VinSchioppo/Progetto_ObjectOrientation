package DAO;

import ClassModel.*;
import Database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ImplementazioneDAO implements InterfacciaDAO {
    private Connection connection = null;

    public ImplementazioneDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkLogin(String NomeUtente, String Password) throws SQLException{
        boolean check = true;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT NomeUtente, Password FROM Partecipante" +
                            " WHERE NomeUtente = '" + NomeUtente + "' AND Password = '" + Password +"';");
            ResultSet rs = ps.executeQuery();
            if (!rs.isBeforeFirst()) {
                ps = connection.prepareStatement("SELECT NomeUtente, Password FROM Giudice" +
                                " WHERE NomeUtente = '" + NomeUtente + "' AND Password = '" + Password +"';");
                rs = ps.executeQuery();
                if (!rs.isBeforeFirst()) {
                    ps = connection.prepareStatement("SELECT NomeUtente, Password FROM Organizzatore" +
                            " WHERE NomeUtente = '" + NomeUtente + "' AND Password = '" + Password +"';");
                    rs = ps.executeQuery();
                    if (!rs.isBeforeFirst()) {
                        check = false;
                    }
                }
            }
            rs.close();
        }
        catch(SQLException e) {
            throw e;
        }
        return check;
    }

    public void getAllRuoliDB(Utente utente, Partecipante partecipante, Giudice giudice, Organizzatore organizzatore) throws SQLException{
        String NomeUtente = utente.getNomeUtente();
        partecipante = getPartecipanteDB(NomeUtente);
        organizzatore = getOrganizzatoreDB(NomeUtente);
        giudice = getGiudiceDB(NomeUtente);
    }

    public int getIdEventoDB() throws SQLException{
        int result;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT MAX(IdEvento) FROM Evento;");
            ResultSet rs = ps.executeQuery();
            rs.next();
            result =  rs.getInt(1);
            rs.close();
        }
        catch (SQLException e) {
            throw e;
        }
        return result;
    }

    public Evento getEventoDB(int IdEvento) throws SQLException{
        Evento evento = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Evento WHERE IdEvento =" + IdEvento + ";");
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                evento = new Evento(IdEvento);
                rs.next();
                evento.setTitolo(rs.getString("titolo"));
                evento.setIndirizzoSede(rs.getString("indirizzosede"));
                evento.setNCivicoSede(rs.getInt("ncivicosede"));
                evento.setMaxIscritti(rs.getInt("maxiscritti"));
                evento.setMaxTeam(rs.getInt("maxteam"));
                evento.setDate(rs.getObject("datainizio", LocalDate.class), rs.getObject("datafine", LocalDate.class));
                evento.setDescrizioneProblema(rs.getString("descrizioneprob"));
                evento.setDateReg(rs.getObject("datainizioreg", LocalDate.class), rs.getObject("datafinereg", LocalDate.class));
                rs.close();
                getOrganizzatoreDB(evento);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return evento;
    }

    public void addEventoDB(Evento evento) throws SQLException {
        try {
            evento.setIdEvento(addEventoDB(evento.getTitolo(), evento.getIndirizzoSede(), evento.getNCivicoSede(), evento.getDataInizio(), evento.getDataFine(), evento.getMaxIscritti(), evento.getMaxTeam(), evento.getDataInizioReg(), evento.getDataFineReg(), evento.getDescrizioneProblema()));
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public int addEventoDB(String Titolo, String Indirizzo, int NCivico) throws SQLException{
        int id;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Evento(Titolo, IndirizzoSede, NCivicoSede) VALUES('"
                            + Titolo + "','" + Indirizzo + "'," + NCivico + ");");
            ps.executeUpdate();
            id = getIdEventoDB();
        }
        catch (SQLException e) {
            throw e;
        }
        return id;
    }

    public int addEventoDB(String Titolo, String Indirizzo, int NCivico, LocalDate DataInizio, LocalDate DataFine, int MaxIscritti, int MaxTeam, LocalDate DataInizioReg, LocalDate DataFineReg, String DescrizioneProb) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Evento(Titolo, IndirizzoSede, NCivicoSede, DataInizio, DataFine," +
                            " MaxIscritti, MaxTeam, DataInizioReg, DataFineReg, DescrizioneProb)" +
                            " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, Titolo);
            ps.setString(2, Indirizzo);
            ps.setInt(3, NCivico);
            ps.setObject(4, DataInizio);
            ps.setObject(5, DataFine);
            ps.setInt(6, MaxIscritti);
            ps.setInt(7, MaxTeam);
            ps.setObject(8, DataInizioReg);
            ps.setObject(9, DataFineReg);
            ps.setString(10, DescrizioneProb);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
        return getIdEventoDB();
    }

    public void addAllEventiDB(ArrayList<Evento> eventi) throws SQLException {
        for(Evento currentEvento : eventi){
            try {
                addEventoDB(currentEvento);
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void updateEventoDB(int IdEvento, LocalDate DataInizio, LocalDate DataFine, int MaxIscritti, int MaxTeam, LocalDate DataInizioReg, LocalDate DataFineReg, String DescrizioneProb) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE Evento SET DataInizio = ?, DataFine = ?," +
                            " MaxIscritti = ?, MaxTeam = ?, DataInizioReg = ?, DataFineReg = ?, DescrizioneProb = ?" +
                            " WHERE IdEvento = " + IdEvento + ";");
            ps.setObject(1, DataInizio);
            ps.setObject(2, DataFine);
            ps.setInt(3, MaxIscritti);
            ps.setInt(4, MaxTeam);
            ps.setObject(5, DataInizioReg);
            ps.setObject(6, DataFineReg);
            ps.setString(7, DescrizioneProb);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public Utente getUtenteDB(String NomeUtente) throws SQLException{
        Utente utente = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante WHERE NomeUtente = '"
                            + NomeUtente + "' AND NomeUtente NOT IN(" +
                            "SELECT NomePartecipante FROM PartecipantiEvento WHERE NomePartecipante = '" + NomeUtente + "');");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                utente = new Utente();
                rs.next();
                utente.setNomeUtente(rs.getString("nomeutente"));
                utente.setPasswordUtente(rs.getString("password"));
                utente.setFnome(rs.getString("fNome"));
                utente.setMnome(rs.getString("mNome"));
                utente.setLnome(rs.getString("lNome"));
                utente.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                rs.close();
            }

        }
        catch(SQLException e) {
            throw e;
        }
        return utente;
    }

    public void addUtenteDB(Utente utente) throws SQLException{
        try {
            addUtenteDB(utente.getNomeUtente(), utente.getPasswordUtente(), utente.getFNome(), utente.getMNome(), utente.getLNome(), utente.getDataNascita());
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addUtenteDB(String NomeUtente, String Password) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password) VALUES('"
                            + NomeUtente + "','" + Password + "');");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addUtenteDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password," +
                            " FNome, MNome, LNome, DataNascita" +
                            " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, NomeUtente);
            ps.setString(2, Password);
            ps.setString(3, FNome);
            ps.setString(4, MNome);
            ps.setString(5, LNome);
            ps.setObject(6, DataNascita);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllUtentiDB(ArrayList<Utente> utenti) throws SQLException{
        for(Utente currentUtente : utenti){
            try {
                addUtenteDB(currentUtente);
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public Partecipante getPartecipanteDB(String NomePartecipante) throws SQLException{
        Partecipante partecipante = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante JOIN PartecipanteEvento ON NomeUtente = NomePartecipante" +
                            " WHERE NomeUtente = '" + NomePartecipante + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                partecipante = new Partecipante();
                rs.next();
                partecipante.setNomeUtente(rs.getString("nomeutente"));
                partecipante.setPasswordUtente(rs.getString("password"));
                partecipante.setFnome(rs.getString("fNome"));
                partecipante.setMnome(rs.getString("mNome"));
                partecipante.setLnome(rs.getString("lNome"));
                partecipante.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                partecipante.addEvento(getEventoDB(rs.getInt("idevento")));
                while(rs.next()){
                    partecipante.addEvento(getEventoDB(rs.getInt("idevento")));
                }
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return partecipante;
    }

    public Partecipante getPartecipanteDB(String NomePartecipante, Evento evento) throws SQLException{
        Partecipante partecipante = null;
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante JOIN PartecipanteEvento ON NomeUtente = NomePartecipante" +
                            " WHERE NomeUtente = '" + NomePartecipante + "' AND idEvento = " + idEvento + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                partecipante = new Partecipante();
                rs.next();
                partecipante.setNomeUtente(rs.getString("nomeutente"));
                partecipante.setPasswordUtente(rs.getString("password"));
                partecipante.setFnome(rs.getString("fNome"));
                partecipante.setMnome(rs.getString("mNome"));
                partecipante.setLnome(rs.getString("lNome"));
                partecipante.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                rs.close();
                partecipante.addEvento(evento);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return partecipante;
    }

    public void addPartecipanteDB(Partecipante partecipante, int idEvento) throws SQLException{
        try {
            addPartecipanteDB(partecipante.getNomeUtente(), partecipante.getPasswordUtente(), partecipante.getFNome(), partecipante.getMNome(), partecipante.getLNome(), partecipante.getDataNascita(), idEvento);
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addPartecipanteDB(String NomeUtente, String Password, int idEvento) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password) VALUES('"
                            + NomeUtente + "','" + Password + "');CALL IscriviEvento('" + NomeUtente + "',"
                            + idEvento + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addPartecipanteDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password," +
                            " FNome, MNome, LNome, DataNascita" +
                            " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, NomeUtente);
            ps.setString(2, Password);
            ps.setString(3, FNome);
            ps.setString(4, MNome);
            ps.setString(5, LNome);
            ps.setObject(6, DataNascita);
            ps.executeUpdate();
            ps = connection.prepareStatement("CALL IscriviEvento('" + NomeUtente + "'," + idEvento + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllPartecipanteDB(ArrayList<Partecipante> partecipanti, int idEvento) throws SQLException {
        for(Partecipante currentPartecipante : partecipanti){
            try {
                addPartecipanteDB(currentPartecipante, idEvento);
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void updatePartecipanteDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE Partecipante SET FNome = ?, MNome = ?, LNome = ?, DataNascita = ?" +
                            " WHERE NomeUtente = " + NomeUtente + ";");
            ps.setString(1, FNome);
            ps.setString(2, MNome);
            ps.setString(3, LNome);
            ps.setObject(4, DataNascita);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public Organizzatore getOrganizzatoreDB(String NomeUtente) throws SQLException {
        Organizzatore organizzatore = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Organizzatore WHERE NomeUtente = '" + NomeUtente + "';");
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                organizzatore = new Organizzatore();
                rs.next();
                organizzatore.setNomeUtente(rs.getString("nomeutente"));
                organizzatore.setPasswordUtente(rs.getString("password"));
                organizzatore.setFnome(rs.getString("fNome"));
                organizzatore.setMnome(rs.getString("mNome"));
                organizzatore.setLnome(rs.getString("lNome"));
                organizzatore.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                organizzatore.selezionaEvento(getEventoDB(rs.getInt("idevento")));
                rs.close();
            }
        } catch (SQLException e) {
            throw e;
        }
        return organizzatore;
    }

    public Organizzatore getOrganizzatoreDB(Evento evento) throws SQLException{
        Organizzatore organizzatore = null;
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Organizzatore WHERE idEvento =" + idEvento + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                organizzatore = new Organizzatore();
                rs.next();
                organizzatore.setNomeUtente(rs.getString("nomeutente"));
                organizzatore.setPasswordUtente(rs.getString("password"));
                organizzatore.setFnome(rs.getString("fNome"));
                organizzatore.setMnome(rs.getString("mNome"));
                organizzatore.setLnome(rs.getString("lNome"));
                organizzatore.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                rs.close();
                organizzatore.selezionaEvento(evento);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return organizzatore;
    }

    public void addOrganizzatoreDB(Organizzatore organizzatore) throws SQLException{
        try {
            addOrganizzatoreDB(organizzatore.getNomeUtente(), organizzatore.getPasswordUtente(), organizzatore.getFNome(), organizzatore.getMNome(), organizzatore.getLNome(), organizzatore.getDataNascita(), organizzatore.getIdEventoOrganizzato());
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addOrganizzatoreDB(String NomeUtente, String Password, int idEvento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Organizzatore(NomeUtente, Password, idEvento) VALUES('"
                            + NomeUtente + "','" + Password + "'," + idEvento + " );");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addOrganizzatoreDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Organizzatore(NomeUtente, Password, FNome, MNome, LNome, DataNascita, idEvento)" +
                            "VALUES(?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, NomeUtente);
            ps.setString(2, Password);
            ps.setString(3, FNome);
            ps.setString(4, MNome);
            ps.setString(5, LNome);
            ps.setObject(6, DataNascita);
            ps.setObject(7, idEvento);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllOrganizzatoriDB(ArrayList<Organizzatore> organizzatori) throws SQLException{
        for(Organizzatore currentOrganizzatore : organizzatori){
            try {
                addOrganizzatoreDB(currentOrganizzatore);
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void updateOrganizzatoreDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE Organizzatore SET FNome = ?, MNome = ?, LNome = ?, DataNascita = ?" +
                            " WHERE NomeUtente = " + NomeUtente + ";");
            ps.setString(1, FNome);
            ps.setString(2, MNome);
            ps.setString(3, LNome);
            ps.setObject(4, DataNascita);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public Giudice getGiudiceDB(String NomeUtente) throws SQLException {
        Giudice giudice = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Giudice JOIN GiudiceEvento ON NomeUtente = NomeGiudice" +
                            " WHERE NomeUtente = '" + NomeUtente + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                giudice = new Giudice();
                rs.next();
                giudice.setNomeUtente(rs.getString("nomeutente"));
                giudice.setPasswordUtente(rs.getString("password"));
                giudice.setFnome(rs.getString("fNome"));
                giudice.setMnome(rs.getString("mNome"));
                giudice.setLnome(rs.getString("lNome"));
                giudice.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                giudice.setEvento(getEventoDB(rs.getInt("idevento")));
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return giudice;
    }

    public Giudice getGiudiceDB(String NomeUtente, Evento evento) throws SQLException {
        Giudice giudice = null;
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Giudice JOIN GiudiceEvento ON NomeUtente = NomeGiudice" +
                            " WHERE NomeUtente = '" + NomeUtente + "' AND idEvento =" + idEvento + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                giudice = new Giudice();
                rs.next();
                giudice.setNomeUtente(rs.getString("nomeutente"));
                giudice.setPasswordUtente(rs.getString("password"));
                giudice.setFnome(rs.getString("fNome"));
                giudice.setMnome(rs.getString("mNome"));
                giudice.setLnome(rs.getString("lNome"));
                giudice.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                rs.close();
                giudice.setEvento(evento);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return giudice;
    }

    public void addGiudiceDB(Giudice giudice) throws SQLException{
        try {
            addGiudiceDB(giudice.getNomeUtente(), giudice.getPasswordUtente(), giudice.getFNome(), giudice.getMNome(), giudice.getLNome(), giudice.getDataNascita(), giudice.getIdEventoGiudicato());
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addGiudiceDB(String NomeUtente, String Password, int idEvento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Giudice(NomeUtente, Password) VALUES('"
                            + NomeUtente + "','" + Password + "');INSERT INTO GiudiceEvento(NomeUtente, idEvento) VALUES('"
                            + NomeUtente + "'," + idEvento + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addGiudiceDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Organizzatore(NomeUtente, Password, FNome, MNome, LNome, DataNascita, idEvento)" +
                            "VALUES(?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, NomeUtente);
            ps.setString(2, Password);
            ps.setString(3, FNome);
            ps.setString(4, MNome);
            ps.setString(5, LNome);
            ps.setObject(6, DataNascita);
            ps.setObject(7, idEvento);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllGiudiciDB(ArrayList<Giudice> giudici) throws SQLException{
        for(Giudice currentGiudice : giudici){
            try {
                addGiudiceDB(currentGiudice);
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void updateGiudiceDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE Giudice SET FNome = ?, MNome = ?, LNome = ?, DataNascita = ?" +
                            " WHERE NomeUtente = " + NomeUtente + ";");
            ps.setString(1, FNome);
            ps.setString(2, MNome);
            ps.setString(3, LNome);
            ps.setObject(4, DataNascita);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public Team getTeamDB(int IdTeam) throws SQLException{
        Team team = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Team AS t JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                            "WHERE t.IdTeam = " + IdTeam + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                team = new Team(IdTeam);
                rs.next();
                Evento evento = getEventoDB(rs.getInt("idevento"));
                Partecipante part = getPartecipanteDB(rs.getString("nomepartecipante"), evento);
                team.addMembroTeam(part);
                while(rs.next()) {
                    part = getPartecipanteDB(rs.getString("nomepartecipante"), evento);
                    team.addMembroTeam(part);
                }
                rs.close();
                team.setEventoIscritto(evento);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return team;
    }

    public int getIdTeamDB() throws SQLException{
        int id;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT MAX(IdTeam) FROM Team;");
            ResultSet rs = ps.executeQuery();
            rs.next();
            id = rs.getInt(1);
        }
        catch (SQLException e) {
            throw e;
        }
        return id;
    }

    public int addTeamDB(String NomeUtente, int idEvento) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("CALL CreaTeam('" + NomeUtente + "'," + idEvento + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
        return getIdTeamDB();
    }

    public void addCompTeamDB(String NomeUtente, int idTeam) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("CALL JoinTeam('" + NomeUtente + "'," + idTeam + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllCompTeamDB(ArrayList<Partecipante> partecipanti, int idTeam) throws SQLException {
        for(Partecipante currentPartecipante : partecipanti){
            try {
                addCompTeamDB(currentPartecipante.getNomeUtente(), idTeam);
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public Progresso getProgressoDB(int IdProgresso) throws SQLException{
        Progresso progresso = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Progresso WHERE IdProgresso = " + IdProgresso + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                progresso = new Progresso(IdProgresso);
                rs.next();
                progresso.setIdTeam(rs.getInt("idteam"));
                progresso.setDataProgresso(rs.getObject("datapubblicazione", LocalDate.class));
                progresso.setTestoDocumeto(rs.getString("testo"));
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return progresso;
    }

    public ArrayList<Progresso> getAllProgressoDB(int idTeam) throws SQLException{
        ArrayList<Progresso> progressi = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Progresso WHERE idTeam = " + idTeam + ";");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Progresso progresso = new Progresso(rs.getInt("idprogresso"));
                progresso.setIdTeam(idTeam);
                progresso.setDataProgresso(rs.getObject("datapubblicazione", LocalDate.class));
                progresso.setTestoDocumeto(rs.getString("testo"));
                if(progressi == null){
                    progressi = new ArrayList<Progresso>();
                }
                progressi.add(progresso);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return progressi;
    }

    public int getIdProgressoDB() throws SQLException{
        int id = -1;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT MAX(IdProgresso) FROM Progresso;");
            ResultSet rs = ps.executeQuery();
            rs.next();
            id = rs.getInt(1);
        }
        catch (SQLException e) {
            throw e;
        }
        return id;
    }

    public void addProgressoDB(Progresso progresso) throws SQLException{
        try{
            progresso.setIdProgresso(addProgressoDB(progresso.getIdTeam(), progresso.getTestoDocumeto()));
        }
        catch(SQLException e){
            throw e;
        }
    }

    public int addProgressoDB(int idTeam, String testo) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Progresso(idTeam, Testo) VALUES("
                            + idTeam + ",'" + testo + "');");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
        return getIdProgressoDB();
    }

    public Commento getCommentoDB(int idProgresso, String Giudice) throws SQLException{
        Commento commento = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Commento WHERE idProgresso = " + idProgresso +
                            "AND NomeGiudice = '" + Giudice + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                commento = new Commento(idProgresso, Giudice);
                rs.next();
                commento.setTesto(rs.getString("testo"));
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return commento;
    }

    public ArrayList<Commento> getAllCommentoDB(int idProgresso) throws SQLException{
        ArrayList<Commento> commenti = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Commento WHERE idProgresso = " + idProgresso + ";");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Commento commento = new Commento(idProgresso, rs.getString("nomegiudice"), rs.getString("testo"));
                if(commenti == null){
                    commenti = new ArrayList<Commento>();
                }
                commenti.add(commento);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return commenti;
    }

    public void addCommentoDB(Commento commento) throws SQLException{
        try{
            addCommentoDB(commento.getGiudice(), commento.getIdProgresso(), commento.getTesto());
        }
        catch(SQLException e){
            throw e;
        }
    }

    public void addCommentoDB(String NomeGiudice, int idProgresso, String testo) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Commento(NomeGiudice, idProgresso, Testo) VALUES('"
                            + NomeGiudice + "'," + idProgresso + ",'" + testo + "');");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public Voto getVotoDB(int idTeam, String Giudice) throws SQLException {
        Voto voto = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Voto WHERE idTeam = " + idTeam +
                            "AND NomeGiudice = '" + Giudice + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                voto = new Voto(idTeam, Giudice);
                rs.next();
                voto.setValore(rs.getInt("valore"));
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return voto;
    }

    public ArrayList<Voto> getAllVotoDB(int idTeam) throws SQLException{
        ArrayList<Voto> voti = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Voto WHERE idTeam = " + idTeam + ";");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Voto voto = new Voto(idTeam, rs.getInt("valore"), rs.getString("nomegiudice"));
                if(voti == null){
                    voti = new ArrayList<Voto>();
                }
                voti.add(voto);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return voti;
    }

    public void addVotoDB(Voto voto) throws SQLException{
        try{
            addVotoDB(voto.getGiudice(), voto.getIdTeam(), voto.getValore());
        }
        catch(SQLException e){
            throw e;
        }
    }

    public void addVotoDB(String NomeGiudice, int idTeam, int valore) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Voto(NomeGiudice, idTeam, Valore) VALUES('"
                            + NomeGiudice + "'," + idTeam + "," + valore + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }
}
