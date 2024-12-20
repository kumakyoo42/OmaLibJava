package de.kumakyoo.omalibjava;

import java.io.*;

public class TightBoundingBox extends BoundingBox
{
    public TightBoundingBox(int minlon, int minlat, int maxlon, int maxlat)
    {
        super(minlon,minlat,maxlon,maxlat);
    }

    public boolean contains(int[] lon, int[] lat)
    {
        for (int i=0;i<lon.length;i++)
            if (!contains(lon[i],lat[i])) return false;
        return true;
    }
}