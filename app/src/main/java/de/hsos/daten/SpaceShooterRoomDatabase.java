package de.hsos.daten;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hsos.daten.entitaeten.ESchiff;
import de.hsos.daten.entitaeten.ESpiel;
import de.hsos.daten.entitaeten.ESpieler;

@Database(entities = {
        ESpieler.class,
        ESpiel.class,
        ESchiff.class
}, version = 18, exportSchema = false)

/**
 * RoomDatabase Spezialisierung, Zugriff auf die DB erfolgt mit dem ExecutorService(4 Threads).
 */
public abstract class SpaceShooterRoomDatabase extends RoomDatabase{
    public static volatile SpaceShooterRoomDatabase INSTANZ;
    private static final int ANZAHL_DER_THREADS = 4;

    static ExecutorService datenbankSchreiberAusfuehrungsDienst
            = Executors.newFixedThreadPool(ANZAHL_DER_THREADS);

    public static SpaceShooterRoomDatabase getInstance(final Context context) {
        if (INSTANZ == null) {
            synchronized (SpaceShooterRoomDatabase.class) {
                if (INSTANZ == null) {
                    INSTANZ = Room.databaseBuilder(context.getApplicationContext(),
                            SpaceShooterRoomDatabase.class,
                            "SpaceShooterDatenbank")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANZ;
    }
    public abstract SpaceShooterDAO spaceShooterDAO();
}
