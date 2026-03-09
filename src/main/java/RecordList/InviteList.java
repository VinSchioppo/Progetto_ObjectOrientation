package RecordList;
import java.util.List;

public class InviteList<T>{
    private RecordList<Invite<T>> invites;

    public int size() {
        if(invites != null)
            return invites.size();
        return 0;
    }

    public void setInvites(List<T> records) {
        if(records != null) {
            if(invites == null)
                invites = new RecordList<Invite<T>>();
            for (T record : records) {
                invites.addRecord(new Invite<T>(record));
            }
        }
    }

    public void setInviteAnswer(boolean answer) {
        if(invites != null) {
            invites.getRecord().setAnswer(answer);
        }
    }

    public void addInvite(T record, Boolean answer) {
        if(invites == null)
            invites = new RecordList<Invite<T>>();
        Invite<T> invite = new Invite<T>(record, answer);
        invites.addRecord(invite);
    }

    public void addInvite(T record) {
        if(invites == null)
            invites = new RecordList<Invite<T>>();
        invites.addRecord(new Invite<T>(record));
    }

    public void removeInvite(){
        if(invites != null) {
            invites.removeRecord();
        }
    }

    @javax.annotation.CheckForNull
    public Boolean getInviteAnswer(){
        if(invites != null && invites.getRecord() != null)
            return invites.getRecord().getAnswer();

        return null;
    }

    public T getInvite(){
        if(invites != null && invites.getRecord() != null)
            return invites.getRecord().getRecordInvite();

        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean firstInviteAnswer(){
        if(invites != null && invites.firstRecord() != null)
            return invites.getRecord().getAnswer();

        return null;
    }

    public T firstInvite(){
        if(invites != null && invites.firstRecord() != null)
            return invites.getRecord().getRecordInvite();

        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean previousInviteAnswer(){
        if(invites != null && invites.previousRecord() != null)
            return invites.getRecord().getAnswer();

        return null;
    }

    public T previousInvite(){
        if(invites != null && invites.previousRecord() != null)
            return invites.getRecord().getRecordInvite();

        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean nextInviteAnswer(){
        if(invites != null && invites.nextRecord() != null)
            return invites.getRecord().getAnswer();

        return null;
    }

    public T nextInvite(){
        if(invites != null && invites.nextRecord() != null)
            return invites.getRecord().getRecordInvite();

        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean lastInviteAnswer(){
        if(invites != null && invites.lastRecord() != null)
            return invites.getRecord().getAnswer();

        return null;
    }

    public T lastInvite(){
        if(invites != null && invites.lastRecord() != null)
            return invites.getRecord().getRecordInvite();

        return null;
    }
}
