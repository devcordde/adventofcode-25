(ns day04
  (:require [clojure.string :as str]))

(def neighbor-offsets
  [[+1 +1]
   [+1 +0]
   [+1 -1]
   [+0 +1]
   [+0 -1]
   [-1 -1]
   [-1 +0]
   [-1 +1]])

(defn get-neighbor-positions
  [x y]
  (map (fn [[a b]]
         [(+ a x) (+ b y)]) neighbor-offsets))

(defn in-range
  [start end n]
  (and (< n end) (>= n start)))

(defn neighbor-coordinates
  [grid x y]
  (let [width (count (first grid))
        height (count grid)]
    (->> (get-neighbor-positions x y)
         (filter (fn [[x y]]
                   (and (in-range 0 width x)
                        (in-range 0 height y)))))))

(defn neighbors
  [grid x y]
  (let [coordinates (neighbor-coordinates grid x y)]
    (map (fn [[x y]] (-> grid
                         (get y)
                         (get x))) coordinates)))

(defn all-coordinates
  [grid]
  (let [width (count (first grid))
        height (count grid)]
    (map #(map (fn [x] [x %]) (range width)) (range height))))

(defn should-remove-paper
  [grid x y]
  (and (= (get (get grid y) x) \@)
       (->> (neighbors grid x y)
            (filter #(= % \@))
            count
            (> 4))))

(defn remove-papers
  [input]
  (reduce (fn [[grid papers-removed] [x y]]
            (if (should-remove-paper input x y)
              [(assoc-in grid [y x] \.) (inc papers-removed)]
              [grid papers-removed]))
          [input 0]
          (->> (all-coordinates input)
               (apply concat))))

(defn main
  [_]
  (time (let [input
              (->> (slurp "input.txt")
                   str/split-lines
                   (map vec)
                   vec)
              papers-1 (second (remove-papers input))
              papers-2
              (loop [grid input
                     total-removed 0]
                (let [[grid removed-papers] (remove-papers grid)]
                  (if (= 0 removed-papers)
                    total-removed
                    (recur grid (+ total-removed removed-papers)))))]
          (println papers-1 papers-2))))

