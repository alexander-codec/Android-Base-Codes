package com.itobuz.android.easybmicalculator;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.ISlidePolicy;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinalFragment extends Fragment {

    private static final String USER_AGE = "userAge";
    private static final String USER_SEX ="userSex";

    private static final String USER_WEIGHT = "userWeight";
    private static final String USER_WEIGHT_UNIT = "userWeightUnit";
    private static final String USER_WEIGHT_UNIT_POS = "userWeightUnitPos";

    private static final String USER_HEIGHT = "userHeight";
    private static final String USER_HEIGHT_INCH = "userHeightInch";
    private static final String USER_HEIGHT_UNIT = "userHeightUnit";
    private static final String USER_HEIGHT_UNIT_POS = "userHeightUnitPos";

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";

    private TextView resultBmi;

    private int layoutResId;
    private float bmiResult;

    private int result_bmi;
    private String userAge;
    private String userSex;
    private double userWeight;
    private String userWeightUnit;
    private int userWeightUnitPos;
    private double userHight;
    private double userHightInch;
    private String userHeightUnit;
    private int userHeightUnitPos;
    private  String strDate;
    private int userStatus;

    DatabaseHelper mydb;
    Context context;

    public static FinalFragment newInstance(int layoutResId) {
        FinalFragment finalFragment = new FinalFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        finalFragment.setArguments(args);

        return finalFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View resultBmiView = inflater.inflate(R.layout.final_fragment, container,false);

        resultBmi = (TextView) resultBmiView.findViewById(R.id.result_bmi);

        return resultBmiView;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            SharedPreferences pref = getActivity().getPreferences(0);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            strDate = sdf.format(new Date());

            userAge = pref.getString(USER_AGE, "0");
            userSex = pref.getString(USER_SEX, "empty");

            userWeight = Double.parseDouble(pref.getString(USER_WEIGHT, "0"));
            userWeightUnit = pref.getString(USER_WEIGHT_UNIT, "empty");
            userWeightUnitPos = pref.getInt(USER_WEIGHT_UNIT_POS, 0);

            userHight = Double.parseDouble(pref.getString(USER_HEIGHT, "0"));
            userHightInch = Double.parseDouble(pref.getString(USER_HEIGHT_INCH, "0"));
            userHeightUnit = pref.getString(USER_HEIGHT_UNIT, "empty");
            userHeightUnitPos = pref.getInt(USER_HEIGHT_UNIT_POS, 0);
            userStatus = 1;

            BmiHelper bh = new BmiHelper();

            if(userWeightUnitPos==0 && userHeightUnitPos==0 ){
                bmiResult = (float) bh.getBMIKg(userHight,userWeight);
            }else if(userWeightUnitPos==1 && userHeightUnitPos==0 ) {
                bmiResult = (float) bh.getBMIKg(userHight,bh.lbToKgConverter(userWeight));
            }else if(userWeightUnitPos==0 && userHeightUnitPos==1 ) {
                bmiResult = (float) bh.getBMIKg(bh.feetInchToCmConverter(userHight, userHightInch),userWeight);
            }else if(userWeightUnitPos==1 && userHeightUnitPos==1 ) {
                bmiResult = (float) bh.getBMILb(userHight, userHightInch, userWeight);
            }

            result_bmi = (int) Math.round(bmiResult);
            animateTextView(0,result_bmi,resultBmi);

            mydb = new DatabaseHelper(this.getContext());

            if(mydb.insertBmiRow(userAge, userSex, userWeight,userWeightUnit, userHight, userHightInch, userHeightUnit, bmiResult,strDate, userStatus)) {

                context = getContext().getApplicationContext();
                Toast.makeText(context, "Details Saved Successfully", Toast.LENGTH_SHORT).show();

            }


        }
    }


    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.8f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = ( finalValue - initialValue); //Math.abs
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {
            int time = Math.round(decelerateInterpolator.getInterpolation((((float) count) / difference)) * 100) *  count;
            final int finalCount = ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textview.setText(finalCount + "");
                }
            }, time);
        }
    }

}
