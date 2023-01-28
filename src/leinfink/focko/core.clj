(comment
  (run)
  )

(ns leinfink.focko.core
  (:gen-class)
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [leinfink.focko.engine :refer [map->label ->label ->rect paint!]]
   [leinfink.focko.events :refer [eventlabels-from-file]]
   [leinfink.focko.lanterna :as l]))

(defn long-str [& xs] (str/join "\n" xs))

(defn menu []
  [(map->label {:pos [0 0] :fg-color :white :bg-color :black
                :str (long-str
                      "Commandant #1"
                      ""
                      (format "Aggro: %d" 100)
                      (format "Speed: %d" 200)
                      ""
                      "Tweaks:"
                      ""
                      "(A) Make More Aggro"
                       "(S) Make More Speedy")})
   (->label " (P)rev | (N)ext | (E)nd Move" [0 -1] :white :black)])

(defn events [_]
  (map
   #(update % :pos (fn [[x y]] [x (+ y 2)]))
   (eventlabels-from-file (io/resource "leinfink/focko/actions.edn"))))

(defn properties [{{unit :active-unit} :view :as state}]
  [(->label
    (->> (get-in state [:game unit])
         (map (fn [[key val]] (str (str/capitalize (name key)) ": " val)))
         (str/join "\n"))
    [0 10] :white :black)])

(defn titlebar [state]
  [(->label (str "Active: " (get-in state [:view :active-unit]))
            [0 0] :white :black)])

(defn game-map [_]
  [(->rect [31 0] [1 40] :white)])

(defn main-loop [scr state]
  (paint! scr (concat
               (titlebar state)
               (events state)
               (properties state)))
  (l/read-input scr)
  #_(when (l/read-input scr) (recur scr (inc val))))

(defn run []
  (l/with-screen [scr (l/create-screen true)]
    (main-loop scr
               {:view {:active-unit "focko1"}
                :game {"focko1" {:aggro 2
                                 :speed 5} }})))

(comment

  (run)

  (def f (future (run)))

  (shutdown-agents)

  ,)
