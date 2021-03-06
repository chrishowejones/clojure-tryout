(ns try-clojure.monads)

(defn return [v]
  (fn [] v))

(defn bind [a f]
  (let [v (a)]
    (if v
      (f v)
      (return nil))))

(defn m-inc [a]
  (bind a (fn [v] (return (inc v)))))

;; unpack a return
((return 5))
 ;; bind to execute a fn on an action and wrap it as an action again
(bind (return 5) (fn [v] (return (inc v))))
 ;; same as above in a monadic fn
(m-inc (return 5))
 ;; and again
(m-inc (return 5))
 ;; this time extracting value
((m-inc (return 5)))


;; not create a div monadic fn
(defn m-div [a n]
  (if (zero? n)
    (return nil)
    (bind a (fn [v] (return (/ v n))))))

;; use m-div to divide by 2 and return new action
(m-div (return 12) 2)

;; unpack result
((m-div (return 12) 2))
;; => 6

;; what happens if div by zero?
((m-div (return 12) 0))
;; => nil

;; div by zero handled
((-> (return 11)
     m-inc
     (m-div 0)
     m-inc))
;; => nil

((-> (return 11)
     m-inc
     (m-div 2)
     m-inc))
;; => 7
