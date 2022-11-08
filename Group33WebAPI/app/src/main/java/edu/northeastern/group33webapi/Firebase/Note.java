package edu.northeastern.group33webapi.Firebase;

public class Note implements Comparable<Note>{
    public int id;
    public String fromUser;
    public String toUser;
    public String sendTime; //format "09-23-2022"
    public String receivedTime;
    //in the list of received stickers, this is the received time
    //in the list of sent stickers, this is the sent time
    public Note(int id, String fromUser, String toUser, String sendTime) {
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.sendTime = sendTime;
    }

    public String getKey() {
        return id + "|" + fromUser + "|" + toUser + "|" + sendTime;
    }

    public int getId() {
        return id;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getSendTime() {
        return sendTime;
    }

    @Override
    public int compareTo(Note other) {
        return this.sendTime.compareTo(other.getSendTime());
    }
}
