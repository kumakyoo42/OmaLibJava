# File Access

The OmaLibJava provides two helper classes to read and write Oma
files: `OmaInputStream` and `OmaOutputStream`. They are basically
meant for internal use, but might prove useful in own applications as
well.

They provide means for fast random access and some methods for special
data structures used in Oma files.

## OmaInputStream

### Constructors

    OmaInputStream(String filename) throws IOException
    OmaInputStream(InputStream in) throws IOException

Creates a new `OmaInputStream` either by specifying a `filename` or an
`InputStream`.

### Methods

    int readSmallInt() throws IOException

Reads a `smallInt` as defined in the specs of oma file format.

    String readString() throws IOException

Reads a `string` as defined in the specs of oma file format.

    void resetDelta()

Resets delta encoding. This should be done at the beginning of each
slice.

    int readDeltaX() throws IOException

Reads a delta encoded x coordinate (longitude).

    int readDeltaY() throws IOException

Reads a delta encoded y coordinate (latitude).

    long getPosition() throws IOException

Returns the current file position.

**Please note:** This doesn't work correctly, when the
`OmaInputStream` has been created using an other `InputStream`. In
this case, the method always returns 0.

    void setPosition(long pos) throws IOException

Sets the current file position.

**Please note:** This doesn't work correctly, when the
`OmaInputStream` has been created using an other `InputStream`. In
this case, the method does nothing.

## OmaOutputStream

### Constructors

    OmaOutputStream(String filename) throws IOException
    OmaOutputStream(OutputStream s)

Creates a new `OmaOutputStream` either by specifying a `filename` or
an `OutputStream`.

### Methods

    void writeSmallInt(int value) throws IOException

Writes a `smallInt` as defined in the specs of oma file format.

    void writeString(String s) throws IOException

Writes a `string` as defined in the specs of oma file format.

    void resetDelta()

Resets delta encoding. This should be done at the beginning of each
slice.

    void writeDeltaX(int val) throws IOException

Writes a delta encoded x coordinate (longitude).

    void writeDeltaY(int val) throws IOException

Writes a delta encoded y coordinate (latitude).

    long getPosition() throws IOException

Returns the current file position.

**Please note:** This doesn't work correctly, when the
`OmaOutputStream` has been created using an other `OutputStream`. In
this case, the method always returns 0.

    void setPosition(long pos) throws IOException

Sets the current file position.

**Please note:** This doesn't work correctly, when the
`OmaOutputStream` has been created using an other `OutputStream`. In
this case, the method does nothing.
