package com.itobuz.android.easybmicalculator;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.ISlidePolicy;


public class FourFragment extends Fragment implements ISlidePolicy {

    private static final String USER_HEIGHT = "userHeight";
    private static final String USER_HEIGHT_INCH = "userHeightInch";
    private static final String USER_HEIGHT_UNIT = "userHeightUnit";
    private static final String USER_HEIGHT_UNIT_POS = "userHeightUnitPos";

//    private LinearLayout layoutContainer;

    private AppCompatEditText heightEt;
    private AppCompatEditText heightInch;
    private Spinner heightUnitSp;

    private String userHeight;
    private String userHeightInch;
    private String userHeightUnit;
    private int userHeightUnitPos;

    SharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.four_fragment, container, false);

//        layoutContainer = (LinearLayout) view.findViewById(R.id.four_fragment);

        heightEt= (AppCompatEditText) view.findViewById(R.id.height);
        heightInch = (AppCompatEditText) view.findViewById(R.id.height_inch);

        heightUnitSp = (Spinner) view.findViewById(R.id.height_unit);

        setupHeightSpinners();

        return view;


    }



    void setupHeightSpinners(){
        heightUnitSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override public void onItemSelected(AdapterView parent, View view, int position, long id) {
                //I.E. if in the height spinner CM is selected I would like to hide the second height edittext field.
                // I'm not sure if this is meant to be "height1" or "height"
                 if (position == 0){
                     heightEt.setHint(null);
                     heightEt.setHint("Cm");
//                     heightEt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                     heightInch.setVisibility(View.GONE);

                 } else {
                     heightInch.setVisibility(View.VISIBLE);
                     heightEt.setHint(null);
                     heightEt.setHint("Feet");

                 }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do Something.
                heightEt.setHint("Cm");
//                heightEt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
        });
     }



        @Override
    public boolean isPolicyRespected() {

        userHeight = heightEt.getText().toString();
        userHeightInch = ( heightInch.getText().toString().length() > 0 ) ? heightInch.getText().toString(): "0";

        userHeightUnit = heightUnitSp.getSelectedItem().toString();
        userHeightUnitPos = heightUnitSp.getSelectedItemPosition();
//            userHeightUnitPos = 0;

        // Assign data in Shared Preference
        pref = getActivity().getPreferences(0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString(USER_HEIGHT, userHeight);
        edt.putString(USER_HEIGHT_INCH, userHeightInch);
        edt.putString(USER_HEIGHT_UNIT, userHeightUnit);
        edt.putInt(USER_HEIGHT_UNIT_POS, userHeightUnitPos);
        edt.commit();

        return userHeight.length() > 0;

    }

    @Override
    public void onUserIllegallyRequestedNextPage() {

        if(userHeight.length() == 0) {
//            Toast.makeText(getContext(), "position is "+userHeightUnitPos, Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), R.string.height_policy_error, Toast.LENGTH_SHORT).show();
        }

    }


}
