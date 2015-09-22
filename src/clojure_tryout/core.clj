(ns clojure-tryout.core
  (:require [clojure-tryout.core :refer [fizzbuzz-number-naive-impl]]
            [clojure.core.async :refer [<! >! chan go timeout]])
  (:gen-class))

;; It does this by getting the modulus of 15 then using this as the index for a
;; map to look up the index of a second map where the index returned is the one
;; for "Fizz" for modulus result 3, 6 & 9 "Buzz" for 5 & 10 and "FizzBuzz" for
;; 0 (i.e. divisable by 15). Anything else will return nil from the first map
;; where nil is mapped to the value of the number in the second map.
;; Finally str is called to ensure that when the number is returned unchanged
;; it is transformed into a string to match "Fizz", "Buzz" & "FizzBuzz".

(defn fizzbuzz-number-naive [n]
  (cond
    (= 0 (mod n 15)) "FizzBuzz"
    (= 0 (mod n 5))  "Buzz"
    (= 0 (mod n 3))  "Fizz"
    :else            (str n)))

(defn fizzbuzz-naive [s]
  (map fizzbuzz-number-naive s))

(defn fizzbuzz-number [n]
  {:pre [(and (pos? n) (integer? n))]}
  (-> n
      (mod 15)
      ({3 0, 5 1, 6 0, 9 0, 10 1, 12 0, 0 2})
      ({nil n, 0 "Fizz", 1 "Buzz", 2 "FizzBuzz"})
      str))

(defn fizzbuzz-number2 [n]
  {:pre [(and (pos? n) (integer? n))]}
  (get {3 "Fizz" 6 "Fizz" 9 "Fizz" 12 "Fizz" 5 "Buzz" 10 "Buzz" 0 "FizzBuzz"}
       (mod n 15)
       (str n)))

(defn fizzbuzz [s]
  (map fizzbuzz-number s))

(defn fizzbuzz2 [s]
  (map fizzbuzz-number2 s))

(defn happy [n]
  (loop [snc (map #(Character/digit % 10) (str n))
         sum-snc 0]
    (if (empty? snc)
      sum-snc
      (let [fst (first snc)]
        (recur
         (rest snc)
         (+ (* fst fst) sum-snc))))))

(defn find-happiness [n]
  (loop [test-integer n
         seen-so-far #{}]
    (let [next-integer (happy test-integer)]
      (if (= next-integer 1)
        [n true]
        (if (seen-so-far next-integer)
          [n false]
          (recur
           next-integer
           (conj seen-so-far next-integer)))))))

(defn fib
  ([n] (fib [1 1] n))
  ([s n]
   (if (>= (count s) n)
     s
     (fib
      (conj s (+ (first s) (second s))) n))))

((fn
   [n]
   (loop [s [1 1]]
     (if (>= (count s) n)
       s
       (recur
        (conj s (+ (last s) (nth s (- (count s) 2))))))))
 6)


(map first (take 20 (iterate #(vector (second %) (+ (first %) (second %))) [1 1])))

(take 20 (iterate #(vector (second %) (+ (first %) (second %))) [1 1]))

(def colleen
  {:name "Colleen"
   :pet {:name "Dexter"
         :mother {:name "Dixie"
                  :owner {:name "Sarah"}}}})

(def richard {:name "Richard"})

(comment)
(defn get-breeder-name [pet-owner]
  ((((pet-owner :pet) :mother) :owner) :name))


(defn call-unless-nil? [function value]
  (if (nil? value)
    nil
    (function value)))

(defn pretty-nil-friendly-get-breeder-name [pet-owner]
  (call-unless-nil?
   (fn [owner]
     (call-unless-nil?
      (fn [pet]
        (call-unless-nil?
         (fn [mother]
           (call-unless-nil?
            (fn [owner]
              (owner :name))
            (mother :owner)))
         (pet :mother)))
      (owner :pet)))
   pet-owner))

(pretty-nil-friendly-get-breeder-name colleen)
(pretty-nil-friendly-get-breeder-name richard)

(defn- sqr [x] (* x x))

(defn- average-func
  [s n]
  (* (/ 1 (* 2 (count s))) n))

(defn cost-func [training-set theta1]
  (let [hypothesis (fn [[x y]] (sqr (- (* theta1 x) y)))]
    (->> training-set
         (map hypothesis)
         (apply +)
         (average-func training-set))))

(cost-func [[1 1] [2 2] [3 3]] 0.0)
(cost-func [[1 1] [2 2] [3 3]] 1.0)
(cost-func [[1 1] [2 2] [3 3]] 0.5)
(cost-func [[1 1] [2 2] [3 3]] -0.5)

(comment
  (let [ch (chan)]
    (go (while true
          (let [v (<! ch)]
            (println "Read:" v))))
    (go (>! ch "Hi")
        (<! (timeout 5000))
        (>! ch "there!"))))



(def run-stuff (atom true))

(def c (chan))

(defn toggle-shout []
  (swap! run-stuff not))

(defn shout [msg] (go
                    (>! c msg)))

(defn out []
  (do
    (reset! run-stuff true)
    (go
      (while @run-stuff
        (let [msg (<! c)]
          (println "Shout:" msg))))))

(defn my-flatten [s]
  (let [[fst & more] s]
    (if (empty? more)
      (if (sequential? fst)
        (recur fst)
        (list fst))
      (if (sequential? fst)
        (concat (my-flatten fst) (my-flatten more))
        (cons fst (my-flatten more))))))


(defn ftn [s]
  (let [fst (first s) more (next s)]
    (concat
     (if (sequential? fst)
       (ftn fst)
       [fst])
     (when (sequential? more)
       (ftn more)))))

(defn series-sum [n]
  (->> (range 1 (* 3 n) 3.0)
       (map #(/ 1.0 %)))
  reduce + 0.0
  )

(series-sum 1)

(->> '() (reduce + 0.0))

(reduce (fn [cnt _] (inc cnt)) 0 [1 2 3 4 4 4 4 ])

(defn count-items
  [s]
  (loop [cnt 0 xs s]
    (if (empty? xs) cnt (recur (inc cnt) (rest xs)))))

(count-items [1 2 3 4 5 6 6 6 6])

(defmulti emit-bash
  (fn [form]
    (class form)))

(defmethod emit-bash
  clojure.lang.PersistentList
  [form]
  (case (name (first form))
    "println" (str "echo " (second form))))

(defmethod emit-bash
  java.lang.String
  [form]
  form)

(defmethod emit-bash
  java.lang.Long
  [form]
  (str form))

(defmethod emit-bash
  java.lang.Double
  [form]
  (str form))


(defmulti emit-batch
  (fn [form]
    (class form)))

(defmethod emit-batch
  clojure.lang.PersistentList
  [form]
  (case (name (first form))
    "println" (str "ECHO " (second form))))

(defmethod emit-batch
  java.lang.String
  [form]
  form)

(defmethod emit-batch
  java.lang.Long
  [form]
  (str form))

(defmethod emit-batch
  java.lang.Double
  [form]
  (str form))

(derive ::bash ::common)

(derive ::batch ::common)

(def ^{:dynamic true}
  *current-implementation*)

(defmulti emit
  (fn [form]
    [*current-implementation* (class form)]))

(defmethod emit [::common java.lang.String]
  [form]
  form)

(defmethod emit [::common java.lang.Long]
  [form]
  (str form))

(defmethod emit [::common java.lang.Double]
  [form]
  (str form))

(defmethod emit [::bash clojure.lang.PersistentList]
  [form]
  (case (name (first form))
    "println" (str "echo " (second form))
    nil))

(defmethod emit [::batch clojure.lang.PersistentList]
  [form]
  (case (name (first form))
    "println" (str "ECHO " (second form))
    nil))

(defmacro script [form]
  `(emit '~form))

(defmacro with-implementation
  [impl & body]
  `(binding [*current-implementation* ~impl]
     ~@body))

(defn emit-bash-form [a]
  "Returns a String containing the equivalent Bash script to its argument"
  (cond
    (= (class a) clojure.lang.PersistentList)
    (case (name (first a))
      "println" (str "echo " (second a)))
    (= (class a) java.lang.String) a
    (= (class a) java.lang.Long) (str a)
    (= (class a) java.lang.Double) (str a)
    :else (throw (Exception. "Fell through"))))



