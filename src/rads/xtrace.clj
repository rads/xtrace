(ns rads.xtrace
  (:require [babashka.process :as p]))

(def ^:dynamic *enabled* true)

(defn default-pre-start-fn [{:keys [cmd]}]
  (binding [*out* *err*]
    (apply println "+" cmd)))

(def ^:dynamic *pre-start-fn* default-pre-start-fn)

(defn- wrap [f]
  (fn [opts? & args]
    (let [command (if (map? opts?) args (cons opts? args))
          opts (merge {:pre-start-fn (fn [& args]
                                       (when *enabled*
                                         (apply *pre-start-fn* args)))}
                      (when (map? opts?) opts?))]
      (apply f opts command))))

(def
  ^{:doc (str "Wrapper for `babashka.process/shell`.\n\n  Original docs:\n\n  "
              (:doc (meta #'babashka.process/shell)))
    :argslists '([opts? & args])}
  shell
  (wrap p/shell))

(def
  ^{:doc (str "Wrapper for `babashka.process/sh`.\n\n  Original docs:\n\n  "
              (:doc (meta #'babashka.process/sh)))
    :argslists '([opts? & args])}
  sh
  (wrap p/sh))

(def
  ^{:doc (str "Wrapper for `babashka.process/process`.\n\n  Original docs:\n\n  "
              (:doc (meta #'babashka.process/process)))
    :argslists '[opts? & args]}
  process
  (wrap p/process))

(def
  ^{:doc (str "Wrapper for `babashka.process/exec`.\n\n  Original docs:\n\n  "
              (:doc (meta #'babashka.process/exec)))
    :argslists '[opts? & args]}
  exec
  (wrap p/exec))
