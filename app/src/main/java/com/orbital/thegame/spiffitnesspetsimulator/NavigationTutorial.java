package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class NavigationTutorial extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(R.layout.tutorial_navigation);

        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FragmentManager fm = getFragmentManager();
                MenuTutorial menuTutorial = new MenuTutorial();
                menuTutorial.setRetainInstance(true);
                menuTutorial.show(fm, "fragment_name");
            }
        });

        return builder.create();
    }
}
