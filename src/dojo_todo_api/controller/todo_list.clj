(ns dojo-todo-api.controller.todo-list
  (:require [dojo-todo-api.model.todo :as model.todo]))

(defn create-id [id-type]
  (str (gensym id-type)))

(defn find-list-by-id [database list-id]
  (get database list-id))

(defn find-list-item-by-ids [database list-id item-id]
  (get-in database [list-id :items item-id] nil))

(defn todo-list-all [database]
  database)

(defn insert-list [database name]
  (assoc database (create-id "list") (model.todo/create-list name)))

(defn insert-item-in-list [database list-id name]
  (if (contains? database list-id)
    (assoc-in database [list-id :items (create-id "item")] (model.todo/create-list-item name))
    database))