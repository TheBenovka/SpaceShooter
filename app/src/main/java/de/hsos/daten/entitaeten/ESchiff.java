package de.hsos.daten.entitaeten;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Die Schiff Entitaet besitz folgende Attribute: id, name.
 */
// TODO: Eventuell Bitmaps speichern.
@Entity(indices = {@Index(value={"name"}, unique = true), @Index(value={"id"}, unique = true)})
public class ESchiff {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @NonNull
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "bildNr")
    public Integer bildNr;
    public ESchiff(@NonNull String name, Integer bildNr) {
        this.name = name;
        this.bildNr = bildNr;
    }
}
