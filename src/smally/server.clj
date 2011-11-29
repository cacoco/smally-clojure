(ns smally.server
  (:use [compojure.core :only [defroutes]])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.adapter.jetty :as ring]
            [smally.controllers.main :as controller]
            [smally.views.layout :as layout]))

(defroutes routes
  controller/routes
  (route/files "/" {:root "public"})
  (route/resources "/" {:root "public"})
  (route/not-found (layout/four-oh-four)))

(def application (handler/site routes))

(defn start [port]
  (ring/run-jetty (var application) {:port (or port 8080) :join? false}))

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))]
    (start port)))

