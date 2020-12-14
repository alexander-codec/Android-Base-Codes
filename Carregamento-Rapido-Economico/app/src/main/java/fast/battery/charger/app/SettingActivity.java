package fast.battery.charger.app;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;



public class SettingActivity extends PreferenceFragment {

    Intent i;

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String version = info.versionName;

        /*Preference Licence = (Preference) findPreference("Licence");
        Licence.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getActivity().getApplicationContext(), LicenseActivity.class);
                startActivity(i);
                return true;
            }
        });*/

        Preference MoreApp = (Preference) findPreference("MoreApp");
        MoreApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=kinemarts")));// ADD YOUR DEVELOPER NAME HERE USE + FOR SPACE

                return true;
            }
        });

        Preference RateUs = (Preference) findPreference("RateUs");
        RateUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }

                return true;
            }
        });

        Preference ShareApp = (Preference) findPreference("ShareApp");
        ShareApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.setType("text/plain");
                final String text = "Conheça um novo aplicativo com "
                        + getResources().getString(R.string.app_name)
                        + ", o aplicativo e gratuito e você pode baixar em https://play.google.com/store/apps/details?id="
                        + getActivity().getPackageName();
                i.putExtra(Intent.EXTRA_TEXT, text);
                Intent sender = Intent.createChooser(i, "Compartilhar " + getResources().getString(R.string.app_name));
                startActivity(sender);

                return true;
            }
        });

        Preference About = (Preference) findPreference("About");
        About.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                i = new Intent(getActivity(), AboutActivity.class);
                startActivity(i);

                return true;
            }
        });

        Preference FeedBack = (Preference) findPreference("FeedBack");
        FeedBack.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                DisplayMetrics displaymetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;


                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contato@kinemarts.com.br"}); // PUT YOUR EMAIL HERE
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)  + version);
                intent.putExtra(Intent.EXTRA_TEXT,
                        "\n" + " Dispositivo :" + getDeviceName() +
                                "\n" + " Android:" + Build.VERSION.SDK_INT +
                                "\n" + " Display Height  :" + height + "px" +
                                "\n" + " Display Width  :" + width + "px" +
                                "\n\n" + " Por favor informe problema a ser corrigido .." +
                                "\n");

                startActivity(Intent.createChooser(intent, "Enviar Email"));

                return true;
            }
        });


        Preference Update = (Preference) findPreference("Update");
        Update.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }

                return true;
            }
        });

        Preference Version = (Preference) findPreference("Version");
        Version.setSummary(version);
    }
}

