# rads.xtrace

Wrappers for `babashka.process` functions to enable tracing by default.

The default behavior is similar to `set -x` in `bash`.

## Installation

Add the following coordinates to your `deps.edn` or `bb.edn` file:

```clojure
io.github.rads/xtrace {:git/tag "v0.0.3" :git/sha "5e7c34c"}
```

## Usage

```clojure
(require '[rads.xtrace :refer [shell sh process exec]])

;; Same API as babashka.process
(shell {} "whoami")
(sh {} "whoami")
@(process {} "whoami")
(exec {} "whoami")

;; Disable tracing globally
(alter-var-root #'rads.xtrace/*enabled* (constantly false))

;; Customize output
(def custom-pre-start-fn #(apply println ">>" (:cmd %)))
(alter-var-root #'rads.xtrace/*pre-start-fn* (constantly custom-pre-start-fn))
```
