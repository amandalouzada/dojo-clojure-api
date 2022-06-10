(ns dojo-todo-api.diplomat.http-server
  (:use clojure.pprint)
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [dojo-todo-api.db :as db]
            [dojo-todo-api.controller.todo-list :as controller.todo-list]))

(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

(def ok (partial response 200))
(def created (partial response 201))
(def bad-request (partial response 400))

(def db-interceptor
  {:name  :db-interceptor
   :enter (fn [context]
            (assoc context :database @db/local-database))
   :leave (fn [context]
            (if-let [[transaction & args] (:db-transaction context)]
              (do
                (apply swap! db/local-database transaction args)
                (assoc-in context [:database] @db/local-database))
              context)
            )})

(def list-all
  {:name  :list-all
   :leave (fn [context]
            (if-let [database (:database context)]
              (assoc context :response (ok (controller.todo-list/todo-list-all database)))
              context))})

(def find-list-by-id
  {:name  :find-list-by-id
   :leave (fn [context]
            (let [list-id (get-in context [:request :path-params :list-id])
                  database (:database context)]
              (assoc context :response (ok (controller.todo-list/find-list-by-id database list-id)))))})

(def find-list-item-by-ids
  {:name  :find-list-item-by-ids
   :leave (fn [context]
            (let [list-id (get-in context [:request :path-params :list-id])
                  item-id (get-in context [:request :path-params :item-id])
                  database (:database context)]
              (assoc context :response (ok (controller.todo-list/find-list-item-by-ids database list-id item-id)))))})

(def create-list
  {:name  :create-list
   :leave (fn [context]
            (let [name (get-in context [:request :json-params :name])]
              (-> context
                  (assoc :db-transaction [controller.todo-list/insert-list name])
                  (assoc :response (created {:message (str name " created")})))))})

(def create-item-list
  {:name  :insert-item-list
   :leave (fn [context]
            (let [name (get-in context [:request :json-params :name])
                  list-id (get-in context [:request :path-params :list-id])]
              (-> context
                  (assoc :db-transaction [controller.todo-list/insert-item-in-list list-id name])
                  (assoc :response (created {:message (str name " created")})))))})




(def common-interceptors
  [(body-params/body-params)
   http/json-body
   db-interceptor])

(def routes
  (route/expand-routes
    #{["/todo" :get (conj common-interceptors list-all) :route-name :todo]
      ["/todo" :post (conj common-interceptors create-list) :route-name :todo-create]
      ["/todo/:list-id" :get (conj common-interceptors find-list-by-id) :route-name :todo-list]
      ["/todo/:list-id" :post (conj common-interceptors create-item-list) :route-name :tod-create-item-list]
      ["/todo/:list-id/:item-id" :get (conj common-interceptors find-list-item-by-ids) :route-name :todo-list-item]
      }))
