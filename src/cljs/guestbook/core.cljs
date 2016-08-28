(ns guestbook.core
  (:require [reagent.core :as reagent :refer [atom]]))

(defn home []
  [:h2 "Hello, Reagent"])

(reagent/render
 [home]
 (.getElementById js/document "content"))
