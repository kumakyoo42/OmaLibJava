import java.io.*;
import de.kumakyoo.omalibjava.*;

// Compile:
//
// javac -cp ../omalibjava.jar CountElements.java
//
// Aufruf:
//
// java -classpath .:../omalibjava.jar CountElements <oma-file>
//
public class CountElements
{
    public static void main(String[] args) throws IOException
    {
        OmaReader r = new OmaReader(args[0]);
        System.out.println(args[0]+" contains "+r.count()+" elements.");
        r.close();
    }
}
