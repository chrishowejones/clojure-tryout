(ns clojure-tryout.chairs)

;; Calculate chair number of last patient
;; Given number of chairs (> 2) and that each patient will:
;;    1. find a place furthest away from any other person
;;    2. find a place closest to the exit
;;

(defn largest-range [v]
  ;; given a vector of ranges (2-tuple vectors of min and max values)
  ;;
  (let [diff #(- (second %) (first %))]
    (reduce #(if (> (diff %1) (diff %2)) %1 %2) [0 0] v)))


(defn allocate-chair [min max]
  ;; given a vector of two numbers split into a vector of up to two vectors
  ;; and the allocated number.
  ;; if the input range is range of 1 then remove that pair.
  (fn [allocation-map]
   (let [{:keys [ranges]} allocation-map
         largest (largest-range ranges)
         split-map (split-range min max largest)])))

(allocate-chair {:chair 5 :ranges [[2 4] [6 9]]})

(comment
 (defn last-chair [n]
   (loop [chairs-left (vec (range 1 n))]
     )))

;; find lowest unallocated range of chairs and divide by two truncating down
;; store vector of vectors of ranges?
