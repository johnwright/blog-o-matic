(ns blog-o-matic.scrape
  (:require [net.cgrand.enlive-html :as html]))

(def base-url "http://www.lshift.net/blog/")

(defn blog-page-url [page-number]
  (str base-url "page/" page-number))

(defn fetch-html [url]
  (html/html-resource (java.net.URL. url)))

(defn fetch-post-links-in-page [page-number]
  (try
    (let [res  (fetch-html (blog-page-url page-number))]
      (map (comp :href :attrs)
           (html/select res [:div.blog.entry :> :h1 :> :a])))
    (catch java.io.FileNotFoundException http-404
      nil)))

(defn fetch-all-post-links []
  (apply concat
         (take-while (complement empty?)
                     (map fetch-post-links-in-page
                          (iterate inc 1)))))

(defn fetch-post-title-and-body [post-link]
  (let [res    (fetch-html post-link)
        title  (first (html/select res [:div.entry :h1 html/text-node]))
        body   (:content (first (html/select res [:div.entry :> :div])))]
    {:title title
     :body  body}))

(defn extract-text [res]
  (apply str
         (-> res
             (html/at #{[:pre] [:div] [:code] [:table]} nil)
             (html/select [:* :> html/text-node]))))

(defn fetch-posts []
  (map (comp extract-text :body fetch-post-title-and-body) (fetch-all-post-links)))

(defn fetch-titles []
  (map (comp :title fetch-post-title-and-body) (fetch-all-post-links)))

(def punctuation?
  (fnil (partial re-matches #"^\.|,|;|:|!|\?$")
        ""))

(defn remove-consecutive-punctuation
  ([tokens] (remove-consecutive-punctuation tokens nil))
  ([tokens reversed-result]
     (if (empty? tokens)
       (reverse reversed-result)
       (let [curr-tok               (first tokens)
             curr-tok-punctuation?  (punctuation? curr-tok)
             prev-tok-punctuation?  (punctuation? (first reversed-result))
             both-punctuation?      (and curr-tok-punctuation?
                                         prev-tok-punctuation?)
             new-result             (if both-punctuation?
                                      reversed-result
                                      (cons curr-tok reversed-result))]
         (recur (rest tokens) new-result)))))

(defn tokenise [text]
  (remove-consecutive-punctuation
   (re-seq #"[a-zA-Z][a-zA-Z'’-]*|\.|,|;|:|!|\?" text)))
