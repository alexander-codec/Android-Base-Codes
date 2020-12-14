package com.itobuz.android.easybmicalculator;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsTabFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    ArrayAdapter<CharSequence> adapter;
    DatabaseHelper mdb;
    Context context;

    ViewPager viewPager;

    private AppCompatEditText editAgeText;
    private AppCompatEditText editWeightText;
    private Spinner editWeightunit;
    private AppCompatEditText editHeightText;
    private AppCompatEditText editHeightInchText;
    private Spinner editHeightunit;
    private RadioButton editMale;
    private RadioButton editFemale;
    private Button calculateBtn;
    private Button cancelBtn;


    private int result_bmi;
    private  int userId;
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
    private String classify;

    private float bmiResult;

    public static SettingsTabFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        SettingsTabFragment settingsFragment = new SettingsTabFragment();
        settingsFragment.setArguments(args);
        return settingsFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        viewPager = (ViewPager) getActivity().findViewById(R.id.view_pager);

        editAgeText = (AppCompatEditText) view.findViewById(R.id.editAge);
        editWeightText =  (AppCompatEditText) view.findViewById(R.id.editWidth);
        editWeightunit =  (Spinner) view.findViewById(R.id.editWeight_unit);
        editHeightText = (AppCompatEditText) view.findViewById(R.id.editHeight);
        editHeightInchText = (AppCompatEditText) view.findViewById(R.id.editHeightInch);
        editHeightunit =  (Spinner) view.findViewById(R.id.editHeight_unit);

        editMale = (RadioButton) view.findViewById(R.id.editRadioMale);
        editFemale = (RadioButton) view.findViewById(R.id.editRadioFemale);


        calculateBtn = (Button) view.findViewById(R.id.editCalculate);
        cancelBtn = (Button) view.findViewById(R.id.editCancel);

        mdb = new DatabaseHelper(this.getContext());
        userId= mdb.lastBmiId();
        //showUserValue();

        // if select height spinner inch field setup
        settingsHeightSpinners();


        editAgeText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    calculateBtn.setEnabled(false);
                } else {
                    calculateBtn.setEnabled(true);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        editWeightText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    calculateBtn.setEnabled(false);
                } else {
                    calculateBtn.setEnabled(true);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        editHeightText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    calculateBtn.setEnabled(false);
                } else {
                    calculateBtn.setEnabled(true);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        //Calculate button click action
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                strDate = sdf.format(new Date());

                userAge = editAgeText.getText().toString();
                userSex = (editMale.isChecked()) ? "Male" : (editFemale.isChecked()) ? "Female" : "";

                try {
                    userWeight = Double.valueOf( editWeightText.getText().toString());
                } catch (NumberFormatException e) {
                    userWeight=0;
                }

                userWeightUnit = editWeightunit.getSelectedItem().toString();
                userWeightUnitPos = editWeightunit.getSelectedItemPosition();

                try {
                    userHight = Double.valueOf( editHeightText.getText().toString());
                } catch (NumberFormatException e) {
                    userHight=0;
                }

                try {
                    userHightInch = Double.valueOf( editHeightInchText.getText().toString());
                } catch (NumberFormatException e) {
                    userHightInch=0;
                }

                userHeightUnit = editHeightunit.getSelectedItem().toString();
                userHeightUnitPos = editHeightunit.getSelectedItemPosition();
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

//                if(userWeightUnitPos==0 && userHeightUnitPos==0 ){
//                    bmiResult = (float) bh.getBMILb(userHight,userHightInch,bh.kgToLbConverter(userWeight));
//                }else if(userWeightUnitPos==1 && userHeightUnitPos==0 ) {
//                    bmiResult = (float) bh.getBMILb(userHight, userHightInch,userWeight);
//                }else if(userWeightUnitPos==0 && userHeightUnitPos==1 ) {
//                    bmiResult = (float) bh.getBMIKg(userHight,userWeight);
//                }else if(userWeightUnitPos==1 && userHeightUnitPos==1 ) {
//                    bmiResult = (float) bh.getBMIKg(userHight,bh.lbToKgConverter( userWeight));
//                }

                if(mdb.insertBmiRow(userAge, userSex, userWeight,userWeightUnit, userHight, userHightInch, userHeightUnit, bmiResult,strDate, userStatus)) {

                    context = getContext().getApplicationContext();

                    classify= bh.getBMIClassification(bmiResult);
                    Toast.makeText(context, "Your Bmi is "+classify, Toast.LENGTH_SHORT).show();

                    viewPager.setCurrentItem(0);

                }
            }
        });

        // Calncel button onclick action
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                viewPager.setCurrentItem(0);
            }
        });

        return view;
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        showUserValue();
    }

    private void showUserValue(){
        Cursor c = mdb.lastRecords();
        c.moveToLast();
        if (c != null) {
            editAgeText.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_AGE)));
            editWeightText.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_WEIGHT)));
            editHeightText.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_HEIGHT)));

            // set weight spinner position;
                adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.weightArray, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editWeightunit.setAdapter(adapter);
                int weightSpinnerPos = adapter.getPosition(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_WEIGHT_UNIT)));
                editWeightunit.setSelection(weightSpinnerPos);

            // set height spinner position;
                adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.heightArray, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editHeightunit.setAdapter(adapter);
                int heightSpinnerPos = adapter.getPosition(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_HEIGHT_UNIT)));
                editHeightunit.setSelection(heightSpinnerPos);

            if(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_HEIGHT_UNIT)).equals("Cm")){
                //do something
                editHeightInchText.setVisibility(View.GONE);
            }else{
                editHeightInchText.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_HEIGHT_INCH)));
                editHeightInchText.setVisibility(View.VISIBLE);
            }
            String sex = (c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_SEX)));

            if(sex.equals("Male")){
                editMale.setChecked(true);
            }else if(sex.equals("Female")) {
                editFemale.setChecked(true);
            }

        }


    }


    public void settingsHeightSpinners(){
        editHeightunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override public void onItemSelected(AdapterView parent, View view, int position, long id) {

                //I.E. if in the height spinner CM is selected I would like to hide the second height edittext field.

                if (position == 0){
                    editHeightInchText.setVisibility(View.GONE);

                } else {
                    editHeightInchText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do Something.

            }
        });
    }

}
