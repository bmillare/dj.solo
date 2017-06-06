(ns dj.dependencies2
  (:require [cemerick.pomegranate :as pom]
            [cemerick.pomegranate.aether :as a]
            [dj.io]))

(defn add-dependencies
  "(add-dependencies '[[incanter \"1.7.0\"]])"
  [coordinates]
  (pom/add-dependencies
   :coordinates coordinates
   :local-repo (dj.io/file (System/getProperty "user.dir")
                           "m2")
   :repositories (merge a/maven-central
                        {"clojars" "https://clojars.org/repo"})))
