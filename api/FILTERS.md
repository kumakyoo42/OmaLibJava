# Filters

While it is possible to query Oma files without a filter, it is often
better to use some filters. Depending on the filters and the Oma file,
a query with filters can be extremly faster.

Filters provide a series of methods, which help to decide very early
if a certain part of the file needs to be read. Normally, you do not
need to know about these methods, and therefore in the following
description, these methods are omitted.

But in case you want to write your own filter class or you want to
replace the `OmaReader` by your own class, using these filters, you
might need to know about these details. They can be found at the end
of this section.

## Filter

    Filter()

All elements will pass this filter. It's the default filter.

This filter is slow.

## TypeFilter

    TypeFilter(byte type)
    TypeFilter(char type)
    TypeFilter(byte[] type)
    TypeFilter(String type)

All elements of at least one of the types specified by the `type`
argument pass this filter. Types can be 'N' for nodes, 'W' for ways,
'A' for areas and 'C' for collections. Other characters are ignored.

Example: Only nodes and areas pass `TypeFilter("NA")`.

This filter is very fast.

## KeyFilter

    KeyFilter(String key)

All elements which contain a tag with key `key` will pass this filter.

Example: `KeyFilter("crossing")` queries all crossings.

*If the key is known to be the key of a block, a `BlockFilter` should
be used instead.*

This filter is slow.

## TagFilter

    TagFilter(String key, String value)

All elements which contain a tag with key `key` and value `value` will
pass this filter.

Example: `TagFilter("crossing","marked")` queries all crossings of type "marked".

*If the key is known to be the key of a block and the value is known to
be the value of a slice in this block, a `BlockSliceFilter` should be
used instead.*

This filter is slow.

## LifecycleFilter

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

## BlockFilter

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

## BlockSliceFilter

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

## BoundingBoxFilter

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

## PolygonFilter

    public PolygonFilter(String filename) throws IOException
    public PolygonFilter(Polygon poly)

All elements which are inside the given polygon will pass this filter.
The polygon can be given either as a poly file or as a Polygon (which
might be queried from the Oma file).

Example: `PolygonFilter("some_file.poly")` will query all elements in
the polygon defined in the specified poly file.

If the polygon is given by a poly file, the non tight version of a
polygon is used.

This filter is fast.

## AndFilter

    public AndFilter(Filter... fs)

All elements, which pass all filters given in the constructor will pass
this filter.

Example: `AndFilter(new TypeFilter("N"),new KeyFilter("highway"))`
will query all nodes with key highway.

This filter is fast, if at least one filter given in the constructor
is fast. Otherwise it is slow.

## OrFilter

    public OrFilter(Filter... fs)

All elements, which pass at least one filter of the filters given in
the constructor will pass this filter.

Example: `OrFilter(new TypeFilter("N"),new KeyFilter("highway"))`
will query all nodes and all elements with key highway.

This filter is almost always slow. Often it is a better idea, to use
separate queries and combine the results.

## NotFilter

    public NotFilter(Filter f)

All elements, which do not pass the filter given in the constructor,
will pass this filter.

Example: `NotFilter(new keyFilter("highway"))` will query all
elements, which do not have a tag with key highway.

This filter is very slow.

## IDFilter

    public IDFilter(long id)

All elements with the given id pass the filter.

Example: `IDFilter(123)` will query all elements with id 123.

This filter is slow. It will only work, if the ID has been added to
the Oma file.

## VersionFilter

    public VersionFilter(int version)

All elements with the given version pass the filter.

Example: `VersionFilter(1)` will query all elements with version 1.

This filter is slow. It will only work, if the version has been added to
the Oma file.

## TimestampFilter

    public TimestampFilter(long start)
    public TimestampFilter(long start, long end)

All elements with a certain timestamp pass this filter. In the first
version, the timestamp must fit exactly. In the second version the
timestamp must be in the period from start to end, including both ends.

Example: `TimestampFilter(1704067200,1735689599)` will query all
elements with last change in year 2024.

This filter is slow. It will only work, if the timestamp information
has been added to the Oma file.

## ChangesetFilter

    public ChangesetFilter(long start)
    public ChangesetFilter(long start, long end)

All elements with a certain changeset number pass this filter. In the
first version, the changeset must fit exactly. In the second version
the changeset must be in the range from start to end, including both
ends.

Example: `ChangesetFilter(149000000)` will query all elements
which where last changed in the changeset 149000000.

This filter is slow. It will only work, if the changeset information
has been added to the Oma file.

## UserFilter

    public UserFilter(int uid)
    public UserFilter(String user)

All elements which where last changed by a certain user will pass this
filter. In the first version the user is provided via his uid. In the
second version the user ist provided via his user name.

This filter is slow. It will only work, if the information about the
user has been added to the Oma file.

## Writing own filters

When writing your own filters, they must extend one of the existing
filters, for example the base filter `Filter`.

There are five methods provided by a filter. The base filter `Filter`
just returns `true` for every one of them.

    public boolean needsChunk(byte type, BoundingBox b)

Returns `true` if chunks of type `type` and bounding box `b` may
contain elements which should pass the filter.

    public boolean needsBlock(String key)

Returns `true` if blocks with key `key` may contain elements which
should pass the filter. It is guaranteed that `needsChunk` has been
called for the surrounding chunk before this method is invoked.

    public boolean needsSlice(String value)

Returns `true` if slices with value `value` may contain elements which
should pass the filter. It is guaranteed that `needsChunk` and
`needsBlock` have been called for the surrounding chunk and block
before this method is invoked.

    public boolean keep(Element e)

Returns `true` if the element `e` should pass the filter. It is
guaranteed that `needsChunk`, `needsBlock` and `needsSlice` have been
called for the surrounding chunk, block and slice before this method
is invoked.

    public boolean countable()

Returns `true` if all elements of a slice pass the filter. It is
guaranteed that `needsChunk`, `needsBlock` and `needsSlice` have been
called for the surrounding chunk, block and slice before this method
is invoked.
