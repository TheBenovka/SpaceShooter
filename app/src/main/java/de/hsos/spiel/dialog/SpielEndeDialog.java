package de.hsos.spiel.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import de.hsos.MainActivity;
import de.hsos.R;
import de.hsos.daten.SpaceShooterRepository;
import de.hsos.spiel.SpielActivity;
import de.hsos.spiel.helfer.Spielwerte;

/**
 * SpielEndeDialog ist das Dialog, welches nach Beendigung des Spiels aufgerufen wird. Mit diesem
 * Dialog kann man zueruck zur MainActivity/Hauptmenue, ein neues Spiel starten oder den Score ueber ein
 * Soziales Netzwerk (als Text) teilen.
 */
public class SpielEndeDialog extends Dialog {
    private Context spielKontext;
    private TextView tvSpielerScore, tvSpielerHighscore;
    private ImageButton btnShare, btnHome, btnReplay;
    private SpielActivity spielActivity;
    private Spielwerte spielwerte;
    private SpaceShooterRepository repo;

    public SpielEndeDialog(@NonNull Context spielKontext, @NonNull SpaceShooterRepository repo, @NonNull SpielActivity spielActivity, Spielwerte spielwerte, String spielerName) {
        super(spielKontext);
        setContentView(R.layout.dialog_spiel_ende);
        this.spielKontext = spielKontext;
        this.spielActivity = spielActivity;
        this.spielwerte = spielwerte;
        this.repo = repo;
        repo.addSpiel(SpaceShooterRepository.getAktuellerSpielername(), spielwerte.getPunkte(), spielwerte.getRunde(), spielwerte.getStartZeit(), spielwerte.getEndeZeit(), spielwerte.getSchuesse(),spielwerte.getTreffer());
    }

    private void setBtnOnClickListeners() {
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spielActivity.initSpiel();
                dismiss();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToHauptmenueFragment();
                dismiss();
            }
        });
        https://developer.android.com/guide/components/intents-filters?hl=de
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareNachricht = spielKontext.getString(R.string.share_text) +  " " + spielwerte.getPunkte();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareNachricht);
                spielActivity.startActivity(Intent.createChooser(shareIntent, "Share using"));
            }
        });
    }
    // https://developer.android.com/guide/components/intents-filters?hl=de
    public void switchToHauptmenueFragment() {
        Intent intent = new Intent(spielActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("LOAD_FRAGMENT", "HAUPTMENU");
        spielActivity.startActivity(intent);
        dismiss(); // Schließt den Dialog
        spielActivity.finish(); // Beendet die SpielActivity
    }

    @Override
    public void onCreate(Bundle buendel) {
        super.onCreate(buendel);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        btnShare = findViewById(R.id.shareButton);
        btnHome = findViewById(R.id.homeButtton);
        btnReplay = findViewById(R.id.replayButton);

        tvSpielerHighscore = findViewById(R.id.tvHighscore);
        if (repo.getHighscore() != null) {
            tvSpielerHighscore.setText(repo.getHighscore().toString());
        } else {
            tvSpielerHighscore.setText(Integer.toString(spielwerte.getPunkte()));
        }

        tvSpielerScore = findViewById(R.id.tvScore);
        tvSpielerScore.setText(Integer.toString(spielwerte.getPunkte()));

        setBtnOnClickListeners();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
