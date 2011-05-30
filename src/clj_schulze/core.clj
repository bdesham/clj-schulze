; clj-schulze
; A Clojure implementation of the Schulze voting method.
;
; https://github.com/bdesham/clj-schulze
;
; Copyright (c) 2011, Benjamin D. Esham.  This program is released under the MIT
; license, which can be found in the "License" section of the file README.md.

(ns clj-schulze.core
  (:use [clojure.set :only (difference)]))

;; # Utility functions

(defn flatten-sets
  "Like flatten, but pulls elements out of sets instead of sequences."
  [v]
  (filter (complement set?)
          (rest (tree-seq set? seq (set v)))))

;; # Validation, canonicalization, etc.

(defn validate-ballot
  "Make sure that the ballot is a vector; contains no duplicate entries,
  including in nested sets; and contains only entries which appear in the set of
  candidates."
  [ballot candidates]
  (and (vector? ballot)
       (when-let [fb (flatten-sets ballot)]
         (and (apply distinct? fb)
              (every? #(candidates %) fb)))))

(defn validate-ballots
  "Check every ballot in a sequence with validate-ballot."
  [ballots candidates]
  (every? #(validate-ballot % candidates)))

(defn validate-candidates
  "Make sure that the candidates set *is* a set and consists entirely of
  keywords."
  [candidates]
  (and (set? candidates)
       (every? keyword? candidates)))

(defn add-missing-candidates
  "If any valid candidates don't appear on the ballot, add them in a set at the
  end of the ballot. (Don't add an empty set if all of the valid candidates have
  already been listed.)"
  [ballot candidates]
  (let [extras (difference candidates (flatten-sets ballot))]
    (if (seq extras)
      (conj ballot extras)
      ballot)))

(defn canonical-ballot
  "Return the ballot with the following changes: all keywords not in a set are
  converted to one-element sets; any nested sets (?!) are flattened; empty sets
  are removed."
  [ballot]
  (map #(if (keyword? %)
          (set (vector %))
          (set (flatten-sets %)))
       (filter #(or (keyword %) (seq %)) ballot)))

(defn canonical-ballots
  "Call canonical-ballot for every ballot in a sequence."
  [ballots]
  (map canonical-ballot ballots))

(defn collapse-ballots
  "Return a map from each ballot to the number of times it occurs."
  [ballots]
  (frequencies ballots))

; vim: tw=80
; intended to be viewed with a window width of 108 columns
