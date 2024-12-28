import java.io.*;
import de.kumakyoo.omalibjava.*;

// Compile:
//
// javac -cp ../omalibjava.jar Types.java
//
// Aufruf:
//
// java -classpath .:../omalibjava.jar Types <oma-file>
//
public class Types
{
    public static void main(String[] args) throws IOException
    {
        OmaReader r = new OmaReader(args[0]);

        System.out.println("File contains blocks of type highway: "+(r.containsBlocks((byte)'W',"highway")?"yes":"no"));
        System.out.println("File contains slices of type highway=micepath: "+(r.containsSlices((byte)'W',"highway","micepath")?"yes":"no"));

        System.out.println("Keys of area blocks:");
        for (String key:r.keySet((byte)'A'))
            System.out.println("  "+key);

        System.out.println("Values of node slices of block with key 'natural':");
        for (String value:r.valueSet((byte)'N',"natural"))
            System.out.println("  "+value);

        r.close();
    }
}
