(ns dojo-todo-api.controller.todo-list)

(defn find-list-by-id [database list-id]
  (get database list-id))

(defn find-list-item-by-ids [database list-id item-id]
  (get-in database [list-id :items item-id] nil))

(defn todo-list-all [database]
  database)