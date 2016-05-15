(ns cryptoquants.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [cljsjs.bootstrap]
            [cryptoquants.markets :as markets]
            [cryptoquants.about :as about]))

(enable-console-print!)

(def app-state (atom {
                      :app/route   '[:app/markets _]
                      :app/markets {:markets/title "Poloniex Markets"
                                    :markets/content {:table {:hdgs ["Currency Pair" "Last" "Lowest Ask" "Highest Bid" "% Change" "Base Volume" "Quote Volume" "24hr High" "24hr Low"]
                                                              :rows {}}}}
                      :app/about   {:about/title "About CryptoQuants"
                                    :about/content "Some blurb about CryptoQuants"}}))

(def route->component
  {:app/markets markets/Markets
   :app/about about/About})

(def route->factory
  (zipmap (keys route->component)
    (map om/factory (vals route->component))))

(defn- change-route [c route e]
  (.preventDefault e)
  (om/transact! c `[(~'change/route! {:route ~route})]))

(defui Root
  static om/IQuery
  (query [this]
    [:app/route
     {:route/data (zipmap (keys route->component)
                    (map om/get-query (vals route->component)))}])
  Object
  (render [this]
    (let [{:keys [app/route route/data]} (om/props this)]
      (dom/div nil 
        (dom/nav #js {:id "nav" :className "navbar navbar-inverse navbar-fixed-top"}
          (dom/div #js {:className "container"}
            (dom/div #js {:className "navbar-header"}
              (dom/button #js {:type "button" :className "navbar-toggle collapsed" 
                              :data-toggle "collapse" :data-target "#navbar" 
                              :aria-expanded "false" :aria-controls "navbar"}
                (dom/span #js {:className "sr-only"} "Toggle navigation")
                (dom/span #js {:className "icon-bar"})
                (dom/span #js {:className "icon-bar"})
                (dom/span #js {:className "icon-bar"}))
              (dom/a #js {:className "navbar-brand" :href "#"} "CryptoQuants"))
            (dom/div #js {:id "navbar" :className "navbar-collapse collapse"}
              (dom/ul #js {:className "nav navbar-nav"}
                (dom/li #js {:className (when (= (first route) :app/markets) "active")} 
                  (dom/a #js {:href "#"
                              :onClick #(change-route this '[:app/markets _] %)} "Markets"))
                (dom/li #js {:className (when (= (first route) :app/about) "active")} 
                  (dom/a #js {:href "#about"
                              :onClick #(change-route this '[:app/about _] %)} "About"))))))
       ((route->factory (first route)) data)))))

(defmulti read om/dispatch)
(defmulti mutate om/dispatch)

(defmethod read :route/data
  [{:keys [state query]} k _]
  (let [st @state
        route (get st :app/route)
        route (cond-> route (= (second route) '_) pop)]
    ;; since the route is an ident, it could also be 
    ;; passed as the second argument to 'db->tree' if 
    ;; our data was normalized
    {:value (get-in st route)}))

(defmethod read :app/route
  [{:keys [state query]} k _]
  (let [st @state]
    {:value (get st k)}))

(defmethod mutate 'change/route!
  [{:keys [state]} _ {:keys [route]}]
  {:value {:keys [:app/route]}
   :action #(swap! state assoc :app/route route)})

(defn merge-market [state market] 
  (let  [currency-pair   (get market "currencyPair")
         currency-values (map market ["currencyPair" "last" "lowestAsk" "highestBid" "percentChange" "baseVolume" "quoteVolume" "24hrHigh" "24hrLow"])
         frozen?         (= 1 (get market "isFrozen"))]
    (assoc-in state 
              [:app/markets :markets/content :table :rows currency-pair] 
              {:isFrozen frozen? :market-values (vec currency-values)})))

(def reconciler
  (om/reconciler 
    {:state app-state
     :parser (om/parser {:read read :mutate mutate})
     :merge-tree merge-market}))

(om/add-root! reconciler
  Root (gdom/getElement "app"))

