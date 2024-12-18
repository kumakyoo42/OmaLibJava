package de.kumakyoo.omalibjava;

import java.io.*;

public class Bounds
{
    public int minlon;
    public int minlat;
    public int maxlon;
    public int maxlat;

    public Bounds(int minlon, int minlat, int maxlon, int maxlat)
    {
        this.minlon = minlon;
        this.minlat = minlat;
        this.maxlon = maxlon;
        this.maxlat = maxlat;
    }

    public Bounds(double minlon, double minlat, double maxlon, double maxlat)
    {
        this.minlon = (int)Math.round(minlon*1e7+0.5);
        this.minlat = (int)Math.round(minlat*1e7+0.5);
        this.maxlon = (int)Math.round(maxlon*1e7+0.5);
        this.maxlat = (int)Math.round(maxlat*1e7+0.5);
    }

    public Bounds(DataInputStream in) throws IOException
    {
        this(in.readInt(),in.readInt(),in.readInt(),in.readInt());
    }

    public boolean contains(int lon, int lat)
    {
        return lon>=minlon && lon<=maxlon && lat>=minlat && lat<=maxlat;
    }

    public boolean contains(int[] lon, int[] lat)
    {
        for (int i=0;i<lon.length;i++)
            if (contains(lon[i],lat[i])) return true;
        return false;
    }

    public boolean contains(Bounds b)
    {
        return contains(b.minlon,b.minlat) && contains(b.maxlon,b.maxlat);
    }

    public boolean intersects(Bounds b)
    {
        if (b.minlon==Integer.MAX_VALUE || minlon==Integer.MAX_VALUE) return true;
        return b.maxlon>=minlon && b.minlon<=maxlon && b.maxlat>=minlat && b.minlat<=maxlat;
    }

    public String toString()
    {
        return (minlon/1e7)+","+(minlat/1e7)+","+(maxlon/1e7)+","+(maxlat/1e7);
    }
}
