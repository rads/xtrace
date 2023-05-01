(ns rads.xtrace
  (:require [babashka.process :as p]))

(def ^:dynamic *enabled* true)

(defn default-pre-start-fn [{:keys [cmd]}]
  (when *enabled*
    (apply println "+" cmd)))

(def ^:dynamic *pre-start-fn* default-pre-start-fn)

(defn- wrap [f]
  (fn [opts? & args]
    (let [command (if (map? opts?) args (cons opts? args))
          opts (merge {:pre-start-fn *pre-start-fn*}
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
