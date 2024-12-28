# OmaLibJava - a library to query OMA files

***Note: [Oma](https://github.com/kumakyoo42/Oma) software (including
additional programs like [Opa](https://github.com/kumakyoo42/Opa) and
libraries) and [related file
formats](https://github.com/kumakyoo42/oma-file-formats) are currently
experimental and subject to change without notice.***

## Install

Download [omalibjava.jar](/omalibjava.jar).

## Usage

When compiling your application, you need to add `omalibjava.jar` to the
classpath, for example with: `javac -cp omalibjava.jar MyApp.java`

Wenn running your application, you need to add `omalibjava.jar` to the
classpath, for example with: `java -classpath .:omalibjava.jar MyApp`

See the [description of api](/API.md) for details on how to use the
library.

## Build

On Linux systems you can use the shell script `build.sh` to build
`omalibjava.jar` on your own.

Building on other platforms is neither tested nor directly supported
yet. Basically you need to compile the java files in folder
`de/kumakyoo/omajavalib` and build a jar file from the resulting class
files.

## Examples

There are some examples in the folder [examples](/examples):

* [CountElements.java](/examples/CountElements.java) just counts the
elements of an Oma file. This may serve as something similar to
HelloWorld programs in programming languages: You can use this program
to check if you set up everything correctly.

* [Cheese.java](/examples/Cheese.java) prints all elements of an Oma
file which feature the tag "cheese". The cheese tag is a very rare tag
and thus this query is an example of a very slow query, because the
whole Oma file needs to be searched for elements.

* [Power.java](examples/Power.java) prints all elements of an Oma file
of type "power" in a certain bounding box. This example uses the fact,
that "power" is a block key for nodes, ways and areas. Using a
BlockFilter, and a BoundingBoxFilter, this query is very fast.
Additionally it uses a LifecycleFilter to restrict the search on
elements without lifecycle prefix.

* [Berlin.java](examples/Berlin.java) provides some figures about
Berlin. This is a more complex example, showing how to use PolyFilter
with polygons derived directly from the Oma file. First, a filter for
boundaries with name "Berlin" is setup. This filter is used to
generate a PolyFilter which is used to query severeal figures about
Berlin.

* [Map.java](examples/Map.java) creates a map of the city Todtnau,
containing forests, highways and mountain peaks. This is an even more
complex example, showing how to create a map. The map is written as an
eps-file to stdout.

* [Types.java](examples/Types.java) prints some information about the
types used to build an Oma file.

## Known bugs

There are no known bugs.
