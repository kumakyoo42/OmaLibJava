package de.kumakyoo.omalibjava;

import java.io.*;

abstract class OmaInputTool extends OmaTool
{
    protected OmaInputStream in;

    protected OmaInputTool(String filename)
    {
        super(filename);
    }

    protected Element readElement(int chunk, String key, String value) throws IOException
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
        case 'C':
            e = new Collection(in,key,value);
            break;
        default:
            enforce(false, "unknown element type '"+((char)chunkTable[chunk].type)+"'");
        }

        e.readTags(in);
        e.readMembers(in);
        e.readMeta(in,features|(chunkTable[chunk].type=='C'?1:0));

        return e;
    }
}
