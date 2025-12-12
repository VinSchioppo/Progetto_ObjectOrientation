package ClassModel;

public class Commento {

    private int idProgresso;
    private String Testo;
    private String Giudice;

    Commento(int idProgresso, String Testo, String Giudice) {
    
        this.idProgresso = idProgresso;
        this.Testo = Testo;
        this.Giudice = Giudice;
    }

    public void setGiudice(String giudice) {Giudice = giudice;}

    public int getIdProgresso(){return this.idProgresso;}
    public String getTesto() {return Testo;}
    public String getGiudice() {return Giudice;}
}
