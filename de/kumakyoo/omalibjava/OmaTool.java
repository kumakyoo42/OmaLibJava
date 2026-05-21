package de.kumakyoo.omalibjava;

import java.io.*;

abstract class OmaTool
{
    static final int VERSION = 1;

    protected String filename;

    protected ChunkTableEntry[] chunkTable;
    protected BlockTableEntry[] blockTable;
    protected SliceTableEntry[] sliceTable;

    protected int features;
    protected boolean zipped;

    protected OmaTool(String filename)
    {
        this.filename = filename;
    }

    protected void enforce(boolean b, String msg) throws IOException
    {
        if (!b) throw new IOException(msg);
    }

    protected class ChunkTableEntry
    {
        public long start;
        public byte type;
        public BoundingBox bounds;

        public ChunkTableEntry(long start, byte type, BoundingBox bounds)
        {
            this.start = start;
            this.type = type;
            this.bounds = bounds;
        }
    }

    protected class BlockTableEntry
    {
        public long start;
        public String key;

        public BlockTableEntry(long start, String key)
        {
            this.start = start;
            this.key = key;
        }
    }

    protected class SliceTableEntry
    {
        public long start;
        public String value;

        public SliceTableEntry(long start, String value)
        {
            this.start = start;
            this.value = value;
        }
    }
}
