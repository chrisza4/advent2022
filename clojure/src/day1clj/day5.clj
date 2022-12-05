(ns day1clj.day5
  (:require [clojure.string :as str]))

(defn str-to-int [i]
  (Integer/parseInt i))

(defn conj-if-not-empty [the-list c]
  (case c
    \space the-list
    nil the-list
    (conj the-list c)))

(defn read-stack-at [position input]
  (reduce #(conj-if-not-empty %1 (get %2 position)) '() input))

(defn read-stack-setting []
  (let [raw-input (slurp "inputday5.txt")
        stack-input (take 8 (str/split raw-input #"\n"))
        positions (map #(+ 1 (* 4 %))
                       (range 9))]
    (map #(reverse (read-stack-at % stack-input)) positions)))

(defn read-move [move]
  (let [regexp  #"move\s(\d+)\sfrom\s(\d+)\sto\s(\d+)"
        matches (re-matcher regexp move)
        matches-found (re-find matches)]
    {:count (str-to-int (get matches-found 1))
     :from (->> (get matches-found 2) (str-to-int) (dec))
     :to   (->> (get matches-found 3) (str-to-int) (dec))}))

(defn read-moves []
  (let [raw-input (slurp "inputday5.txt")
        moves-input (drop 10 (str/split raw-input #"\n"))]
    (map read-move moves-input)))

(defn move [stacks amount from to]
  (let [stack-from (nth stacks from)]
    (map-indexed
     (fn [index stack]
       (cond
         (= index from) (drop amount stack)
         (= index to) (concat (reverse (take amount stack-from)) stack)
         :else stack))
     stacks)))

(defn move-cratemover9001 [stacks amount from to]
  (let [stack-from (nth stacks from)]
    (map-indexed
     (fn [index stack]
       (cond
         (= index from) (drop amount stack)
         (= index to) (concat (take amount stack-from) stack)
         :else stack))
     stacks)))

(defn main1 []
  (->>
   (loop [stacks (read-stack-setting)
          moves (read-moves)]
     (let [next-move (first moves)]
       (if (zero? (count moves))
         stacks
         (recur
          (move stacks (:count next-move) (:from next-move) (:to next-move))
          (drop 1 moves)))))
   (map first)
   (apply str)))

(defn main2 []
  (->>
   (loop [stacks (read-stack-setting)
          moves (read-moves)]
     (let [next-move (first moves)]
       (if (zero? (count moves))
         stacks
         (recur
          (move-cratemover9001 stacks (:count next-move) (:from next-move) (:to next-move))
          (drop 1 moves)))))
   (map first)
   (apply str)))



(comment
  (concat '(1 2) '(3 4))
  (let [m  #"move\s(\d+)\sfrom\s(\d+)\sto\s(\d+)"
        n (re-matcher m "move 10 from 2 to 9")]
    (.group n))
  (let [m  #"move\s(\d+)\sfrom\s(\d+)\sto\s(\d+)"
        n (re-matcher m "move 10 from 2 to 9")]
    (re-find n))
  (:count (first (read-moves)))
  (move (read-stack-setting) 2 0 1))