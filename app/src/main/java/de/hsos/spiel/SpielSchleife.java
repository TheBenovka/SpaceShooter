package de.hsos.spiel;

/**
 * SpielSchleife besitzt ein Thread und Spiel Objekt. Der Thread startet, dan das Spiel.
 * Dies wird benötigt, um ein Reibungslosen Spielzyklus zu gewaehrleisten.
 * Grundlegendes Prinzip (Implementierung ohne FPS Drosselung):
 */
public class SpielSchleife implements Runnable {
    private Thread spielThread;
    private Spiel spiel;
    private boolean laufend;

    public SpielSchleife(Spiel spiel) {
        this.spiel = spiel;
        spielThread = new Thread(this);
    }

    @Override
    public void run() {
        long letzterDelta = System.nanoTime();
        float nanoSekunden = 1_000_000_000.0f;

        while (laufend) {
            try {
                long neuerDelta = System.nanoTime();
                long zeitSeitLetztemDelta = neuerDelta - letzterDelta;

                float delta = zeitSeitLetztemDelta / nanoSekunden;

                spiel.update(delta);
                spiel.render();

                letzterDelta = neuerDelta;
            } catch (Exception ex) {
            }
        }
    }

    public void start() {
        laufend = true;
        if (spielThread == null || !spielThread.isAlive()) {
            laufend=true;
            spielThread = new Thread(this);
            spielThread.start();
        }
    }

    public void stop() {
        if (laufend) {
            laufend = false;
            boolean wiederholung = true;
            while (wiederholung) {
                try {
                    spielThread.interrupt();
                    spielThread.join();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void fortfuehren() {
        this.start();
    }

    public void pausieren() {
        this.laufend=false;
    }
}
