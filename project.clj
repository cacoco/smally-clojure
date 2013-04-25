(defproject smally "0.0.9-SNAPSHOT"
    :description "A simple url shortener written in clojure+redis using the compojure."
    :dependencies [[org.clojure/clojure "1.3.0"]
       [clj-redis "0.0.12"]
       [ring/ring-jetty-adapter "0.3.10"]
       [compojure "0.6.4"]
       [hiccup "0.3.6"]
       [clj-json "0.4.3"]])