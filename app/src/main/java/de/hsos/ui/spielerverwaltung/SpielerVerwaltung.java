package de.hsos.ui.spielerverwaltung;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hsos.daten.SpaceShooterRepository;
import de.hsos.daten.entitaeten.ESchiff;
import de.hsos.daten.entitaeten.ESpieler;

/**
 * Die Klasse ist die Schnittstelle zwischen Datenbank und RecyclerAdapter. Hier wird die Logik des Spielers implementiert
 * und das Datenbank Repository für den Zugriff auf unteranderem die ESpieler Klasse gehalten
 */

public class SpielerVerwaltung {
    private LiveData<List<ESpieler>> listeSpieler;
    SpaceShooterRepository repo;

    public SpielerVerwaltung(Application application) {
        this.repo = new SpaceShooterRepository(application);
        this.listeSpieler = repo.getAlleSpieler();
    }

    public LiveData<List<ESpieler>> getListeSpieler() {
        return listeSpieler;
    }

    public void addSpieler(String name, String schiffName) {
        this.repo.addSpieler(name, schiffName);
    }
    public void addSpieler(String name, ESchiff schiff) {
        this.repo.addSpieler(name, schiff);
    }

    public void loescheSpieler(String name){
        repo.removeSpieler(name);
    }

    public void updateSpielerSchiff(String spielerName, ESchiff eSchiff){
        this.repo.setSpielerSchiff(spielerName, eSchiff);
    }

    public void setAktuellesSchiff(ESchiff schiff) {
        repo.setAktuellesSchiff(schiff);
    }

    public void setAktuellerSpieler(String name) {
        this.repo.setAktuellerSpielerName(name);
    }
}
