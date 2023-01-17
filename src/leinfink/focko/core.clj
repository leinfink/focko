(ns leinfink.focko.core
  (:gen-class)
  (:require
   [leinfink.focko.engine :refer [->rect paint! strings-with-mappings]]
   [leinfink.focko.lanterna :as l]))

(defn menu []
  (strings-with-mappings
   {:fg-color :black :bg-color :white}
   [["         Commandant #1" [0 0]]
    ["Aggro: 100" [1 2]]
    ["Speed: 10" [1 3]]
    ["Tweaks:" [1 6]]
    ["(A) Make More Aggro" [4 8]]
    ["(S) Make More Speedy" [4 9]]
    ["Actions: [3/4]" [1 12]]
    ["(M) Move" [4 14]]
    ["(B) Build House" [4 15]]
    ["    (P)rev | (N)ext | (E)nd" [0 -1]]]))

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
(print "hi")
  (shutdown-agents)

  ,)
