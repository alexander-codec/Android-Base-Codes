package com.itobuz.android.easybmicalculator;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryTabFragment extends Fragment {


    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private List<DataProvider> mBmiList;
    private ListDataAdapter mListDataAdapter;
    ListView listView;
    Cursor cursor;

    private int list_id;
    private String list_date;
    private String list_age;
    private Float list_result;


    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    public static HistoryTabFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        HistoryTabFragment historyFragment = new HistoryTabFragment();
        historyFragment.setArguments(args);
        return historyFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_history, container, false);

        listView = (ListView) mview.findViewById(R.id.lv_bmi);
        mBmiList = new ArrayList<>();
        dbHelper = new DatabaseHelper(getContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getBmiInformation();
        if (cursor.moveToFirst()){
            do{

                list_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                list_date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
                list_age = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AGE));
                list_result = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RESULT)));
                DataProvider dataProvider = new DataProvider(list_id,list_date,list_age,list_result);
                mBmiList.add(dataProvider);
            }while (cursor.moveToNext());

        }

        mListDataAdapter = new ListDataAdapter(getContext(),R.layout.row_history,mBmiList);
        listView.setAdapter(mListDataAdapter);

        return mview;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //updatePersonList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            updatePersonList();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        mListDataAdapter.notifyDataSetChanged();
        updatePersonList();
    }


    public void updatePersonList(){
        mBmiList.clear();
        dbHelper = new DatabaseHelper(getContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();
        cursor = dbHelper.getBmiInformation();
        if (cursor.moveToFirst()){
            do{
                list_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                list_date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
                list_age = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AGE));
                list_result = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RESULT)));

                DataProvider dataProvider = new DataProvider(list_id,list_date,list_age,list_result);
                mBmiList.add(dataProvider);
            }while (cursor.moveToNext());
        }
        mListDataAdapter.notifyDataSetChanged();
    }



}
