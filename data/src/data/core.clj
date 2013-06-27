(ns data.core
  (:require [monger.core :as mg]
            [monger.collection :as mc])
  (:import [org.bson.types ObjectId]))

(mg/connect!)
(mg/set-db! (mg/get-db "lis"))

(mc/insert 
 "regions" 
 {:_id (ObjectId.)
  :names ["扎西镇","双河乡"]})

(mc/insert 
 "rate" 
 {:_id (ObjectId.)
  :tax-object "建筑业－建筑",
  :rate [{:class "营业税" 
          :object "建筑业"
          :rate 0.03 
          :append-tax 
          [{:class "教育费附加" :object "营业税税额" :rate 0.03},
           {:class "地方教育附加" :object "营业税税额" :rate 0.02},
           {:class "城建税" :object "营业税税额" :rate 0.01 :subclass "乡"},
           {:class "城建税" :object "营业税税额" :rate 0.05 :subclass "镇"}]},
         
         {:class "印花税" :object "建安合同" :rate 0.0003}
         {:class "资源税" :object "石灰石" :rate 1 :taxable-rate 0.002},
         {:class "个人所得税" :object "经营所得" :rate 0.02},
         {:class "企业所得税" :object "经营所得" :rate 0.25 :taxable-rate 0.08}
]})
