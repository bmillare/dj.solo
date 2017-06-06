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