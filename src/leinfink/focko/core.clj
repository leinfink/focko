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

(defn events []
  (eventlabels-from-file (io/resource "leinfink/focko/actions.edn")))

(defn game-map [_]
  [(->rect [31 0] [1 40] :white)])

(defn main-loop [scr val]
  (paint! scr (concat (events) (game-map val)))
  (l/read-input scr)
  #_(when (l/read-input scr) (recur scr (inc val))))

(defn run []
  (l/with-screen [scr (l/create-screen true)]
    (main-loop scr 0)))

(comment

  (run)

  (def f (future (run)))

  (shutdown-agents)

  ,)
