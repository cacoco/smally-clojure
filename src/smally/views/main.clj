(ns smally.views.main
  (:use [hiccup.core :only [html h]]
        [hiccup.page-helpers :only [doctype unordered-list]]
        [hiccup.form-helpers :only [form-to label text-field submit-button]])
  (:require [smally.views.layout :as layout]))

(defn url-form []
  [:div {:id "url-form" :class "sixteen columns alpha omega"}
   (form-to [:post "/"]
            (label "url" "URL to shorten:")
            (html [:input {:type "text" :maxlength "2000" :name "url" :size "100"}])
            (submit-button "Submit"))])

(defn index []
  (layout/common "smal.ly" (url-form)))