package com.example.eden62.GENSMobile.Adapters.RNFAdapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers.BaseTW;
import com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers.NbFemaleTW;
import com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers.NbMaleTW;
import com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers.NombreTW;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.RNFInventaire;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.MatchableArrayAdapter;

import java.util.List;

/**
 * Créée en suivant l'exemple : https://stackoverflow.com/questions/51946434/value-of-edittext-inside-listview-messing-up-while-filtering/51961128?noredirect=1#comment90911083_51961128
 */
public class RNFMatchableArrayAdapter extends MatchableArrayAdapter<RNFInventaire> {

    private boolean latinFilterMode;

    public RNFMatchableArrayAdapter(Context context, int resource, List<RNFInventaire> items) {
        super(context, resource);
        addAll(items);
    }

    @Override
    protected void onBind(final RNFInventaire item, View itemView, int position) {

        TextView nomEspece = (TextView) itemView.findViewById(R.id.nomEspeceRNFInv);
        final EditText nombre = (EditText) itemView.findViewById(R.id.nombre);
        final EditText nbMale = (EditText) itemView.findViewById(R.id.nbMale);
        final EditText nbFemale = (EditText) itemView.findViewById(R.id.nbFemale);
        Button decNombreButton = (Button) itemView.findViewById(R.id.decDenombrement);
        Button incNombreButton = (Button) itemView.findViewById(R.id.incDenombrement);
        Button decNbMaleButton = (Button) itemView.findViewById(R.id.decNbMale);
        Button incNbMaleButton = (Button) itemView.findViewById(R.id.incNbMale);
        Button decNbFemaleButton = (Button) itemView.findViewById(R.id.decNbFemale);
        Button incNbFemaleButton = (Button) itemView.findViewById(R.id.incNbFemale);

        // On associe chaque view avec l'item pour ne pas rencontrer le bug qui modifie plusieurs editText en même temps
        bindNombreWatcher(nombre, item);
        bindNbMaleWatcher(nbMale, item);
        bindNbFemaleWatcher(nbFemale, item);

        String nom;
        String nomFr = item.getNomFr();
        nom = item.getNomLatin();

        if(!nomFr.isEmpty())
            nom += " - " + nomFr;

        nomEspece.setText(nom);

        decNombreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decNombre(item, nombre);
            }
        });

        incNombreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incNombre(item, nombre);
            }
        });


        decNbMaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decNbMale(item, nbMale);
            }
        });

        incNbMaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incNbMale(item, nbMale);
            }
        });

        decNbFemaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decNbFemale(item,nbFemale);
            }
        });

        incNbFemaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incNbFemale(item,nbFemale);
            }
        });

        int nb = item.getNombre();
        int nbMaleInt = item.getNbMale();
        int nbFemaleInt = item.getNbFemale();
        // Si le dénombrement n'a pas été défini
        if(nb != 0)
            nombre.setText(nb + "");
        else
            nombre.setText("");

        if(nbMaleInt != 0)
            nbMale.setText(nbMaleInt + "");
        else
            nbMale.setText("");

        if(nbFemaleInt != 0)
            nbFemale.setText(nbFemaleInt + "");
        else
            nbFemale.setText("");
    }

    @Override
    protected boolean matches(RNFInventaire value, CharSequence constraint, CharSequence lowerCaseConstraint) {
        String itemName;

        if(isLatinFilterMode())
            itemName = value.getNomLatin().toLowerCase();
        else
            itemName = value.getNomFr().toLowerCase();

        return itemName.startsWith(lowerCaseConstraint.toString());
    }

    @Override
    protected void onViewCreated(View itemView) {
        final EditText nombre = (EditText) itemView.findViewById(R.id.nombre);
        final EditText nbMale = (EditText) itemView.findViewById(R.id.nbMale);
        final EditText nbFemale = (EditText) itemView.findViewById(R.id.nbFemale);

        NombreTW nombreTW = new NombreTW();
        NbFemaleTW nbFemaleTW = new NbFemaleTW(nombre);
        NbMaleTW nbMaleTW = new NbMaleTW(nombre);

        setWatcherToET(nombre, nombreTW);
        setWatcherToET(nbFemale, nbFemaleTW);
        setWatcherToET(nbMale, nbMaleTW);
    }

    // Associe le TextWatcher tw à l'EditText et
    private void setWatcherToET(EditText et, BaseTW tw){
        et.addTextChangedListener(tw);
        et.setTag(tw);
    }

    // Lie l'item à l'EditText nombre via son TextWatcher
    private void bindNombreWatcher(EditText nombre, RNFInventaire item){
        ((NombreTW) nombre.getTag()).item = item;
    }

    // Lie l'item à l'EditText nbMale via son TextWatcher
    private void bindNbMaleWatcher(EditText nbMale, RNFInventaire item){
        ((NbMaleTW) nbMale.getTag()).item = item;
    }

    // Lie l'item à l'EditText nbFemale via son TextWatcher
    private void bindNbFemaleWatcher(EditText nbFemale, RNFInventaire item){
        ((NbFemaleTW) nbFemale.getTag()).item = item;
    }

    /**
     * Décrémente la valeure contenue dans un EditText, utile pour les différents dénombrements
     *
     * @param et L'editText à changer
     * @param nb La valeur à modifier
     * @return La valeur décrémenté de 1
     */
    protected int decreaseDecompteEditText(EditText et, int nb){
        if(nb > 0) {
            String newText = "";
            nb--;
            if(nb > 0)
                newText += nb;
            et.setText(newText);
        }
        return nb;
    }

    //ajuste le dénombrement total de cet Inventaire s'il est inférieur au dénombrement mâle + femelle
    private int adjustNombre(RNFInventaire inv){
        int nbGenre = inv.getNbGenre();
        int nb = inv.getNombre();
        if(nb < nbGenre)
            nb += nbGenre - nb;
        inv.setNombre(nb);
        return nb;
    }

    /**
     * Incrémente le dénombrement total de 1 si le dénombrement à déjà été renseigné par l'utilisateur et effectue une
     * modification supplémentaire si le dénombrement est incohérant
     */
    protected void incNombre(RNFInventaire inv, EditText nombre){
        int nbBeforeAdjust = inv.getNombre();
        boolean wasCoherentNombreBeforeAdjust = nbBeforeAdjust >= inv.getNbGenre();

        adjustNombre(inv);

        int nb = inv.getNombre();

        // On incrémente le dénombrement seulement si celui-ci n'a pas été modifié pour être égale au total de genre
        if(wasCoherentNombreBeforeAdjust)
            nb ++;

        nombre.setText(nb + "");
    }

    /**
     * Décrémente le dénombrement total de 1
     */
    protected void decNombre(RNFInventaire inv, EditText nombre){
        decreaseDecompteEditText(nombre,inv.getNombre());
    }

    /**
     * Incrémente de 1 le nombre de mâle
     */
    protected void incNbMale(RNFInventaire inv, EditText nbMaleText){
        int nbMale = inv.getNbMale();
        nbMale ++ ;

        nbMaleText.setText(nbMale + "");
    }

    /**
     * Décrémente de 1 le nombre de mâle
     */
    private void decNbMale(RNFInventaire inv, EditText nbMaleText){
        decreaseDecompteEditText(nbMaleText,inv.getNbMale());
    }

    /**
     * Incrémente de 1 le nombre de femelle
     */
    private void incNbFemale(RNFInventaire inv, EditText nbFemaleText){
        int nbFemale = inv.getNbFemale();
        nbFemale ++;

        nbFemaleText.setText(nbFemale + "");
    }

    /**
     * Décrémente de 1 le nombre de femelle
     */
    private void decNbFemale(RNFInventaire inv, EditText nbFemaleText){
        decreaseDecompteEditText(nbFemaleText,inv.getNbFemale());
    }

    public boolean isLatinFilterMode() {
        return latinFilterMode;
    }

    public void setLatinFilterMode(boolean latinFilterMode) {
        this.latinFilterMode = latinFilterMode;
    }
}
