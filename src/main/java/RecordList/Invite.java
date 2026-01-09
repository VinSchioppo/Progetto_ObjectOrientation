package RecordList;

public class Invite<T> {
    private Boolean Answer = null;
    private T Record = null;

    public Invite(T Record) {
        this.Record = Record;
    }

    public Boolean getAnswer() {return Answer;}
    public T getRecordInvite() {return Record;}

    public void setAnswer(boolean answer) {Answer = answer;}
}
