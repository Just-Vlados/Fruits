import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class Message implements JSONAware{

    private String userName;
    private int id;
    private String text;
    //private String time;
    private boolean deleted=false;
    private boolean changed=false;

    public Message() {
        this.userName = "Name";
        this.id = -1;
        this.text = "";
        DateFormat df = DateFormat.getTimeInstance(DateFormat.DEFAULT);
        Date currentDate = new Date();
        //time = df.format(currentDate);
    }

    public Message(String text, String userName) {
        this.userName = userName;
        this.text = text;
        this.id = -1;
        DateFormat df = DateFormat.getTimeInstance(DateFormat.DEFAULT);
        Date currentDate = new Date();
       // time = df.format(currentDate);
    }

    public Message(int id, String userName, String text) {
        this.userName = userName;
        this.id = id;
        this.text = text;
        DateFormat df = DateFormat.getTimeInstance(DateFormat.DEFAULT);
        Date currentDate = new Date();
        //time = df.format(currentDate);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public void deleteMessage() {
        this.text = "";
        this.setDeleted(true);
    }
    public static Message parseMessage(JSONObject obj) {
        Message mes = new Message();
        mes.setUserName((String) obj.get("username"));
        mes.setText((String) obj.get("message"));
        //mes.setTime((String) obj.get("time"));
        if(obj.get("id") != null) {
            mes.setId(Integer.parseInt(obj.get("id").toString()));
        }
        return mes;
    }
    @Override
    public String toString() {
        return  this.userName +" : " + this.text;
    }
    @Override
    public String toJSONString() {
        JSONObject obj = new JSONObject();
        obj.put("username", userName);
        obj.put("message", text);
        obj.put("id", id);
        //obj.put("time", time);
        return obj.toString();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        Message msg = (Message) obj;
        if (id != msg.id) return false;
        return true;
    }
}