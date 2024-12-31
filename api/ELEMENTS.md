# Elements

## Element

`Element` is an abstract base class for all elements. It contains
several fields that all elements share:

    long id
    int version
    long timestamp
    long changeset
    int uid
    String user

    String key
    String value

    Map<String, String> tags

The first six entries are the meta information of the element, if
present. If not present, the values are `Long.MIN_VALUE`,
`Integer.MIN_VALUE` or `null`, depending on the type of that entry.

The two strings `key` and `value` are the key and value of the block
and slice which contains this element.

The tags of the element are provided as a map.

## Node

Nodes provide the following to fields:

    int lon;
    int lat;

These two coordinates specify the position of the node. Values are
given in WGS84 multiplied by 10,000,000.

## Way

Ways provide the following fields:

    int[] lon;
    int[] lat;

These provide a series of coordinates specifying the way. Values are
given in WGS84 multiplied by 10,000,000.

## Area

Areas provide the following fields:

    int[] lon;
    int[] lat;

    int[][] holes_lon;
    int[][] holes_lat;

The first two items define a closed loop (the first coordinate is not
repeated at the end).

The second two items define holes inside the area defined by this
loop. Every hole is itself a closed loop (again the first coordinate
is not repeated at the end). An area may contain zero holes.

Values are given in WGS84 multiplied by 10,000,000.
