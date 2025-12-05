(ns day01
  (:require [clojure.string :as str]))

(defn process
  [current direction amount]
  (let [r (map #(mod % 100) (if (= direction "R")
                              (reverse (range (+ 1 current) (+ current amount 1)))
                              (range (- current amount) current)))]
    (list (count (filter #(= 0 %) r)) (first r))))

(defn parse-input
  [line]
  (list (subs line 0 1) (Integer/parseInt (subs line 1))))

(defn main
  [_]
  (let [input (map parse-input (str/split-lines (slurp "input.txt")))
        result (reduce (fn [state [direction amount]]
                         (let [[zeros next] (process (:value state) direction amount)]
                           (-> state
                               (assoc :value next)
                               (update :rotations-hitting-zero #(+ zeros %))
                               (update :zeros (if (= next 0) inc identity)))))
                       {:value 50
                        :zeros 0
                        :rotations-hitting-zero 0}
                       input)]
    (println (:zeros result) (:rotations-hitting-zero result))))

