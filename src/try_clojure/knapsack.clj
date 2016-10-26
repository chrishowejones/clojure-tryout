(ns try-clojure.knapsack
  (:require [clojure.string :as s]))

(def item-data
  [ "map"         9 150
    "compass"    13  35
    "water"     153 200
    "sandwich"   50 160
    "glucose"    15  60
    "tin"        68  45
    "banana"     27  60
    "apple"      39  40
    "cheese"     23  30
    "beer"       52  10
    "suntan cream"   11  70
    "camera"     32  30
    "t-shirt"    24  15
    "trousers"   48  10
    "umbrella"   73  40
    "waterproof trousers"    42  70
    "waterproof overclothes" 43  75
    "note-case"  22  80
    "sunglasses"  7  20
    "towel"      18  12
    "socks"       4  50
    "book"       30  10])

(defrecord item [name weight value])

(def items (vec (map #(apply ->item %) (partition 3 item-data))))

(declare mm)

(defn m [i w]
  (cond
    (neg? i) [0 []]
    (zero? w) [0 []]
    :else
    (let [{wi :weight vi :value} (get items i)]
      (if (> wi w)
        (mm (dec i) w)
        (let [[vn sn :as no] (mm (dec i) w)
              [vy sy :as yes] (mm (dec i) (- w wi))]
          (if (> (+ vy vi) vn)
            [(+ vy vi) (conj sy i)]
            no))))))

(def mm (memoize m))

(let [[value indexes] (m (-> items count dec) 400)
      names (map (comp :name items) indexes)]
  (println "items to pack:" (s/join ", " names))
  (println "total value:" value)
  (println "total weight:" (reduce + (map (comp :weight items) indexes))))
