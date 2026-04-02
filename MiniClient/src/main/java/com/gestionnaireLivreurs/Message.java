package com.gestionnaireLivreurs;

public class Message {
    private static int nextId = 1;

    private int id;
    private String contenu; //  {idLivreur | *}  {le message texte}

    public Message(int idClient, String contenu) {
        this.id = idClient * 10000 + Message.nextId;
        this.contenu = contenu;

        Message.nextId++;
    }

    public int getId() {
        return id;
    }

    public String getContenu() {
        return contenu;
    }

    @Override
    public String toString() {
        return this.id + " " + this.contenu;
    }
}
