package de.hsos.daten.entitaeten;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Spiel Entitaet ist die "auseinander gezogene" M:N Beziehung zwischen Spieler und Schiff. Die Entitaet
 * besitzt folgende Attribute: id, schiffID, spielerID, score, runde, startDatum, benoetigteZeit.
 * Kann auch als N:M definiert werden, jedoch habe ich mich dagegen entschieden, da das DB Schema damit unuebersichtlich ist.
 */
@Entity(indices = {@Index(value={"schiffID","spielerID","start"}, unique = true), @Index(value={"spielID"}, unique = true)})
public class ESpiel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "spielID")
    public int id;
    @ColumnInfo(name = "schiffID")
    public int schiffID;
    @ColumnInfo(name = "spielerID")
    public int spielerID;
    @ColumnInfo(name = "score")
    public int score;
    @ColumnInfo(name = "runde")
    public int runde;
    @ColumnInfo(name = "start")
    public long timestampStart;
    @ColumnInfo(name = "ende")
    public long timestampEnde;
    @ColumnInfo(name = "benoetigteZeit")
    public long benoetigteZeit;
    @ColumnInfo(name = "treffer")
    public int treffer;
    @ColumnInfo(name = "schuesse")
    public int schuesse;

    public ESpiel(int spielerID, int schiffID, int score, int runde, long timestampStart, long timestampEnde, int schuesse, int treffer) {
        this.schiffID = schiffID;
        this.spielerID = spielerID;
        this.score = score;
        this.runde = runde;
        this.timestampStart = timestampStart;
        this.timestampEnde = timestampEnde;
        this.schuesse = schuesse;
        this.treffer = treffer;
        this.benoetigteZeit = timestampEnde - timestampStart;
    }
}

