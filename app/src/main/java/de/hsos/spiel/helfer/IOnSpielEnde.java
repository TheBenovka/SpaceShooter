package de.hsos.spiel.helfer;

/**
 * IOnSpielEnde wird zur Kommunikaion zur Activity genutzt. Um das Spiel Ordnungs gemaess
 * (Stichwort Threads) zu beenden und der Activity die Spielwerte zu uebermitteln.
 */
public interface IOnSpielEnde {
    void onSpielBeendet(Spielwerte spielwerte);
}
