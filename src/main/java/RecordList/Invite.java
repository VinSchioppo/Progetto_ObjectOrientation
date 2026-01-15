package RecordList;

public class Invite<T> {
    private Boolean Answer = null;
    private T Record = null;

    public Invite(T Record) {
        this.Record = Record;
    }

    public Invite(T Record, Boolean Answer) {
        this.Record = Record;
        this.Answer = Answer;
    }

    public Boolean getAnswer() {return Answer;}
    public T getRecordInvite() {return Record;}

    public void setAnswer(boolean answer) {Answer = answer;}
}
