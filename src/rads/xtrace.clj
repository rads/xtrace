(ns rads.xtrace
  (:require [babashka.process :as p]))

(def ^:dynamic *enabled* true)

(defn default-pre-start-fn [{:keys [cmd]}]
  (when *enabled*
    (apply println "+" cmd)))

(def ^:dynamic *pre-start-fn* default-pre-start-fn)

(defn shell
  {:doc (str "Wrapper for `babashka.process/shell`.\n\n  Original docs:\n\n  "
             (:doc (meta #'babashka.process/shell)))}
  [opts? & args]
  (let [command (if (map? opts?) args (cons opts? args))
        opts (merge {:pre-start-fn *pre-start-fn*}
                    (when (map? opts?) opts?))]
    (apply p/shell opts command)))

(defn sh
  {:doc (str "Wrapper for `babashka.process/sh`.\n\n  Original docs:\n\n  "
             (:doc (meta #'babashka.process/sh)))}
  [opts? & args]
  (let [command (if (map? opts?) args (cons opts? args))
        opts (merge {:pre-start-fn *pre-start-fn*}
                    (when (map? opts?) opts?))]
    (apply p/sh opts command)))

(defn process
  {:doc (str "Wrapper for `babashka.process/process`.\n\n  Original docs:\n\n  "
             (:doc (meta #'babashka.process/process)))}
  [& args]
  (let [command (if (map? (last args)) (butlast args) args)
        opts (merge {:pre-start-fn *pre-start-fn*})]
    (when (map? (last args)) (last args)
      (p/process command opts))))
