package com.inotes.strategy;

public class NoteException extends RuntimeException {
    public NoteException(String message, Throwable cause) { super(message, cause); }
    public NoteException(String message) { super(message); }
}
