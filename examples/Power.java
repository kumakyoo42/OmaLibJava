import java.io.*;
import de.kumakyoo.omalibjava.*;

// Compile:
//
// javac -cp ../omalibjava.jar Power.java
//
// Aufruf:
//
// java -classpath .:../omalibjava.jar Power <oma-file>
//
public class Power
{
    public static void main(String[] args) throws IOException
    {
        Filter f1 = new BlockFilter("power");
        Filter f2 = new BoundingBoxFilter(8.194,49.563,8.195,49.564);
        Filter f3 = new AndFilter(f1,f2);

        OmaReader r = new OmaReader(args[0]);
        r.setFilter(f3);

        while (true)
        {
            Element e = r.next();
            if (e==null) break;

            System.out.println(e);
        }

        r.close();
    }
}
