package com.example.laborator_7.Domain;

import com.example.laborator_7.Utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Message extends Entity<UUID> {

    private User from;
    private List<User> to;
    private String messageText;
    private LocalDateTime dateTime;
    private Message replyMessage;

    public Message(User from, List<User> to, String messageText, LocalDateTime dateTime) {
        this.id = UUID.randomUUID();
        this.from = from;
        this.to = to;
        this.messageText = messageText;
        this.dateTime = dateTime;
        this.replyMessage = null;
    }

    public Message(UUID uuid, User from, List<User> to, String messageText, LocalDateTime dateTime) {
        setId(uuid);
        this.from = from;
        this.to = to;
        this.messageText = messageText;
        this.dateTime = dateTime;
        this.replyMessage = null;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Message getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(Message replyMessage) {
        this.replyMessage = replyMessage;
    }

    @Override
    public String toString() {
//        return "Message{" +
//                "from=" + from.getEmail() +
//                ", to=" + to.stream().map(User::getEmail).toList() +
//                ", messageText='" + messageText + '\'' +
//                ", dateTime=" + dateTime.format(Constants.DATE_TIME_FORMATTER) +
//                '}';
        return "from=" + from.getEmail() +
                ", message=" + messageText;
    }

}
