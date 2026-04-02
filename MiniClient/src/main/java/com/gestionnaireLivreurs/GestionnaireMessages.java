package com.gestionnaireLivreurs;

import com.atoudeft.client.Client;
import com.atoudeft.commun.evenement.Arguments;
import com.atoudeft.commun.evenement.Evenement;

import java.util.Random;
import java.util.Vector;

public class GestionnaireMessages extends Thread {
    private static final int POURCENTAGE_PERTE_MESSAGE = 20;

    private Client client;
    private int idLivreur;
    Vector<Message> messages;

    public GestionnaireMessages(Client client, int idLivreur) {
        this.client = client;
        this.idLivreur = idLivreur;
        this.messages = new Vector();
    }

    public void ajouter(Message message) {
        messages.add(message);
    }

    public void traiterACK(Evenement evt) {
        Arguments args = new Arguments(evt);
        int id = Integer.parseInt(args.extraireArgumentSuivant());

        int i = 0;
        while (i < this.messages.size() && this.messages.get(i).getId() != id) {
            i++;
        }

        this.messages.remove(i);
    }

    @Override
    public void run() {
        Random rnd = new Random();
        while (client.isConnecte()) {
            try {
                for (Message msg : this.messages) {
                    if (rnd.nextInt(100) < 100 - GestionnaireMessages.POURCENTAGE_PERTE_MESSAGE) {
                        // envoi du message
                        String toSend = "SEND " + msg;
                        System.out.println("Gest. Msg. envoie : " + toSend);
                        this.client.envoyer(toSend);
                    } else {
                        // message perdu...
                    }
                }

                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
