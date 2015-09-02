(ns clojure-tryout.core-test
  (:require [midje.sweet :refer :all]
            [clojure-tryout.core :refer :all]))

(fact "Given a 3 return 'Fizz'"
      (fizzbuzz-number 3) => "Fizz")

(fact "Given a 5 return 'Buzz'"
      (fizzbuzz-number 5) => "Buzz")

(fact "Given 15 return 'FizzBuzz'"
      (fizzbuzz-number 15) => "FizzBuzz")

(fact "Given 4 return '4'"
      (fizzbuzz-number 4) => "4")

(fact "Given 20 return 'Buzz'"
      (fizzbuzz-number 20) => "Buzz")

(fact "Given 21 return 'Fizz'"
      (fizzbuzz-number 21) => "Fizz")

(fact "Given 90 return 'FizzBuzz'"
      (fizzbuzz-number 90) => "FizzBuzz")

(fact "Given 13 return '13'"
      (fizzbuzz-number 13) => "13")

(fact "Given 13 return '13'"
      (fizzbuzz-number2 13) => "13")
