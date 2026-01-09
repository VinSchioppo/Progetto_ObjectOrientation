package ClassModel;

import RecordList.RecordList;
import java.util.ArrayList;
import java.time.LocalDate;

public class Progresso {

    private int idProgresso;
    private int idTeam;
    private LocalDate DataProgresso;
    private String TestoDocumeto;

    private RecordList<Commento> Commenti = null;

    public Progresso(int idProgresso) {this.idProgresso = idProgresso;}
    public Progresso(String testo, int idTeam) {

        DataProgresso = LocalDate.now();
        TestoDocumeto = testo;
        this.idTeam = idTeam;

    }

    public void setIdProgresso(int idProgresso) {this.idProgresso = idProgresso;}
    public void setIdTeam(int idTeam) {this.idTeam = idTeam;}
    public void setDataProgresso(LocalDate dataProgresso) {this.DataProgresso = dataProgresso;}
    public void setTestoDocumeto(String TestoDocumento) {this.TestoDocumeto = TestoDocumento;}
    public void setCommenti(ArrayList<Commento> commenti){
        if(Commenti == null)
            Commenti = new RecordList<Commento>();
        Commenti.setRecords(commenti);
    }

    public int getIdProgresso() {return idProgresso;}
    public LocalDate getDataProgresso() {return this.DataProgresso;}
    public String getTestoDocumeto() {return this.TestoDocumeto;}
    public int getIdTeam() {return this.idTeam;}

    public void addCommento(Commento commento) {

        if(Commenti == null)
            Commenti = new RecordList<Commento>();

        Commenti.addRecord(commento);
    }

    public boolean removeCommento(String nomeGiudice) {
        if(Commenti != null){
            seekCommento(nomeGiudice);
            Commenti.removeRecord();
            return true;
        }
        return false;
    }

    public Commento getCommento(){
        if(Commenti != null){
            return Commenti.getRecord();
        }
        return null;
    }

    public Commento firstCommento(){
        if(Commenti != null){
            return Commenti.firstRecord();
        }
        return null;
    }

    public Commento previousCommento(){
        if(Commenti != null){
            return Commenti.previousRecord();
        }
        return null;
    }

    public Commento nextCommento(){
        if(Commenti != null){
            return Commenti.nextRecord();
        }
        return null;
    }

    public Commento lastCommento(){
        if(Commenti != null){
            return Commenti.lastRecord();
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
        if(Commenti != null){
            Commento commento = seekCommento(nomeGiudice);
            Commenti.removeRecord();
            return commento;
        }
        return null;
    }

}