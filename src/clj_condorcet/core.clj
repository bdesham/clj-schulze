(ns clj-condorcet.core)

(defn flatten-sets [v]
  "Like flatten, but also pulls elements out of sets. (Sets which are not
  top-level in the input vector are left untouched.)"
  (flatten (map #(if (set? %)
                   (seq %)
                   %)
                v)))

(defn validate-ballot [ballot candidates]
  (and (vector? ballot)
       (when-let [fb (flatten-sets ballot)]
         (and (distinct? fb)
              (every? #(candidates %) fb)))))

(defn validate-candidates [candidates]
  (and (set? candidates)
       (every? keyword? candidates)))

; vim: tw=80
; intended to be viewed with a window width of 108 columns
