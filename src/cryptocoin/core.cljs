(ns cryptocoin.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(defui Cryptocoin
  Object
  (render [this]
    (dom/div nil "Cryptocoin")))

(def cryptocoin (om/factory Cryptocoin))

(js/ReactDOM.render (cryptocoin) (gdom/getElement "app"))
