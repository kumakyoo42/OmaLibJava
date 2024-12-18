#!/bin/sh

set -e

javac de/kumakyoo/omalibjava/*.java
jar cf omalibjava.jar de/kumakyoo/omalibjava/*.class
