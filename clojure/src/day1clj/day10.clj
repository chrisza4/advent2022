(ns day1clj.day10
  (:require [clojure.string :as str]))

(def monkey-regexp #"Monkey (\d+):\n. Starting items: ([\d,\s]+)\n  Operation: (.+)\n. Test: divisible by (\d+)\n.   If true: throw to monkey (\d+)\n.   If false: throw to monkey (\d+)")

(def operation-regexp #"new = (\S+) (\S) (\S+)")

(defn str-to-int [i]
  (Integer/parseInt i))


(defn parse-items-str [items-str]
  (let [items (-> (str/replace items-str #" " "")
                  (str/split #","))]
    (mapv str-to-int items)))

; Parse operation
(defn parse-value [old val]
  (if (= val "old")
    old
    (str-to-int val)))

(defn parse-math-ops [m]
  (case m
    "+" +
    "-" -
    "*" *
    "/" /
    identity))

(defn parse-operation-str [operation-str]
  (let [regexp-match (re-matches operation-regexp operation-str)
        operation {:first (nth regexp-match 1)
                   :math-ops (nth regexp-match 2)
                   :second (nth regexp-match 3)}]
    (fn [old]
      (let [first-value (parse-value old (:first operation))
            second-value (parse-value old (:second operation))
            math-ops (parse-math-ops (:math-ops operation))]
        (math-ops first-value second-value)))))

(defn read-input []
  (let [raw-input (slurp "inputday10.txt")]
    (->> (re-seq monkey-regexp raw-input)
         (map #(identity {:monkey-id (nth % 1)
                          :items (parse-items-str (nth % 2))
                          :operation (parse-operation-str (nth % 3))
                          :test-divisible-by (str-to-int (nth % 4))
                          :true-throw-to (str-to-int (nth % 5))
                          :false-throw-to (str-to-int (nth % 6))})))))

(defn run-monkeys [monkeys]
  (loop [monkeys monkeys
         monkey-index 0
         item-index 0]
    (let [current-monkey (nth monkey-index monkeys nil)
          current-item (nth item-index (:items current-monkey) nil)]
      (cond
        (nil? current-monkey) monkeys
        (nil? current-item) (recur monkeys (+ 1 monkey-index) 0)
        (let [operation (:operation current-monkey)
              divisible-by (:test-divisible-by current-monkey)
              new-worry-level (-> (operation current-item) (/ 3))
              move-to (if (= 0 (mod new-worry-level divisible-by)) (:true-throw-to current-monkey) (:false-throw-to current-monkey))]
          (recur))))))

    ;; (if (= index (count monkeys))
    ;;   monkeys
    ;;   (if (nil? item-worry-level)
    ;;     (recur monkeys (nth monkeys (+ 1 index)) (+ 1 index) ()))
    ;;   (let [operation (:operation monkey)
    ;;         divisible-by (:test-divisible-by monkey)
    ;;         new-worry-level (-> (operation item-worry-level) (/ 3))]
    ;;     (if (= (% new-worry-level 3) 0))))))


(comment
  (parse-items-str "79, 60, 97")
  (parse-value "5" "10")
  ((let [o (parse-operation-str "new = old * old")]
     (o 5))))
