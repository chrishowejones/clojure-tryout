(ns try-clojure.romannumerals)

(def numeral-pairs [[1000 "M"]
                    [900 "CM"]
                    [500 "D"]
                    [400 "CD"]
                    [100 "C"]
                    [90 "XC"]
                    [50 "L"]
                    [40 "XL"]
                    [10 "X"]
                    [9 "IX"]
                    [5 "V"]
                    [4 "IV"]
                    [1 "I"]])

(defn- generate-numeral-str
  [[res rem] [num rn]]
  (if (<= num rem)
    [(str res rn) (- rem num)]
    (reduced [res rem])))

(defn- append-numeral
  [[numerals-res remainder] numeral-pair]
  (reduce generate-numeral-str
        [numerals-res remainder] (repeat numeral-pair)))

(defn convert [number]
  (first (reduce append-numeral ["" number] numeral-pairs)))

(comment

  ;; The recursive version of convert

  (defn convert [number]
    (loop [remainder number numerals-res "" [num rn] (first numeral-pairs) more-pairs (rest numeral-pairs)]
      (if (<= remainder 0)
        numerals-res
        (if (<= num remainder)
          (recur (- remainder num) (str numerals-res rn) [num rn] more-pairs)
          (recur remainder numerals-res (first more-pairs) (rest more-pairs))))))

  ;; repl testing of reduction fns
  (generate-numeral-str ["I" 2] [1 "I"])

  (append-numeral ["" 3] [1 "I"])
  )
