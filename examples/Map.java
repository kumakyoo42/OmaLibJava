import java.io.*;
import java.util.*;
import de.kumakyoo.omalibjava.*;

// Compile:
//
// javac -cp ../omalibjava.jar Map.java
//
// Aufruf:
//
// java -classpath .:../omalibjava.jar Map <oma-file>
//
public class Map
{
    static double left;
    static double bottom;
    static double factor;

    public static void main(String[] args) throws IOException
    {
        OmaReader r = new OmaReader(args[0]);

        Filter f1 = new TagFilter("name","Todtnau");
        Filter f2 = new BlockFilter("boundary");
        Filter f3 = new TypeFilter('A');

        r.setFilter(new AndFilter(f1,f2,f3));

        List<Area> bounding_areas = new ArrayList<>();
        while (true)
        {
            Area a = (Area)r.next();
            if (a==null) break;
            bounding_areas.add(a);
        }

        Polygon bounds = new Polygon(bounding_areas.toArray(new Area[bounding_areas.size()]));
        BoundingBox bb = bounds.getBoundingBox();

        left = lon2x(bb.minlon);
        bottom = lat2y(bb.minlat);
        factor = Math.min(612/(lon2x(bb.maxlon)-left),828/(lat2y(bb.maxlat)-bottom));

        StringBuffer boundings = new StringBuffer();
        for (Area a:bounding_areas)
            addArea(boundings,a);

        Filter f4 = new BoundingBoxFilter(bb);

        StringBuffer background = new StringBuffer();
        StringBuffer foreground = new StringBuffer();

        r.setFilter(new AndFilter(f3,f4,new BlockSliceFilter("landuse","forest")));
        foreground.append("0.4 0.8 0.4 c n\n");
        background.append("0.8 g n\n");
        while (true)
        {
            Area a = (Area)r.next();
            if (a==null) break;

            addArea(background,a);
            background.append("f\n");
            if (bounds.contains(a.lon,a.lat))
            {
                addArea(foreground,a);
                foreground.append("f\n");
            }
        }

        r.setFilter(new AndFilter(new TypeFilter("W"),f4,new BlockFilter("highway")));
        foreground.append("0.5 g n\n");
        background.append("0.75 g n\n");
        while (true)
        {
            Way w = (Way)r.next();
            if (w==null) break;

            addWay(background,w);
            background.append("s\n");
            if (bounds.contains(w.lon,w.lat))
            {
                addWay(foreground,w);
                foreground.append("s\n");
            }
        }

        r.setFilter(new AndFilter(new TypeFilter("N"),f4,new BlockSliceFilter("natural","peak")));
        foreground.append("0.5 0.2 0 c n\n");
        background.append("0.5 g n\n");
        while (true)
        {
            Node n = (Node)r.next();
            if (n==null) break;

            addPeak(background,n);
            if (bounds.contains(n.lon,n.lat))
                addPeak(foreground,n);
        }

        System.out.println("%!PS-Adobe-3.0 EPSF-3.0");
        System.out.println("%%Pages: 1");
        System.out.println("%%BoundingBox: 0 0 612 828");
        System.out.println("%%DocumentData: Clean7Bit");
        System.out.println("%%EndComments");
        System.out.println("%%Page: 1 1");
        System.out.println("/pgsave save def 200 dict begin");
        System.out.println("/m { moveto } bind def");
        System.out.println("/l { lineto } bind def");
        System.out.println("/g { setgray } bind def");
        System.out.println("/c { setrgbcolor } bind def");
        System.out.println("/f { eofill } bind def");
        System.out.println("/s { stroke } bind def");
        System.out.println("/n { newpath } bind def");
        System.out.println("/p { closepath } bind def");
        System.out.println("/a { gsave } bind def");
        System.out.println("/b { grestore } bind def");
        System.out.println("/t { translate } bind def");

        System.out.println(background);

        System.out.println("n\n");
        System.out.println(boundings);
        System.out.println("clip\n");

        System.out.println(foreground);
        System.out.println("0 g\n");
        System.out.println(boundings);
        System.out.println("s\n");

        System.out.println("end pgsave restore showpage");
        System.out.println("%%EOF");
    }

    public static void addPeak(StringBuffer b, Node n)
    {
        b.append("a ").append(transform(n.lon,n.lat)).append(" t -4 -2 m 4 -2 l 0 3 l p f b\n");
    }

    public static void addWay(StringBuffer b, Way w)
    {
        for (int i=0;i<w.lon.length;i++)
            b.append(transform(w.lon[i],w.lat[i])).append(i==0?"m\n":"l\n");
    }

    public static void addArea(StringBuffer b, Area a)
    {
        for (int i=0;i<a.lon.length;i++)
            b.append(transform(a.lon[i],a.lat[i])).append(i==0?"m\n":"l\n");
        b.append("p\n");

        for (int j=0;j<a.holes_lon.length;j++)
        {
            for (int i=0;i<a.holes_lon[j].length;i++)
                b.append(transform(a.holes_lon[j][i],a.holes_lat[j][i])).append(i==0?"m\n":"l\n");
            b.append("p\n");
        }
    }

    public static String transform(int lon, int lat)
    {
        double x = (lon2x(lon)-left)*factor;
        double y = (lat2y(lat)-bottom)*factor;

        return (Math.round(x*1000)/1000.0)+" "+(Math.round(y*1000)/1000.0)+" ";
    }

    //////////////////////////////////////////////////////////////////

    public static final double RADIUS = 6378137.0;

    public static double lon2x(double lon)
    {
        return Math.toRadians(lon/1e7)*RADIUS;
    }

    public static double lat2y(double lat)
    {
        return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(lat/1e7) / 2)) * RADIUS;
    }
}
