package de.kumakyoo.omalibjava;

import java.io.*;

public class Way extends Element
{
    public int[] lon;
    public int[] lat;

    public Way(MyDataInputStream in, String key, String value) throws IOException
    {
        super(key,value);
        int count = in.readSmallInt();
        lon = new int[count];
        lat = new int[count];
        for (int i=0;i<count;i++)
        {
            lon[i] = in.readDeltaX();
            lat[i] = in.readDeltaY();
        }
    }

    public String toString()
    {
        StringBuffer b = new StringBuffer();
        b.append("way @");
        for (int i=0;i<lon.length;i++)
            b.append(" ").append(lon[i]/1e7).append(",").append(lat[i]/1e7);
        b.append("\n");
        b.append(toPartialString());
        return b.toString();
    }

    public String toPartialString()
    {
        return super.toString();
    }
}
