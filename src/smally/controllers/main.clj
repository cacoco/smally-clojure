(ns smally.controllers.main
  (:use [compojure.core :only [defroutes GET POST]])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [clj-json.core :as json]
            [clj-redis.client :as redis]
            [smally.views.main :as view]
            [smally.views.layout :as layout]))

; default counter value -- will be used when there is no value in redis
(def counter (atom 10000000))
(def ^{:private true} local-redis-url "redis://127.0.0.1:6379")
(def db (redis/init {:url (or (get (System/getenv) "REDISTOGO_URL") local-redis-url)}))

(defn log [data]
  (prn (merge {:ns "smal.ly"} data)))

; increase the given key value
; using redis/incr
(defn incr [name]
  (redis/incr db name))

; Looks up the next counter value in Redis
(defn next-val []
  (if (nil? (redis/get db "counter"))
    (redis/incrby db "counter" counter)
    (swap! counter (fn [counter] (incr "counter"))))
  (log {:fn "next-val" :counter @counter}))

; Sets the url mapped to the counter value in Redis
(defn set-val [#^String url]
  (next-val)
  (redis/set db (str "url-" @counter) url)
  (Integer/toString @counter 32))

; Expands the url-index and looks up the value in Redis
(defn get-val [url-index]
  (try
    (redis/get db (str "url-" (Integer/parseInt url-index 32)))
    (catch Exception _ nil)))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defn index []
  (view/index))

(defn redirect [id]
  (let [path (get-val id)]
    (if (nil? path)
      (layout/four-oh-four)
      (ring/redirect path))))

(defn respond [referer url]
  (let [result (set-val url)]
    (log {:fn "respond" :result result})
    (json-response {:smally-url (str referer result)})))

(defn shorten [headers params]
  (let [url (:url params)
        referer (get headers "referer")]
    (log {:fn "shorten" :headers headers :url url})
    (when-not (str/blank? url)
      (respond referer url))))

(defroutes routes
  (GET  "/" [] (index))
  (POST "/" {params :params
             headers :headers} (shorten headers params))
  (GET  "/:id" [id] (redirect id)))