(ns day1clj.day11-test
  (:require [clojure.test :refer :all]
            [day1clj.day11 :refer :all]))

(def fixture [{:monkey-id "0",
               :items [79 98],
               :operation nil,
               :test-divisible-by 23,
               :true-throw-to 2,
               :false-throw-to 3
               :inspected-count 0}
              {:monkey-id "1",
               :items [54 65 75 74],
               :operation nil,
               :test-divisible-by 19,
               :true-throw-to 2,
               :false-throw-to 0
               :inspected-count 0}
              {:monkey-id "2",
               :items [79 60 97],
               :operation nil,
               :test-divisible-by 13,
               :true-throw-to 1,
               :false-throw-to 3
               :inspected-count 0}
              {:monkey-id "3",
               :items [74],
               :operation nil,
               :test-divisible-by 17,
               :true-throw-to 0,
               :false-throw-to 1
               :inspected-count 0}])
(deftest move
  (testing "Move item to monkey 3"
    (let [new-monkeys (move-first-item-to fixture 9999 0 3)]
      (is
       (=
        (get-in new-monkeys [3 :items])
        [74 9999]))
      (is
       (=
        (get-in new-monkeys [0 :items])
        [98])))))

(comment
  (move))

