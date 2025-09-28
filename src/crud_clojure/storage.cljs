(ns crud-clojure.storage
  (:require [cljs.reader :as reader]))

;; Chaves para localStorage
(def clientes-key "crud-clojure-clientes")
(def produtos-key "crud-clojure-produtos")

;; Funções auxiliares para localStorage
(defn get-item [key]
  (when-let [item (.getItem js/localStorage key)]
    (try
      (reader/read-string item)
      (catch js/Error e
        (js/console.warn "Erro ao ler dados do localStorage:" e)
        nil))))

(defn set-item! [key value]
  (try
    (.setItem js/localStorage key (pr-str value))
    (catch js/Error e
      (js/console.error "Erro ao salvar dados no localStorage:" e))))

(defn remove-item! [key]
  (.removeItem js/localStorage key))

;; Funções específicas para clientes
(defn load-clientes []
  (or (get-item clientes-key) []))

(defn save-clientes! [clientes]
  (set-item! clientes-key clientes))

(defn clear-clientes! []
  (remove-item! clientes-key))

;; Funções específicas para produtos
(defn load-produtos []
  (or (get-item produtos-key) []))

(defn save-produtos! [produtos]
  (set-item! produtos-key produtos))

(defn clear-produtos! []
  (remove-item! produtos-key))

;; Função para limpar todos os dados
(defn clear-all-data! []
  (clear-clientes!)
  (clear-produtos!))

;; Função para exportar dados
(defn export-data []
  {:clientes (load-clientes)
   :produtos (load-produtos)
   :exported-at (.toISOString (js/Date.))})

;; Função para importar dados
(defn import-data! [data]
  (when (:clientes data)
    (save-clientes! (:clientes data)))
  (when (:produtos data)
    (save-produtos! (:produtos data))))

;; Função para obter estatísticas
(defn get-stats []
  (let [clientes (load-clientes)
        produtos (load-produtos)]
    {:total-clientes (count clientes)
     :total-produtos (count produtos)
     :valor-total-estoque (reduce + (map #(* (:preco %) (:estoque %)) produtos))
     :last-updated (.toISOString (js/Date.))}))