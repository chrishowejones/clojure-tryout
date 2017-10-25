(ns try-clojure.core-test
  (:require [clojure.test :refer :all]
            [try-clojure.core :refer :all]))

(deftest failing
  (is (= 0 1)))
