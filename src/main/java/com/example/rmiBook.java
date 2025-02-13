package com.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class rmiBook  extends UnicastRemoteObject implements Book {
    protected rmiBook() throws RemoteException {
        super();
    }

    @Override
    public String toString() {
        return "sus";
    }
    
}
