(ns leinfink.focko.core
  (:gen-class)
  (:require
   [clojure.string :as str]
   [leinfink.focko.engine :refer [map->label ->label ->rect paint!]]
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

(defn game-map [_]
  [(->rect [31 0] [1 40] :white)])

(defn main-loop [scr val]
  (paint! scr (concat (menu) (game-map val)))
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
