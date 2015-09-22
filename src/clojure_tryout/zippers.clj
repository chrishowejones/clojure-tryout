(ns clojure-tryout.zippers
  (:require [clojure.zip :as zip]))

(def ordinary-tree
  [1 [:a :b] 2 3 [40 50 60]])

(def vector-zipper (zip/vector-zip ordinary-tree))

(-> vector-zipper
    zip/down
    zip/right)

(comment
  "Result of above"
  [[:a :b]
 {:l [1],
  :pnodes [[1 [:a :b] 2 3 [40 50 60]]],
  :ppath nil,
  :r (2 3 [40 50 60])}])

;; zip/next walks tree depth first (NB zip/node returns value of current node
;; rather than whole zipper.
(-> vector-zipper zip/next zip/node) ;; gives - 1
(-> vector-zipper zip/next zip/next zip/node) ;; gives [:a :b]
(-> vector-zipper zip/next zip/next zip/next zip/node) ;; gives :a
(-> vector-zipper zip/next zip/next zip/next zip/next zip/node) ;; gives :b

;; use end? to determine when we have reached the end of the tree
(defn fast-forward-z [loc]
  (if (zip/end? loc)
    loc
    (recur (zip/next loc))))

(fast-forward-z vector-zipper) ; returns - [[1 [:a :b] 2 3 [40 50 60]] :end]

;; want to find first keyword
(defn first-keyword [loc]
  (if (zip/end? loc)
    nil
    (if (keyword? (zip/node loc))
      (zip/node loc)
      (recur (zip/next loc)))))

(first-keyword vector-zipper) ;; returns :a

;; editing a tree using a zipper
(-> vector-zipper zip/down (zip/edit inc) zip/root) ;; returns - [2 [:a :b] 2 3 [40 50 60]] - vector-zipper has both original tree and edit
(-> vector-zipper zip/down zip/node) ;; still returns 1 after change above

;; insert a node
(-> vector-zipper
    zip/down ;; we are at 1
    zip/right ;; in sub vector [:a :b]
    (zip/append-child :c) ;; add as last child
    zip/root)
;; above returns - [1 [:a :b :c] 2 3 [40 50 60]]

;; prepend a node
(-> vector-zipper
    zip/down
    zip/right
    zip/down ;; down to the :a node
    (zip/insert-left :aa)
    zip/root)
;; above returns - [1 [:aa :a :b] 2 3 [40 50 60]]

;; delete any numbers greate than 10
(defn death-to-big-numbers
  [loc]
  (if (zip/end? loc)
    loc
    (if (or (not (integer? (zip/node loc)))
            (< (zip/node loc) 11))
      (recur (zip/next loc)) ;; base case: keep moving
      (recur (zip/next (zip/remove loc))) ;; evil big numbers
      )))

(zip/root (death-to-big-numbers vector-zipper)) ;; returns - [1 [:a :b] 2 3 []]

;; start with new vector tree
(def z2 (zip/vector-zip [1 2 :a :b [3 4 :c :d 5] :e]))

;; Goal - all keyword nodes wrapped together in their own subtrees,
;; with adjacent keywords wrapped together
;; i.e. for z2 - [1 2 [:a :b] [3 4 [:c :d] 5] [:e]]

;; Strategy one - a: wrap and go down
(defn strategy-one-a [loc]
  (if (zip/end? loc)
    loc
    (if (keyword? (zip/node loc))
      (recur (zip/next (zip/edit loc #(vector %))))
      (recur (zip/next loc))))) ;; wait, don't do this: infinite loop
;; when we get to :a we wrap in vector but when we call
;; zip/next that's down into vector just created!!!
;; as next is called relative to the new zipper just created

;; don't wrap keywords already inside vectors only containing keywords
(defn parent-ok? [loc]
  (when (zip/up loc)
    (every? keyword? (zip/children (zip/up loc)))))
;; zip/children returns nodes not locs (other zippers) so can feed results
;; directly to keyword? without calling zip/node

;; Strategy one - b
(defn strategy-one-b [loc]
  (if (zip/end? loc)
    loc
    (if (and (keyword? (zip/node loc))
             (not (parent-ok? loc)))
      (recur (zip/next (zip/edit loc #(vector %))))
      (recur (zip/next loc)))))

(strategy-one-b z2) ;; we get - [[1 2 [:a] [:b] [3 4 [:c] [:d] 5] [:e]] :end]
;; no infinite recursion but keywords not grouped together

;; lets try grabbing left most keyword and then gobble up keywords to the right
(defn strategy-one-c [loc]
  (if (zip/end? loc)
    loc
    (if (and (keyword? (zip/node loc))
             (not (parent-ok? loc)))
      (let [right-keys (take-while #(keyword? %) (zip/rights loc))]
        (recur (zip/next
                (reduce (fn [z n]
                          (zip/append-child z n))
                        (zip/edit loc #(vector %))
                        right-keys))))
      (recur (zip/next loc)))))

(strategy-one-c z2) ;; produces - [[1 2 [:a :b] [:b] [3 4 [:c :d] [:d] 5] [:e]] :end]
