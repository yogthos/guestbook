(ns guestbook.routes.home
  (:require
   [bouncer.core :as b]
   [bouncer.validators :as v]
   [guestbook.layout :as layout]
   [compojure.core :refer [defroutes GET POST]]
   [ring.util.http-response :as response]
   [guestbook.db.core :as db]))

(defn validate-message [params]
  (first
   (b/validate
    params
    :name v/required
    :message [v/required [v/min-count 10]])))

(defn home-page []
  (layout/render "home.html"))

(defn save-message! [{:keys [params]}]
  (if-let [errors (validate-message params)]
    (response/bad-request {:errors errors})
    (try
      (db/save-message!
       (assoc params :timestamp (java.util.Date.)))
      (response/ok {:status :ok})
      (catch Exception e
        (response/internal-server-error
         {:errors {:server-error ["Failed to save message!"]}})))))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/messages" [] (response/ok (db/get-messages)))
  (POST "/message" req (save-message! req))
  (GET "/about" [] (about-page)))
