(ns leinfink.focko.engine
  (:gen-class)
  (:require
   [clojure.string :as str]
   [leinfink.focko.lanterna :as l]))

(defrecord label [str pos fg-color bg-color])
(defrecord rect [pos size color])

(defn labels-with-mappings [mappings str-pos-xs]
  (for [[str, pos] str-pos-xs]
    (map->label (assoc mappings :str str :pos pos))))

(defmulti draw! (fn [_ obj] (class obj)))

(defmethod draw! label [tgx  {:keys [str pos] :as props}]
  (let [[x y] pos
        lines (str/split-lines str)
        colors (select-keys props [:fg-color :bg-color])
        put-line! (fn [idx s] (l/put-string tgx [x (+ idx y)] s colors))]
    (doall (map-indexed put-line! lines))))

(defmethod draw! rect [tgx {:keys [pos size color]}]
  (l/put-rect tgx pos size color))

(defn paint! [scr objects]
  (l/clear scr)
  (let [tgx (l/create-graphics scr)]
    (doseq [obj objects]
      (print objects)
      (draw! tgx obj)))
  (l/refresh scr))
