package de.kumakyoo.omalibjava;

import java.io.*;
import java.util.zip.*;

public class OmaReader
{
    private String filename;
    private Filter filter;

    private MyDataInputStream in;
    private MyDataInputStream save;

    private int features;

    private Bounds globalBounds;
    private ChunkTableEntry[] chunkTable;
    private BlockTableEntry[] blockTable;
    private SliceTableEntry[] sliceTable;

    private boolean chunkFinished;
    private int chunk;
    private boolean blockFinished;
    private int block;
    private boolean sliceFinished;
    private int slice;
    private int elementCount;
    private int element;

    private String key;
    private String value;

    public OmaReader(String filename)
    {
        this.filename = filename;
        filter = new Filter();
        key = value = null;
    }

    public void close() throws IOException
    {
        in.close();
    }

    public void reset() throws IOException
    {
        if (in==null) openFile();

        chunkFinished = true;
        chunk = -1;
    }

    public Filter getFilter()
    {
        return filter;
    }

    public void setFilter(Filter filter)
    {
        this.filter = filter;
    }

    public Element next() throws IOException
    {
        if (in==null) openFile();

        while (true)
        {
            if (chunkFinished)
                if (!readNextChunk())
                    return null;

            if (!filter.needsChunk(chunkTable[chunk].type,chunkTable[chunk].bounds))
            {
                chunkFinished = true;
                continue;
            }

            if (blockFinished)
                if (!readNextBlock())
                {
                    chunkFinished = true;
                    continue;
                }

            if (!filter.needsBlock(blockTable[block].key))
            {
                blockFinished = true;
                continue;
            }

            if (sliceFinished)
                if (!readNextSlice())
                {
                    blockFinished = true;
                    continue;
                }

            if (!filter.needsSlice(sliceTable[slice].value))
            {
                in = save;
                sliceFinished = true;
                continue;
            }

            element++;
            if (element>=elementCount)
            {
                in = save;
                sliceFinished = true;
                continue;
            }

            Element e = readElement();
            if (filter.keep(e))
                return e;
        }
    }

    public long count() throws IOException
    {
        if (in==null) openFile();

        long c = 0;

        for (chunk=0;chunk<chunkTable.length;chunk++)
        {
            if (!filter.needsChunk(chunkTable[chunk].type,chunkTable[chunk].bounds))
                continue;

            readChunk();

            for (block=0;block<blockTable.length;block++)
            {
                key = blockTable[block].key;
                if (!filter.needsBlock(key))
                    continue;

                readBlock();

                for (slice=0;slice<sliceTable.length;slice++)
                {
                    value = sliceTable[slice].value;

                    if (!filter.needsSlice(value))
                        continue;

                    readSlice();

                    if (filter.countable())
                        c += elementCount;
                    else
                        for (element=0;element<elementCount;element++)
                            if (filter.keep(readElement()))
                                c++;

                    in = save;
                }
            }
        }

        return c;
    }

    //////////////////////////////////////////////////////////////////

    private void openFile() throws IOException
    {
        in = new MyDataInputStream(filename);

        enforce(in.readByte()=='O', "oma-file expected");
        enforce(in.readByte()=='M', "oma-file expected");
        enforce(in.readByte()=='A', "oma-file expected");

        features = in.readByte();

        globalBounds = new Bounds(in.readInt(),in.readInt(),in.readInt(),in.readInt());

        long chunkTablePos = in.readLong();
        in.setPosition(chunkTablePos);

        int count = in.readInt();
        chunkTable = new ChunkTableEntry[count];
        for (int i=0;i<count;i++)
            chunkTable[i] = new ChunkTableEntry(in.readLong(),in.readByte(),new Bounds(in));

        reset();
    }

    private boolean readNextChunk() throws IOException
    {
        chunkFinished = false;
        chunk++;
        if (chunk>=chunkTable.length) return false;
        readChunk();
        blockFinished = true;
        block = -1;
        return true;
    }

    private void readChunk() throws IOException
    {
        in.setPosition(chunkTable[chunk].start);
        long blockTablePos = chunkTable[chunk].start+in.readInt();
        in.setPosition(blockTablePos);

        int count = in.readSmallInt();
        blockTable = new BlockTableEntry[count];
        for (int i=0;i<count;i++)
            blockTable[i] = new BlockTableEntry(chunkTable[chunk].start+in.readInt(),in.readString());
    }

    private boolean readNextBlock() throws IOException
    {
        blockFinished = false;
        block++;
        if (block>=blockTable.length) return false;
        key = blockTable[block].key;

        readBlock();

        sliceFinished = true;
        slice = -1;

        return true;
    }

    private void readBlock() throws IOException
    {
        in.setPosition(blockTable[block].start);
        long sliceTablePos = blockTable[block].start+in.readInt();
        in.setPosition(sliceTablePos);

        int count = in.readSmallInt();
        sliceTable = new SliceTableEntry[count];
        for (int i=0;i<count;i++)
            sliceTable[i] = new SliceTableEntry(blockTable[block].start+in.readInt(),in.readString());
    }

    private boolean readNextSlice() throws IOException
    {
        sliceFinished = false;
        slice++;
        if (slice>=sliceTable.length) return false;
        value = sliceTable[slice].value;

        readSlice();
        element = -1;

        return true;
    }

    private void readSlice() throws IOException
    {
        in.resetDelta();
        in.setPosition(sliceTable[slice].start);

        elementCount = in.readInt();
        save = in;
        if ((features&1)!=0)
            in = new MyDataInputStream(new BufferedInputStream(new InflaterInputStream(in)));
    }

    private Element readElement() throws IOException
    {
        Element e = null;

        switch (chunkTable[chunk].type)
        {
        case 'N':
            e = new Node(in,key,value);
            break;
        case 'W':
            e = new Way(in,key,value);
            break;
        case 'A':
            e = new Area(in,key,value);
            break;
        default:
            enforce(false, "unknown element type '"+((char)chunkTable[chunk].type)+"'");
        }

        e.readTags(in);
        e.readMeta(in,features);

        return e;
    }

    private void enforce(boolean b, String msg) throws IOException
    {
        if (!b) throw new IOException(msg);
    }

    class ChunkTableEntry
    {
        long start;
        byte type;
        Bounds bounds;

        public ChunkTableEntry(long start, byte type, Bounds bounds)
        {
            this.start = start;
            this.type = type;
            this.bounds = bounds;
        }
    }

    class BlockTableEntry
    {
        long start;
        String key;

        public BlockTableEntry(long start, String key)
        {
            this.start = start;
            this.key = key;
        }
    }

    class SliceTableEntry
    {
        long start;
        String value;

        public SliceTableEntry(long start, String value)
        {
            this.start = start;
            this.value = value;
        }
    }
}
