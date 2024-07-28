package de.hsos.daten.beziehungen;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import de.hsos.daten.entitaeten.ESpiel;
import de.hsos.daten.entitaeten.ESpieler;

/**
 * Beziehung Spiel -N- : -1- Spieler
 */
public class SpielMitSpieler {
    @Embedded
    public ESpiel spiel;
    @Relation(
            parentColumn = "spielerID",
            entityColumn = "id"
    )
    public List<ESpieler> spielerListe;
}
