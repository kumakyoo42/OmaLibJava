package de.kumakyoo.omalibjava;

import java.io.*;
import java.util.*;

abstract public class Element
{
    public long id;
    public int version;
    public long timestamp;
    public long changeset;
    public int uid;
    public String user;

    public String key;
    public String value;

    public Map<String, String> tags;

    public Element(String key, String value)
    {
        this.key = key;
        this.value = value;
        this.id = Long.MIN_VALUE;
        this.version = Integer.MIN_VALUE;
        this.timestamp = Long.MIN_VALUE;
        this.changeset = Long.MIN_VALUE;
        this.uid = Integer.MIN_VALUE;
        this.user = null;
        this.tags = null;
    }

    public void readTags(MyDataInputStream in) throws IOException
    {
        tags = new HashMap<>();
        int count = in.readSmallInt();
        for (int i=0;i<count;i++)
            tags.put(in.readString(),in.readString());
    }

    public void readMeta(MyDataInputStream in, int features) throws IOException
    {
        if ((features&2)!=0)
            id = in.readLong();
        if ((features&4)!=0)
            version = in.readSmallInt();
        if ((features&8)!=0)
            timestamp = in.readLong();
        if ((features&16)!=0)
            changeset = in.readLong();
        if ((features&32)!=0)
        {
            uid = in.readInt();
            user = in.readString();
        }
    }

    public String toString()
    {
        StringBuffer b = new StringBuffer();
        b.append(key).append(" (").append(value).append(")\n");
        if (id!=Long.MIN_VALUE)
            b.append("  id=").append(id).append("\n");
        if (version!=Integer.MIN_VALUE)
            b.append("  version=").append(version).append("\n");
        if (timestamp!=Long.MIN_VALUE)
            b.append("  timestamp=").append(timestamp).append("\n");
        if (changeset!=Long.MIN_VALUE)
            b.append("  changeset=").append(changeset).append("\n");
        if (uid!=Integer.MIN_VALUE)
            b.append("  uid=").append(uid).append("\n");
        if (user!=null)
            b.append("  user=").append(user).append("\n");

        if (tags!=null)
            for (String tag:tags.keySet())
                b.append("    ").append(tag).append(" = ").append(tags.get(tag)).append("\n");

        return b.toString();
    }
}
