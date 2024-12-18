package de.kumakyoo.omalibjava;

import java.io.*;

public class Node extends Element
{
    public int lon;
    public int lat;

    public Node(MyDataInputStream in, String key, String value) throws IOException
    {
        super(key,value);
        lon = in.readDeltaX();
        lat = in.readDeltaY();
    }

    public String toString()
    {
        StringBuffer b = new StringBuffer();
        b.append("node @ ").append(lon/1e7).append(",").append(lat/1e7).append("\n");
        b.append(super.toString());
        return b.toString();
    }
}
