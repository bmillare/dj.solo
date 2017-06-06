# dj.solo
a minimal standalone kit for distributing Clojure apps that can grow

## motivation

Imagine that you want to show off your new Clojure app to your friend
but it turns out she uses Windows. She doesn't like you installing
weird stuff on her computer so she said you are only allowed to run
everything from a usb stick. To top it off, she doesn't even have
Java installed.

`dj.solo` solves this problem of "deploying" your app to your friend under her constraints. It does so by creating a standalone Clojure
app/distribution that bundles everything you need to run the
application, including the dependent jars, app source code, and the desired
JVM.

Nothing technically novel is presented. Instead a simple approach is documented that is easy to customize to your needs.

The advantages to this approach are:

- isolated installation
- barebones, minimal software
  - you know what's being installed and being depended on
  - startup to repl is much faster than `leiningen` or `boot`
    - you can always add them later if you need their functionality
    
The limitations to this approach are:

- the OS will not suggest updates to Java
  - although, theoretically, you could have the app detect a new Java version, download it, and set the correct new paths
- You might have extra copies of Java, the system installation and the app provided one
  - potentially, if you put multiple apps, you will have a lot of copies of the JVM
  - this could be considered a feature if you want to run specific versions
  - still trivial to manage your self with symlinks

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
include. For the Windows case, install a Java version using
instructions from Oracle. Navigate to the installed `Program Files->Java`
directory, and copy over your desired JRE version to the app
directory. You can rename it to `jre`, create a symlink to it called
`jre`, or hard code the version info in the run script.

(For JDK installations, note that they include a JRE inside, so be
sure to copy that directory and not the JDK's.)

Example Paths:
- Windows 7 ->  `C:\Program Files\Java\jre1.8.0_60` -> `C:\Foo\myapp\jre`
- Arch Linux jre8 -> `/usr/lib/jvm/java-8-openjdk/jre/` -> `/home/foo/myapp/jre`

### base jars

It's recommended that you create a `uberjar` from a `leiningen` project
that uses `cemerick.pomegranate` and copy that standalone jar into the
`jars` folder. A good example project is
https://github.com/bmillare/dj.project

```bash
cd dj.project
lein uberjar
cp target/*standalone*jar
foo-path/jar
```

You will also need to include the Clojure jar available https://clojure.org/community/downloads. For alpha 1.9.0 users,
you will need to also include the jars from
https://github.com/clojure/spec.alpha and
https://github.com/clojure/core.specs.alpha. I found the jars after clicking "Development Snapshot Versions".

### src directory

You are free to populate this as you please. `src/dj/dependencies2.clj`
includes code that calls pomegranate to ensure that it stores the
downloaded jars in the local `m2` directory. Also, an `src/init.clj` is provided that is run at the start prior to starting a repl

### run script

The main thing is setting the correct paths, classpaths, and then loading clojure.main with any initialization clojure files. Using the following documentation https://clojure.org/reference/repl_and_main I supplied arguments `-i` and `-r`.

## example build instructions for a windows deployment from linux

```bash
# download template
git clone https://github.com/bmillare/dj.solo.git

# get JRE
wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jre-8u131-windows-x64.tar.gz
tar jre*.tar.gz
mv jre1.8.0_131 dj.solo/jre

# build template uberjar
git clone https://github.com/bmillare/dj.project.git
cd dj.project
lein uberjar
mkdir ../dj.solo/jars
mv target/dj.project-0.3.5-standalone.jar ../dj.solo/jars/
cd ..

# rename app
mv dj.solo myapp

# download Clojure jars
cd myapp/jars
wget 'http://repo1.maven.org/maven2/org/clojure/clojure/1.9.0-alpha17/clojure-1.9.0-alpha17.jar'
wget 'http://repo1.maven.org/maven2/org/clojure/spec.alpha/0.1.123/spec.alpha-0.1.123.jar'
wget 'http://repo1.maven.org/maven2/org/clojure/core.specs.alpha/0.1.10/core.specs.alpha-0.1.10.jar'

# "install" app to memstick
cd ../..
cp -r myapp /path/to/memstick
```

## example dynamic load run usage on Windows

```clojure
;; copy myapp to local directory (optional, runs faster)
;; double click run.bat
(require 'dj.dependencies2)
(dj.dependencies2/add-dependencies '[[incanter "1.5.7"]])
(use '(incanter core stats charts io))
(view (histogram (sample-normal 1000)))
```
