(ns clj-condorcet.core
  (:use [clojure.set :only (difference)]))

(defn flatten-sets [v]
  "Like flatten, but pulls elements out of sets instead of sequences."
  (filter (complement set?)
          (rest (tree-seq set? seq (set v)))))

(defn validate-ballot [ballot candidates]
  (and (vector? ballot)
       (when-let [fb (flatten-sets ballot)]
         (and (apply distinct? fb)
              (every? #(candidates %) fb)))))

(defn validate-ballots [ballots candidates]
  (every? #(validate-ballot % candidates)))

(defn validate-candidates [candidates]
  (and (set? candidates)
       (every? keyword? candidates)))

(defn add-missing-candidates [ballot candidates]
  (let [extras (difference candidates (flatten-sets ballot))]
    (if (not (empty? extras))
      (conj ballot extras)
      ballot)))

(defn canonical-ballot [ballot]
  "Return the ballot with the following changes: all keywords not in a set are
  converted to one-element sets; any nested sets (?!) are flattened; empty sets
  are removed."
  (map #(if (keyword? %)
          (set (vector %))
          (set (flatten-sets %)))
       (filter #(or (keyword %) (seq %)) ballot)))

(defn canonical-ballots [ballots]
  (map canonical-ballot ballots))

; vim: tw=80
; intended to be viewed with a window width of 108 columns
