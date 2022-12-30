(ns day1clj.day9
  (:require [clojure.string :as str]))

(defn parse-input-row [row]
  (let [row-split (str/split row #" ")]
    {:direction (nth row-split 0)
     :times (Integer/parseInt (nth row-split 1))}))

(defn read-input []
  (let [raw-input (str/split (slurp "inputday9.txt") #"\n")]
    (map parse-input-row raw-input)))

(def initial-state {:h {:x 0 :y 0} :t {:x 0 :y 0}})

(defn move-head [state direction]
  (case direction
    "U" (update-in state [:h :y] inc)
    "D" (update-in state [:h :y] dec)
    "R" (update-in state [:h :x] inc)
    "L" (update-in state [:h :x] dec)))

(defn- move-tail-x-axis [state allow-cover head-key tail-key]
  (let [head-x (get-in state [head-key :x])
        tail-x (get-in state [tail-key :x])
        factor (if allow-cover 0 1)]
    (cond
      (> (- head-x tail-x) factor) (update-in state [tail-key :x] inc)
      (< (- head-x tail-x) (- factor)) (update-in state [tail-key :x] dec)
      :else state)))

(defn- move-tail-y-axis [state allow-cover head-key tail-key]
  (let [head-y (get-in state [head-key :y])
        tail-y (get-in state [tail-key :y])
        factor (if allow-cover 0 1)]
    (cond
      (> (- head-y tail-y) factor) (update-in state [tail-key :y] inc)
      (< (- head-y tail-y) (- factor)) (update-in state [tail-key :y] dec)
      :else state)))

(defn- move-tail-diagonal [state head-key tail-key]
  (-> (move-tail-x-axis state true head-key tail-key)
      (move-tail-y-axis true head-key tail-key)))

(defn head-tail-touching? [state head-key tail-key]
  (let [head-y (get-in state [head-key :y])
        tail-y (get-in state [tail-key :y])
        head-x (get-in state [head-key :x])
        tail-x (get-in state [tail-key :x])]
    (and (<= (abs (- head-y tail-y)) 1)
         (<= (abs (- head-x tail-x)) 1))))

(defn move-tail-along-the-head [state head-key tail-key]
  (let [head-y (get-in state [head-key :y])
        tail-y (get-in state [tail-key :y])
        head-x (get-in state [head-key :x])
        tail-x (get-in state [tail-key :x])]
    (cond
      (head-tail-touching? state head-key tail-key) state
      (= head-x tail-x) (move-tail-y-axis state false head-key tail-key)
      (= head-y tail-y) (move-tail-x-axis state false head-key tail-key)
      :else (move-tail-diagonal state head-key tail-key))))


(defn move2 [state direction knot-keys]
  (loop [knot-keys-left knot-keys
         current-state (move-head state direction)]
    (if (= 1 (count knot-keys-left))
      current-state
      (recur
       (drop 1 knot-keys-left)
       (move-tail-along-the-head current-state (nth knot-keys-left 0) (nth knot-keys-left 1))))))

(defn execute-move-record-y [state moves recorded]
  (let [direction (:direction moves)
        times (:times moves)]
    (loop [t 0
           current-state state
           recorded recorded]
      (let [new-recorded (conj recorded (get current-state :t))]
        ;; (println current-state)
        (if (>= t times)
          [current-state new-recorded]
          (recur (inc t) (move2 current-state direction [:h :t]) new-recorded))))))

(defn execute-move-record-state [state moves knot-keys recorded]
  (let [direction (:direction moves)
        times (:times moves)]
    (loop [t 0
           current-state state
           recorded (conj recorded current-state)]
      (if (>= t times)
        [current-state recorded]
        (let [next-state (move2 current-state direction knot-keys)]
          (recur (inc t) next-state (conj recorded next-state)))))))


(defn main1 []
  (let [all-moves (read-input)]
    (loop [state initial-state
           record []
           move-index 0]
      (if (>= move-index (count all-moves))
        [record]
        (let [current-move (nth all-moves move-index)
              [next-state next-record] (execute-move-record-state state current-move [:h :t] record)]
          (recur next-state next-record (inc move-index)))))))

(def start-point {:x 0 :y 0})
(def initial-state-2
  {:h start-point
   :1 start-point
   :2 start-point
   :3 start-point
   :4 start-point
   :5 start-point
   :6 start-point
   :7 start-point
   :8 start-point
   :9 start-point})
(def knot-keys-2 [:h :1 :2 :3 :4 :5 :6 :7 :8 :9])

(defn cal-unique-position [record-states knot-key]
  (-> (map #(knot-key %) record-states)
      (set)
      (count)))


(defn main2 []
  (let [all-moves (read-input)]
    (loop [state initial-state-2
           record []
           move-index 0]
      (if (>= move-index (count all-moves))
        (cal-unique-position record :9)
        (let [current-move (nth all-moves move-index)
              [next-state next-record] (execute-move-record-state state current-move knot-keys-2 record)]
          (recur next-state next-record (inc move-index)))))))

