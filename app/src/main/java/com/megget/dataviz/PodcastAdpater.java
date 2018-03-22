package com.megget.dataviz;

import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Classe qui gère la liste d'émissions
 */
public class PodcastAdpater extends RecyclerView.Adapter<PodcastAdpater.PodcastViewHolder> {

    /**
     * Liste des émissions
     */
    private final List<Pair<String, String>> characters = Arrays.asList(
            Pair.create("Guerre du vietnam", "Brave, curious, and crafty, she has been prophesied by the witches to help the balance of life"),
            Pair.create("Hilter n'est pas mort", "Lyra's daemon, nicknamed Pan."),
            Pair.create("Excursion en Alaska", "Lyra's friends"),
            Pair.create("La Général connu", "Lyra's uncle"),
            Pair.create("Marisa Coulter", "Intelligent and beautiful, but extremely ruthless and callous."),
            Pair.create("Iorek Byrnison", "Armoured bear, Rightful king of the panserbjørne. Reduced to a slave of the human village Trollesund."),
            Pair.create("Serafina Pekkala", "Witch who closely follows Lyra on her travels."),
            Pair.create("Lee Scoresby", "Texan aeronaut who transports Lyra in his balloon. Good friend with Iorek Byrnison."),
            Pair.create("Ma Costa", "Gyptian woman whose son, Billy Costa is abducted by the \"Gobblers\"."),
            Pair.create("John Faa", "The King of all gyptian people.")
    );

    /**
     * Retourne le nombre d'évènements dans la liste
     * @return taille de la liste
     */
    @Override
    public int getItemCount() {
        return characters.size();
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
        Pair<String, String> pair = characters.get(position);
        holder.display(pair);
    }

    /**
     * Classe qui associe un contenu à un élément
     */
    class PodcastViewHolder extends RecyclerView.ViewHolder{

        /**
         * Champs où afficher le contenu dans la vue
         */
        private final TextView name;
        private final TextView description;

        /**
         * Contenu de la cellule de la liste à afficher
         */
        private Pair<String, String> currentPair;

        /**
         * Création à partir d'un layout
         * @param itemView layout d'un élément
         */
        public PodcastViewHolder(final View itemView) {
            super(itemView);

            name = ((TextView) itemView.findViewById(R.id.name));
            description = ((TextView) itemView.findViewById(R.id.description));

            //affiche le contenu dans un pop up si on click sur l'élément
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(currentPair.first)
                            .setMessage(currentPair.second)
                            .show();
                }
            });
        }

        /**
         * Affiche le contenu de la cellule de la liste dans le layout correspondant
         * @param pair cellule de la liste
         */
        public void display(Pair<String, String> pair) {
            currentPair = pair;
            name.setText(pair.first);
            description.setText(pair.second);
        }
    }
}
