(ns leinfink.focko.engine
  (:gen-class)
  (:require [leinfink.focko.lanterna :as l]))

(defrecord string [str pos fg-color bg-color])
(defrecord rect [pos size color])

(defn strings-with-mappings [mappings string-pos-xs]
  (for [[str, pos] string-pos-xs]
    (map->string (assoc mappings :str str :pos pos))))

(defmulti draw! (fn [_ obj] (class obj)))

(defmethod draw! string [tgx  {:keys [str pos fg-color bg-color]}]
  (l/put-string tgx pos str (hash-map fg-color bg-color)))

(defmethod draw! rect [tgx {:keys [pos size color]}]
  (l/put-rect tgx pos size color))

(defn paint! [scr objects]
  (l/clear scr)
  (let [tgx (l/create-graphics scr)]
    (doseq [obj objects]
      (draw! tgx obj)))
  (l/refresh scr))
