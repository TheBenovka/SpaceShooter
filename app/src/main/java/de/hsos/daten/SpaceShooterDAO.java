package de.hsos.daten;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.hsos.daten.beziehungen.SpielerMitSchiff;
import de.hsos.daten.beziehungen.SpielMitSchiffUndSpieler;
import de.hsos.daten.entitaeten.ESchiff;
import de.hsos.daten.entitaeten.ESpiel;
import de.hsos.daten.entitaeten.ESpieler;

/**
 * Die SpaceShooterDAO Klasse definiert alle verfügbaren SQL-Befehle.
 */
@Dao
public interface SpaceShooterDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSpieler(final ESpieler spieler);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSchiff(final ESchiff schiff);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSpiel(final ESpiel spiel);
    @Delete
    void deleteSpieler(final ESpieler spieler);
    @Delete
    void deleteSchiff(final ESchiff schif);
    @Query("DELETE FROM ESpiel WHERE spielerID = :spielerID")
    void deleteSpieleVonSpieler(final int spielerID);

    // Falls in Zukunft Schiffe entfernt werden muessen. (Special Events etc.)
    @Query("DELETE FROM ESpiel WHERE schiffID = :schiffID")
    void deleteSpieleVomSchiff(final int schiffID);

    @Transaction
    @Query("SELECT * FROM ESpieler WHERE name = :name")
    ESpieler getSpieler(final String name);

    @Query("SELECT name FROM ESpieler WHERE id = :spielerID")
    String getSpielerNameBySpielerID(final int spielerID);

    @Query("SELECT * FROM ESpieler")
    LiveData<List<ESpieler>> getAlleSpieler();

    @Query("SELECT * FROM ESchiff WHERE name = :name")
    ESchiff getSchiff(final String name);

    @Query("SELECT name FROM ESchiff WHERE id = :schiffID")
    String getSchiffNameByID(final int schiffID);
    @Query("SELECT * FROM ESchiff WHERE id = :id")
    ESchiff getSchiff(final int id);

    @Query("SELECT * FROM ESpieler AS sp JOIN ESchiff AS sch" +
            " ON sch.id = sp.aktivesSchiffID WHERE sp.name = :name")
    ESchiff getSchiffVonSpieler(final String name);

    @Query("SELECT * FROM ESchiff")
    LiveData<List<ESchiff>> getAlleSchiffe();

    @Query("SELECT * FROM ESpiel WHERE spielerID = :spielerID")
    LiveData<List<ESpiel>> getAlleSpieleVonSpieler(final int spielerID);

    @Query("SELECT * " +
            "FROM ESpiel " +
            "ORDER BY score " +
            "DESC LIMIT 100")
    LiveData<List<ESpiel>> getTop100Spiele();

    @Query("SELECT score FROM ESpiel ORDER BY score DESC LIMIT 1")
    Integer getHighscore();

    @Query("SELECT * FROM ESpiel WHERE schiffID = :schiffID")
    LiveData<List<ESpiel>> getAlleSpieleVomSchiff(final int schiffID);

    @Query("UPDATE ESpieler SET aktivesSchiffID = :schiffID " +
            "WHERE id = :spielerID")
    void updateSpielerSchiff(final int spielerID, final int schiffID);

    @Transaction
    @Query("SELECT * FROM ESpiel")
    LiveData<List<SpielMitSchiffUndSpieler>> getSpielMitSchiffUndSpieler();

    @Transaction
    @Query("SELECT * FROM ESpieler")
    LiveData<List<SpielerMitSchiff>> getAlleSpielerMitSchiff();
}
