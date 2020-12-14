package com.mp3player.searchonline;
/**
 * Created by Usman Jamil on 02/02/2017.
 * Usmans.net
 * Skype usman.jamil78
 * email usmanjamil547@gmail.com
 */
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.mp3player.Adapter.CustomAdapter;
import com.mp3player.model.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SongFragment extends Fragment implements constants  {
    ListView lv;
    RelativeLayout NothingToShow;
    ProgressBar loading;
    private List<Song> songList = new ArrayList<Song>();
    private CustomAdapter adapter;
    int offset =0;

    private SearchView mSearchView;
    String[] Final_Suggestions=null;
    private SimpleCursorAdapter mAdapter;
    String SearchText=null;
    MaterialRefreshLayout materialRefreshLayout;
    View view;
    private long downloadID;
    private DownloadManager downloadManager;
    InterstitialAd mInterstitialAd;
    SharedPreferences sharedpreferences;
    Boolean Is = false;
    public SongFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_one, container, false);

        lv = (ListView) view.findViewById(R.id.lvVideo);
        NothingToShow = (RelativeLayout)view.findViewById(R.id.NothingToShow);
        loading = (ProgressBar)view.findViewById(R.id.progressBar);

        //load Ad

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("E217AFB254077FA0ACCAC26583532DB8").build();
   mAdView.loadAd(adRequest);
//
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
               // beginPlayingGame();
            }
        });

        requestNewInterstitial();
        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        materialRefreshLayout.finishRefresh();
    //    materialRefreshLayout.autoRefreshLoadMore();// pull up refresh automatically

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                LoadMore(SearchText);
            }
        });

                setHasOptionsMenu(true);

        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {R.id.suggest};
        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.item_suggestion,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);



            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                    final CharSequence[] items = {"Play", "Download"};
                    final Song m = songList.get(position);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Options");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            if (items[item].equals("Download")) {


                                String cutTitle = m.getTitle();
                                    cutTitle = cutTitle.replace(" ", "-").replace(".", "-") + ".mp3";
                                    downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(m.getStream_url()));
                                    request.setTitle(m.getTitle());
                                    request.setDescription("Downloading");
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(DOWNLOAD_DIRECTORY, cutTitle);
                                    request.allowScanningByMediaScanner();
                                    downloadID = downloadManager.enqueue(request);
                                    mInterstitialAd.show();

                                    Toast.makeText(getActivity(), "Downloading Start", Toast.LENGTH_SHORT).show();


                                return;
                            } else if (items[item].equals("Play")) {

                                // Uri uri = Uri.parse(m.getStream_url());
                                //Intent intent = new Intent(Intent.ACTION_VIEW);
                                //intent.setDataAndType(uri, "audio/mp3");
                                //startActivity(intent);

                                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                                Bundle extras = new Bundle();
                                //intent.putExtra(ID, m.getID());

                                extras.putString("TITLE", m.getTitle());
                                extras.putString("ARTWORK", m.getArtwork_url());
                                extras.putString("STREAM", m.getStream_url());
                                intent.putExtras(extras);
                                mInterstitialAd.show();
                                startActivity(intent);

                            }

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });



return  view;

    }



    public void search(String text){


        if (JSONParser.isNetworkAvailable(getActivity())) {
            loading.setVisibility(loading.VISIBLE);
            NothingToShow.setVisibility(NothingToShow.INVISIBLE);

            String Furl=URL_SEARCH_API+"?client_id="+CLIENT_ID2+"&q="+Uri.encode(text);
            new HttpAsyncTaskSearch().execute(Furl);
        } else {
            toast("No Network Connection!!!");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle("No Internet");
            builder.setMessage("Internet is required. Please Retry.");

            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                 }
            });
            AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
            dialog.show();
        }



    }

    public void LoadMore(String value){
        offset=offset+20;
        String Furl=URL_SEARCH_API+"?client_id="+CLIENT_ID2+"&q="+Uri.encode(value)+"&offset="+offset;
        new HttpAsyncTaskLoadMore().execute(Furl);

    }
    public void toast (String text){
        Toast.makeText(getActivity(),text, Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {



            android.support.v7.app.AlertDialog.Builder localBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            localBuilder.setTitle(getString(R.string.action_settings));
            localBuilder
                    .setMessage(getString(R.string.disclaimer)).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(
                                DialogInterface paramAnonymousDialogInterface,
                                int paramAnonymousInt) {
                            paramAnonymousDialogInterface.dismiss();

                        }
                    });
            localBuilder.show();

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setSuggestionsAdapter(mAdapter);

        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                                                @Override
                                                public boolean onSuggestionSelect(int position) {
                                                    return false;
                                                }
                                                @Override
                                                public boolean onSuggestionClick(int position) {

                                                    if (Final_Suggestions != null && Final_Suggestions.length > 0) {
                                                        mSearchView.setQuery(Final_Suggestions[position], false);
                                                        //processSearchData(Final_Suggestions[position], false);
                                                        search(Final_Suggestions[position]);

                                                    }
                                                    return false;
                                                }

                                            }

        );

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.matches("")) {
                    // s=URLEncoder.encode(s, "utf-8");

                    if (JSONParser.isNetworkAvailable(getActivity())) {
                        new HttpAsyncTaskSuggestion().execute(String.format(URL_FORMAT_SUGESSTION, Uri.encode(s)));
                    }
                    SearchText = s;


                }
                return false;
            }
        });

       // return true;
    }


    private class HttpAsyncTaskSuggestion extends AsyncTask<String, Void, JSONArray> {
        JSONParser FJson = new JSONParser();
        @Override
        protected JSONArray doInBackground(String... urls) {
            return FJson.getJSONFromUrl(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(JSONArray result) {
            String resultU=result.toString();
            String replace1= "[\""+SearchText+"\",[";
            String replace2= "]]";
            Final_Suggestions = resultU.replace(replace1, "").replace(replace2, "").replace("\"","").replace("[","").split("\\,");
            // Toast.makeText(getBaseContext(), SearchText, Toast.LENGTH_LONG).show();
            final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
            for (int i=0; i<Final_Suggestions.length; i++) {
                if (Final_Suggestions[i].toLowerCase().startsWith(SearchText))
                    Final_Suggestions[i]=Final_Suggestions[i];
                c.addRow(new Object[] {i, Final_Suggestions[i]});
            }
            mAdapter.changeCursor(c);

        }
    }

    private class HttpAsyncTaskSearch extends AsyncTask<String, Void, JSONArray> {
        JSONParser FJson = new JSONParser();
        @Override
        protected JSONArray doInBackground(String... urls) {
            return FJson.getJSONFromUrl(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(JSONArray result) {


                    songList.clear();
            for (int i = 0; i < result.length(); i++) {
                try {
                    Song song = new Song();

                    JSONObject obj = result.getJSONObject(i);
                    song.setTitle(obj.getString("title"));
                    song.setArtwork_url(obj.getString("artwork_url"));
                    song.setStream_url(obj.getString("stream_url"));
                    song.setDuration(obj.getInt("duration"));
                    song.setFav(obj.getInt("favoritings_count"));
                    song.setLike(obj.getInt("playback_count"));
                    songList.add(song);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(result.length()==0){
                materialRefreshLayout.setLoadMore(false);
                toast(getString(R.string.no_result)+SearchText);

            }
            adapter = new CustomAdapter(getActivity(),songList);
            lv.setAdapter(adapter);
            materialRefreshLayout.finishRefreshLoadMore();
            loading.setVisibility(loading.INVISIBLE);

            lv.setVisibility(lv.VISIBLE);

        }
    }
    private class HttpAsyncTaskLoadMore extends AsyncTask<String, Void, JSONArray> {
        JSONParser FJson = new JSONParser();
        @Override
        protected JSONArray doInBackground(String... urls) {
            return FJson.getJSONFromUrl(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(JSONArray result) {



            for (int i = 0; i < result.length(); i++) {
                try {
                    Song song = new Song();

                    JSONObject obj = result.getJSONObject(i);
                    song.setTitle(obj.getString("title"));
                    song.setArtwork_url(obj.getString("artwork_url"));
                    song.setStream_url(obj.getString("stream_url"));
                    song.setDuration(obj.getInt("duration"));
                    song.setFav(obj.getInt("favoritings_count"));
                    song.setLike(obj.getInt("likes_count"));
                    songList.add(song);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(result.length()==0){
                materialRefreshLayout.setLoadMore(false);
                toast(getString(R.string.no_more));

            }
            adapter.notifyDataSetChanged();
            materialRefreshLayout.finishRefreshLoadMore();

        }
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("BE9BB532808E71B746B8C96E58F9576D")
                .addTestDevice("FFCE8C2A9F940F24B9B69E0D0728064C")
                .build();




        mInterstitialAd.loadAd(adRequest);
    }

}