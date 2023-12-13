package dev.levelupschool.backend.exception;

public class CommunicationException extends RuntimeException{
    public CommunicationException(String message){
        super(message);
    }
}
