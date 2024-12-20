package de.kumakyoo.omalibjava;

public class BoundingBoxFilter extends Filter
{
    protected BoundingBox bounds;

    protected BoundingBox cb;

    public BoundingBoxFilter(BoundingBox bounds)
    {
        this.bounds = bounds;
    }

    public BoundingBoxFilter(int minlon, int minlat, int maxlon, int maxlat)
    {
        this.bounds = new BoundingBox(minlon,minlat,maxlon,maxlat);
    }

    public BoundingBoxFilter(double minlon, double minlat, double maxlon, double maxlat)
    {
        this.bounds = new BoundingBox(minlon,minlat,maxlon,maxlat);
    }

    public boolean needsChunk(byte type, BoundingBox b)
    {
        cb = b;
        return bounds.intersects(b);
    }

    public boolean keep(Element e)
    {
        if (e instanceof Node)
            return bounds.contains(((Node)e).lon,((Node)e).lat);
        if (e instanceof Way)
            return bounds.contains(((Way)e).lon,((Way)e).lat);
        if (e instanceof Area)
            return bounds.contains(((Area)e).lon,((Area)e).lat);
        return false;
    }

    public boolean countable()
    {
        return bounds.contains(cb);
    }
}
