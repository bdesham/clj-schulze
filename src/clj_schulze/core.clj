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

(defn valid-ballot?
  "Make sure that the ballot is a vector; contains no duplicate entries,
  including in nested sets; and contains only entries which appear in the set of
  candidates."
  [ballot candidates]
  (and (vector? ballot)
       (when-let [fb (flatten-sets ballot)]
         (and (apply distinct? fb)
              (every? #(candidates %) fb)))))

(defn valid-ballots?
  "Check every ballot in a sequence with validate-ballot."
  [ballots candidates]
  (every? #(valid-ballot? % candidates) ballots))

(defn valid-candidates?
  "Make sure that the candidates set *is* a set and consists entirely of
  keywords."
  [candidates]
  (and (set? candidates)
       (every? keyword? candidates)))

(defn add-missing-candidates
  "If any valid candidates don't appear on the ballot, add them in a set at the
  end of the ballot. (This adds an empty set if all of the valid candidates have
  already been listed, but that's taken care of by canonical-ballot.)"
  [ballot candidates]
  (conj ballot (difference candidates (flatten-sets ballot))))

(defn canonical-element
  "Return a ballot element, converted to canonical form. This means that
  keywords are wrapped in sets and sets are flattened."
  [element]
  (if (keyword? element)
    (set (vector element))
    (set (flatten-sets element))))

(defn valid-element?
  "Make sure that a ballot element is either a keyword or a set. In the latter
  case, make sure that the set isn't something daft like #{#{}} or #{#{#{}}}."
  [element]
  (or (keyword element)
      (and (seq element)
           (seq (flatten-sets element)))))

(defn canonical-ballot
  "Return the ballot with the following changes: all keywords not in a set are
  converted to one-element sets; any nested sets (?!) are flattened; empty sets
  are removed."
  [ballot]
  (map canonical-element (filter valid-element? ballot)))

(defn validate-and-canonicalize
  "Takes a vector of ballots and a set of candidates. Validates the candidates
  set. Validates each ballot, adds any missing candidates, and converts it to
  canonical form. Ballots are passed through `frequencies` on their way out. If
  the candidates or the ballots are not valid, throws an exception."
  [ballots candidates]
  (when-not (valid-candidates? candidates)
    (throw (Exception. "Candidates set is not valid")))
  (when-not (valid-ballots? ballots candidates)
    (throw (Exception. "Ballots vector is not valid")))
  (frequencies
    (map (comp canonical-ballot #(add-missing-candidates % candidates))
         ballots)))

; vim: tw=80
; intended to be viewed with a window width of 108 columns
