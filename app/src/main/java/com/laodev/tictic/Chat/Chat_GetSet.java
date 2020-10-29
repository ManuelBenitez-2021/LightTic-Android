package com.laodev.tictic.Chat;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Chat_GetSet {
  String receiver_id,sender_id,chat_id,sender_name,text,pic_url,status,time,timestamp,type;

    public Chat_GetSet() {

    }

  public String getReceiver_id() {
    return receiver_id;
  }

  public void setReceiver_id(String receiver_id) {
    this.receiver_id = receiver_id;
  }

  public String getSender_id() {
    return sender_id;
  }

  public void setSender_id(String sender_id) {
    this.sender_id = sender_id;
  }

  public String getChat_id() {
    return chat_id;
  }

  public void setChat_id(String chat_id) {
    this.chat_id = chat_id;
  }

  public String getSender_name() {
    return sender_name;
  }

  public void setSender_name(String sender_name) {
    this.sender_name = sender_name;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPic_url() {
    return pic_url;
  }

  public void setPic_url(String pic_url) {
    this.pic_url = pic_url;
  }

}
