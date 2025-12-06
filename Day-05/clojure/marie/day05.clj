(ns day05
  (:require [clojure.string :as str]))

(defn parse-range
  [text]
  (->> (str/split text #"-")
       (map Long/parseLong)))

(defn parse-input
  [text]
  (-> (->> (str/split-lines text)
           (reduce (fn [state line]
                     (cond
                       (:read-available state) (update state :available-ids #(conj % (Long/parseLong line)))
                       (empty? line) (assoc state :read-available true)
                       :else (update state :fresh-ids #(conj % (parse-range line)))))
                   {:fresh-ids #{}
                    :available-ids []
                    :read-available false}))
      (dissoc :read-available)))

(defn in-range
  [[start end] value]
  (and (>= value start) (<= value end)))

(defn compact-range
  [[start-1 end-1] [start-2 end-2]]
  (if (or (in-range [start-1 end-1] start-2)
          (in-range [start-2 end-2] start-1))
    [(min start-1 start-2) (max end-1 end-2)]
    nil))

(defn compact-ranges
  [ranges]
  (loop [ranges ranges]
    (let [compacted (reduce (fn [result range]
                              (set (map (fn [r] (or (compact-range range r) r)) result)))
                            ranges ranges)]
      (if (not= compacted ranges)
        (recur compacted)
        compacted))))

(defn main
  [_]
  (time (let [{fresh-ids :fresh-ids
               available-ids :available-ids} (parse-input (slurp "input.txt"))
              compacted (compact-ranges fresh-ids)
              fresh-1 (count (filter (fn [id] (some #(in-range % id) compacted)) available-ids))
              fresh-2 (reduce + (map (fn [[a b]] (+ 1 (- b a))) compacted))]
          (println fresh-1 fresh-2))))

