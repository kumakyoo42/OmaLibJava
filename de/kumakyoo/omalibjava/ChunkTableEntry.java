package de.kumakyoo.omalibjava;

class ChunkTableEntry
{
    long start;
    byte type;
    BoundingBox bounds;

    public ChunkTableEntry(long start, byte type, BoundingBox bounds)
    {
        this.start = start;
        this.type = type;
        this.bounds = bounds;
    }
}

