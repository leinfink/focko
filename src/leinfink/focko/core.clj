(comment
  (run)
  )

(ns leinfink.focko.core
  (:gen-class)
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [leinfink.focko.engine :refer [map->label ->label ->rect paint! translate]]
   [leinfink.focko.events :refer [eventlabels-from-file]]
   [leinfink.focko.lanterna :as l]))


(defn titlebar [state]
  [(->label (str "Active: " (get-in state [:view :active-unit]))
            [0 0] :white :black)])

(defn events [_]
  (->> (eventlabels-from-file (io/resource "leinfink/focko/actions.edn"))
       (map #(translate % [0 3]))))

(defn properties [{{unit :active-unit} :view :as state}]
  [(map->label {:pos [0 10] :fg-color :white :bg-color :black
                :str (->> (get-in state [:game unit])
                          (map (fn [[key val]]
                                 (str (str/capitalize (name key)) ": " val)))
                          (str/join "\n"))})])

(defn game-map [{{unit :active-unit} :view :as state}]
  [(-> (->rect (get-in state [:game unit :pos]) [1 1] :white)
       (translate [30 0]))])

(defn main-loop [scr state]
  (paint! scr (mapcat #(% state) [titlebar
                                  events
                                  properties
                                  game-map]))
  (l/read-input scr)
  #_(when (l/read-input scr) (recur scr (inc val))))

(defn run []
  (l/with-screen [scr (l/create-screen true)]
    (main-loop scr
               {:view {:active-unit "focko1"}
                :game {"focko1" {:aggro 2
                                 :speed 5
                                 :pos [0 5]} }})))

(comment

  (run)

  (def f (future (run)))

  (shutdown-agents)

  ,)
