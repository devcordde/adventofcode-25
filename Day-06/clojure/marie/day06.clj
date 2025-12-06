(ns day06
  (:require [clojure.string :as str]))

(defn parse-line
  [line]
  (str/split (str/trim line) #"\s+"))

(defn rotate
  [array]
  (let [width (count (first array))
        height (count array)]
    (map (fn [x] (map (fn [y] (get (get array y) x)) (range height))) (range width))))

(defn interpret
  [line]
  (map (fn [value]
         (cond
           (= value "+") '+
           (= value "*") '*
           :else (Integer/parseInt value))) line))

(defn collect-expressions
  [values]
  (loop [exprs []
         rest values]
    (if (empty? rest)
      exprs
      (let [[first-expr & expr-rest] (take-while #(not= % "") rest)
            operator (str (last first-expr))
            first-value (str/trim (subs first-expr 0 (dec (count first-expr))))
            new-expr (cons first-value expr-rest)]
        (recur (conj exprs (cons operator new-expr)) (drop (+ 2 (count expr-rest)) rest))))))

(defn main
  [_]
  (time (let [input (str/split-lines (slurp "input.txt"))
              part-1 (->> input
                          (map parse-line)
                          vec
                          rotate
                          (map (comp eval reverse interpret))
                          (reduce +))
              part-2 (->> input
                          vec
                          rotate
                          (map (comp str/trim (partial apply str)))
                          collect-expressions
                          (map (comp eval interpret))
                          (reduce +))]
          (println part-1 part-2))))

