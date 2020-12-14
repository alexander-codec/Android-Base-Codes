package com.example.hatiboy.gpcapture;

/**
 * Created by HATIBOY on 7/22/2015.
 */

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by mac on 5/5/15.
 */


public class Resolution implements Serializable
{
    public static Resolution Resolution_1080p;
    public static Resolution Resolution_360p;
    public static Resolution Resolution_480p;
    public static Resolution Resolution_720p;
    public int Height;
    public int Width;

    static {
        Resolution.Resolution_1080p = new Resolution(1920, 1080);
        Resolution.Resolution_720p = new Resolution(1280, 720);
        Resolution.Resolution_480p = new Resolution(720, 480);
        Resolution.Resolution_360p = new Resolution(640, 360);
    }

    public Resolution(final int width, final int height) {
        super();
        this.Width = width;
        this.Height = height;
    }

    public Resolution copy() {
        return new Resolution(this.Width, this.Height);
    }

    public boolean equals(final Object o) {
        final boolean b = o instanceof Resolution;
        boolean b2 = false;
        if (b) {
            b2 = false;
            if (o != null) {
                final Resolution resolution = (Resolution)o;
                if (this.Width != resolution.Width || this.Height != resolution.Height) {
                    final int width = this.Width;
                    final int height = resolution.Height;
                    b2 = false;
                    if (width != height) {
                        return b2;
                    }
                    final int height2 = this.Height;
                    final int width2 = resolution.Width;
                    b2 = false;
                    if (height2 != width2) {
                        return b2;
                    }
                }
                b2 = true;
            }
        }
        return b2;
    }

    public int getMaxDimension() {
        return Math.max(this.Width, this.Height);
    }

    public String getName() {
        return String.format(Locale.US, "%dx%d", this.Width, this.Height);
    }

    public boolean isPortrait() {
        return this.Height > this.Width;
    }

    public void rotate() {
        final int width = this.Width;
        this.Width = this.Height;
        this.Height = width;
    }
}

