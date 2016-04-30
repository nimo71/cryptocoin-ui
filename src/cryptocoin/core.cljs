(ns cryptocoin.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [cljsjs.bootstrap]
            [cryptocoin.table :as table]))

(def markets (atom  {:hdgs ["Currency" "% Change"]
                     :rows     [["BTC" 0.01] 
                                ["ETH" 0.12]
                                ["GBP" 0.11]]}))

(def reconciler
  (om/reconciler {:state markets}))

(om/add-root! reconciler
  table/TableCondensed (gdom/getElement "app"))

;;
;; TODO:
;; -  Manage css using sass or equivalent
;; 
;; - List the markets with the currency with the most activity listed first:
;; --"Currency" "%Increase: Minute, 15 Mins, 30 Mins, Hour, 8 Hours, 24 Hours"
;;



