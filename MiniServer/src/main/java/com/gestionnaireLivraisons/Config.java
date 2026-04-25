package com.gestionnaireLivraisons;

import java.nio.file.Files;
import java.nio.file.Path;

public interface Config {
    // Emplacement du fichier contenant la liste des livreurs enregistres.
    String fichierLivreurs = "src/main/livreurs.txt";
    String fichierLivreursDepuisRacine = "MiniServer/src/main/livreurs.txt";

    /**
     * Retourne un chemin valide vers le fichier des livreurs selon le dossier d'execution.
     * - Execution depuis MiniServer : src/main/livreurs.txt
     * - Execution depuis la racine du repo : MiniServer/src/main/livreurs.txt
     *
     * @return Le chemin a utiliser.
     */
    static Path cheminFichierLivreurs() {
        Path cheminLocal = Path.of(fichierLivreurs);
        if (Files.exists(cheminLocal)) {
            return cheminLocal;
        }

        Path cheminRacine = Path.of(fichierLivreursDepuisRacine);
        if (Files.exists(cheminRacine)) {
            return cheminRacine;
        }

        // Fallback: conserver le chemin historique pour produire une erreur claire a la lecture/ecriture.
        return cheminLocal;
    }
}
