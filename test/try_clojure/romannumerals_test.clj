(ns try-clojure.romannumerals-test
  (:require [clojure.test :as t :refer [deftest is]]
            [try-clojure.romannumerals :refer [convert]]))

(deftest givenOneExpectI
  (is (= "I" (convert 1))))

(deftest givenTwoExpectII
  (is (= "II" (convert 2))))

(deftest givenThreeExpectIII
  (is (= "III" (convert 3))))

(deftest givenFourExpectIV
  (is (= "IV" (convert 4))))

(deftest givenFiveExpectV
  (is (= "V" (convert 5))))

(deftest givenSixExpectVI
  (is (= "VI" (convert 6))))

(deftest givenNineExpectIX
  (is (= "IX" (convert 9))))

(deftest givenTenExpectX
  (is (= "X" (convert 10))))

(deftest givenElevenExpectXI
  (is (= "XI" (convert 11))))

(deftest givenNineteenExpectXIX
  (is (= "XIX" (convert 19))))

(deftest givenThirtyNineExpectXXXIX
  (is (= "XXXIX" (convert 39))))

(deftest givenFortyExpectXL
  (is (= "XL" (convert 40))))

(deftest givenFiftyExpectL
  (is (= "L" (convert 50))))

(deftest givenNinetyExpectXC
  (is (= "XC" (convert 90))))

(deftest givenOneHundredExpectC
  (is (= "C" (convert 100))))

(deftest givenThreeHundredAndNinetyNineExpectCCCXCIX
  (is (= "CCCXCIX" (convert 399))))

(deftest givenFourHundredExpectCD
  (is (= "CD" (convert 400))))

(deftest givenFiveHundredExpectD
  (is (= "D" (convert 500))))

(deftest givenNineHundredExpectCM
  (is (= "CM" (convert 900))))

(deftest givenOneThousandExpectM
  (is (= "M" (convert 1000))))

(deftest givenThreeThousandNineHundredAndNinetyNineExpectMMMCMXCIX
  (is (= "MMMCMXCIX" (convert 3999))))
