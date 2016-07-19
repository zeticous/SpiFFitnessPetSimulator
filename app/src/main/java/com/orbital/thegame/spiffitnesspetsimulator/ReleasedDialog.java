package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ReleasedDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
    //    alertDialogBuilder.setTitle("Release Spirits");
        alertDialogBuilder.setMessage("Release successful. You are given a new egg.");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GameService.UserSpirit = GameService.UserSpirit.initialise();
                getActivity().finish();
                dialog.dismiss();
            }

        });
        return alertDialogBuilder.create();
    }
}
