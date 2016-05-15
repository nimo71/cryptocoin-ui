(ns cryptoquants.poloniex
  (:require [cljsjs.autobahn]
            [cljs.core.async :refer [put! chan]]))

(def autobahn js/autobahn)

(def wsuri "wss://api.poloniex.com")

(def conn
  (autobahn.Connection. (clj->js {:url   wsuri
                                  :realm "realm1"})))
(def ticker-chan (chan))

(def conn-opened
  (fn [session]
    (let [ticker-event (fn [args, kwargs]
                         (let [tick-labels ["currencyPair" "last" "lowestAsk" "highestBid" "percentChange" "baseVolume" "quoteVolume" "isFrozen" "24hrHigh" "24hrLow"]
                               tick-event (zipmap tick-labels args)]
                           (put! ticker-chan tick-event)))]
      (.subscribe session "ticker" ticker-event))))

(def conn-closed
  (fn [] (println "Websocket connection closed")))

(defn start []
  (println "Starting poloniex ticker feed...")
  (set! (.-onopen conn) conn-opened)
  (set! (.-onclose conn) conn-closed)
  (when (not (.-isOpen conn))
    (.open conn))
  ticker-chan)

(defn stop []
  (println "Stopping poloinex ticker feed...")
  (.close conn))

;; should the ticker chan be closed? 
