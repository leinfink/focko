(ns leinfink.focko.core
  (:gen-class)
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.edn :as edn]
   [leinfink.focko.engine :refer [->label ->rect paint! read-input translate]]
   [leinfink.focko.events :as events]
   [leinfink.focko.lanterna :as l]))

(defn active-unit [state] (get-in state [:view :active-unit]))

(defn titlebar [state]
  [(->label (str "Active: " (active-unit state)) [0 0] :white :black)])

(defn events [_]
  (->> (io/resource "leinfink/focko/actions.edn")
       events/read-events-from-file
       events/eventlabels
       (map #(translate % [0 3]))))

(defn properties-list [state]
  (->> (get-in state [:game (active-unit state)])
       (map (fn [[key val]] (str (str/capitalize (name key)) ": " val)))
       (str/join "\n")))

(defn properties [state]
  [(->label (properties-list state) [0 10] :white :black)])

(defn game-map [{{unit :active-unit} :view :as state}]
  [(-> (->rect (get-in state [:game unit :pos]) [1 1] :white)
       (translate [30 0]))])

(defn check-input [scr state]
  (let [events (edn/read-string
                (slurp (io/resource "leinfink/focko/actions.edn")))]
    (if-let [func (events/process-input (read-input scr) events)]
      (func state)
      state)))

(defn main-loop [scr state]
  (paint! scr (mapcat #(% state) [titlebar events properties game-map]))
  (recur scr (check-input scr state)))

(defn run []
  (l/with-screen [scr (l/create-screen true)]
    (main-loop scr
               {:view {:active-unit "focko1"}
                :game {"focko1" {:aggro 2
                                 :speed 5
                                 :pos [0 5]} }})))

(comment

  (def f (future (run)))
  (future-cancel f)
  (shutdown-agents)

  ,)
