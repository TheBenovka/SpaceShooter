package de.hsos.daten.beziehungen;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import de.hsos.daten.entitaeten.ESchiff;
import de.hsos.daten.entitaeten.ESpiel;
import de.hsos.daten.entitaeten.ESpieler;

/**
 * Kompltter Datensatz eines highscores inkl. Spielername und SchiffName
 */
public class SpielMitSchiffUndSpieler {
    @Embedded
    public ESpiel spiel;
    @Relation(
            parentColumn = "spielerID",
            entityColumn = "id"
    )
    public List<ESchiff> spielMitSchiffe;
    @Relation(
            parentColumn = "spielerID",
            entityColumn = "id"
    )
    public List<ESpieler> spielerListe;
}
