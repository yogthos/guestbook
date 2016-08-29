(ns guestbook.ws
  (:require [cognitect.transit :as t]))

(defonce ws-chan (atom nil))

(def json-reader (t/reader :json))
(def json-writer (t/writer :json))

(defn receive-message! [handler]
  (fn [msg]
    (->> msg .-data (t/read json-reader) handler)))

(defn send-message! [msg]
  (if @ws-chan
    (->> msg (t/write json-writer) (.send @ws-chan))
    (throw (js/Error. "WebSocket is not available!"))))

(defn connect! [url receive-handler]
  (if-let [chan (js/WebSocket. url)]
    (do
      (set! (.-onmessage chan) (receive-message! receive-handler))
      (reset! ws-chan chan))
    (throw (js/Error. "WebSocket connection failed!"))))
