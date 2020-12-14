package com.mp3player.searchonline;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class DownloadFragment extends Fragment implements  constants {
    View view;
    ListView lv;
    String[] items;
    RelativeLayout NoShow;
    ArrayAdapter<String> adp;
    ArrayList<File> mySongs;
    public DownloadFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            mySongs = findSongs(new File(Environment.getExternalStorageDirectory()+DOWNLOAD_DIRECTORY));

            setVal();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_two, container, false);
        lv=(ListView)view.findViewById(R.id.lvVideo);
        NoShow=(RelativeLayout)view.findViewById(R.id.NothingToShow);
        File dir  =  new File(Environment.getExternalStorageDirectory()+DOWNLOAD_DIRECTORY);


        if (!dir.exists() && !dir.isDirectory()) {
                    dir.mkdir();
        }
        mySongs = findSongs(dir);



        setVal();





        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                final CharSequence[] items = {"Play", "Delete"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Options");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {


                        if (items[item].equals("Delete")) {
                            removeItemFromList(position);
                        } else if (items[item].equals("Play")) {


                            Intent intent1 = new Intent(getActivity(), PlayerActivity.class);
                            Bundle extras = new Bundle();
                            //intent.putExtra(ID, m.getID());

                            extras.putString("TITLE", mySongs.get(position).getName().toString());
                            extras.putString("ARTWORK", "nothing");
                            extras.putString("STREAM", mySongs.get(position).getAbsolutePath());
                            intent1.putExtras(extras);

                            startActivity(intent1);
                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        return  view;
    }

    public void setVal(){
        items = new String[mySongs.size()];

        if(mySongs.size()>0){
            NoShow.setVisibility(NoShow.INVISIBLE);
            lv.setVisibility(lv.VISIBLE);
        }
        for(int i=0; i<mySongs.size(); i++){
            //toast(mySongs.get(i).toString());
            items[i]=mySongs.get(i).getName().toString().replace(".mp3","");

        }
        adp= new ArrayAdapter<String>(getActivity(),R.layout.song_down_layout,R.id.title,items);
        lv.setAdapter(adp);
    }

    protected void removeItemFromList(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(
                getActivity());

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File  f = new File(mySongs.get(deletePosition).getAbsolutePath());
                f.delete();
              mySongs.remove(deletePosition);
                setVal();
                adp.notifyDataSetChanged();
                adp.notifyDataSetInvalidated();
                Toast.makeText(getActivity(), "Song Deleted!", Toast.LENGTH_SHORT).show();

            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();

    }
    public ArrayList<File> findSongs(File root){
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(findSongs(file));
            } else {
                if(file.getName().endsWith(".mp3")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;

    }

    public void toast (String text){
        Toast.makeText(getActivity(),text, Toast.LENGTH_SHORT).show();

    }


}