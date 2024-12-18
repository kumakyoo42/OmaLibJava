import java.io.*;
import de.kumakyoo.omalibjava.*;

// Compile:
//
// javac -cp ../omalibjava.jar Cheese.java
//
// Aufruf:
//
// java -classpath .:../omalibjava.jar Cheese <oma-file>
//
public class Cheese
{
    public static void main(String[] args) throws IOException
    {
        OmaReader r = new OmaReader(args[0]);
        r.setFilter(new KeyFilter("cheese"));

        while (true)
        {
            Element e = r.next();
            if (e==null) break;

            System.err.println(e);
        }
        
        r.close();
    }
}
