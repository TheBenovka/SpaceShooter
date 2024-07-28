package de.hsos.ui.einstellungen;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BGMManager ist eine Singleton Klasse. Verwaltet sich selbst und es existiert nur ein
 * Objekt Gleichzeitig. Der BGMManager ist Threadsicher.
 * BGM - Backgroundmusic - Hintergrundmusik
 */
public class BGMManager {
    private Context context;
    public AtomicBoolean pausiert = new AtomicBoolean(true);
    private AtomicInteger resumePosition = new AtomicInteger(0);
    /**
     * Hintergrundmusik Player - in einer ständigen loop
     */
    private MediaPlayer bgmPlayer;
    private static volatile BGMManager instanz;
    private final String PREF_NAME = "EinstellungenPrefs";
    private final String MUSIC_CHECKED_KEY = "musicChecked";

    public BGMManager(Context context, int resID){
        this.context = context;
        initialisiereBackgroundSong(resID);
    }

    private void initialisiereBackgroundSong(int resID) {
        synchronized (this) {
            if (bgmPlayer == null) {
                bgmPlayer = MediaPlayer.create(context, resID);
                bgmPlayer.setLooping(true);
                bgmPlayer.setVolume(0.1f, 0.1f);
                Log.d(TAG, "Background song initialized and set to loop.");
            }
        }
    }
    /**
     * Methode: die Metode startet den Background Song wenn die checkBox aktiviert wurde
     * bzw der boolean des SharedPreferences auf true steht
     * @param context
     */
    public void starteBackgroundSongWennChecked(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean checkBoxAN = sharedPreferences.getBoolean(MUSIC_CHECKED_KEY, false);
        if (checkBoxAN) {
            starteBackgroundSong();
        }
    }

    public void starteBackgroundSong(){
        if (bgmPlayer != null) {
            if (pausiert.get()) {
                bgmPlayer.seekTo(resumePosition.get());
                bgmPlayer.start();
                pausiert.set(false);
            } else {
                bgmPlayer.start();
                bgmPlayer.setVolume(0.1f, 0.1f);
            }
        }
    }

    public void pauseBackgroundSong(){
        if (bgmPlayer != null && bgmPlayer.isPlaying()) {
            bgmPlayer.pause();
            pausiert.set(true);
            resumePosition.set(bgmPlayer.getCurrentPosition());
        }
    }

    public void stoppeBackgroundSong(){
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.release();
            bgmPlayer = null;
            instanz = null;
            pausiert.set(true);
            resumePosition.set(0);
        }
    }

    public static BGMManager getInstanz(Context context, int resID) {
        if (instanz == null) {
            synchronized (BGMManager.class) {
                if (instanz == null) {
                    instanz = new BGMManager(context, resID);
                }
            }
        }
        return instanz;
    }
}
