package com.megget.dataviz;

import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe qui gère la liste d'émissions
 */
public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder> {

    /**
     * Liste des émissions
     */
    private ArrayList<Podcast> podcasts=new ArrayList<>();

    /**
     * Retourne le nombre d'évènements dans la liste
     * @return taille de la liste
     */
    @Override
    public int getItemCount() {
        return podcasts.size();
    }

    /**
     * Créer l'élément de la liste qui contiendra le texte.
     * Appelée une fois au debut.
     * @param parent
     * @param viewType si on utilise des types de vue différent dans une même liste (ici non)
     * @return l'élément créé avec la vue correspondante
     */
    @Override
    public PodcastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell, parent, false);
        return new PodcastViewHolder(view);
    }

    /**
     * Remplie l'élément avec le contenu de la cellule de la liste.
     * Appelée à chaque fois qu'un élément change de contenu.
     * @param holder élément à remplir
     * @param position position dans la liste du contenu à afficher
     */
    @Override
    public void onBindViewHolder(PodcastViewHolder holder, int position) {
        Podcast podcast = podcasts.get(position);
        holder.display(podcast);
    }

    /**
     * Classe qui associe un contenu à un élément
     */
    class PodcastViewHolder extends RecyclerView.ViewHolder{

        /**
         * Champs où afficher le contenu dans la vue
         */
        private final TextView nom;
        private final TextView description;

        /**
         * Contenu de la cellule de la liste à afficher
         */
        private Podcast current;

        /**
         * Création à partir d'un layout
         * @param itemView layout d'un élément
         */
        public PodcastViewHolder(final View itemView) {
            super(itemView);

            nom = ((TextView) itemView.findViewById(R.id.name));
            description = ((TextView) itemView.findViewById(R.id.description));

            //affiche le contenu dans un pop up si on click sur l'élément
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(current.getNom())
                            .setMessage(current.getDescription())
                            .show();
                }
            });
        }

        /**
         * Affiche le contenu de la cellule de la liste dans le layout correspondant
         * @param podcast cellule de la liste
         */
        public void display(Podcast podcast) {
            current = podcast;
            nom.setText(podcast.getNom());
            description.setText(podcast.getDescription());
        }
    }

    public void setPodcasts(ArrayList<Podcast> podcasts) {
        this.podcasts = podcasts;
        notifyDataSetChanged(); //indique que les données ont changé donc force le rafraichissement de l'affichage avec l'evenement 'change'
    }
}
