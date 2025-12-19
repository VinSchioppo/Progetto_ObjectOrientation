package RecordList;

import java.util.ArrayList;

public class RecordList <T>
{
    private int current = -1;
    private ArrayList<T> records = null;

    public int size() {
       if(records != null) return records.size();
       return 0;
    }

    public void setRecords(ArrayList<T> Records) {
        current = 0;
        records = Records;
    }

    public void addRecord(T record) {
        if(records == null) {
            current = 0;
            records = new ArrayList<T>();
        }
        records.add(record);
    }

    public void removeRecord(){
        if(getRecord() != null) {
            records.remove(current);
            if(records.isEmpty()) {
                current = -1;
                records = null;
            }
            else firstRecord();
        }
    }

    public T getRecord(){
        if(records != null) {
            if(current >= 0 && current < records.size()){
                return records.get(current);
            }
        }
        else current = -1;
        return null;
    }

    public T firstRecord(){
        current = 0;
        return getRecord();
    }

    public T previousRecord(){
        if(current >= 0) {
            current--;
        }
        return getRecord();
    }

    public T nextRecord(){
        if(current < records.size()) {
            current++;
        }
        return getRecord();
    }

    public T lastRecord(){
        current = records.size() - 1;
        return getRecord();
    }
}
