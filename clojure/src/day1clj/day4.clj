(ns day1clj.day4
  (:require [clojure.string :as str]))


(defn str-to-int [i]
  (Integer/parseInt i))

; Input "3-4,5-8"
; Output ((3 4) (5 8))
(defn line-to-ranges [line]
  (->> (str/split line #",")
       (map #(str/split % #"-"))
       (map #(map str-to-int %))))

(defn is-cover [range1 range2]
  (or
   (and (<= (first range1) (first range2)) (>= (last range1) (last range2)))
   (and (>= (first range1) (first range2)) (<= (last range1) (last range2)))))

(defn is-overlap [range1 range2]
  (and (>= (last range1) (first range2)) (>= (last range2) (first range1))))

; 3-5 4-7
(defn main1 []
  (let [raw-input (slurp "inputday4.txt")]
    (->> (str/split raw-input #"(\n)")
         (map line-to-ranges)
         (filter #(apply is-cover %))
         (count))))

(defn main2 []
  (let [raw-input (slurp "inputday4.txt")]
    (->> (str/split raw-input #"(\n)")
         (map line-to-ranges)
         (filter #(apply is-overlap %))
         (count))))
