(defproject smally-clojure "0.1.0-SNAPSHOT"
            :description "A simple url shortener written in clojure+redis using the noir framework."
            :dependencies [[org.clojure/clojure "1.3.0"]
                           [clj-redis "0.0.12"]
                           [noir "1.2.0"]]
            :main smally-clojure.server)

