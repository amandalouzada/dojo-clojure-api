(ns dojo-todo-api.diplomat.http-server
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [dojo-todo-api.db :as db]
            [dojo-todo-api.controller.todo-list :as controller.todo-list]))

(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

(def ok (partial response 200))
(def bad-request (partial response 400))

(def db-interceptor
  {:name  :db-interceptor
   :enter (fn [context]
            (assoc context :database db/fake-database))})

(def list-all
  {:name  :respond-ok
   :leave (fn [context]
            (if-let [database (:database context)]
              (assoc context :response (ok (controller.todo-list/todo-list-all database)))
              context))})

(def find-list-by-id
  {:name  :respond-ok
   :leave (fn [context]
            (let [list-id (get-in context [:request :path-params :list-id])
                  database (:database context)]
              (assoc context :response (ok (controller.todo-list/find-list-by-id database list-id)))))})

(def find-list-item-by-ids
  {:name  :respond-ok
   :leave (fn [context]
            (let [list-id (get-in context [:request :path-params :list-id])
                  item-id (get-in context [:request :path-params :item-id])
                  database (:database context)]
              (assoc context :response (ok (controller.todo-list/find-list-item-by-ids database list-id item-id)))))})


(def common-interceptors
  [http/json-body
   db-interceptor])

(def routes
  (route/expand-routes
    #{["/todo" :get (conj common-interceptors list-all) :route-name :todo]
      ["/todo/:list-id" :get (conj common-interceptors find-list-by-id) :route-name :todo-list]
      ["/todo/:list-id/:item-id" :get (conj common-interceptors find-list-item-by-ids) :route-name :todo-list-item]}))
