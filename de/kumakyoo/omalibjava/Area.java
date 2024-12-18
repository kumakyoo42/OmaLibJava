package de.kumakyoo.omalibjava;

import java.io.*;

public class Area extends Way
{
    public int[][] holes_lon;
    public int[][] holes_lat;

    public Area(MyDataInputStream in, String key, String value) throws IOException
    {
        super(in,key,value);

        int count = in.readSmallInt();
        holes_lon = new int[count][];
        holes_lat = new int[count][];
        for (int i=0;i<count;i++)
        {
            int holeCount = in.readSmallInt();
            holes_lon[i] = new int[holeCount];
            holes_lat[i] = new int[holeCount];
            for (int j=0;j<holeCount;j++)
            {
                holes_lon[i][j] = in.readDeltaX();
                holes_lat[i][j] = in.readDeltaY();
            }
        }
    }

    public String toString()
    {
        StringBuffer b = new StringBuffer();
        b.append("area @");
        for (int i=0;i<lon.length;i++)
            b.append(" ").append(lon[i]/1e7).append(",").append(lat[i]/1e7);
        b.append("\n");
        if (holes_lon.length>0)
        {
            b.append("  holes:\n");
            for (int j=0;j<holes_lon.length;j++)
            {
                for (int i=0;i<holes_lon[j].length;i++)
                    b.append(" ").append(holes_lon[j][i]/1e7).append(",").append(holes_lat[j][i]/1e7);
                b.append("\n");
            }
        }
        b.append(super.toPartialString());
        return b.toString();
    }
}
