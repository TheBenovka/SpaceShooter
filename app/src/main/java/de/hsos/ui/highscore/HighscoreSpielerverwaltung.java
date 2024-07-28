package de.hsos.ui.highscore;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hsos.daten.SpaceShooterRepository;
import de.hsos.daten.entitaeten.ESchiff;
import de.hsos.daten.entitaeten.ESpiel;
import de.hsos.daten.entitaeten.ESpieler;
/**
 * Die Klasse ist die Schnittstelle zwischen Datenbank und RecyclerAdapter. Hier wird die Logik des HighScores implementiert
 * und das Datenbank Repository für den Zugriff auf unteranderem die ESchiffe Klasse gehalten
 */
public class HighscoreSpielerverwaltung {

    private LiveData<List<ESpiel>> listeSpiel;

    private LiveData<List<ESpieler>> listeEspieler;
    private LiveData<List<ESchiff>> listeEschiffe;
    SpaceShooterRepository spaceShooterRepository;

    public HighscoreSpielerverwaltung(Application application) {

        this.spaceShooterRepository = new SpaceShooterRepository(application);
        this.listeSpiel = spaceShooterRepository.getTop100spiele();
        this.listeEspieler = spaceShooterRepository.getAlleSpieler();
        this.listeEschiffe = spaceShooterRepository.getAlleSchiffe();
    }

    public LiveData<List<ESpiel>> getListe100Spiele() {
        return listeSpiel;
    }

    public LiveData<List<ESpieler>> getListeEspieler() {
        return listeEspieler;
    }
    public LiveData<List<ESchiff>> getlisteEschiffe() {
        return listeEschiffe;
    }  
}
