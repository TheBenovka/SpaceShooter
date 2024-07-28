package de.hsos.daten;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hsos.daten.beziehungen.SpielMitSchiffUndSpieler;
import de.hsos.daten.entitaeten.ESchiff;
import de.hsos.daten.entitaeten.ESpiel;
import de.hsos.daten.entitaeten.ESpieler;

/**
 * SpaceShooterRepository ist die Schnittstelle zur Datenbank.
 */
public class SpaceShooterRepository {
    SpaceShooterDAO spaceShooterDAO;
    private LiveData<List<ESpiel>> alleSpieleVonSpieler;
    private LiveData<List<ESpieler>> alleSpieler;
    private LiveData<List<ESchiff>> alleSchiffe;
    private LiveData<List<ESpiel>> top100spiele;
    private LiveData<List<SpielMitSchiffUndSpieler>> listSpielMitSiffUndSpieler;

    private static String aktuellerSpielername; // ist in allen Repos identisch

    private static ESchiff aktuellesSchiff;
    private Integer highscore;
    public SpaceShooterRepository(Application application) {
        SpaceShooterRoomDatabase roomDatabase = SpaceShooterRoomDatabase.getInstance(application);
        this.spaceShooterDAO = roomDatabase.spaceShooterDAO();
        this.alleSchiffe = spaceShooterDAO.getAlleSchiffe();
        this.alleSpieler = spaceShooterDAO.getAlleSpieler();
        this.listSpielMitSiffUndSpieler = spaceShooterDAO.getSpielMitSchiffUndSpieler();
        this.top100spiele = spaceShooterDAO.getTop100Spiele();
        SpaceShooterRoomDatabase.datenbankSchreiberAusfuehrungsDienst.execute(() -> {
            this.highscore = spaceShooterDAO.getHighscore();
        });
    }

    public static String getAktuellerSpielername() {
        return aktuellerSpielername;
    }

    public void setAktuellerSpielerName(String aktuellerSpielerName) {
        SpaceShooterRepository.aktuellerSpielername = aktuellerSpielerName;
    }

    public ESchiff getAktuellesSchiff() {
        return aktuellesSchiff;
    }

    public Integer getHighscore() {
        return this.highscore;
    }

    public LiveData<List<ESpiel>> getTop100spiele() {
        return top100spiele;
    }

    public LiveData<List<ESpieler>> getAlleSpieler() {
        return alleSpieler;
    }

    public LiveData<List<ESchiff>> getAlleSchiffe() {
        return alleSchiffe;
    }

    public LiveData<List<ESpiel>> getAlleSpieleVonSpieler() {
        return alleSpieleVonSpieler;
    }
    public void addSpieler(final String name, final ESchiff schiff) {
        SpaceShooterRoomDatabase.datenbankSchreiberAusfuehrungsDienst.execute(() -> {
           this.spaceShooterDAO.insertSpieler(new ESpieler(name, schiff.id));
        });
    }

    public void setAktuellesSchiff(ESchiff schiff) {
        aktuellesSchiff = schiff;
    }
    public String getSpielerNameByID(int spielerId) {
        return this.spaceShooterDAO.getSpielerNameBySpielerID(spielerId).toString();
    }

    public String getSchiffNameByID(int schiffId){
        return this.spaceShooterDAO.getSchiffNameByID(schiffId).toString();
    }

    public void setSpielerSchiff(final ESpieler spieler, final ESchiff schiff) {
        SpaceShooterRoomDatabase.datenbankSchreiberAusfuehrungsDienst.execute(() -> {
            this.spaceShooterDAO.updateSpielerSchiff(spieler.id, schiff.id);
        });
    }

    public void setSpielerSchiff(final String spielerName, final ESchiff schiff) {
        SpaceShooterRoomDatabase.datenbankSchreiberAusfuehrungsDienst.execute(() -> {
            ESpieler spieler = this.spaceShooterDAO.getSpieler(spielerName);
            if (spieler != null) {
                this.spaceShooterDAO.updateSpielerSchiff(spieler.id, schiff.id);
            }
        });
    }

    public void addSchiff(final String name, final int bildNr) {
        SpaceShooterRoomDatabase.datenbankSchreiberAusfuehrungsDienst.execute(() -> {
            this.spaceShooterDAO.insertSchiff(new ESchiff(name, bildNr));
        });
    }

    public void addSpieler(final String spieler, final String SchiffName) {
        SpaceShooterRoomDatabase.datenbankSchreiberAusfuehrungsDienst.execute(() ->{
            ESchiff schiff = this.spaceShooterDAO.getSchiff(SchiffName);
            addSpieler(spieler, schiff);
        });
    }

    public void addSpiel(final String spielerName, final int score,
                            final int runde,final long startZeit,  final long endeZeit, final int schuesse, final int treffer) {

        SpaceShooterRoomDatabase.datenbankSchreiberAusfuehrungsDienst.execute(() -> {
            if (spaceShooterDAO.getSpieler(spielerName) == null) {
            return;
        }

        ESpiel spiel = new ESpiel(
                spaceShooterDAO.getSpieler(spielerName).id,
                spaceShooterDAO.getSchiffVonSpieler(spielerName).id,
                score,
                runde,
                startZeit,
                endeZeit,
                schuesse,
                treffer
        );
            spaceShooterDAO.insertSpiel(spiel);
        });
    }

    public void addSpiel(final ESpieler eSpieler, final int score,
                         final int runde,final long startZeit,  final long endeZeit, final int schuesse, final int treffer) {
        addSpiel(eSpieler.name,score,runde,startZeit,endeZeit,schuesse,treffer);
    }

    public void removeSpieler(final String name) {
        SpaceShooterRoomDatabase.datenbankSchreiberAusfuehrungsDienst.execute(() -> {
            // erst Spiel loeschen, dann Spieler oder sollen die HS denooch bestehen bleiben?
            ESpieler spieler = spaceShooterDAO.getSpieler(name);
            if(spieler != null) {
                removeSpieleVonSpieler(name);
                spaceShooterDAO.deleteSpieler(spaceShooterDAO.getSpieler(name));
            }
        });
    }

    public void removeSpieleVonSpieler(final String spielerName) {
        SpaceShooterRoomDatabase.datenbankSchreiberAusfuehrungsDienst.execute(() -> {
            spaceShooterDAO.deleteSpieleVonSpieler(spaceShooterDAO.getSpieler(spielerName).id);
        });
    }

    public void removeSchiff(final String name) {
        SpaceShooterRoomDatabase.datenbankSchreiberAusfuehrungsDienst.execute(() -> {
            removeSpieleVonSpieler(name);
            spaceShooterDAO.deleteSchiff(spaceShooterDAO.getSchiff(name));
        });
    }
}
