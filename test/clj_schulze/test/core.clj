(ns clj-schulze.test.core
  (:use [clj-schulze.core] :reload)
  (:use [clojure.test]))

(deftest ballot-validation
         (let [candidates #{:a :b :c :d}]
           (is (false? (valid-ballot? '(:a :b :c :d) candidates)))
           (is (false? (valid-ballot? [:a :b :c :d :c] candidates)))
           (is (false? (valid-ballot? [:a #{:b :c} :d :c] candidates)))
           (is (false? (valid-ballot? [:a :b :c #{:d :c}] candidates)))
           (is (false? (valid-ballot? [:a :b :e :c :d :c] candidates)))
           (is (false? (valid-ballot? [:a :b #{:c :d :e}] candidates)))
           (is (true? (valid-ballot? [:a :b :c :d] candidates)))
           (is (true? (valid-ballot? [:a :b #{:c :d}] candidates)))
           (is (true? (valid-ballot? [#{:a :b :c :d}] candidates)))
           (is (true? (valid-ballot? [:a #{:b :c} #{} #{#{}} :d] candidates)))))

(deftest candidate-validation
         (is (false? (valid-candidates? '(:a :b :c :d))))
         (is (false? (valid-candidates? #{:a :b :c :d "e"})))
         (is (false? (valid-candidates? #{:a :b :c :d #{:e}})))
         (is (true? (valid-candidates? #{:a :b :c :d}))))

(deftest canonical-ballots
         (is (= (canonical-ballot [:a :b :c :d :e])
                [#{:a} #{:b} #{:c} #{:d} #{:e}]))
         (is (= (canonical-ballot [:a #{:b :c} :d :e])
                [#{:a} #{:c :b} #{:d} #{:e}]))
         (is (= (canonical-ballot [:a #{:b :c} #{:d} :e])
                [#{:a} #{:c :b} #{:d} #{:e}]))
         (is (= (canonical-ballot [:a #{:b :c} #{} :d :e])
                [#{:a} #{:c :b} #{:d} #{:e}]))
         (is (= (canonical-ballot [:a #{:b :c} #{} :d #{#{#{}} #{}} :e])
                [#{:a} #{:c :b} #{:d} #{:e}]))
         (is (= (canonical-ballot [#{:a :b :c :d :e}])
                [#{:a :b :c :d :e}])))

(deftest valid-and-canonical
         (is (= (validate-and-canonicalize [[:a :b :c :d],
                                            [:a #{:b :c} :d],
                                            [#{:b :e} :c],
                                            [:b :a :e]]
                                           #{:a :b :c :d :e})
                {[#{:a} #{:b} #{:c} #{:d} #{:e}] 1,
                 [#{:a} #{:c :b} #{:d} #{:e}] 1,
                 [#{:b :e} #{:c} #{:a :d}] 1,
                 [#{:b} #{:a} #{:e} #{:c :d}] 1})))
