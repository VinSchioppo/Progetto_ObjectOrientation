package classmodel;

public class Commento {

    private int idProgresso;
    private String testo;
    private String giudice;

    public Commento(int idProgresso, String giudice){
        this.idProgresso = idProgresso;
        this.giudice = giudice;
    }

    public Commento(int idProgresso, String testo, String giudice) {
    
        this.idProgresso = idProgresso;
        this.testo = testo;
        this.giudice = giudice;
    }

    public void setIdProgresso(int idProgresso) {this.idProgresso = idProgresso;}
    public void setTesto(String testo) {this.testo = testo;}
    public void setGiudice(String giudice) {
        this.giudice = giudice;}

    public int getIdProgresso(){return this.idProgresso;}
    public String getTesto() {return testo;}
    public String getGiudice() {return giudice;}
}
