(ns leinfink.focko.events
  (:gen-class)
  (:require
   [clojure.edn :as edn]
   [clojure.string :as str]
   [leinfink.focko.engine :refer [map->label]]))

(defn label-keybind [label keybind]
  (let [key (str/capitalize (str (symbol keybind)))
        key-desc (str "[" key "]")]
    (str key-desc " " (str/replace-first label key key-desc))))

(defn event-label [{:keys [label keybind]} pos]
  (map->label {:str (label-keybind label keybind)
               :pos pos}))

(defn eventlabels-from-file [file]
  (->> (edn/read-string (slurp file))
       (map-indexed (fn [idx val] (event-label val [0 idx])))))
