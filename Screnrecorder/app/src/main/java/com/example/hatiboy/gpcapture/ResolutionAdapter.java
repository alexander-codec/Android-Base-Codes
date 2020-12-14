package com.example.hatiboy.gpcapture;

/**
 * Created by HATIBOY on 7/22/2015.
 */

/**
 * Created by mac on 5/5/15.
 */


import android.print.PrintAttributes;
import android.widget.*;
import android.content.*;
import android.app.*;
import java.util.*;

public class ResolutionAdapter extends ArrayAdapter<String>
{
    private Map<String, Resolution> mapResolution;

    public ResolutionAdapter(final Context context, final int n, final Map<String, Resolution> mapResolution) {
        super(context, n, (List)new ArrayList(mapResolution.keySet()));
        this.setDropDownViewResource(R.layout.resolution_spinner_dropdown_item);
        this.mapResolution = mapResolution;
    }

    public static ResolutionAdapter createAdapter(final Activity activity, final int n) {
        final LinkedHashMap<String, Resolution> linkedHashMap = new LinkedHashMap<String, Resolution>();
        for (final Resolution resolution : ResolutionHelper.getRecordingResolutionsForScreenSize(activity, n)) {
            linkedHashMap.put(resolution.getName(), resolution);
        }
        return new ResolutionAdapter((Context)activity, R.layout.resolution_spinner_item, (Map<String, Resolution>)linkedHashMap);
    }

    public int getPosition(final Resolution resolution) {
        final Iterator<Map.Entry<String, Resolution>> iterator = this.mapResolution.entrySet().iterator();
        int n = 0;
        while (iterator.hasNext()) {
            if (Objects.equals(resolution, iterator.next().getValue())) {
                return n;
            }
            ++n;
        }
        return 0;
    }

    public PrintAttributes.Resolution getResolution(final int n) {
        final Iterator<Map.Entry<String, Resolution>> iterator = this.mapResolution.entrySet().iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            final Map.Entry entry = (Map.Entry)((Map.Entry)iterator.next());
            if (n2 == n) {
                return (PrintAttributes.Resolution)(entry).getValue();
            }
            ++n2;
        }
        return null;
    }
}
