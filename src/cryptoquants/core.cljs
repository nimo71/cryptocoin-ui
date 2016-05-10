(ns cryptoquants.core
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [cljsjs.bootstrap]
            [cljsjs.autobahn]
            [cryptoquants.table :as table]
            [cryptoquants.poloniex :as poloniex]
            [cljs.core.async :refer [<!]]))

(enable-console-print!)

(def markets (atom  {:table {:hdgs ["Currency Pair" "Last" "Lowest Ask" "Highest Bid" "% Change" "Base Volume" "Quote Volume" "24hr High" "24hr Low"]
                             :rows {}}}))

(defn read [{:keys [state] :as env} key params]
  (let [st @state]
    (if-let [[_ value] (find st key)]
      {:value value}
      {:value "not-found"})))

(defn mutate [{:keys [state] :as env} key params]
  (println "mutate, key: " key ", params: " params)
  (if (= :table key)
    {:value {:keys [:rows]}
     :action #(swap! state assoc-in [:table :rows (key params)] (vals params))}
    {:value :not-found}))

(defn merge-market [state market] 
  (let  [currency-pair   (get market "currencyPair")
         currency-values (map market ["currencyPair" "last" "lowestAsk" "highestBid" "percentChange" "baseVolume" "quoteVolume" "24hrHigh" "24hrLow"])
         frozen?         (= 1 (get market "isFrozen"))]
    (assoc-in state 
              [:table :rows currency-pair] 
              {:isFrozen frozen? :market-values (vec currency-values)})))

(def reconciler
  (om/reconciler 
    {:state markets
     :parser (om/parser {:read read :mutate mutate})
     :merge-tree merge-market}))

(om/add-root! reconciler
  table/MarketTable (gdom/getElement "app"))

(defn update-market [market]
  (om/merge! reconciler market)
  (om/force-root-render! reconciler))

(let [ticker-chan (poloniex/start)]
  (go-loop [market (<! ticker-chan)]
    (update-market market)
    (recur (<! ticker-chan))))


;; TODO:
;; -  Manage css using sass or equivalent
;; 
;; - List the markets with the currency with the most activity listed first:
;; --"Currency" "%Increase: Minute, 15 Mins, 30 Mins, Hour, 8 Hours, 24 Hours"
;;



