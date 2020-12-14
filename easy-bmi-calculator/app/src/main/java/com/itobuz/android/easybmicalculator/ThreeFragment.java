package com.itobuz.android.easybmicalculator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.paolorotolo.appintro.ISlidePolicy;

public class ThreeFragment extends Fragment implements ISlidePolicy {

    private static final String USER_WEIGHT = "userWeight";
    private static final String USER_WEIGHT_UNIT = "userWeightUnit";
    private static final String USER_WEIGHT_UNIT_POS = "userWeightUnitPos";

    private AppCompatEditText weightEt;
    private Spinner weightUnitSp;

    private String userWeight;
    private String userWeightUnit;
    private int userWeightUnitPos;

    SharedPreferences pref;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.three_fragment, container, false);

        weightEt = (AppCompatEditText) view.findViewById(R.id.weight);

        weightUnitSp = (Spinner) view.findViewById(R.id.weight_unit);

        setupHeightSpinners();

        return view;
    }

    void setupHeightSpinners(){
        weightUnitSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override public void onItemSelected(AdapterView parent, View view, int position, long id) {
                //I.E. if in the height spinner CM is selected I would like to hide the second height edittext field.
                // I'm not sure if this is meant to be "height1" or "height"
                if (position == 0){
                    weightEt.setHint(null);
                    weightEt.setHint("Kg");

                } else {
                    weightEt.setHint(null);
                    weightEt.setHint("Lb");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do Something.
                weightEt.setHint("Kg");
            }
        });
    }



    @Override
    public boolean isPolicyRespected() {

        userWeight = weightEt.getText().toString();

        userWeightUnit = weightUnitSp.getSelectedItem().toString();
        userWeightUnitPos = weightUnitSp.getSelectedItemPosition();

        // Assign data in Shared Preference
        pref = getActivity().getPreferences(0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString(USER_WEIGHT, userWeight);
        edt.putString(USER_WEIGHT_UNIT, userWeightUnit);
        edt.putInt(USER_WEIGHT_UNIT_POS, userWeightUnitPos);
        edt.commit();

        return userWeight.length() > 0;
    }



    @Override
    public void onUserIllegallyRequestedNextPage() {

        if(userWeight.length() == 0) {

//            if(userWeightUnitPos == 0) {
//                Toast.makeText(getContext(), "You have select Kg "+ userWeightUnitPos , Toast.LENGTH_SHORT).show();
//            }


            // if weight value not entered
            Toast.makeText(getContext(), R.string.weight_policy_error, Toast.LENGTH_SHORT).show();
        }

    }


}

