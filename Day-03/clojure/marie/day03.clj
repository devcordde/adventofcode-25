(ns day03
  (:require [clojure.string :as str]))

(defn numbers-starting-at
  [n pack]
  (map #(drop-while (partial not= %) pack) n))

(defn find-numbers
  [amount pack]
  (if (= amount 0)
    []
    (take amount
          (let [[[highest & rest]] (->> pack
                                        (numbers-starting-at (reverse (range 0 10)))
                                        (filter #(>= (count %) amount)))]
            (cons highest (find-numbers (dec amount) rest))))))

(defn to-number
  [pack]
  (Long/parseLong (apply str pack)))

(defn highest-joltage
  [input batteries]
  (reduce + (map #(to-number (find-numbers batteries %)) input)))

(defn char->int
  [c]
  (- (int c) (int \0)))

(def to-number-list (partial map char->int))

(defn main
  [_]
  (time (let [input (->> (slurp "input.txt")
                         str/split-lines
                         (map to-number-list))
              joltage-1 (highest-joltage input 2)
              joltage-2 (highest-joltage input 12)]
          (println joltage-1 joltage-2))))

