(ns lis.module.data
  (:require [monger.core :as mg]
            [monger.collection :as mgcoll]
            [lis.module.mongo :as lis]))

(mg/connect!)
(mg/set-db! (mg/get-db "lis"))

(mgcoll/insert "regions" {:_id "威信县" :name ["扎西镇" "双河乡"]})

(mgcoll/insert 
 "rate" 
 {:_id "建筑业－建筑"
  :rate [{:taxClass "营业税" 
          :object "建筑业-建筑"
          :rate 0.03 
          :append-tax 
          [{:taxClass "教育费附加"    :object "营业税税额" :rate 0.03},
           {:taxClass "地方教育附加"  :object "营业税税额" :rate 0.02},
           {:taxClass "城建税"      :object "营业税税额" :rate 0.01 :subclass "乡"},
           {:taxClass "城建税" :object "营业税税额" :rate 0.05 :subclass "镇"}]},
         
         {:taxClass "印花税" :object "建安合同" :rate 0.0003}
         {:taxClass "资源税" :object "石灰石" :rate 1 :taxable-rate 0.002},
         {:taxClass "个人所得税" :object "经营所得" :rate 0.02},
         {:taxClass "企业所得税" :object "经营所得" :rate 0.25 :taxable-rate 0.08}
         ]})


