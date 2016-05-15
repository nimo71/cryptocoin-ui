(ns cryptoquants.markets
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [cljs.core.async :refer [chan put! alts! close!]]
            [cryptoquants.poloniex :as poloniex]
            [cryptoquants.table :as table]))

(defui Markets
  static om/IQuery
  (query [this]
    [:markets/title :markets/content])
  Object
  (componentWillMount [this]
    (let [ticker-chan (poloniex/start)
          kill-chan   (chan)]
      (om/set-state! this {:kill-chan kill-chan})
      (go-loop [[market c] (alts! [ticker-chan kill-chan])]
        (if (not= c kill-chan)
          (let [reconciler (om/get-reconciler this)] 
            (om/merge! reconciler market)
            (om/force-root-render! reconciler)
            (recur (alts! [ticker-chan kill-chan])))
          (do
            (poloniex/stop)
            (close! kill-chan))))))
  
  (componentWillUnmount [this]
    (let [kill-chan (:kill-chan (om/get-state this))
          {:keys [markets/content]} (om/props this)
          app-state (om/app-state (om/get-reconciler this))]
      (put! kill-chan :quit)
      (swap! app-state assoc-in [:app/markets :markets/content :table :rows] {})))

  (render [this]
    (let [{:keys [markets/title markets/content]} (om/props this)]
      (dom/div #js {:className "col-md-12"}
        (dom/h1 nil title)
        (table/market-table content)))))

