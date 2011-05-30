(ns clj-schulze.core
  (:use [clojure.set :only (difference)]))

;; # Utility functions

(defn flatten-sets [v]
  "Like flatten, but pulls elements out of sets instead of sequences."
  (filter (complement set?)
          (rest (tree-seq set? seq (set v)))))

;; # Validation, canonicalization, etc.

(defn validate-ballot [ballot candidates]
  "Make sure that the ballot is a vector; contains no duplicate entries,
  including in nested sets; and contains only entries which appear in the set of
  candidates."
  (and (vector? ballot)
       (when-let [fb (flatten-sets ballot)]
         (and (apply distinct? fb)
              (every? #(candidates %) fb)))))

(defn validate-ballots [ballots candidates]
  "Check every ballot in a sequence with validate-ballot."
  (every? #(validate-ballot % candidates)))

(defn validate-candidates [candidates]
  "Make sure that the candidates set *is* a set and consists entirely of
  keywords."
  (and (set? candidates)
       (every? keyword? candidates)))

(defn add-missing-candidates [ballot candidates]
  "If any valid candidates don't appear on the ballot, add them in a set at the
  end of the ballot."
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
  "Call canonical-ballot for every ballot in a sequence."
  (map canonical-ballot ballots))

; vim: tw=80
; intended to be viewed with a window width of 108 columns
