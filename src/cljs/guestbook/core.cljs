(ns guestbook.core
  (:require [reagent.core :as reagent :refer [atom]]))

(defn home []
  [:div.row
   [:div.span12
    [message-form]]])

(defn message-form []
  (let [fields (atom {})]
    (fn []
      [:div.content
       [:div.form-group
        [:p "Name:"
         [:input.form-control
          {:type :text
           :name :name
           :on-change #(swap! fields assoc :name (-> % .-target .-value))
           :value (:name @fields)}]]
        [:p "Message:"
         [:textarea.form-control
          {:rows 4
           :cols 50
           :name :message
           :value (:message @fields)
           :on-change #(swap! fields assoc :message (-> % .-target .-value))}]]
        [:input.btn.btn-primary {:type :submit :value "comment"}]]])))

(reagent/render
 [home]
 (.getElementById js/document "content"))
