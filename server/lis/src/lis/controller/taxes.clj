(ns lis.controller.taxes
  (:require [lis.module.mongo :as module]))

(defn round [num precision]
  (Double/parseDouble (format (str "%." precision "f") (+ num 0.0))))

(defn- income-tax-filter [taxes-related]
  (fn [rate]
    (or 
     (and 
      (not= (:taxClass rate) "个人所得税") 
      (not= (:taxClass rate) "企业所得税"))
     (= (:taxClass rate) (:income-tax taxes-related)))))

(defn- city-tax-filter [taxes-related]
  (fn [append-tax-rate]
    (or 
     (not= (:taxClass append-tax-rate) "城建税")
     (= (:subclass append-tax-rate) (:region-type taxes-related)))))

(defn get-taxes-related [doc]
  (assoc {} 
    :tax-object (:taxObject doc)
    :income-tax (if (> (count (:taxpayer doc)) 4)
                  "企业所得税" 
                  "个人所得税")
    :region-type (let [region (:region doc)]
                   (subs region (dec (count region))))))

(defn query-rate [taxes-related rate]
  (filter 
   (income-tax-filter taxes-related) 
   (map #(if (:append-tax %)
           (update-in % [:append-tax]
                      (fn [x] (filter (city-tax-filter taxes-related) x))) %) 
        (:rate rate))))
         
(defn query-taxes [taxes-related rate taxBasis]
  (let [f-rate (query-rate taxes-related rate)]
    (map #(assoc % :taxes (round (* (:taxBasis %) (:rate %)) 2))
         (concat
          (map #(dissoc % :append-tax)
               (map (fn [x] 
                      (assoc x :taxBasis
                             (round
                              (if (:taxable-rate x)
                                (* taxBasis (:taxable-rate x))
                                taxBasis ) 2))) f-rate))
          (filter #(not (nil? %))
                  (reduce conj
                          (map (fn [x]
                                 (let [y (:append-tax x)]
                                   (if y 
                                     (map #(assoc % :taxBasis (round
  (* taxBasis (:rate x)) 2)) y))))
                               f-rate)))))))

(defn sum [doc]
  (round
   (reduce + (map #(:taxes %)
                  (query-taxes (get-taxes-related doc)
                               (module/query "rate" (:taxObject doc))
                               (:taxBasis doc)))) 2))

(defn get-tax-objects []
  (map #(:_id %) 
       (module/query "rate")))

(defn get-taxes-detail [doc]
  (query-taxes (get-taxes-related doc)
               (module/query "rate" (:taxObject doc))
               (:taxBasis doc)))
