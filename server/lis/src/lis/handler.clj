(ns lis.handler
  (:use [compojure.core]
        [clojure.walk])
  (:require [compojure.handler      :as handler]
            [ring.middleware.json   :as middleware]
            [compojure.route        :as route]
            [lis.controller.decl    :as decl]
            [lis.controller.regions :as regions]
            [lis.controller.taxes   :as taxes]
            [lis.controller.levy    :as levy]
            ))

(defroutes app-routes

  (context "/decl" [] 
           (defroutes decl-routes
             (GET "/" [] (decl/query))
             (POST "/" {body :body} (decl/create (keywordize-keys body)))
             (context "/:id" [id] 
                      (defroutes decl-routes
                        (GET "/" [] {:body (decl/query (Long. id))})
                        (DELETE "/" [] (decl/delete (Long. id)))
                        (PUT "/" {body :body} (decl/update (Long. id) (keywordize-keys body)))))))
  
  (context "/regions" [] 
           (defroutes regions-routes
             (GET "/" [] (regions/query))))

  (context "/tax-objects" []
           (defroutes tax-object-routes
             (GET "/" [] (taxes/get-tax-objects))))

   (context "/levy" [] 
           (defroutes levy-routes
             (GET "/" [] (levy/query))
             (POST "/" {body :body} (levy/create (keywordize-keys body)))
             (context "/:id" [id] 
                      (defroutes decl-routes
                        (GET "/" [] {:body (levy/query (Long. id))})
                        (PUT "/" {body :body} (levy/update (Long. id) (keywordize-keys body)))))))
  )

(def app
  (-> 
   (handler/api app-routes)
   (middleware/wrap-json-body)
   (middleware/wrap-json-response)
   ))
