(ns day02
  (:require [clojure.string :as str]
            [clojure.math :as math]))

(defn parse-input
  [text]
  (map (comp (partial map (comp Long/parseLong str/trim)) #(str/split % #"-")) (str/split text #",")))

(defn to-range
  [[a b]]
  (range a (+ 1 b)))

(defn digits
  [num n]
  (let [factor (long (math/pow 10 n))]
    (loop [result []
           number num]
      (if (> number 0)
        (recur (cons (mod number factor) result) (quot number factor))
        result))))

(defn is-invalid-id
  [id]
  (let [digit-count (count (digits id 1))]
    (if (odd? digit-count) false
        (apply = (digits id (quot digit-count 2))))))

(defn is-invalid-id2
  [id]
  (let [digit-count (count (digits id 1))
        is-dividable-by-digit-count #(= 0 (rem digit-count %))
        n (filter is-dividable-by-digit-count (range 1 digit-count))]
    (some identity (map #(apply = %) (map #(digits id %) n)))))

(defn main
  [_]
  (time (let [ranges (flatten (map to-range (parse-input (slurp "input.txt"))))
              invalid-id-sum (reduce + (filter is-invalid-id ranges))
              second-invalid-id-sum (reduce + (filter is-invalid-id2 ranges))]
          (println invalid-id-sum second-invalid-id-sum))))

