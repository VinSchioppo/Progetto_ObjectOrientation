package RecordList;

public class Invite<T> {
    private Boolean answer = null;
    private T record = null;

    public Invite(T record) {
        this.record = record;
    }

    public Invite(T record, Boolean answer) {
        this.record = record;
        this.answer = answer;
    }

    public Boolean getAnswer() {return answer;}
    public T getRecordInvite() {return record;}

    public void setAnswer(boolean answer) {
        this.answer = answer;}
}
