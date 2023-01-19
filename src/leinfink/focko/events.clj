(ns leinfink.focko.events
  (:gen-class)
  (:require
   [clojure.edn :as edn]
   [clojure.string :as str]
   [leinfink.focko.engine :refer [map->label]]))

(defn label-keybind [label keybind]
  (let [key (str/capitalize (str (symbol keybind)))]
    (str/replace-first label key (str "(" key ")"))))

(defn event-label [{:keys [label keybind]} pos]
  (map->label {:str (label-keybind label keybind)
               :pos pos}))

(def aggro-event {:label "Make More Aggro"
                  :keybind :a
                  :fn (fn [{{unit :active-unit} :view :as state}]
                        (update-in state [:game unit :aggro] inc))
                  :success "got more aggro."
                  :failure "couldn't get more aggro."})

(defn eventlabels-from-file [file]
  (->> (edn/read-string (slurp file))
       (map-indexed (fn [idx val] (event-label val [0 idx])))))
