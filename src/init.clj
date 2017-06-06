(require 'dj.io)

(let [m2-dir (dj.io/file (System/getProperty "user.dir")
                         "m2")]
  (when (not (.exists ^java.io.File m2-dir))
    (dj.io/mkdir m2-dir)))
