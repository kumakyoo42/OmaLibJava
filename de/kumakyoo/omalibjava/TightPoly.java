package de.kumakyoo.omalibjava;

import java.io.*;

public class TightPoly extends Poly
{
    public TightPoly(String filename) throws IOException
    {
        super(filename);
    }

    public TightPoly(String filename, int stripeSize) throws IOException
    {
        super(filename,stripeSize);
    }

    public TightPoly(OmaReader r, Filter f) throws IOException
    {
        super(r,f);
    }

    public TightPoly(OmaReader r, Filter f, int stripeSize) throws IOException
    {
        super(r,f,stripeSize);
    }

    public boolean contains(int[] lon, int[] lat)
    {
        for (int i=0;i<lon.length;i++)
            if (!contains(lon[i],lat[i])) return false;
        return true;
    }
}
