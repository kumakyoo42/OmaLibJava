package de.kumakyoo.omalibjava;

import java.io.*;

public class Collection extends Element
{
    public String[] node_role;
    public int[] node_lon;
    public int[] node_lat;

    public String[] way_role;
    public int[][] way_lon;
    public int[][] way_lat;

    public Collection(String[] node_role, int[] node_lon, int[] node_lat,
                      String[] way_role, int[][] way_lon, int[][] way_lat)
    {
        this.node_role = node_role;
        this.node_lon = node_lon;
        this.node_lat = node_lat;
        this.way_role = way_role;
        this.way_lon = way_lon;
        this.way_lat = way_lat;
    }

    public Collection(OmaInputStream in, String key, String value) throws IOException
    {
        super(key,value);

        int count = in.readSmallInt();
        node_role = new String[count];
        node_lon = new int[count];
        node_lat = new int[count];
        for (int i=0;i<count;i++)
        {
            node_role[i] = in.readString();
            node_lon[i] = in.readDeltaX();
            node_lat[i] = in.readDeltaY();
        }

        count = in.readSmallInt();
        way_role = new String[count];
        way_lon = new int[count][];
        way_lat = new int[count][];
        for (int i=0;i<count;i++)
        {
            way_role[i] = in.readString();
            int count2 = in.readSmallInt();
            way_lon[i] = new int[count2];
            way_lat[i] = new int[count2];
            for (int j=0;j<count2;j++)
            {
                way_lon[i][j] = in.readDeltaX();
                way_lat[i][j] = in.readDeltaY();
            }
        }

        count = in.readSmallInt();
    }

    public void writeGeo(OmaOutputStream out) throws IOException
    {
        out.writeSmallInt(node_lon.length);
        for (int i=0;i<node_lon.length;i++)
        {
            out.writeString(node_role[i]);
            out.writeDeltaX(node_lon[i]);
            out.writeDeltaY(node_lat[i]);
        }

        out.writeSmallInt(way_lon.length);
        for (int i=0;i<way_lon.length;i++)
        {
            out.writeString(way_role[i]);
            out.writeSmallInt(way_lon[i].length);
            for (int j=0;j<way_lon[i].length;j++)
            {
                out.writeDeltaX(way_lon[i][j]);
                out.writeDeltaY(way_lat[i][j]);
            }
        }

        out.writeSmallInt(0);
    }

    public String toString()
    {
        StringBuffer b = new StringBuffer();
        b.append("collection\n");
        b.append("  nodes:\n");
        for (int i=0;i<node_lon.length;i++)
        {
            b.append("    ").append(node_role[i]).append("\n");
            b.append("    ").append(node_lon[i]/1e7).append(",").append(node_lat[i]/1e7).append("\n");
        }
        b.append("  ways:\n");
        for (int i=0;i<way_lon.length;i++)
        {
            b.append("    ").append(way_role[i]).append("\n");
            b.append("   ");
            for (int j=0;j<way_lon[i].length;j++)
                b.append(" ").append(way_lon[i][j]/1e7).append(",").append(way_lat[i][j]/1e7);
            b.append("\n");
        }
        b.append(super.toString());
        return b.toString();
    }
}
