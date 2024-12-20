# Description of the API of OmaLibJava

Programs using OmaLibJava essentially resemble more or less the
following example:

    OmaReader r = OmaReader("some_file.oma");
    r.setFilter(new TypeFilter('A'));
    while (true)
    {
        Element e = r.next();
        if (e==null) break;

        // do something with e
    }
    r.close();

First, an `OmaReader` is created, then some `Filter` is set and
finally the resulting `Element`s are processed in a loop calling
`next()` until `null` is returned. Finally, the `OmaReader` is closed.

The API consists of the central class `OmaReader`, several filters,
classes which contain the elements and some helper classes for
bounding boxes and polygons.


## OmaReader

The central object of the API is an OmaReader.

### Constructor

    OmaReader(String filename) throws IOException

Creates a new instance of `OmaReader`, which provides easy access to
an Oma file `filename`.

### Methods

    close() throws IOException

Closes the underlying file.

    reset() throws IOException

Resets the `OmaReader`. After the reset, the `OmaReader` is ready to
read the same file with a different filter applied, thus several
queries can be run, without having to create a new `OmaReader` every
time.

    Filter getFilter()

Provides the filter, which is currently used.

    setFilter(Filter filter)

Sets a new filter to use. This should only be used at the very
beginning of using an OmaReader and directly before or after using
`reset()`. The result of using this command with other timing, is not
defined.

    Element next() throws IOException

Provides the next element which passes the filter or `null` if no such
element exists.

    long count() throws IOException

Counts the number of elements which pass the filter. `count()` and
`next()` should never be mixed.


## Filters

While it is possible to query Oma files without a filter, it is often
better to use some filters. Depending on the filters and the Oma file,
a query with filters can be extremly faster.

Filters provide a series of methods, which help to decide very early
if a certain part of the file needs to be read. Normally, you do not
need to know about these methods, and therefore in the following
description, these methods are omitted.

But in case you want to write your own filter class or you want to
replace the OmaReader by your own class, using these filters, you
might need to know about these details. They can be found at the end
of this section.

### Filter

    Filter()

All elements will pass this filter. It's the default filter.

This filter is slow.

### TypeFilter

    TypeFilter(byte type)
    TypeFilter(char type)
    TypeFilter(byte[] type)
    TypeFilter(String type)

All elements of at least one of the types specified by the
constructors argument pass this filter. Types can be 'N' for nodes,
'W' for ways and 'A' for areas. Other characters are ignored.

Example: Only nodes and areas pass `TypeFilter("NA")`.

This filter is very fast.

### KeyFilter

    KeyFilter(String key)

All elements which contain a tag with key `key` will pass this filter.

Example: `KeyFilter("crossing")` queries all crossings.

*If the key is known to be the key of a block, a `BlockFilter` should
be used instead.*

This filter is slow.

### TagFilter

    TagFilter(String key, String value)

All elements which contain a tag with key `key` and value `value` will
pass this filter.

Example: `TagFilter("crossing","marked")` queries all crossings of type "marked".

*If the key is known to be the key of a block and the value is known to
be the value of a slice in this block, a `BlockSliceFilter` should be
used instead.*

This filter is slow.

### LifecycleFilter

    LifecycleFilter()

All elements without a lifecycle prefix for the key of the block will
pass this filter.

    LifecycleFilter(String lifecycle)

All elements with the specified lifecycle prefix for the key of the
block will pass this filter. (If `lifecycle` is the empty string or
`null` this is identical to the constructor without parameters.)

Example: `LifecycleFilter("demolished")` queries elements which have
been demolished.

This filter is slow.

### BlockFilter

    BlockFilter(String key)

All elements, which are part of a block where the key is the key of
this block, will pass this filter.

Example: `BlockFilter("highway")` queries all highway elements.

If the key is empty or null, elements of blocks without key will pass
this filter.

This filter is very fast.

**Important:** This filter depends on the type file used, when the Oma
file was created. For example, with the default type file, the query
`BlockFilter("crossing")` would result in an empty list of elements,
because `crossing` is not listed in this file as a key of a block. Use
`KeyFilter` in this case.

### BlockSliceFilter

    BlockSliceFilter(String key, String value)

All elements, which are part of a slice in a block, where the key is
the key of the block and the value is the value of the slice, will
pass this filter.

Example: `BlockSliceFilter("highway","footway")` queries all highway
elements which are footways.

If the value is empty or null, elements of slices without value will
pass this filter. If the key is empty or null, elements of blocks
without key will pass this filter.

This filter is very fast.

**Important:** This filter depends on the type file used, when the Oma
file was created. For example, with the default type file, the query
`BlockSliceFilter("highway","primary_link")` would result in an empty
list of elements, because the value `primary_link` is not among the
values for key `highway` in this file. But, because `highway` is the
key of a block, you could use `AndFilter(new
BlockSliceFilter("highway",null),new
TagFilter("highway","primary_link")` instead. If the key isn't the key
of a block, use `TagFilter` instead.

### BoundingBoxFilter

    public BoundingBoxFilter(BoundingBox bounds)
    public BoundingBoxFilter(int minlon, int minlat, int maxlon, int maxlat)
    public BoundingBoxFilter(double minlon, double minlat, double maxlon, double maxlat)

All elements which are inside the given bounding box will pass this
filter.

Example: `BoundingBoxFilter(7.9,49.5,8.0,49.6)` queries all elements
in the "rectangle" with bottom left corner at 7.9°E and 49.5°N and top
right corner at 8.0°E and 49.6°N.

If the bounding box is given by coordinates, a non tight bounding box
is used.

This filter is fast.

### PolygonFilter

    public PolygonFilter(String filename) throws IOException
    public PolygonFilter(Polygon poly)

All elements which are inside the given polygon will pass this filter.

Example: `PolygonFilter("some_file.poly")` will query all elements in
the polygon defined in the specified poly file.

If the polygon is given by a poly file, the non tight version of the
bounding box is used.

This filter is fast.

### AndFilter

    public AndFilter(Filter... fs)

All elements, which pass all filters given in the constructor will pass
this filter.

Example: `AndFilter(new TypeFilter("N"),new KeyFilter("highway"))`
will query all nodes with key highway.

This filter is fast, if at least one filter given in the constructor
is fast. Otherwise it is slow.

### OrFilter

    public OrFilter(Filter... fs)

All elements, which pass at least one filter of the filters given in
the constructor will pass this filter.

Example: `OrFilter(new TypeFilter("N"),new KeyFilter("highway"))`
will query all nodes and all elements with key highway.

This filter is almost always slow. Often it is a better idea, to use
separate queries and combine the results.

### NotFilter

    public NotFilter(Filter f)

All elements, which do not pass the filter given in the constructor,
will pass this filter.

Example: `NotFilter(new keyFilter("highway"))` will query all
elements, which do not have a tag with key highway.

This filter is very slow.


## Elements

### Element

`Element` is an abstract base class for all elements. It contains
several fields that all elements share:

    long id
    int version
    long timestamp
    long changeset
    int uid
    String user

    String key
    String value

    Map<String, String> tags

The first six entries are the meta information of the element, if
present. If not present, the values are Long.MIN_VALUE,
Integer.MIN_VALUE or null, depending on the type of that entry.

The two strings `key` and `value` are the key and value of the block
and slice which contained this element.

The tags of the element are provided as a map.

### Node

Nodes provide the following to fields:

    int lon;
    int lat;

These two coordinates specify the position of the node. Values are
given in WGS84 multiplied by 10,000,000.

### Way

Ways provide the following fields:

    int[] lon;
    int[] lat;

These provide a series of coordinates specifying the way. Values are
given in WGS84 multiplied by 10,000,000.

### Area

Areas provide the following fields:

    int[] lon;
    int[] lat;

    int[][] holes_lon;
    int[][] holes_lat;

The first two items define a closed loop (the first coordinate is not
repeated at the end).

The second two items define holes inside the area defined by this
loop. Every hole is itself a closed loop (again the first coordinate
is not repeated at the end). An area may contain zero holes.

Values are given in WGS84 multiplied by 10,000,000.


## Bounding Boxes and Polygons

Bounding boxes and polygons are currently provided in two flavours: A
non tight version and a tight version. They differ in what is
considered inside. In case of the tight version all coordinates of
ways and areas need to be inside, while in the non tight version it is
sufficient that one of the coordinates is inside.

### BoundingBox and TightBoundingBox

    BoundingBox(int minlon, int minlat, int maxlon, int maxlat)
    BoundingBox(double minlon, double minlat, double maxlon, double maxlat)
    BoundingBox(TightBoundingBox tbb)

    TightBoundingBox(int minlon, int minlat, int maxlon, int maxlat)
    TightBoundingBox(double minlon, double minlat, double maxlon, double maxlat)
    TightBoundingBox(BoundingBox bb)

A bounding box is a "rectangle" defined by the coordinates of the
lower left corner (`minlon`, `minlat`) and the upper right corner
(`maxlon`, `maxlat`). Values are given in WGS84. In case of integers
they are multiplied by 10,000,000.

Please note: The borders of a bounding box are considered to be inside
the bounding box.

Methods provided are:

    boolean contains(int lon, int lat)
    boolean contains(int[] lon, int[] lat)
    boolean contains(BoundingBox b)
    boolean intersects(BoundingBox b)

The first three methods check, if a point, a series of points or a
bounding box is contained in this bounding box. In case of a
`TightBoundingBox` all points of a series of points have to be inside
the bounding box to evaluate to true, while for a `BoundingBox` only
one of them needs to fulfill this property.

The last method checks, if two bounding boxes intersect each other.

### Polygon and TightPolygon

    Polygon(String filename) throws IOException
    Polygon(String filename, int stripeSize) throws IOException
    Polygon(OmaReader r, Filter f) throws IOException
    Polygon(OmaReader r, Filter f, int stripeSize) throws IOException
    Polygon(Polygon tp)

    TightPolygon(String filename) throws IOException
    TightPolygon(String filename, int stripeSize) throws IOException
    TightPolygon(OmaReader r, Filter f) throws IOException
    TightPolygon(OmaReader r, Filter f, int stripeSize) throws IOException
    TightPolygon(Polygon p)

Polygons provide a representation of poly files (which essentially is
a multipolygon). They are designed for deciding fast, whether a point
is inside this polygon. This is done by slicing the polygon into
stripes of a certain width.

Polygons can either be provided by a poly file (which is read in by
the constructor) or an OmaReader an a filter, which are used to query
areas from an Oma file.

If `stripeSize` is provided, it defines the width of the stripes. If
the value is too small, this will result in huge data structures
without much benefit in speed gain. If it is too large, querying the
polygons will be slow. The default is 100,000 (which is 0.01°). This
seems to keep a good balance in most cases.

Methods provided are:

    public BoundingBox getBoundingBox()
    public boolean contains(int lon, int lat)
    public boolean contains(int[] lon, int[] lat)

The first method returns the bounding box of this polygon.

The second and third method check, if a point or a series of points is
contained in this polygon. In case of a `TightPolygon` all points of a
series of points have to be inside the polygon to evaluate to true,
while for a `Polygon` only one of them needs to fulfill this property.

