(ns try-clojure.core
  (require [clojure.core.match :as m])
  (:require [clojure.string :as s]
            [clojure.string :as str]))

(def nested [[1 2] 3 [4 [5 6 ] [7 8]]])

(def nested2 [[9] 10 11 [12 [13 14]]])

(apply str (flatten nested))

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
;;(reduce + double-sqr-iter)
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

(contains? #{1 2 3 5} 4)
;; => false

(contains? [3 3 3] 3)
;; => false

(contains? ["yes" "yes" "yes"] "yes")
;; => false

(contains? ["zero" "zero" "zero"] 0)
;; => true

(some #{:a :b} [:c :f :a :b])

(defn contains-in-vector? [x vec]
  {:pre [(vector? vec)]}
  (some #{x} vec))

(contains-in-vector? "me" ["chris" "fred" "me" "them"])

(contains-in-vector? 1 [1 2 3])
;; => 1




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

(map (fn [[s v]] (assoc {:f :read} :state s :value v)) [[:ok 123] [:not-ok 456]])
;; => ({:f :read, :state :ok, :value 123} {:f :read, :state :not-ok, :value 456})

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

(select-keys map3 [:a :c])
;; => {:a 1, :c 3}

(map map3 [:a :c])
;; => (1 3)

(clojure.set/index [map4] [:a :c])
;; => {{:a 1, :c {:e 5, :d 4}} #{{:c {:e 5, :d 4}, :b 2, :a 1}}}

(clojure.set/rename [map3] {:a :ay :c :see})
;; => #{{:b 2, :ay 1, :see 3}}

(def m {:a 1})

(let [m2 {:a 2}]
  (condp = (m2 :a)
   1 (assoc m2 :b 2)
   2 (assoc m2 :b 1)
   (assoc m2 :b 0)))
;; => {:a 2, :b 1}

(apply hash-map (interleave [:a :b :c] (repeat 0)))
;; => {:c 0, :b 0, :a 0}
(reduce (fn [m k] (assoc m k 0)) {} [:a :b :c])
;; => {:a 0, :b 0, :c 0}
(mapcat (fn [k] [k 0]) [:a :b :c])
;; => (:a 0 :b 0 :c 0)
(find (zipmap [:a :b :c] (repeat 0)) :a)
;; => [:a 0]
(find {:a "fred" :b "jim"} :b)
;; => [:b "jim"]

(let [my-map (zipmap [:a :b :c :d] (range 3))]
  (as-> (:b my-map) b-value
    (inc b-value)
    (* 2 b-value)
    (assoc my-map :b b-value))) ;; => {:a 0, :b 4, :c 2}

(map (fn [input]
        (let [reverser
              (if (string? input)
                (comp (partial apply str) reverse)
                reverse)]
          (= input (reverser input)))) [[1 2 1] [1 2 3] "abba"])

(into-array ["a" "b"])
;; => #object["[Ljava.lang.String;" 0x740dfede "[Ljava.lang.String;@740dfede"]

(defn a-var-arg [& args]
  (type args))

(a-var-arg "a" "b" "c") ;; => clojure.lang.ArraySeq

(java.text.MessageFormat/format "Hello {0}" (into-array ["Chris"]))
;; => "Hello Chris"

(defn my-fn
  "This is my function"
  [n]
  (repeat n "x"))

(my-fn 10)
;; => ("x" "x" "x" "x" "x" "x" "x" "x" "x" "x")

#_(def numbers (repeatedly))

(defn factorial [n]
  (apply * (range 1 (inc n))))

(defn fibonacci [n]
  (first (drop n (map first (iterate (fn [[a b]] [b (+ a b)]) [0 1])))))

(fibonacci 10)

(defn fibonacci2 [n]
  (cond
    (< n 1) 0
    (= n 1) 1
    :else (+ (fibonacci2 (- n 2)) (fibonacci2 (dec n)))))

(fibonacci2 6)
(fibonacci 6 )

(def key-order
  [:c :d :e :a :f])

(defn reorder-entry
  [key-order]
  (fn [x]
    (reduce (fn [acc key] (assoc acc key (key x))) {} key-order)))

(def maps
  [{:a "1" :c 2 :d 3 :e 4 :f 5}
   {:a "11" :c 12 :d 13 :e 14 :f 15}
   {:a "21" :c 22 :d 23 :e 24 :f 25}])

(defn reorder-maps
  [m]
  (let [reorder-fn (reorder-entry key-order)]
    (map reorder-fn m)))

(reorder-maps maps)

(defrecord OrderedColumns
    [c d e a f])

(defn create-ordered-columns
  [conversion-mapping]
  (fn [x]
    (let [convertor (fn [m k v]
                      (if-let [conversion-fn (k conversion-mapping)]
                        (assoc m k (conversion-fn v))
                        (assoc m k v)))]
      (map->OrderedColumns (reduce-kv convertor {} x)))))

(def conv-mapping
  {:a #(Double/parseDouble %)})

(map (create-ordered-columns conv-mapping) maps)
;; => (#try_clojure.core.OrderedColumns{:c 2, :d 3, :e 4, :a 1.0, :f 5} #try_clojure.core.OrderedColumns{:c 12, :d 13, :e 14, :a 11.0, :f 15} #try_clojure.core.OrderedColumns{:c 22, :d 23, :e 24, :a 21.0, :f 25})

(defn convert-using-array-map
  [col-conversion-mapping]
  (fn [x]
    (apply array-map (mapcat (fn [[k f]] [k (f (k x))]) col-conversion-mapping))))

(def col-conv-mapping
  [[:c identity]
   [:d identity]
   [:e identity]
   [:a #(Double/parseDouble %)]
   [:f identity]])

(map (convert-using-array-map col-conv-mapping) maps)

(map map->OrderedColumns maps)
;; => (#try_clojure.core.OrderedColumns{:c 2, :d 3, :e 4, :a 1, :f 5} #try_clojure.core.OrderedColumns{:c 12, :d 13, :e 14, :a 11, :f 15} #try_clojure.core.OrderedColumns{:c 22, :d 23, :e 24, :a 21, :f 25})


(comment

  (def ^:dynamic *my-assert-enabled* false)

  (defmacro my-assert [f?]
    `(let [pred-res# ~(eval f?)]
       (if (and *my-assert-enabled*
                (not pred-res#))
         (throw  (ex-info
                  (format "My assert failed- returned %s" pred-res#)
                  {}))
         pred-res#)))

  (with-local-vars [*my-assert-enabled* false]
    *my-assert-enabled*)


  (my-assert (< 2 2))

  (binding [*my-assert-enabled* true]
    (my-assert (< 2 2)))


  )
