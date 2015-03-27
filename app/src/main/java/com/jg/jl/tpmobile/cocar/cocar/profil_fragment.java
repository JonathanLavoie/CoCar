package com.jg.jl.tpmobile.cocar.cocar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Jiimmy on 2015-03-04.
 */
public class profil_fragment extends Fragment{
    SessionManager session;
    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.profil_layout,container,false);
        chargerProfil();
        return rootView;
    }

    public void chargerProfil()
    {
        session = new SessionManager(getActivity().getApplicationContext());
        UserRepo repo = new UserRepo(getActivity());
        User unUtilisateur = repo.getUserByIdentification(session.getIdentification());
        TextView nom = (TextView)rootView.findViewById(R.id.lblVotreNom);
        TextView adresse = (TextView)rootView.findViewById((R.id.lblVotreAdresse));
        TextView email = (TextView)rootView.findViewById((R.id.lblVotreEmail));
        TextView telephone = (TextView)rootView.findViewById(R.id.lblVotrePhone);
        TextView votreMoyennne = (TextView)rootView.findViewById(R.id.lblVotreMoyenne);
        RatingBar rate = (RatingBar)rootView.findViewById(R.id.ratingBarProfil);

        nom.setText(unUtilisateur.get_nom().toString());
        adresse.setText(unUtilisateur.get_adresse());
        email.setText(unUtilisateur.get_identification());
        telephone.setText(unUtilisateur.get_phone());
        float rating = 0;

        if (unUtilisateur.get_countRate() != 0) {
            rating = (float)unUtilisateur.get_sumRate() / (float)unUtilisateur.get_countRate();
        }
        votreMoyennne.setText("Votre moyenne: " + rating + " étoiles");
        rate.setRating(rating);
    }
}
