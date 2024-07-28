package de.hsos.ui.spielerverwaltung;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hsos.daten.SpaceShooterRepository;
import de.hsos.daten.entitaeten.ESchiff;

/**
 * Die Klasse ist die Schnittstelle zwischen Datenbank und RecyclerAdapter. Hier wird die Logik der Raumschiffe implementiert
 * und das Datenbank Repository für den Zugriff auf unteranderem die ESchiffe Klasse gehalten
 */

public class SchiffVerwaltung {

    private LiveData<List<ESchiff>> listeAlleVerfuegbarenSchiffe;
    private SpaceShooterRepository repo;
    public SchiffVerwaltung(Application application){
        this.repo = new SpaceShooterRepository(application);
        this.listeAlleVerfuegbarenSchiffe = repo.getAlleSchiffe();
    }

    public LiveData<List<ESchiff>> getListeAlleVerfuegbarenSchiffe() {
        return this.listeAlleVerfuegbarenSchiffe;
    }
}
