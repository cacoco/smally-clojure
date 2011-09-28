(ns smally-clojure.views.common
  (:use noir.core
        hiccup.core
        hiccup.page-helpers))

(defpartial layout [& content]
            (html5
              [:head
               [:title "smal.ly"]
               (include-css "/css/reset.css")]
              [:body
               [:div#wrapper
                content]]))
