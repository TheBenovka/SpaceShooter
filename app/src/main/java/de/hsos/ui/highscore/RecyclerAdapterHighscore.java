package de.hsos.ui.highscore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hsos.R;
import de.hsos.daten.entitaeten.ESchiff;
import de.hsos.daten.entitaeten.ESpiel;
import de.hsos.daten.entitaeten.ESpieler;

/**
 * Die Klasse stellt den RecyclerAdapter für den HighScore. Es werden die Daten der RecyclerViews verarbeitet,
 * mithilfe von Methode angepasst und mithilfe eines Observers live aktualisiert.
 * Die onBindViewHolder() Methode passt dabei die Daten so an, dass sie grafisch passend angezeigt werde
 */
public class RecyclerAdapterHighscore extends RecyclerView.Adapter<RecyclerAdapterHighscore.MyViewHolder> {

    private List<ESpiel> spielListHighscore;

    private List<ESpieler> eSpielerListHighscore;
    private List<ESchiff> eSchiffeFuerHighscore;

    private final Context context;
    private int counter;

    public RecyclerAdapterHighscore(Context context, List<ESpiel> spielListe, List<ESpieler> eSpielerListe, List<ESchiff> eSchiffListe) {
        this.spielListHighscore = spielListe;
        this.eSpielerListHighscore = eSpielerListe;
        this.eSchiffeFuerHighscore = eSchiffListe;
        this.counter = 1;

        this.context = context;
        if (this.spielListHighscore == null) this.spielListHighscore = new ArrayList<>();
        if (this.eSpielerListHighscore == null) this.eSpielerListHighscore = new ArrayList<>();
        if (this.eSchiffeFuerHighscore == null) this.eSchiffeFuerHighscore = new ArrayList<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_counter;
        private TextView tv_spielerName;
        private ImageView tv_spielerSchifftyp;
        private TextView tv_spielerScore;
        private TextView tv_spielerTime;

        public MyViewHolder(final View view) {
            super(view);
            tv_counter = view.findViewById(R.id.highscore_recyclerview_counterVorne);
            tv_spielerName = view.findViewById(R.id.highscore_recyclerview_spielername);
            tv_spielerSchifftyp = view.findViewById(R.id.highscore_recyclerview_schifftyp);
            tv_spielerScore = view.findViewById(R.id.highscore_recyclerview_score);
            tv_spielerTime = view.findViewById(R.id.highscore_recyclerview_time);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.highscore_reihen_recyclerview, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * Methode zeigt die Elemente des ViewHolders an. Dabei werden die lokalen ESpiel, ESpieler und ESchiffe benutzt +#
     * um mithilfe von deren LiveData Listen die jeweiligen Daten anzeigen lassen zu können.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (this.spielListHighscore != null && !this.spielListHighscore.isEmpty()) {
            if (this.spielListHighscore.size() > position) {
              ESpiel spiel = spielListHighscore.get(position);
              ESpieler eSpieler = new ESpieler("test", 1);
              //eSpieler wird zunächst mit irrelevanten temporären Daten befüllt
                for (ESpieler spieler : eSpielerListHighscore) {
                    if (spieler.id == spiel.spielerID) {
                        eSpieler.name = spieler.name;
                        break;
                    }
                }
                for (ESchiff schiff : eSchiffeFuerHighscore) {
                    if (schiff.id == spiel.schiffID) {
                        eSpieler.aktivesSchiffID = schiff.id;
                        holder.tv_spielerSchifftyp.setImageResource(schiff.bildNr);
                        break;
                    }
                }
                holder.tv_counter.setText(String.format(this.counter++ + "."));
                holder.tv_spielerName.setText(String.format(eSpieler.name));
                holder.tv_spielerScore.setText(String.format("%d", spiel.score));
                holder.tv_spielerTime.setText(String.format("%d",
                        (spiel.benoetigteZeit)/1000));
            }
        }
    }

    @Override
    public int getItemCount() {
        return spielListHighscore.size();
    }


    public void updateSpielListe(final List<ESpiel> spielListe) {
        if (this.spielListHighscore != null) {
            this.spielListHighscore.clear();
        }
        this.spielListHighscore = spielListe;
        notifyDataSetChanged();
    }

    public void updateEspielerListe(final List<ESpieler> eSpielerListe) {
        if (this.eSpielerListHighscore != null) {
            this.eSpielerListHighscore.clear();
        }
        this.eSpielerListHighscore = eSpielerListe;
        notifyDataSetChanged();
    }
    public void updateEschiffeListe(final List<ESchiff> eSchiffeListe) {
        if (this.eSchiffeFuerHighscore != null) {
            this.eSchiffeFuerHighscore.clear();
        }
        this.eSchiffeFuerHighscore = eSchiffeListe;
        notifyDataSetChanged();
    }
}