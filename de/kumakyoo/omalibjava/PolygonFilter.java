package de.kumakyoo.omalibjava;

import java.io.*;

public class PolygonFilter extends BoundingBoxFilter
{
    private Polygon poly;

    public PolygonFilter(String filename) throws IOException
    {
        super(null);
        poly = new Polygon(filename);
        bounds = poly.getBoundingBox();
    }

    public PolygonFilter(Polygon poly)
    {
        super(null);
        this.poly = poly;
        bounds = poly.getBoundingBox();
    }

    public boolean needsChunk(byte type, BoundingBox b)
    {
        cb = b;
        return bounds.intersects(b);
    }

    public boolean keep(Element e)
    {
        if (!super.keep(e)) return false;
        if (e instanceof Node)
            return poly.contains(((Node)e).lon,((Node)e).lat);
        if (e instanceof Way)
            return poly.contains(((Way)e).lon,((Way)e).lat);
        return false;
    }

    public boolean countable()
    {
        return false;
    }
}
