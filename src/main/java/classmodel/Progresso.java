package classmodel;

import recordlist.RecordList;

import java.time.LocalDate;
import java.util.List;

public class Progresso {

    private int idProgresso = -1;
    private int idTeam = -1;
    private LocalDate dataProgresso = null;
    private String testoDocumeto = null;

    private RecordList<Commento> commenti = null;

    public Progresso(int idProgresso, int idTeam, LocalDate dataProgresso, String testo) {
        this.idProgresso = idProgresso;
        this.idTeam = idTeam;
        this.dataProgresso = dataProgresso;
        testoDocumeto = testo;
    }
    public Progresso(String testo, int idTeam) {
        dataProgresso = LocalDate.now();
        testoDocumeto = testo;
        this.idTeam = idTeam;
    }

    public void setIdProgresso(int idProgresso) {this.idProgresso = idProgresso;}
    public void setIdTeam(int idTeam) {this.idTeam = idTeam;}
    public void setDataProgresso(LocalDate dataProgresso) {this.dataProgresso = dataProgresso;}
    public void setTestoDocumeto(String testoDocumento) {this.testoDocumeto = testoDocumento;}
    public void setCommenti(List<Commento> commenti){
        if(this.commenti == null)
            this.commenti = new RecordList<>();
        this.commenti.setRecords(commenti);
    }

    public int getIdProgresso() {return idProgresso;}
    public LocalDate getDataProgresso() {return this.dataProgresso;}
    public String getTestoDocumeto() {return this.testoDocumeto;}
    public int getIdTeam() {return this.idTeam;}

    public void addCommento(Commento commento) {

        if(commenti == null)
            commenti = new RecordList<>();

        commenti.addRecord(commento);
    }

    public boolean removeCommento() {
        if(commenti != null){
            commenti.removeRecord();
            return true;
        }
        return false;
    }

    public Commento getCommento(){
        if(commenti != null){
            return commenti.getRecord();
        }
        return null;
    }

    public Commento firstCommento(){
        if(commenti != null){
            return commenti.firstRecord();
        }
        return null;
    }

    public Commento previousCommento(){
        if(commenti != null){
            return commenti.previousRecord();
        }
        return null;
    }

    public Commento nextCommento(){
        if(commenti != null){
            return commenti.nextRecord();
        }
        return null;
    }

    public Commento lastCommento(){
        if(commenti != null){
            return commenti.lastRecord();
        }
        return null;
    }

    public Commento seekCommento(String nomeGiudice){
        Commento commento = firstCommento();
        while(commento != null){
            if(commento.getGiudice().equals(nomeGiudice)) {
                return commento;
            }
            else commento = nextCommento();
        }
        return null;
    }

    public Commento seekAndRemoveCommento(String nomeGiudice){
        Commento commento = seekCommento(nomeGiudice);
        removeCommento();
        return commento;
    }

}