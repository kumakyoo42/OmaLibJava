# OmaReader

## Basic Use

Programs using `OmaReader` essentially resemble the following code:

    OmaReader r = OmaReader(<some file>);
    r.setFilter(<some filter>);
    while (true)
    {
        Element e = r.next();
        if (e==null) break;

        <do something with e>
    }
    r.close();

First, an `OmaReader` is created, then a `Filter` is applied and
finally the resulting `Element`s are processed in a loop calling
`next()` until `null` is returned. Finally, the `OmaReader` is closed
(or reused after a call to `reset()`).


## Constructors

Currently, there is only one constructor:

    OmaReader(String filename) throws IOException

Creates a new instance of `OmaReader`, which provides easy access to
an Oma file `filename`. The constructor immediately opens the file,
checks it and may throw an `IOException` if something goes wrong or if
the file appears not to be in
[oma file format](https://github.com/kumakyoo42/oma-file-formats).

## Methods

### Basics

    reset()

Resets the `OmaReader`. After the reset, the `OmaReader` is ready to
read the same file with the same filter again. `reset()` is
automatically called, whenever a new filter is set.

    close() throws IOException

Closes the OmaReader.

### Filters

    setFilter(Filter filter)

Sets a new filter to use. This method automatically resets the
`OmaReader`.

    Filter getFilter()

Provides the filter, which is currently used.

### Retrieving Elements

There are two methods for retrieving elements: `next()` retrieves the
elements one after another, while `count()` just counts all of them.
Depending on the filter used, `count()` can be much faster, than using
`next()` for counting the elements.

**Please note:** These two methods should never be mixed without a
`reset()` in between.

    Element next() throws IOException

Provides the next element which passes the filter or `null` if no such
element exists.

    long count() throws IOException

Counts the number of elements which pass the filter.

### Querying Meta Data

It's possible to query the feature byte of the file, as well as the
type table.

    public boolean isZipped()
    public boolean containsID()
    public boolean containsVersion()
    public boolean containsTimestamp()
    public boolean containsChangeset()
    public boolean containsUser()
    public boolean elementsOnce()

These methods return true, if the corresponding bit of the features
byte is set to 1.

    boolean containsBlocks(byte type, String key)
    boolean containsSlices(byte type, String key, String value)
    Set<String> keySet(byte type)
    Set<String> valueSet(byte type, String key)

Each Oma file contains a table with the keys and values used to build
blocks and slices, called type table. These four methods query this
table. The first two just tell, if a key or a key-value has been used
for a certain chunk type. The last two provide access to a set of all
keys used and a set of all values of a given key used.


