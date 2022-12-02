(ns day1clj.core
  [:require [clojure.string :as str]])


(defn str-to-int [i]
  (Integer/parseInt i))

(defn read-and-format-input []
  (let [raw-input (slurp "input.txt")]
    (->> (str/split raw-input #"(\n\n)")
         (map #(->> (str/split % #"\n")
                    (map str-to-int))))))

(defn sum-calories-each-elves [input]
  (map #(reduce + %) input))

(defn main1
  []
  (->> (read-and-format-input)
       (sum-calories-each-elves)
       (apply max)))

(defn main2
  []
  (->> (read-and-format-input)
       (sum-calories-each-elves)
       (sort)
       (reverse)
       (take 3)
       (reduce +)))


(comment)
