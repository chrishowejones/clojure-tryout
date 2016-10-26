(ns try-clojure.monads3
  (require [cats.core :as m]
           [cats.builtin]
           [cats.monad.maybe :as maybe]))

;; semi-group is an algebraic structure with an associative binary operator e.g. mappend
(m/mappend [1 2 3] [4 5 6])
;; => [1 2 3 4 5 6]

;; monoid is semi-group with an identity fn maybe/nothing in the case of maybe
(m/mappend (maybe/just [1 2 3])
           (maybe/just [4 5 6])
           (maybe/nothing)
           (maybe/nothing))
;; => #<Just [1 2 3 4 5 6]>


;; Functor is a morphism with a 'computational context' that has one function - fmap
;; The Maybe type is a 'functor' - it's a wrapper that contains context and functor protocols

(m/fmap inc (maybe/just 1));; => #<Just 2>

(m/fmap #(* 2 %) (maybe/nothing));; => #<Nothing>
