(ns leinfink.focko.utils
  (:gen-class)
  (:require [clojure.string :as str]))

(defn long-str [& xs] (str/join "\n" xs))
