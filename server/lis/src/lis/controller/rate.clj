(ns lis.controller.rate
  (:require [lis.module.mongo :as module]))

(defn round2 [num]
  (Double/parseDouble (format "%.2f" (+ num 0.00))))

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

(defn- get-taxes-related [decl]
  (assoc {} 
    :tax-object (:taxObject decl)
    :income-tax (if (> (count (:taxpayer decl)) 4)
                  "企业所得税" 
                  "个人所得税")
    :region-type (let [region (:region decl)]
                   (subs region (dec (count region))))
    ))

(defn query-rate [taxes-related rate]
  (filter 
   (income-tax-filter taxes-related) 
   (map #(if (:append-tax %)
           (update-in % [:append-tax]
                      (fn [x] (filter (city-tax-filter taxes-related) x))) %) 
        (:rate rate))))
         
(defn query-taxes [taxes-related rate taxBasis]
  (let [f-rate (query-rate taxes-related rate)]
    (map #(assoc % :taxes (round2 (* (:taxBasis %) (:rate %))))
         (concat
          (map #(dissoc % :append-tax)
               (map (fn [x] 
                      (assoc x :taxBasis
                             (round2 
                              (if (:taxable-rate x)
                                (* taxBasis (:taxable-rate x))
                                taxBasis )))) f-rate))
          (filter #(not (nil? %))
                  (reduce conj
                          (map (fn [x]
                                 (let [y (:append-tax x)]
                                   (if y 
                                     (map #(assoc % :taxBasis (round2 (* taxBasis (:rate x)))) y))))
                               f-rate)))))))

(defn sum [taxes-related rate taxBasis]
  (round2 
   (reduce + (map #(:taxes %) (query-taxes  taxes-related rate taxBasis)))))

(defn get-tax-objects []
  (map #(:_id %) 
       (module/query "rate")))
