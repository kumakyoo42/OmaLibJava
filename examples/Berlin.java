import java.io.*;
import de.kumakyoo.omalibjava.*;

// Compile:
//
// javac -cp ../omalibjava.jar Berlin.java
//
// Aufruf:
//
// java -classpath .:../omalibjava.jar Berlin <oma-file>
//
public class Berlin
{
    public static void main(String[] args) throws IOException
    {
        OmaReader r = new OmaReader(args[0]);

        Filter f1 = new TagFilter("name","Berlin");
        Filter f2 = new BlockFilter("boundary");
        Filter f3 = new AndFilter(f1,f2);
        Filter berlin = new PolygonFilter(new TightPolygon(r,f3));

        r.setFilter(new AndFilter(berlin,new TypeFilter("N")));
        System.out.println("nodes in berlin: "+r.count());
        r.reset();
        r.setFilter(new AndFilter(berlin,new TypeFilter("W")));
        System.out.println("ways in berlin: "+r.count());
        r.reset();
        r.setFilter(new AndFilter(berlin,new TypeFilter("A")));
        System.out.println("areas in berlin: "+r.count());
        System.out.println();

        r.reset();
        r.setFilter(new AndFilter(berlin,new TypeFilter("N"),new BlockSliceFilter("natural","tree")));
        System.out.println("tree nodes in berlin: "+r.count());
        r.reset();
        r.setFilter(new AndFilter(berlin,new TypeFilter("W"),new BlockFilter("highway"),new TagFilter("lit","yes")));
        System.out.println("lit highways in berlin: "+r.count());
        r.reset();
        r.setFilter(new AndFilter(berlin,new TypeFilter("A"),new BlockFilter("area:highway")));
        System.out.println("area:highways in berlin: "+r.count());
        r.close();
    }
}
