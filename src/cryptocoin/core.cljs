(ns cryptocoin.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [cljsjs.bootstrap]))

(defui Cryptocoin
  Object
  (render [this]
    (dom/div nil "Cryptocoin")))

(def cryptocoin (om/factory Cryptocoin))

(js/ReactDOM.render (cryptocoin) (gdom/getElement "app"))

;;
;; TODO:
;; -  Manage css using sass or equivalent
;; 
;; - List the markets with the currency with the most activity listed first:
;; --"Currency" "%Increase: Minute, 15 Mins, 30 Mins, Hour, 8 Hours, 24 Hours"
;;



