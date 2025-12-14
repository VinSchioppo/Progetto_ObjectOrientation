package ClassModel;

public class Commento {

    private int idProgresso;
    private String Testo;
    private String Giudice;

    public Commento(int idProgresso, String Giudice){
        this.idProgresso = idProgresso;
        this.Giudice = Giudice;
    }

    public Commento(int idProgresso, String Testo, String Giudice) {
    
        this.idProgresso = idProgresso;
        this.Testo = Testo;
        this.Giudice = Giudice;
    }

    public void setIdProgresso(int idProgresso) {this.idProgresso = idProgresso;}
    public void setTesto(String Testo) {this.Testo = Testo;}
    public void setGiudice(String giudice) {Giudice = giudice;}

    public int getIdProgresso(){return this.idProgresso;}
    public String getTesto() {return Testo;}
    public String getGiudice() {return Giudice;}
}
