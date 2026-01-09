package RecordList;

import java.util.ArrayList;

public class InviteList<T>{
    private RecordList<Invite<T>> Invites;

    public int size() {
        if(Invites != null)
            return Invites.size();
        return 0;
    }

    public void setInvites(ArrayList<T> Records) {
        if(Records != null) {
            if(Invites == null)
                Invites = new RecordList<Invite<T>>();
            for (T Record : Records) {
                Invites.addRecord(new Invite<T>(Record));
            }
        }
    }

    public void setInviteAnswer(boolean Answer) {
        if(Invites != null) {
            Invites.getRecord().setAnswer(Answer);
        }
    }

    public void addInvite(T record) {
        if(Invites == null)
            Invites = new RecordList<Invite<T>>();
        Invites.addRecord(new Invite<T>(record));
    }

    public void removeInvite(){
        if(Invites != null) {
            Invites.removeRecord();
        }
    }

    public Boolean getInviteAnswer(){
        if(Invites != null) {
            if(Invites.getRecord() != null)
                return Invites.getRecord().getAnswer();
        }
        return null;
    }

    public T getInvite(){
        if(Invites != null) {
            if(Invites.getRecord() != null)
                return Invites.getRecord().getRecordInvite();
        }
        return null;
    }

    public Boolean firstInviteAnswer(){
        if(Invites != null) {
            if(Invites.firstRecord() != null)
                return Invites.getRecord().getAnswer();
        }
        return null;
    }

    public T firstInvite(){
        if(Invites != null) {
            if(Invites.firstRecord() != null)
                return Invites.getRecord().getRecordInvite();
        }
        return null;
    }

    public Boolean previousInviteAnswer(){
        if(Invites != null) {
            if(Invites.previousRecord() != null)
                return Invites.getRecord().getAnswer();
        }
        return null;
    }

    public T previousInvite(){
        if(Invites != null) {
            if(Invites.previousRecord() != null)
                return Invites.getRecord().getRecordInvite();
        }
        return null;
    }

    public Boolean nextInviteAnswer(){
        if(Invites != null) {
            if(Invites.nextRecord() != null)
                return Invites.getRecord().getAnswer();
        }
        return null;
    }

    public T nextInvite(){
        if(Invites != null) {
            if(Invites.nextRecord() != null)
                return Invites.getRecord().getRecordInvite();
        }
        return null;
    }

    public Boolean lastInviteAnswer(){
        if(Invites != null) {
            if(Invites.lastRecord() != null)
                return Invites.getRecord().getAnswer();
        }
        return null;
    }

    public T lastInvite(){
        if(Invites != null) {
            if(Invites.lastRecord() != null)
                return Invites.getRecord().getRecordInvite();
        }
        return null;
    }
}
