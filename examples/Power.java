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
        Filter f3 = new LifecycleFilter(); // try new LifecycleFilter("removed") instead...
        Filter f4 = new AndFilter(f1,f2,f3);

        OmaReader r = new OmaReader(args[0]);
        r.setFilter(f4);

        while (true)
        {
            Element e = r.next();
            if (e==null) break;

            System.out.println(e);
        }

        r.close();
    }
}
