package de.hsos.daten.entitaeten;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Spieler Entitaet mit den Attributen: id, name, aktivesSchiffID.
 */
@Entity(indices = {@Index(value={"name"}, unique = true), @Index(value={"id"}, unique = true)})
public class ESpieler {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @NonNull
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "aktivesSchiffID")
    public int aktivesSchiffID;
    public ESpieler(@NonNull String name, int aktivesSchiffID) {
        this.name = name;
        this.aktivesSchiffID = aktivesSchiffID;
    }
}

