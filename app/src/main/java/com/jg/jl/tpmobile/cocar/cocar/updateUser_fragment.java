package com.jg.jl.tpmobile.cocar.cocar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jonathan Lavoie on 25/03/2015.
 */
public class updateUser_fragment extends Fragment {

    // Variable pour les champs de l'activity
    EditText txtNom, txtMotPasse, txtAdresse, txtNumTel;
    TextView lblEmail;
    SessionManager session;

    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        rootView = inflater.inflate(R.layout.fragment_update_user,container,false);
        // Attribut les textBox a des varibales
        lblEmail = (TextView) rootView.findViewById(R.id.lblEmail);
        txtNom = (EditText) rootView.findViewById(R.id.txtNom);
        txtMotPasse = (EditText) rootView.findViewById(R.id.txtMotDePasse);
        txtAdresse = (EditText) rootView.findViewById(R.id.txtAddress);
        txtNumTel = (EditText) rootView.findViewById(R.id.txtPhone);

        UserRepo repo = new UserRepo(getActivity().getApplicationContext());
        User user = repo.getUser();
        lblEmail.setText(user.get_identification());
        txtNom.setText(user.get_nom());
        txtAdresse.setText(user.get_adresse());
        txtNumTel.setText(user.get_phone());
        Button button = (Button) rootView.findViewById(R.id.btnModifier);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                modifier(rootView);
            }
        });
        return rootView;
    }

    /**
     * Méthode pour regardé si les champs sont valide
     * @param nom
     * @param motPasse
     * @param adresse
     * @param phone
     * @return
     */
    public Boolean champsValide(String nom, String motPasse, String adresse, String phone) {
        // Vérifie si les champs ne sont pas vide
        if(nom.length() > 0 &&
                motPasse.length() > 0 &&
                adresse.length() > 0 &&
                phone.length() > 0) {
                // Vérifie si le numéro de téléphone est valide
                if (Util.phoneValide(phone)) {
                    return true;
                }
                else {
                    // Erreur sur la validation du numéro de téléphone
                    Util.afficherAlertBox(getActivity(), "Le numéro de téléphone n'est pas valide", "Numéro de téléphone invalide");
                    return false;
                }
        }
        else {
            // Erreur sur les champs vide
            Util.afficherAlertBox(getActivity(), "Tous les champs sont obligatoire et ne doivent pas être vide!","Erreur");
            return false;
        }
    }

    public void modifier(View view) {
        String nom = txtNom.getText().toString();
        String email = session.getIdentification();
        String motDePasse = txtMotPasse.getText().toString();
        String adresse = txtAdresse.getText().toString();
        String phone = txtNumTel.getText().toString();

        if (champsValide(nom.trim(), motDePasse.trim(),adresse.trim(),phone.trim())) {
            // Insertion dans la bd
            UserRepo repo = new UserRepo(getActivity().getApplicationContext());
            User newUser = new User();
            newUser.set_nom(nom);
            newUser.set_identification(email);
            newUser.set_motPasse(Util.encryptPassword(motDePasse));
            newUser.set_adresse(adresse);
            newUser.set_phone(phone);
            repo.update(newUser);
            Toast.makeText(getActivity().getApplicationContext(), "Modification réussie", Toast.LENGTH_SHORT).show();
        }
    }
}
