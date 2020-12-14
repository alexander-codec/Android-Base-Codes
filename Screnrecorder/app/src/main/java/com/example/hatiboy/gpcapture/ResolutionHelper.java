package com.example.hatiboy.gpcapture;

/**
 * Created by HATIBOY on 7/22/2015.
 */

import java.util.*;
import android.app.*;
import android.graphics.*;


public class ResolutionHelper
{
    private static List<Resolution> listResolution;

    static {
        ResolutionHelper.listResolution = new ArrayList<Resolution>(Arrays.asList(Resolution.Resolution_1080p, Resolution.Resolution_720p, Resolution.Resolution_480p, Resolution.Resolution_360p));
    }

    public static Resolution getRecordingResolutionForScreenSize(final Resolution resolution)
    {
        for (final Resolution resolution2 : ResolutionHelper.listResolution)
        {
            if (resolution.getMaxDimension() >= resolution2.getMaxDimension())
            {
                Resolution resolution3 = resolution2.copy();
                if (resolution3 == null) {
                    resolution3 = Resolution.Resolution_360p;
                }
                if (resolution.isPortrait()) {
                    resolution3.rotate();
                }
                //Logg.d("getRecordingResolutionForScreenSize( %d, %d ) returning ( %d, %d )", resolution.Width, resolution.Height, resolution3.Width, resolution3.Height);
                return resolution3;
            }
        }
        return null;

    }

    public static List<Resolution> getRecordingResolutionsForScreenSize(final Activity activity, final int n) {
        return getResolutionsLessThanOrEqual(getScreenResolution(activity), n);
    }

    public static List<Resolution> getResolutionsLessThanOrEqual(final Resolution resolution, final int n) {
        ArrayList<Resolution> list = new ArrayList<Resolution>();
        for (final Resolution resolution2 : ResolutionHelper.listResolution) {
            if (resolution2.getMaxDimension() <= resolution.getMaxDimension()) {
                final Resolution copy = resolution2.copy();
                if (n == 1) {
                    copy.rotate();
                }
                list.add(copy);
            }
        }
        return list;
    }

    public static Resolution getScreenResolution(final Activity activity) {
        final Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getRealSize(point);
        return new Resolution(point.x, point.y);
    }
}
