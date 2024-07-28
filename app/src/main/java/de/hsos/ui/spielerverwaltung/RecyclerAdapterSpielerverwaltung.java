package de.hsos.ui.spielerverwaltung;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hsos.R;
import de.hsos.daten.entitaeten.ESchiff;
import de.hsos.daten.entitaeten.ESpieler;

/**
 * Die Klasse stellt den RecyclerAdapter für die SpielerVerwaltung. Es werden die Daten der RecyclerViews verarbeitet,
 * mithilfe von Methode angepasst und mithilfe eines Observers live aktualisiert
 */

public class RecyclerAdapterSpielerverwaltung extends RecyclerView.Adapter<RecyclerAdapterSpielerverwaltung.MyViewHolder> {

    private List<ESchiff> raumschiffList;
    private List<ESpieler> spielerList;
    private final Context context;

    public RecyclerAdapterSpielerverwaltung(Context context, List<ESchiff> raumschiffList ){
        this.raumschiffList = raumschiffList;
        this.context = context;
        if (this.raumschiffList == null) {
            this.raumschiffList = new ArrayList<>();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView angezeigtes_raumschiff_view;
        public MyViewHolder(final View view){
            super(view);
            angezeigtes_raumschiff_view = view.findViewById(R.id.spielerverwaltung_recylcerview_raumschiffBild);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View imageViewRaumschiffe = LayoutInflater.from(parent.getContext()).inflate(R.layout.spielerverwaltung_recyclerview_eigenschaften, parent, false);
        return new MyViewHolder(imageViewRaumschiffe);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (this.raumschiffList != null && !this.raumschiffList.isEmpty()) {
            if (this.raumschiffList.size() > position) {
                ESchiff raumschiff = raumschiffList.get(position);
                holder.angezeigtes_raumschiff_view.setImageResource(raumschiff.bildNr);
            }
        }
    }
    @Override
    public int getItemCount() {
       return this.raumschiffList.size();
    }
    public void updateRaumschiffListe(final List<ESchiff> raumschiffList) {
        if (this.raumschiffList != null) {
            this.raumschiffList.clear();
        }
        this.raumschiffList = raumschiffList;
        notifyDataSetChanged();
    }
    public void updateSpielerListe(final List<ESpieler> spielerList) {
        if (this.spielerList!= null) {
            this.spielerList.clear();
        }
        this.spielerList = spielerList;
        notifyDataSetChanged();
    }

    public List<ESpieler> getSpielerList() {
        return spielerList;
    }

    public ESchiff getAktuellesShiff() {
        return raumschiffList.get(FragmentSpielerverwaltung.elementPositionRecyclerView);
    }
}
