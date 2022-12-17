(ns day1clj.day11
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
         (mapv #(identity {:monkey-id (nth % 1)
                           :items (parse-items-str (nth % 2))
                           :operation (parse-operation-str (nth % 3))
                           :test-divisible-by (str-to-int (nth % 4))
                           :true-throw-to (str-to-int (nth % 5))
                           :false-throw-to (str-to-int (nth % 6))
                           :inspected-count 0})))))

(defn vector-if-nil [v]
  (if (nil? v) [] v))

(defn move-first-item-to [monkeys item-worry-level from-monkey-id to-monkey-id]
  (-> (update-in
       monkeys
       [to-monkey-id :items]
       (fn [v] (conj (vector-if-nil v) item-worry-level)))
      (update-in [from-monkey-id :items] (fn [v] (apply vector (drop 1 v))))
      (update-in [from-monkey-id :inspected-count] #(+ 1 %))))

(defn run-monkeys [monkeys]
  (loop [monkeys monkeys
         monkey-index 0
         item-left (:items (nth monkeys monkey-index nil))]
    (let [current-monkey (nth monkeys monkey-index nil)
          current-item (first item-left)]
      (cond
        (nil? current-monkey) monkeys
        (nil? current-item) (recur monkeys (+ 1 monkey-index) (get-in monkeys [(+ 1 monkey-index) :items]))
        :else
        (let [operation (:operation current-monkey)
              divisible-by (:test-divisible-by current-monkey)
              new-worry-level (int (/ (operation current-item) 3))
              move-to-monkey-index (if (zero? (mod new-worry-level divisible-by)) (:true-throw-to current-monkey) (:false-throw-to current-monkey))]
          (recur
           (move-first-item-to monkeys new-worry-level monkey-index  move-to-monkey-index)
           monkey-index
           (drop 1 item-left)))))))

(defn create-get-equivalent-worry-level-fn [monkeys]
  (fn [w]
    (let [products-divisible-by (->> (map :test-divisible-by monkeys)
                                     (reduce *))]
      (mod w products-divisible-by))))

(defn run-monkeys-2 [monkeys]
  (let [equivalent-worry-level-fn (create-get-equivalent-worry-level-fn monkeys)]
    (loop [monkeys monkeys
           monkey-index 0
           item-left (:items (nth monkeys monkey-index nil))]
      (let [current-monkey (nth monkeys monkey-index nil)
            current-item (first item-left)]
        (cond
          (nil? current-monkey) monkeys
          (nil? current-item) (recur monkeys (+ 1 monkey-index) (get-in monkeys [(+ 1 monkey-index) :items]))
          :else
          (let [operation (:operation current-monkey)
                divisible-by (:test-divisible-by current-monkey)
                new-worry-level (equivalent-worry-level-fn (operation current-item))
                move-to-monkey-index (if (zero? (mod new-worry-level divisible-by)) (:true-throw-to current-monkey) (:false-throw-to current-monkey))]
            (recur
             (move-first-item-to monkeys new-worry-level monkey-index  move-to-monkey-index)
             monkey-index
             (drop 1 item-left))))))))

(defn monkey-business [monkeys]
  (let [top-2-scores (->> (map #(:inspected-count %) monkeys)
                          (sort)
                          (reverse)
                          (take 2))
        [score1 score2] top-2-scores]
    (* score1 score2)))

(defn run-monkey-loop-1 [times]
  (loop [input (read-input)
         current-times 0]
    (if (= times current-times)
      input
      (recur (run-monkeys input) (+ 1 current-times)))))

(defn run-monkey-loop-2 [times]
  (loop [input (read-input)
         current-times 0]
    (if (= times current-times)
      input
      (recur (run-monkeys-2 input) (+ 1 current-times)))))

(defn main []
  (let [result (run-monkey-loop-1 20)
        score (monkey-business result)]
    score))

(defn main2 []
  (let [result (run-monkey-loop-2 10000)
        score (monkey-business result)]
    score))

(comment
  (parse-items-str "79, 60, 97")
  (parse-value "5" "10")
  ((let [o (parse-operation-str "new = old * old")]
     (o 5)))
  (first [])
  (int (/ 22 7))
  (doall (drop 1 [1 2 3])))
