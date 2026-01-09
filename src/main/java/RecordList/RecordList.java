package RecordList;

import java.util.ArrayList;

public class RecordList <T>
{
    private int Current = -1;
    private ArrayList<T> Records = null;

    public int size() {
       if(Records != null) return Records.size();
       return 0;
    }

    public void setRecords(ArrayList<T> Records) {
        Current = 0;
        this.Records = Records;
    }

    public void addRecord(T record) {
        if(Records == null) {
            Current = 0;
            Records = new ArrayList<T>();
        }
        Records.add(record);
    }

    public void removeRecord(){
        if(getRecord() != null) {
            Records.remove(Current);
            if(Records.isEmpty()) {
                Current = -1;
                Records = null;
            }
            else firstRecord();
        }
    }

    public T getRecord(){
        if(Records != null) {
            if(Current >= 0 && Current < Records.size()){
                return Records.get(Current);
            }
        }
        else Current = -1;
        return null;
    }

    public T firstRecord(){
        Current = 0;
        return getRecord();
    }

    public T previousRecord(){
        if(Current >= 0) {
            Current--;
        }
        return getRecord();
    }

    public T nextRecord(){
        if(Current < Records.size()) {
            Current++;
        }
        return getRecord();
    }

    public T lastRecord(){
        Current = Records.size() - 1;
        return getRecord();
    }
}
