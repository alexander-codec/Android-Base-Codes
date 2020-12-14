package com.example.hatiboy.gpcapture;

/**
 * Created by HATIBOY on 7/22/2015.
 */
import android.widget.*;
import android.content.*;
import android.app.*;
import android.content.res.*;
import java.util.*;

public class OrientationAdapter extends ArrayAdapter<String>
{
    private Map<String, String> mapOrientaion;

    public OrientationAdapter(final Context context, final int n, final Map<String, String> mapOrientaion) {
        super(context, n, (List)new ArrayList(mapOrientaion.keySet()));
        this.setDropDownViewResource(R.layout.resolution_spinner_dropdown_item);
        this.mapOrientaion = mapOrientaion;
    }

    public static OrientationAdapter createAdapter(final Activity activity, final Configuration configuration) {
        final String string = "Portrait";
        final String string2 = "Landscape";
        final LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        final Object[] array = { null };
        String s;
        if (configuration.orientation == 1) {
            s = string;
        }
        else {
            s = string2;
        }
        array[0] = s;
        linkedHashMap.put(String.format("%s (current)", array), "ORIENTATION_CURRENT");
        linkedHashMap.put(string, "ORIENTATION_PORTRAIT");
        linkedHashMap.put(string2, "ORIENTATION_LANDSCAPE");
        return new OrientationAdapter((Context)activity, R.layout.resolution_spinner_item, (Map<String, String>)linkedHashMap);
    }

    public String getOrientation(final int n) {
        Iterator<Map.Entry<String, String>> iterator = this.mapOrientaion.entrySet().iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            final Map.Entry entry = (Map.Entry)((Map.Entry)iterator.next());
            if (n2 == n) {
                return (String)(entry).getValue();
            }
            ++n2;
        }
        return "ORIENTATION_CURRENT";
    }

    public int getOrientationPosition(final String s) {
        final Iterator<Map.Entry<String, String>> iterator = this.mapOrientaion.entrySet().iterator();
        int n = 0;
        while (iterator.hasNext()) {
            if (Objects.equals(s, iterator.next().getValue())) {
                return n;
            }
            ++n;
        }
        return 0;
    }
}

