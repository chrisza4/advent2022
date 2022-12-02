(ns day1clj.day2
  [:require [clojure.string :as str]])

; rps = rock-paper-scissors

(defn to-rps-symbol-part-1 [str]
  (case str
    "A" :rock
    "B" :paper
    "C" :scissors
    "X" :rock
    "Y" :paper
    "Z" :scissors
    :error))

(defn to-rps-symbol-part-2 [str]
  (case str
    "A" :rock
    "B" :paper
    "C" :scissors
    "X" :lose
    "Y" :draw
    "Z" :win
    (throw (Exception. "Unknown symbol"))))

(defn part2-to-concrete-rps [input]
  (case input
    [:rock :win] [:rock :paper]
    [:rock :draw] [:rock :rock]
    [:rock :lose] [:rock :scissors]
    [:paper :win] [:paper :scissors]
    [:paper :draw] [:paper :paper]
    [:paper :lose] [:paper :rock]
    [:scissors :win] [:scissors :rock]
    [:scissors :draw] [:scissors :scissors]
    [:scissors :lose] [:scissors :paper]
    (throw (Exception. "Unknown symbol"))))

(defn score-from-win [input]
  (case input
    [:rock :paper] 6
    [:rock :scissors] 0
    [:paper :rock] 0
    [:paper :scissors] 6
    [:scissors :rock] 6
    [:scissors :paper] 0
    3))

(defn score-from-chosen [input]
  (let [chosen (last input)]
    (case chosen
      :rock 1
      :paper 2
      :scissors 3
      :error)))

(defn read-and-format-input-1 []
  (let [raw-input (slurp "inputday2.txt")]
    (->> (str/split raw-input #"(\n)")
         (map #(->> (str/split % #" ")
                    (mapv to-rps-symbol-part-1))))))

(defn read-and-format-input-2 []
  (let [raw-input (slurp "inputday2.txt")]
    (->> (str/split raw-input #"(\n)")
         (map #(->> (str/split % #" ")
                    (mapv to-rps-symbol-part-2)
                    (part2-to-concrete-rps))))))

(defn main1 []
  (->> (read-and-format-input-1)
       (map #(+ (score-from-chosen %) (score-from-win %)))
       (reduce +)))

(defn main2 []
  (->> (read-and-format-input-2)
       (map #(+ (score-from-chosen %) (score-from-win %)))
       (reduce +)))


(comment
  (score-from-win [:rock :paper])
  (last (read-and-format-input-2)))
