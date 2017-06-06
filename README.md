# dj.solo
a minimal standalone kit for distributing clojure apps that can grow

## motivation

Imagine that you want to show off your new clojure app to your friend
but it turns out she uses windows and she doesn't like you installing
weird stuff on her computer. She said you are only allowed to run
everything from a usb stick, and to top it off, she doesn't even have
Java installed.

dj.solo solves this problem by creating a standalone clojure
app/distribution that bundles everything you need to run the
application, including the jars, source code, and even the desired
jvm.

Nothing particularly novel is presented, but I believe this is a
simple approach that is easy to customize to your needs.

## strategy

Some app components are your responsibility for setting
up. Instructions and guidelines are provided but no turn-key scripts
are presented.

### directory layout

- any directory can be assumed to be the app parent directory
  - for our purposes, we will assume this git repository will be used
    as a base

+ dj.solo
  + jre (a blatant copy of a jre directory)
  + jars (filled with a base set of jars, up to you on what to
    include, a suggested base is provided)
  + m2 (a maven repository, needed if you want to dynamically pull in
    jars)
  + src (for source code you'd like to keep easy to modify)
  - run.bat (for windows)
  - run.sh (for mac/linux)

### copying over a jre

It's up to you to include whichever version of Java you'd like to
include. For the windows case, install as normal a java version as
instructions from Oracle. Navigate to the Program Files->Java
directory, and copy over your desired version of to the app
directory. You can rename it to jre, create a symlink to it called
jre, or hard code the version info in the run script.

(For Jdks installations, note that they include a jre inside, so be
sure to copy that directory and not the jdk.)

### base jars

It's recommended that you create a uberjar from a leiningen project
that uses cemerick.pomegranate and copy that standalone jar into the
jars folder. A good example project is
https://github.com/bmillare/dj.project

$ cd dj.project; lein uberjar; cp target/\*standalone\*jar foo-path/jar

You will also need to include the Clojure jar. For alpha 1.9.0 users,
you will need to also include the jars from
https://github.com/clojure/spec.alpha and
https://github.com/clojure/core.specs.alpha

### src directory

You are free to populate this as you please. dj/dependencies2.clj
includes code that calls pomegranate to ensure that it stores the
downloaded jars in the local m2 directory
