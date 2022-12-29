(ns day1clj.day13-test
  (:require [clojure.test :refer :all]
            [day1clj.day13 :refer :all]))

(deftest comparison
  (testing "compare number"
    (is (= (compare-packet 1 2) -1))
    (is (= (compare-packet 2 2) 0))
    (is (= (compare-packet 3 2) 1)))
  (testing "compare vector"
    (is (= (compare-packet [] [1]) -1))
    (is (= (compare-packet [1] []) 1))
    (is (= (compare-packet [1] [1]) 0))
    (is (= (compare-packet [1 1 1 1] [1 3]) -1))
    (is (= (compare-packet [1 1 1 1] [1 1 1 1 1 1]) -1))
    (is (= (compare-packet 2 [2]) 0))
    (is (= (compare-packet 2 [3]) -1))
    (is (= (compare-packet 4 [3]) 1))))


(def fixture
  [[[1,1,3,1,1]
    [1,1,5,1,1]]

   [[[1],[2,3,4]]
    [[1],4]]])

(deftest sample-test
  (testing "sample case"
    (is (= (take 2 (load-input (slurp "./test/day1clj/inputday13_test.txt")))
           fixture))))

(comment
  (comparison)
  (sample-test)
  (eval "[1,2,3]")
  (compare-packet [[4,4],4,4]
                  [[4,4],4,4,4]))