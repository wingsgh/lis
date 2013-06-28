(ns lis.controllers.taxes)

(defn round2 [num]
  (Double/parseDouble (format "%.2f" (+ num 0.00))))

(defn- income-tax-filter [taxes-related]
  (fn [rate]
    (or 
     (and 
      (not= (:class rate) "个人所得税") 
      (not= (:class rate) "企业所得税"))
     (= (:class rate) (:income-tax taxes-related)))))

(defn- city-tax-filter [taxes-related]
  (fn [append-tax-rate]
    (or 
     (not= (:class append-tax-rate) "城建税")
     (= (:subclass append-tax-rate) (:region-type taxes-related)))))

(defn query-rate [rate taxes-related]
  (filter 
   (income-tax-filter taxes-related) 
   (map #(if (:append-tax %)
           (update-in % [:append-tax]
                      (fn [x] (filter (city-tax-filter taxes-related) x))) %) 
        (:rate (first (filter #(= (:tax-object %) (:tax-object taxes-related)) rate))))))

         
(defn query-taxes [taxBasis taxes-related rate]
  (let [f-rate (query-rate rate taxes-related)]
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


(defn sum [taxBasis taxes-related rate]
  (round2 
   (reduce + (map #(:taxes %) (query-taxes taxBasis taxes-related rate)))))



