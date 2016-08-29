(ns guestbook.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [guestbook.layout :refer [error-page]]
            [guestbook.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [guestbook.env :refer [defaults]]
            [mount.core :as mount]
            [guestbook.middleware :as middleware]
            [guestbook.routes.ws :refer [websocket-routes]]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
   #'websocket-routes
   (wrap-routes #'home-routes middleware/wrap-csrf)
   (route/not-found
    (:body
     (error-page {:status 404
                  :title "Page not found"})))))

(defn app [] (middleware/wrap-base #'app-routes))
