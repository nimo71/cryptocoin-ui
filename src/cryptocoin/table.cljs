(ns cryptocoin.table
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

  ;; "Render a bootstrap table from a map of the form:
  ;;    {:headings [h1 h2 ... hn] 
  ;;     :rows [[a1 a2 ... an]
  ;;            [b1 b2 ... bn]
  ;;            [c1 c2 ... cn]]}"
(defui TableCondensed
  static om/IQuery
  (query [this] [:table])
  Object
  (render [this]
    (let [{:keys [table]} (om/props this)
          hdgs            (:hdgs table)
          rows            (:rows table)]
      (dom/div #js {:className "col-md-12"}
        (dom/table #js {:className "table table-condensed"}
          (dom/thead nil
            (dom/tr nil
              (map #(dom/th nil %) hdgs)))
          (dom/tbody nil
            (map 
              (fn [row] 
                (dom/tr nil 
                  (map (fn [col] (dom/td nil col)) row))) 
              (vals rows))))))))

(def table-condensed (om/factory TableCondensed))
