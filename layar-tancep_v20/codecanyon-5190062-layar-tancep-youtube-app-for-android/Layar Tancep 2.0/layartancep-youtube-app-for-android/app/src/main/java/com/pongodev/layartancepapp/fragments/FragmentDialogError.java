package com.pongodev.layartancepapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Design and developed by pongodev.com
 *
 * FragmentDialogError is created to display the error dialog generated in showErrorDialog.
 * Created using DialogFragment.
 */
public class FragmentDialogError extends DialogFragment {
    // Global field to contain the error dialog
    private Dialog mDialog;

    /**
     * Default constructor. Sets the dialog field to null
     */
    public FragmentDialogError() {
        super();
        mDialog = null;
    }

    /**
     * Set the dialog to display
     *
     * @param dialog An error dialog
     */
    public void setDialog(Dialog dialog) {
        mDialog = dialog;
    }

    /*
     * This method must return a Dialog to the DialogFragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialog;
    }
}
