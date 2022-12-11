(ns day1clj.day8
  (:require [clojure.string :as str]))

(defn str-to-int [i]
  (Integer/parseInt i))

(defn str-to-int-vec [input]
  (->> (str/split input #"")
       (mapv str-to-int)))

(defn get-height [heights row-index col-index]
  (-> (nth heights row-index)
      (nth col-index)))

(defn get-col-as-vec [heights col-index]
  (reduce
   (fn [vec row]
     (conj vec (nth row col-index)))
   []
   heights))

(defn get-left-side [heights row-index col-index]
  (let [row (nth heights row-index)]
    (take col-index row)))

(defn get-right-side [heights row-index col-index]
  (let [row (nth heights row-index)]
    (drop (+ 1 col-index) row)))

(defn get-top-side [heights row-index col-index]
  (take row-index (get-col-as-vec heights col-index)))

(defn get-bottom-side [heights row-index col-index]
  (drop (+ 1 row-index) (get-col-as-vec heights col-index)))

(defn is-visible-left [heights row-index col-index]
  (let [left-side (get-left-side heights row-index col-index)
        height (get-height heights row-index col-index)]
    (if (= (count left-side) 0)
      true
      (< (apply max left-side) height))))

(defn is-visible-right [heights row-index col-index]
  (let [right-side (get-right-side heights row-index col-index)
        height (get-height heights row-index col-index)]
    (if (= (count right-side) 0)
      true
      (< (apply max right-side) height))))

(defn is-visible-top [heights row-index col-index]
  (let [top-side (get-top-side heights row-index col-index)
        height (get-height heights row-index col-index)]
    (if (= (count top-side) 0)
      true
      (< (apply max top-side) height))))

(defn is-visible-bottom [heights row-index col-index]
  (let [bottom-side (get-bottom-side heights row-index col-index)
        height (get-height heights row-index col-index)]
    (if (= (count bottom-side) 0)
      true
      (< (apply max bottom-side) height))))

(defn scenic-score [side height]
  (if (= (count side) 0)
    0
    (loop [index 0]
      (let [evaluated-tree-height (nth side index)]
        (if (or (>= evaluated-tree-height height) (= index (- (count side) 1)))
          (+ 1 index)
          (recur (+ 1 index)))))))

(defn scenic-left [heights row-index col-index]
  (let [left-side (get-left-side heights row-index col-index)
        rev-left-side (reverse left-side)
        height (get-height heights row-index col-index)]
    (scenic-score rev-left-side height)))

(defn scenic-right [heights row-index col-index]
  (let [right-side (get-right-side heights row-index col-index)
        height (get-height heights row-index col-index)]
    (scenic-score right-side height)))

(defn scenic-top [heights row-index col-index]
  (let [top-side (get-top-side heights row-index col-index)
        rev-top-side (reverse top-side)
        height (get-height heights row-index col-index)]
    (scenic-score rev-top-side height)))

(defn scenic-bottom [heights row-index col-index]
  (let [bottom-side (get-bottom-side heights row-index col-index)
        height (get-height heights row-index col-index)]
    (scenic-score bottom-side height)))

(defn scenic-total [heights row-index col-index]
  (*
   (scenic-bottom heights row-index col-index)
   (scenic-top heights row-index col-index)
   (scenic-left heights row-index col-index)
   (scenic-right heights row-index col-index)))


(defn is-visible [heights row-index col-index]
  (or
   (is-visible-left heights row-index col-index)
   (is-visible-right heights row-index col-index)
   (is-visible-top heights row-index col-index)
   (is-visible-bottom heights row-index col-index)))

(defn read-input []
  (let [raw-input (slurp "inputday8.txt")]
    (->> (str/split raw-input #"(\n)")
         (map str-to-int-vec))))

(defn get-visibilities []
  (let [heights (read-input)]
    (map-indexed
     (fn [row-index row]
       (map-indexed
        (fn [col-index _]
          (is-visible heights row-index col-index))
        row))
     heights)))

(defn get-scenic-scores []
  (let [heights (read-input)]
    (map-indexed
     (fn [row-index row]
       (map-indexed
        (fn [col-index _]
          (scenic-total heights row-index col-index))
        row))
     heights)))

(defn main1 []
  (->> (get-visibilities)
       (flatten)
       (filter identity)
       (count)))

(defn main2 []
  (->> (get-scenic-scores)
       (flatten)
       (apply max)))
