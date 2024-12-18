package de.kumakyoo.omalibjava;

import java.io.*;

public class PolyFilter extends BoundingBoxFilter
{
    private Poly poly;

    public PolyFilter(String filename) throws IOException
    {
        super(null);
        poly = new Poly(filename);
        bounds = poly.getBounds();
    }

    public PolyFilter(Poly poly)
    {
        super(null);
        this.poly = poly;
        bounds = poly.getBounds();
    }

    public boolean needsChunk(byte type, Bounds b)
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
