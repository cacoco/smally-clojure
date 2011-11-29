(ns smally.views.layout
  (:use [hiccup.core :ony [html]]
        [hiccup.page-helpers :only [doctype include-css]]))

(defn common [title & body]
  (html
    (doctype :html5)
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
     [:title title]
     (include-css "/css/reset.css")
     (include-css "http://fonts.googleapis.com/css?family=Sigmar+One&v1")]
   [:body
    [:div {:id "header"}]
    [:div {:id "content" :class "container"} body]]))

(defn four-oh-four []
  (common "Page Not Found"
    [:div {:id "four-oh-four"}
     [:img {:src "/images/404.gif"}]]))