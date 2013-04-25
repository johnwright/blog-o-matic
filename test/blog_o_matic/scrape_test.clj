(ns blog-o-matic.scrape-test
  (:use [blog-o-matic.scrape]
        [clojure.test]))

(deftest removes-consecutive-punctuation
  (is (= ["foo"]
         (remove-consecutive-punctuation ["foo"])))
  (is (= ["foo" "."]
         (remove-consecutive-punctuation ["foo" "." "!"])))
  (is (= ["foo" "."]
         (remove-consecutive-punctuation ["foo" "." "!" ","])))
  (is (= ["foo" "." "bar"]
         (remove-consecutive-punctuation ["foo" "." "," "!" "?" "bar"]))))
