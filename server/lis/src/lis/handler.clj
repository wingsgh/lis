(ns lis.handler
  (:use [compojure.core]
        [clojure.walk])
  (:require [compojure.handler       :as handler]
            [ring.middleware.json    :as middleware]
            [compojure.route         :as route]
            [lis.controller.decl     :as decl]
            [lis.controller.regions  :as regions]
            [lis.controller.taxes    :as taxes]
            [lis.controller.levy     :as levy]
            [lis.controller.tax-source :as tax-source]
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
  
  (context
   "/tax-source" []
   (defroutes tax-source-routes

     (context 
      "/tax-object" [] 
      (defroutes tax-source-routes
        (context 
         "/:taxObject" [taxObject]
         (defroutes tax-source-routes
           (GET "/" [] 
                (map #(select-keys % [:_id :name]) (tax-source/query {:taxObject taxObject}) ))))))

             (context 
              "/building" 
              []
              (defroutes
                tax-source-routes
                (GET "/"
                     []
                     (tax-source/query {:industry "建筑业"}))
                (POST "/" 
                      {body :body} 
                      (tax-source/create 
                       (assoc (keywordize-keys body) 
                         :industry "建筑业")))
                (context 
                 "/:id" [id]
                 (defroutes tax-source-routes
                   (PUT "/"
                        {body :body}
                        (tax-source/update (Long. id) (keywordize-keys body)))
                   (DELETE "/" []
                           (tax-source/delete (Long. id)))
                   
                   (GET "/" []
                        {:body (first (seq (tax-source/query
                                            {:industry "建筑业" :_id (Long. id)})))})))))))
   (context "/levy" [] 
           (defroutes levy-routes
             (GET "/" [] (levy/query))
             (POST "/" {body :body} (levy/create (keywordize-keys body)))
             (context "/:id" [id] 
                      (defroutes levy-routes
                        (GET "/" [] {:body (levy/query (Long. id))})
                        (PUT "/" {body :body} (levy/update (Long. id)
           (keywordize-keys body)))))))
   )

(def app
  (-> 
   (handler/api app-routes)
   (middleware/wrap-json-body)
   (middleware/wrap-json-response)
   ))
