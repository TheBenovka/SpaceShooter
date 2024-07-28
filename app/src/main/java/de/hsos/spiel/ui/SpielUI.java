package de.hsos.spiel.ui;

import android.graphics.Canvas;

import de.hsos.spiel.spielStatus.ISpielerInteraktion;
import de.hsos.spiel.spielobjekt.Bewegungsrichtung;
import de.hsos.spiel.ui.controller.ControllerDarstellung;

/**
 * SpielUI behandelt die Interaktion des Nutzers und die Darstellung des Controllers.
 */
public class SpielUI {
    private ControllerDarstellung controllerDarstellung;
    private int btnBewegungsZeigerID = -1;
    private int btnSchiessemZeigerID = -1;

    private final ISpielerInteraktion spielerInteraktion;

    public SpielUI(ISpielerInteraktion spielerInteraktion) {
        this.spielerInteraktion = spielerInteraktion;
        controllerDarstellung = new ControllerDarstellung(Bildschirm.LINKS.wert,Bildschirm.CONTROLLER_OBEN.wert, Bildschirm.RECHTS.wert, Bildschirm.UNTEN.wert);
    }

    // Multi-Touch: https://medium.com/@arj.sna/multi-touch-in-android-c87031d106b3
    public void behandleAktionDown(float x, float y,int zeigerID) {
        if (controllerDarstellung.beinhaltetInLinkenPfeil(x,y)) {
            controllerDarstellung.setBtnPfeilLinksGedrueckt(true);
            btnBewegungsZeigerID = zeigerID;
            spielerInteraktion.setSpielerBewegung(Bewegungsrichtung.LINKS);
        } else if (controllerDarstellung.beinhaltetInRechtenPfeil(x,y)) {
            controllerDarstellung.setBtnPfeilRechtsGedrueckt(true);
            btnBewegungsZeigerID = zeigerID;
            spielerInteraktion.setSpielerBewegung(Bewegungsrichtung.Rechts);
        }
        if (controllerDarstellung.beinhaltetInSchieesen(x,y)) {
            controllerDarstellung.setBtnSchiessenGedrueckt(true);
            btnSchiessemZeigerID = zeigerID;
            spielerInteraktion.spielerSchiessen();
        }
    }

    public void behandleAktionMove(float x, float y,int zeigerID, int zeigerAnzahl) {
        for (int i = 0; i < zeigerAnzahl; ++i) {
            if (zeigerID == btnBewegungsZeigerID) {
                if (controllerDarstellung.beinhaltetInLinkenPfeil(x, y)) {
                    controllerDarstellung.setBtnPfeilLinksGedrueckt(true);
                    spielerInteraktion.setSpielerBewegung(Bewegungsrichtung.LINKS);
                } else if (controllerDarstellung.beinhaltetInRechtenPfeil(x, y)) {
                    controllerDarstellung.setBtnPfeilRechtsGedrueckt(true);
                    spielerInteraktion.setSpielerBewegung(Bewegungsrichtung.Rechts);
                } else {
                    controllerDarstellung.setBtnPfeilLinksGedrueckt(false);
                    controllerDarstellung.setBtnPfeilRechtsGedrueckt(false);
                }
            }
            if (zeigerID == btnSchiessemZeigerID) {
                if (controllerDarstellung.beinhaltetInSchieesen(x, y)) {
                    controllerDarstellung.setBtnSchiessenGedrueckt(true);
                } else {
                    controllerDarstellung.setBtnSchiessenGedrueckt(false);
                }
            }
        }
    }

    public void behandleAktionUp(float x, float y,int zeigerID) {
        if (zeigerID == btnBewegungsZeigerID) {
            controllerDarstellung.setBtnPfeilLinksGedrueckt(false);
            controllerDarstellung.setBtnPfeilRechtsGedrueckt(false);
            btnBewegungsZeigerID = -1;
            spielerInteraktion.setSpielerBewegung(Bewegungsrichtung.KEINE_BEWEGUNG);
        }
        if (zeigerID == btnSchiessemZeigerID) {
            controllerDarstellung.setBtnSchiessenGedrueckt(false);
            btnSchiessemZeigerID = -1;
        }
    }

    public void zeichnen(Canvas canvas) {
        controllerDarstellung.draw(canvas);
    }
}
