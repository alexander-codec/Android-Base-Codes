package com.itobuz.android.easybmicalculator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

/**
 * Created by itobuz-01 on 26/10/16.
 */

public class ListDataAdapter extends ArrayAdapter {

    List<DataProvider> mlist;
    FragmentManager fm;
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private int pos;
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());

    public ListDataAdapter(Context context, int resource,List<DataProvider> list )
    {
        super(context, resource);
        mlist = list;

    }

    static class LayoutHandler{
        TextView date, weight,height;
        Button infoDelete;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View mview = convertView;
        final LayoutHandler layoutHandler;
        if(mview==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mview = layoutInflater.inflate(R.layout.row_history,parent,false);
            layoutHandler = new LayoutHandler();
            layoutHandler.infoDelete = (Button)mview.findViewById(R.id.info_delete);
            layoutHandler.date = (TextView)mview.findViewById(R.id.tv_date);
            layoutHandler.weight = (TextView)mview.findViewById(R.id.tv_age);
            layoutHandler.height = (TextView)mview.findViewById(R.id.tv_result);

            mview.setTag(layoutHandler);
        }else {
            layoutHandler = (LayoutHandler) mview.getTag();
        }

        dbHelper = new DatabaseHelper(getContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();
        DataProvider dataProvider = (DataProvider)this.getItem(position);

        layoutHandler.infoDelete.setTag(dataProvider.getId());
        layoutHandler.date.setText("Date : "+dataProvider.getDate());
        layoutHandler.weight.setText("Age : "+String.valueOf(dataProvider.getAge()));
        layoutHandler.height.setText("Result : "+String.valueOf(dataProvider.getResult()));

        layoutHandler.infoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pos = (int)v.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ListDataAdapter.this.getContext());
                builder.setTitle("Delete Alert");
                builder.setMessage("Do you want to delete ? ");
                builder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Toast.makeText(getContext(),"No is clicked",Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if(pos!=1){
                                    sqLiteDatabase.execSQL("DELETE FROM BmiPersons where id = "+pos );
                                    Toast.makeText(getContext(),"Delete Success !",Toast.LENGTH_SHORT).show();
//                                    ListDataAdapter.this.notifyDataSetChanged();

                                }else {
                                    Toast.makeText(getContext(),"You Can't delete initial data. ",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                builder.show();
            }
        });

        return mview;
    }


}