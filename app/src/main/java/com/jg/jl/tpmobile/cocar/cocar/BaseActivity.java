package com.jg.jl.tpmobile.cocar.cocar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class BaseActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getString(R.string.title_Rechercher);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    //Évènement qui indique quel fragment pour chaque élément de la liste du menu.
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        android.app.Fragment objFragment = null;

        switch (position) {
            case 0:
                objFragment = new creation_fragment();
                break;
            case 1:
                objFragment = new depart_fragment();
                break;
            case 2:
                objFragment = new proposition_fragment();
                break;
            case 3:
                objFragment = new note_fragment();
                break;
            case 4:
                objFragment = new profil_fragment();
                break;
            case 5:
                objFragment = new updateUser_fragment();
                break;
        }
        // update the main content by replacing fragments
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objFragment)
                .commit();
        onSectionAttached(position + 1);
        restoreActionBar();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_Rechercher);
                break;
            case 2:
                mTitle = getString(R.string.title_Depart);
                break;
            case 3:
                mTitle = getString(R.string.title_Proposition);
                break;
            case 4:
                mTitle = getString(R.string.title_Note);
                break;
            case 5:
                mTitle = getString(R.string.title_Profil);
                break;
            case 6:
                mTitle = getString(R.string.title_modifProfil);
                break;
        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.base, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Deconnexion) {
            UserRepo repo = new UserRepo(this);
            session = new SessionManager(getApplicationContext());
            repo.delete(session.getIdentification());
            session.logoutUser();
            Intent i = new Intent(BaseActivity.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        if(id == R.id.action_help) {
            if (mTitle.equals("Créer un parcours") || mTitle.equals("Create a travel")) {
                Util.afficherAlertBox(BaseActivity.this,"Vous avez le choix de créer un parcours passager ou conduceur." +
                        " Avec les informations nécessaires, vous allez créer un parcours qui va être disponible par tous les autres utilisateurs de CoCar. ",
                        "Guide utilisateur - Créer Parcours");
            }
            if (mTitle.equals("Départ") || mTitle.equals("Departure")) {
                Util.afficherAlertBox(BaseActivity.this,"Vous avez une liste de tous vos départs que vous avez acceptés." +
                        " Si vous appuyez sur les départs, vous allez retrouver plus d’information sur ce départ" +
                        " et vous pouvez voir le départ sur une carte en appuyant sur <AFFICHER CARTE> et la supprimer","Guide utilisateur - Départ");
            }
            if (mTitle.equals("Proposition") || mTitle.equals("Offer")) {
                Util.afficherAlertBox(BaseActivity.this,"Vous avez une liste de toutes les propositions que vous pouvez accepter." +
                        " Lorsque vous appuyez sur une des propositions, il vous affiche plus d’information sur cette proposition et la possibilité d'accepter cette offre" +
                        " et vous pouvez voir la proposition sur une carte en appuyant sur <VOIR CARTE>"
                        ,"Guide utilisateur - Proposition");
            }
            if (mTitle.equals("Évaluer un utilisateur") ||mTitle.equals("Rate a user")) {
                Util.afficherAlertBox(BaseActivity.this,"Sur cette page, vous pouvez évaluer un autre utilisateur que vous avez partagé la même voiture." +
                        " Le plus d`étoiles que vous donnez, la meilleure note vous y donnerait.","Guide utilisateur - Évaluer");
            }
            if (mTitle.equals("Profil") || mTitle.equals("Profile")) {
                Util.afficherAlertBox(BaseActivity.this,"Sur cette page, vous pouvez voir votre profil avec toutes vos informations. " +
                        "Vous pouvez également voir votre le total des évaluations que les autres utilisateurs vous ont donnés.","Guide utilisateur - Profil");
            }
            if (mTitle.equals("Modifier le profil") || mTitle.equals("Update profile")) {
                Util.afficherAlertBox(BaseActivity.this,"Sur cette page, vous pouvez modifier votre profil." +
                        " Vous pouvez modifier votre nom, votre position, votre numéro de téléphone." +
                        " Si vous désirer changé votre mot de passe, vous entré un différent, mais si vous entré rien," +
                        "le mot de passe va rester le même.","Guide utilisateur - Modifier");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_base, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((BaseActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
