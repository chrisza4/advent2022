(ns day1clj.day3
  (:require [clojure.set :as set]
            [clojure.string :as str]))


(defn read-and-format-input []
  (let [raw-input (slurp "inputday3.txt")]
    (->> (str/split raw-input #"(\n)")
         (map #(map set (split-at (/ (count %) 2) %))))))

(defn read-and-format-input-2 []
  (let [raw-input (slurp "inputday3.txt")]
    (->> (str/split raw-input #"(\n)")
         (partition 3))))

(defn is-uppercase [character]
  (= (str character) (str/upper-case character)))

(defn priority [item-type]
  (if (is-uppercase item-type)
    (+ 27 (- (int item-type) (int \A)))
    (+ 1 (- (int item-type) (int \a)))))

(defn priority-sum [item-type-set]
  (->> (map priority item-type-set)
       (reduce +)))

(defn main []
  (->> (read-and-format-input)
       (map #(set/intersection (first %) (last %)))
       (map priority-sum)
       (reduce +)))

(defn main2 []
  (->> (read-and-format-input-2)
       (map #(map set %))
       (map #(set/intersection (first %) (nth % 1) (last %)))
       (map priority-sum)
       (reduce +)))


(comment
  (priority \a)
  (partition 3 (range 42))
  (last '(1 2))
  (str \a))
