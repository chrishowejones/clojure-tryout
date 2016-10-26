(ns try-clojure.core
  (require [clojure.core.match :as m])
  (:require [clojure.string :as s]))

(def nested [[1 2] 3 [4 [5 6 ] [7 8]]])

(def nested2 [[9] 10 11 [12 [13 14]]])

(flatten nested)

{:a "value"}
#{"hello" "world" 1 2.33949}

'(1 1 2)

[1 2 3]

(:b {:a "value"} :default)

(def map1 {:a 1 :b 2 :c 3})

(def map2 {:a 1 :d 20 :c 30 :f 50})

(map (juxt :a :b :f) [map1 map2])

(defn sqr [n] (* n n))

(defn times2 [n] (* n 2))

((comp sqr times2) 4)
;; => 64

;; equivalent to
(sqr (times2 4))
;; => 64

((comp times2 sqr) 4)
;; => 32

;; equivalent to
(times2 (sqr 4))
;; => 32

;; transduce a seq
(def numbers [2 4])

(def double-sqr (comp (map sqr) (map times2)))
(def sqr-doubles (comp (map times2) (map sqr)))

(transduce double-sqr + numbers)
;; => 40
(transduce sqr-doubles + numbers)
;; => 80

(sequence double-sqr numbers)
;; => (8 32)

(sequence sqr-doubles numbers)
;; => (16 64)

(def double-sqr-iter (eduction double-sqr numbers))
(reduce + double-sqr-iter)
;; => 40
(sequence double-sqr-iter)
;; => (8 32)


(into [] double-sqr numbers)
;; => [8 32]
(into [] sqr-doubles numbers)
;; => [16 64]

(defn mean-reducer [memo x]
     (-> memo
      (update-in [:sum] + x)
      (update-in [:count] inc)))

(reduce mean-reducer {:sum 0 :count 0} (range 10));; => {:sum 45, :count 10}

(defn increment-transducer [f1]
  (fn [result input]
    (f1 result (inc input))))

(reduce (increment-transducer mean-reducer) {:sum 0 :count 0} (range 10))
;; => {:sum 55, :count 10}

(defmulti fac identity)

(defmethod fac 0 [_] 1)

(defmethod fac :default [n]
  (* n (fac (dec n))))

(fac 5)
;; => 120

;; core.match
(defn match-stuff
  [pattern]
  (m/match [pattern]
           ["fred"] :found-fred
           :else :not-found))
(def x 10)

(defn output-x [] x)

(def x 20)

(defn output-x-later [] x)

(partition-by #(>= 3 %) [1 2 3 4 5])

(def nums (range 100000))

(range 1 10 3)
;; => (1 4 7)

(mapv inc (range 10))
;; => [1 2 3 4 5 6 7 8 9 10]


;; equivalent of zip in Scala
(map vector "christopher" (range 11))
;; => ([\c 0] [\h 1] [\r 2] [\i 3] [\s 4] [\t 5] [\o 6] [\p 7] [\h 8] [\e 9] [\r 10])

(set "christopher")
;; => #{\c \e \h \i \o \p \r \s \t}

(contains? #{1 2 3 4} 3)
;; => true

(some #{:a :b} [:c :f :a :b])

(defn contains-in-vector? [x vec]
  {:pre [(vector? vec)]}
  (some #{x} vec))

(contains-in-vector? "me" ["chris" "fred" "me" "them"])

(contains-in-vector? 1 [1 2 3])
;; => 1

(re-seq #"fred" "This sentence contains fred once")

(:a {:a 1 :b 2})
;; => 1

(:d {:a 1 :b 2})
;; => nil

(:d {:a 1 :b 2} :none);; => :none

(group-by first ["fred" "bill" "jim"]);; => {\f ["fred"], \b ["bill"], \j ["jim"]}

(group-by count ["fred" "bill" "jim" "tony" "william"])
;; => {4 ["fred" "bill" "tony"], 3 ["jim"], 7 ["william"]}

(def name-pairs (map vector (cycle (range 3)) ["fred" "bill" "william" "chris" "gareth" "cerys" "danielle" "bruce"]))

(group-by first name-pairs)
;; => {0 [[0 "fred"] [0 "chris"] [0 "danielle"]], 1 [[1 "bill"] [1 "gareth"] [1 "bruce"]], 2 [[2 "william"] [2 "cerys"]]}

(frequencies ["fred" "bill" "jim" "tony" "william" "bill" "fred" "bill"])
;; => {"fred" 2, "bill" 3, "jim" 1, "tony" 1, "william" 1}

(def my-map {:a 1 :b 2 :c 3})

(letfn [(map-with-default [k] (my-map k :none))]
  (vector (map-with-default :a)
          (map-with-default :d)))
;; => [1 :none]

(let [value 10
      fn1 (fn [n] (+ n 5))]
  (fn1 value));; => 15

(def word "christopher")

(map identity (frequencies word))
;; => ([\c 1] [\e 1] [\h 2] [\i 1] [\o 1] [\p 1] [\r 2] [\s 1] [\t 1])

(s/join (filter #(Character/isLetter %) "This has, some!$punctuation<>chars"))
;; => "Thishassomepunctuationchars"

(map (fn [[s v]] (assoc {:f :read} :state s :value v)) [[:ok 123]])
;; => ({:f :read, :state :ok, :value 123})

(def brown [0 1 2 3 4 5 6 7 8])
(def blue (assoc brown 5 'beef))
brown
;; => [0 1 2 3 4 5 6 7 8]

blue
;; => [0 1 2 3 4 beef 6 7 8]

(def lard [[\l 1] [\a 1] [\r 2] [\d 1]])
(def r [[\r 1]])

(defn- fn-subtract-freq
  [y-map]
  (fn [acc x] (conj acc (let [y-value (y-map (first x))]
                                     (if y-value
                                       [(first x) (- (second x) y-value)]
                                       x)))))

(defn subtract-occurrences [xs ys]
  (let [y-map (into {} ys)]
    (filter #(pos? (second %))
     (reduce (fn-subtract-freq y-map) [] xs))))

(subtract-occurrences lard r)
;; => ([\l 1] [\a 1] [\r 1] [\d 1])

(def map3 {:a 1 :b 2 :c 3})

((juxt :a :c) map3)
;; => [1 3]

(vals (select-keys map3 [:a :c]))
;; => (1 3)

(first (clojure.set/project [map3] [:a :c]))
;; => {:a 1, :c 3}

(def map4 {:a 1 :b 2 :c {:d 4 :e 5}})

((juxt :a #(get-in % [:c :e])) map4)
;; => [1 5]

(select-keys map3 [:a :c]);; => {:a 1, :c 3}

(map map3 [:a :c])
;; => (1 3)

(clojure.set/index [map4] [:a :c])
;; => {{:a 1, :c {:e 5, :d 4}} #{{:c {:e 5, :d 4}, :b 2, :a 1}}}

(clojure.set/rename [map3] {:a :ay :c :see})
;; => #{{:b 2, :ay 1, :see 3}}
