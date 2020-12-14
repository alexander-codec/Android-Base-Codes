package com.itobuz.android.easybmicalculator;



import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTabFragment extends Fragment {

    private GaugeView mGaugeView;
    private TextView userInfo, infoResult, infoSuggest;
    private ImageView imageView;

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

//    private float bmiResult;
    private float bmi_result;
    private String userAge;
    private double userWeight;
    private String userWeightUnit;
    private double userHeight;
    private int userHeightInch;
    private String userHeightUnit;

    DatabaseHelper mdb;
    ArrayAdapter<CharSequence> adapter;
    Context context;
    BmiHelper bh;

    public static HomeTabFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        HomeTabFragment fragment = new HomeTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageView = (ImageView) view.findViewById(R.id.expression);
        mGaugeView = (GaugeView) view.findViewById(R.id.gauge_view);
        userInfo = (TextView) view.findViewById(R.id.user_info);
        infoResult = (TextView) view.findViewById(R.id.info_result);
        infoSuggest = (TextView) view.findViewById(R.id.result_suggest);
//        bh = new BmiHelper();
//        mdb = new DatabaseHelper(this.getContext());
//        showUserDetails();

        return view;
    }



    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        bh = new BmiHelper();
        mdb = new DatabaseHelper(this.getContext());
        showUserDetails();
    }

    /**
     * showUserDetails() method.
     * user details and gauge meter create.
     */

    private void showUserDetails(){

        //cursor initialize to fetch last records from db.
        Cursor c = mdb.lastRecords();
        c.moveToLast();

        //check cursor not equals null
        if (c != null) {
            //get value from database
            userAge = (c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_AGE)));
            userWeight = Double.parseDouble(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_WEIGHT)));
            userWeightUnit = (c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_WEIGHT_UNIT)));
            userHeight = Double.parseDouble(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_HEIGHT)));
            userHeightInch = (c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_HEIGHT_INCH)));
            userHeightUnit = (c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_HEIGHT_UNIT)));
            bmi_result = (c.getFloat(c.getColumnIndex(DatabaseHelper.COLUMN_RESULT)));

            //set gauge meter value
            mGaugeView.setTargetValue(bmi_result);

            // show result in text view
            infoResult.setText(""+bmi_result);

            if(bmi_result < 18.5) {
                infoResult.setTextColor(Color.rgb(0, 153, 232));
                imageView.setImageResource(R.drawable.ic_expressions_blue);
            } else if (bmi_result < 25){
                infoResult.setTextColor(Color.rgb(0, 174, 74));
                imageView.setImageResource(R.drawable.ic_expressions_green);
            } else if (bmi_result < 30) {
                infoResult.setTextColor(Color.rgb(255, 198, 0));
                imageView.setImageResource(R.drawable.ic_expressions_yellow);
            } else {
                infoResult.setTextColor(Color.rgb(224, 25, 43));
                imageView.setImageResource(R.drawable.ic_expressions_red);
            }


            infoSuggest.setText("Your BMI is "+bh.getBMIClassification(bmi_result));

            //user information text setup
            if(userHeightUnit.equals("Cm")) {
                userInfo.setText(userAge+" yr | "+userWeight+" "+userWeightUnit+" | "+userHeight+" "+userHeightUnit);
            }else{
                userInfo.setText(userAge+" yr | "+userWeight+" "+userWeightUnit+" | "+(int) userHeight+"' "+userHeightInch+"\"");
            }

        }//end if

    } //end showDetails()
}