# Bounding Boxes and Polygons

Bounding boxes and polygons are currently provided in two flavours: A
non tight version and a tight version. They differ in what is
considered inside. In case of the tight version all coordinates of
ways and areas need to be inside, while in the non tight version it is
sufficient that one of the coordinates is inside. The borders of
bounding boxes and polygones are always considered to be inside.

## BoundingBox and TightBoundingBox

    BoundingBox(int minlon, int minlat, int maxlon, int maxlat)
    BoundingBox(double minlon, double minlat, double maxlon, double maxlat)
    BoundingBox(BoundingBox b)

    TightBoundingBox(int minlon, int minlat, int maxlon, int maxlat)
    TightBoundingBox(double minlon, double minlat, double maxlon, double maxlat)
    TightBoundingBox(BoundingBox b)

A bounding box is a "rectangle" defined by the coordinates of the
lower left corner (`minlon`, `minlat`) and the upper right corner
(`maxlon`, `maxlat`). Values are given in WGS84. In case of integers
they are multiplied by 10,000,000.

Methods provided are:

    boolean contains(int lon, int lat)
    boolean contains(int[] lon, int[] lat)
    boolean contains(BoundingBox b)
    boolean intersects(BoundingBox b)

The first three methods check, if a point, a series of points or a
bounding box is contained in this bounding box. In case of a
`TightBoundingBox` all points of a series of points have to be inside
the bounding box to evaluate to true, while for a (non tight)
`BoundingBox` only one of them needs to fulfill this property.

The last method checks, if two bounding boxes intersect each other.

## Polygon and TightPolygon

    Polygon(String filename) throws IOException
    Polygon(String filename, int stripeSize) throws IOException
    Polygon(OmaReader r, Filter f) throws IOException
    Polygon(OmaReader r, Filter f, int stripeSize) throws IOException
    Polygon(Area[] areas)
    Polygon(Area[] areas, int stripeSize)
    Polygon(Polygon tp)

    TightPolygon(String filename) throws IOException
    TightPolygon(String filename, int stripeSize) throws IOException
    TightPolygon(OmaReader r, Filter f) throws IOException
    TightPolygon(OmaReader r, Filter f, int stripeSize) throws IOException
    TightPolygon(Area[] areas)
    TightPolygon(Area[] areas, int stripeSize)
    TightPolygon(Polygon p)

Polygons provide a representation of poly files (which essentially is
a multipolygon). They are designed for fast decision, whether a point
is inside this polygon. This is done by slicing the polygon into
stripes of a certain width.

Polygons can either be provided by a poly file (which is read in by
the constructor), an `OmaReader` and a filter, which are used to query
areas from an Oma file, or an array of areas.

If `stripeSize` is provided, it defines the width of the stripes. If
the value is too small, this will result in huge data structures
without much benefit in speed gain. If it is too large, querying the
polygons will be slow. The default is 100,000 (which is 0.01°). This
seems to keep a good balance in most cases.

Methods provided are:

    public BoundingBox getBoundingBox()
    public boolean contains(int lon, int lat)
    public boolean contains(int[] lon, int[] lat)
    public boolean contains(BoundingBox b)

The first method returns the bounding box of this polygon.

The other three methods check, if a point, a series of points or a
bounding box is contained in this polygon. In case of a `TightPolygon`
all points of a series of points (and all four croners of a bounding
box) have to be inside the polygon to evaluate to true, while for a
(non tight) `Polygon` only one of them needs to fulfill this property.
