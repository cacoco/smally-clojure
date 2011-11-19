(ns smally-clojure.views.smally
  (:require [smally-clojure.views.common :as common]
            [noir.validation :as validation]
            [noir.response :as response]
            [noir.options :as options]
            [noir.session :as session]
            [noir.server :as server]
            [clj-redis.client :as redis])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers))
		
(def ^{:private true} local-redis-url
				  "redis://127.0.0.1:6379")
		
(def db (redis/init {:url (or (get (System/getenv) "REDISTOGO_URL") local-redis-url)}))
		
(defpartial layout [& content]
		  (html5
		    [:head
		     [:title "smal.ly"]]
		    [:body
		     content]))

; default counter value
; will be used when there is no value in redis
(def counter 10000000)

; increase the given key value
; using redis/incr
(defn incr [name]
  (redis/incr db name))

; Looks up the next counter value in Redis
(defn next-val []
  (if (nil? (redis/get db "counter"))
    (redis/incrby db "counter" counter)
    (binding [counter (incr "counter")]))
	(eval counter))
	
(defn set-val
	"Sets the url mapped to the counter value in Redis"
	[counter-val #^String url]
		(redis/set db (str "url-" counter-val) (:url url))
		(Integer/toString counter-val 32))

(defn get-val
	"Expands the url-index and looks up the value in Redis"
	[url-index]
		(redis/get db (str "url-" (Integer/parseInt url-index 32))))
		
(defn get-name [handler]
	(fn [request]
		(session/put! :uri (str "http://" (:server-name request) ":" (:server-port request)))
		(handler request)))
		
(def init (server/add-middleware get-name))
			
(defpartial error-item [[first-error]]
	[:p.error first-error])
		
(defpartial url-fields [{:keys [url]}]
	(validation/on-error :url error-item)
	(html [:input {:type "text" :maxlength "2000" :name "url" :size "100" :value url}]))

(defn valid? [{:keys [url]}]
	(validation/rule (validation/min-length? url 5)
		[:url "Your URL MUST have 5 or more characters."])
	(not (validation/errors? :url)))
	
(defpage [:get "/ping"] [ ]
	"PONG")
	
(defpage [:get "/error"] [ ]
	{:status 500
	 :body "ZONKS! An error has occurred"})

(defpage "/" {:as url}
	(common/layout
		(form-to [:post "/"]
			(url-fields url)
			(submit-button "Shorten Url"))))
			
(defpage "/:id" {:keys [id]}
	(response/redirect (get-val id)))
				
(defpage [:post "/"] {:as url}	
	(if (valid? url)
		(response/json {:smally-url (str (session/get :uri) "/" (set-val (next-val) url))})
		(response/empty)))
