(ns day1clj.day13
  (:require [clojure.string :as str]))

(defn load-input [raw-input]
  (->> (str/split raw-input #"\n\n")
       (mapv (fn [a]
               (->> (str/split a #"\n")
                    (mapv read-string))))))

(defn load-input2 [raw-input]
  (->> (str/split raw-input #"\n")
       (filter #(not (= "" %)))
       (mapv read-string)
       (concat [[[2]] [[6]]])))


(defn compare-packet [a b]
  (cond
    (and (nil? a) (not (nil? b))) -1
    (and (nil? a) (nil? b)) 0
    (and (not (nil? a)) (nil? b)) 1
    (and (number? a) (number? b)) (compare a b)
    (and (number? a) (vector? b)) (recur (vector a) b)
    (and (number? b) (vector? a)) (recur a (vector b))
    ; Recursively compare for vector and vector
    (and (vector? a) (vector? b))
    (let [max-index (max (count a) (count b))]
      (loop [index 0 acc 0]
        (if (or (not (= 0 acc)) (= index max-index))
          acc
          (recur (+ 1 index) (compare-packet (nth a index nil) (nth b index nil))))))
    :else (println "Can't compare" a b)))

(defn main1 []
  (let [input (load-input (slurp "./inputday13.txt"))
        comparison (mapv #(compare-packet (nth % 0) (nth % 1)) input)]
    (reduce-kv
     (fn [acc index val]
       (if (= -1 val)
         (+ acc (+ 1 index))
         acc))
     0
     comparison)))

(defn position [coll val]
  (loop [index 0]
    (if (= val (nth coll index))
      index
      (recur (+ 1 index)))))

(defn main2 []
  (let [input (load-input2 (slurp "./inputday13.txt"))
        ordered-packets (sort compare-packet input)
        position1 (position ordered-packets [[2]])
        position2 (position ordered-packets [[6]])]
    (* (+ 1 position1) (+ 1 position2))))



(comment
  (str/replace "[1,2]" #"[\[\]]" "")
  (main1)
  (main2)
  (compare 1 -1)
  (compare-packet [[4,4],4,4]
                  [[4,4],4,4,4])
  (concat [1 2] [3 4]))
