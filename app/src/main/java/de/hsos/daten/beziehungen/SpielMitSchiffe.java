package de.hsos.daten.beziehungen;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import de.hsos.daten.entitaeten.ESchiff;

/**
 * Beziehung Spiel -N- : -1- Schiff
 */
public class SpielMitSchiffe {
    @Embedded
    public ESchiff schiff;
    @Relation(
            parentColumn = "schiffID",
            entityColumn = "id"
    )
    public List<ESchiff> schiffListe;
}
