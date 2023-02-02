(ns leinfink.focko.events
  (:gen-class)
  (:require
   [clojure.edn :as edn]
   [clojure.string :as str]
   [leinfink.focko.engine :refer [map->label]]))

(defn read-events-from-file [file]
  (edn/read-string (slurp file)))

(defn label-keybind [label keybind]
  (let [key (str/capitalize (str (symbol keybind)))
        key-desc (str "[" key "]")]
    (str key-desc " " (str/replace-first label key key-desc))))

(defn event-label [{:keys [label keybind]} pos]
  (map->label {:str (label-keybind label keybind)
               :pos pos}))

(defn eventlabels [events]
  (map-indexed (fn [idx val] (event-label val [0 idx])) events))

(defn process-input [input events]
  (when-let [event (some #(when (= input (:keybind %)) %) events)]
    (load-string (str (:fn event)))))
