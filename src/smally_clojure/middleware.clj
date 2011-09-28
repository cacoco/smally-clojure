(ns smally-clojure.middleware
  (:require [noir.response :as response]
			[noir.options :as options]
			[noir.server :as server]
			[noir.session :as session])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
		hiccup.form-helpers))
		
(defn store-server-name
	"Stores the server name in the request into the session."
	[handler]
	(fn [request]
		(println request)
			(session/put! "server-name" "http://localhost:8080")))