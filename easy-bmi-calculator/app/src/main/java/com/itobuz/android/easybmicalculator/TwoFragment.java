package com.itobuz.android.easybmicalculator;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.paolorotolo.appintro.ISlidePolicy;

public class TwoFragment extends Fragment implements ISlidePolicy {

    private static final String USER_AGE = "userAge";
    private static final String USER_SEX ="userSex";

    private AppCompatEditText ageEt;
    private AppCompatRadioButton maleRadioButton;
    private AppCompatRadioButton femaleRadioButton;

    private String userAge;
    private String userSex;
//    private Button calculate_btn;

    SharedPreferences pref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_fragment, container, false);

        //get edit text
        ageEt = (AppCompatEditText) view.findViewById(R.id.age);

        //get radio buttons
        maleRadioButton = (AppCompatRadioButton) view.findViewById(R.id.radioMale);
        femaleRadioButton = (AppCompatRadioButton) view.findViewById(R.id.radioFemale);


        return view;
    }

    @Override
    public boolean isPolicyRespected() {

        // get age value
        userAge = ageEt.getText().toString();
        //check which one and set userSex
        userSex = (maleRadioButton.isChecked()) ? "Male" : (femaleRadioButton.isChecked()) ? "Female" : "";

        // Assign data in Shared Preference
        pref = getActivity().getPreferences(0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString(USER_AGE, userAge);
        edt.putString(USER_SEX, userSex);
        edt.commit();

        //return if all field set
        return userAge.length() > 0 && (maleRadioButton.isChecked() || femaleRadioButton.isChecked());

    }


    @Override
    public void onUserIllegallyRequestedNextPage() {

        //make toast error if all field not filled
        if(userAge.length() == 0 ){
            Toast.makeText(getContext(), R.string.age_policy_error, Toast.LENGTH_SHORT).show();
        }
        else if(userSex == "") {
            Toast.makeText(getContext(), R.string.gender_policy_error, Toast.LENGTH_SHORT).show();
        }

    }

}
