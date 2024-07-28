package de.hsos.spiel.helfer;

/**
 * Spielwerte ist eine Helfer Klasse. Diese wird fuer die Rückgabe der ISpielEventListener
 * zur Activity genutzt, damit nicht unnoetige Klassenbeziehungen verwendet werden.
 */
public class Spielwerte {
    private int punkte;
    private int runde;
    private long startZeit;
    private long endeZeit;
    private int schuesse;
    private int treffer;

    public long getStartZeit() {
        return startZeit;
    }

    public long getEndeZeit() {
        return endeZeit;
    }

    public int getSchuesse() {
        return schuesse;
    }

    public int getTreffer() {
        return treffer;
    }

    public Spielwerte(int punkte, int runde, long startZeit, long endeZeit, int schuesse, int treffer) {
      this.punkte = punkte;
      this.runde = runde;
      this.startZeit = startZeit;
      this.endeZeit = endeZeit;
      this.schuesse = schuesse;
      this.treffer = treffer;
    }

    public int getPunkte() {
        return punkte;
    }


    public int getRunde() {
        return runde;
    }


}
