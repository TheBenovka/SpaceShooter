package de.hsos.spiel.sfx;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.SparseIntArray;

import de.hsos.R;

/**
 * Ähnlich zu BGMPlayerManager. Realisiert als Singleton Klasse.
 */
public class SoundEffektManager {
    private SoundPool soundPool;
    private SparseIntArray soundEffektKarte; // Zum Speichern der Sound-IDs
    private Context kontext;
    private static boolean areSoundEffekteAktiv;
    private static SoundEffektManager soundEffekteManager;
    private final float lautstaerke = 0.2f;


    public static SoundEffektManager getInstance(Context kontext) {
        final String PREF_NAME = "EinstellungenPrefs";
        final String MUSIC_CHECKED_KEY = "sfxChecked";
        SharedPreferences sharedPreferences = kontext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        areSoundEffekteAktiv = sharedPreferences.getBoolean(MUSIC_CHECKED_KEY, false);
        if (soundEffekteManager == null){
            soundEffekteManager = new SoundEffektManager(kontext);
        }
        return soundEffekteManager;
    }

    public SoundEffektManager(Context kontext) {
        this.kontext = kontext;
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(50) // Anzahl gleichzeitiger Streams
                .setAudioAttributes(audioAttributes)
                .build();

        soundEffektKarte = new SparseIntArray();
        soundEffektKarte.put(1, soundPool.load(kontext,R.raw.laser_shooting_sfx,0));
        soundEffektKarte.put(2, soundPool.load(kontext, R.raw.explosion_bomb_8bit, 1));
    }

    private void spieleSoundEffekt(int soundId) {
        if (areSoundEffekteAktiv) {
            int soundPoolId = soundEffektKarte.get(soundId);
            if (soundPoolId != 0) { // Sicherstellen, dass der Sound geladen wurde
                soundPool.play(soundPoolId, lautstaerke, lautstaerke, 1, 0, 1f);
            }
        }
    }

    // Freigeben
    public void aufraeumen() {
        if (soundPool != null){
            soundEffekteManager.soundPool = null;
            soundEffekteManager.soundEffektKarte = null;
        }
        soundEffekteManager = null;
    }

    public void spielSchussSound() {
        spieleSoundEffekt(1);
    }

    public void spielTrefferSound() {
        spieleSoundEffekt(2);
    }

    public void spielExplosionSound() {

    }
}
