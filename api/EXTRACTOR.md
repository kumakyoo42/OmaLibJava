# Extractor

## Basic Use

    Extractor e = new Extractor(<oma file>);

    e.addExtract(<first filter>,<first output file>);
    e.addExtract(<second filter>,<second output file>);
    ...

    e.run();

First a new Extractor is created, referencing the Oma file from which
the extract should be taken. Second, some output files with a filter
for each are defined. Finally, the extract is run.

## Constructors

Currently, there is only one constructor:

    Extractor(String filename)

Creates a new Extractor, which is ready to create extracts from the
file `filename`.

## Methods

    void addExtract(Filter f, String filename)

Adds a new extract defined by the filter `f`. The extract will be saved
to the file called `filename`.

**Please note:** If two extracts write to the same file, the result of
calling `run()` is undefined.

    void run() throws IOException

Runs the extract process and writes the files defined with
`addExtract`. Depending on the Oma file, the amount of simultaneous
extracts and the filters used, this may take some time.

**Please note:** No checks for overwriting files are applied. Data may
be lost.

