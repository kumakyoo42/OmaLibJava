import java.io.*;
import de.kumakyoo.omalibjava.*;

// Compile:
//
// javac -cp ../omalibjava.jar Extract.java
//
// Aufruf:
//
// java -classpath .:../omalibjava.jar Extract <oma-file>
//
public class Extract
{
    public static void main(String[] args) throws IOException
    {
        OmaReader r = new OmaReader(args[0]);

        Filter f1 = new TagFilter("name","Ochtrup");
        Filter f2 = new BlockFilter("boundary");
        Filter f3 = new AndFilter(f1,f2);
        Filter city1 = new PolygonFilter(new TightPolygon(r,f3));

        f1 = new TagFilter("name","Deutschneudorf");
        f3 = new AndFilter(f1,f2);
        Filter city2 = new PolygonFilter(new TightPolygon(r,f3));
        Filter trees_of_city2 = new AndFilter(city2,new BlockSliceFilter("natural","peak"));

        r.close();

        Extractor e = new Extractor(args[0]);

        e.addExtract(city1,"ochtrup.oma");
        e.addExtract(trees_of_city2,"peaks_of_deutschneudorf.oma");

        e.run();
    }
}
