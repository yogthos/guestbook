(ns guestbook.core
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST]]))

(defn home []
  [:div.row
   [:div.span12
    [message-form]]])

(defn message-form []
  (let [fields (atom {})]
    (fn []
      [:div.content
       [:div.form-group
        [:p "Name:" (:name @fields)
         [:input.form-control
          {:type :text
           :name :name
           :on-change #(swap! fields assoc :name (-> % .-target .-value))
           :value (:name @fields)}]]
        [:p "Message:" (:message @fields)
         [:textarea.form-control
          {:rows 4
           :cols 50
           :name :message
           :value (:message @fields)
           :on-change #(swap! fields assoc :message (-> % .-target .-value))}]]
        [:input.btn.btn-primary {:type :submit
                                 :on-click #(send-message! fields)
                                 :value "comment"}]]])))

(defn send-message! [fields]
  (POST "/message"
        {:format :json
         :headers
         {"Accept" "application/transit+json"
          "x-csrf-token" (.-value (.getElementById js/document "token"))}
         :params @fields
         :handler #(.log js/console (str "Response:" %))
         :error-handler #(.error js/console (str "error:" %))}))

(reagent/render
 [home]
 (.getElementById js/document "content"))
