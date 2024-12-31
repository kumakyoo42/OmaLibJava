# Description of the API of OmaLibJava

The library `OmaLibJava` essentially contains two entry points:
`OmaReader` for using the data in an Oma file and `Extractor` for
creating one or more extracts of an Oma file. An important concept for
both of them are filters, which can be used to describe the data you
are interested in.

Further more, the library provides classes for the elements contained
in an Oma file (nodes, ways, areas), for bounding boxes, polygons and
some helper classes for reading and writing oma files.

To keep the documentation clear, each of these areas is described in a
separate document:

* [OmaReader](/api/OMAREADER.md)
* [Extractor](/api/EXTRACTOR.md)
* [Filters](/api/FILTERS.md)
* [Elements](/api/ELEMENTS.md)
* [Bounding Boxes and Polygons](/api/BBANDPOLYGONS.md)
* [File Access](/api/FILEACCESS.md)

