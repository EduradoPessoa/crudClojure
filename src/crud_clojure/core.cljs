(ns crud-clojure.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [crud-clojure.models :as models]
            [crud-clojure.storage :as storage]
            [crud-clojure.components :as components]))

;; Estado global da aplicação
(defonce app-state 
  (r/atom {:current-tab :clientes
           :clientes (storage/load-clientes)
           :produtos (storage/load-produtos)
           :search-term ""
           :editing-cliente nil
           :editing-produto nil}))

;; Funções para gerenciar abas
(defn set-tab! [tab]
  (swap! app-state assoc :current-tab tab))

;; Funções para busca
(defn set-search-term! [term]
  (swap! app-state assoc :search-term term))

;; Funções para clientes
(defn add-cliente! [cliente]
  (let [new-cliente (models/create-cliente cliente)]
    (swap! app-state update :clientes conj new-cliente)
    (storage/save-clientes! (:clientes @app-state))))

(defn update-cliente! [id cliente-data]
  (swap! app-state update :clientes 
         (fn [clientes]
           (mapv #(if (= (:id %) id)
                    (merge % cliente-data)
                    %) clientes)))
  (storage/save-clientes! (:clientes @app-state))
  (swap! app-state assoc :editing-cliente nil))

(defn delete-cliente! [id]
  (swap! app-state update :clientes 
         (fn [clientes]
           (filterv #(not= (:id %) id) clientes)))
  (storage/save-clientes! (:clientes @app-state)))

(defn start-edit-cliente! [cliente]
  (swap! app-state assoc :editing-cliente cliente))

(defn cancel-edit-cliente! []
  (swap! app-state assoc :editing-cliente nil))

;; Funções para produtos
(defn add-produto! [produto]
  (let [new-produto (models/create-produto produto)]
    (swap! app-state update :produtos conj new-produto)
    (storage/save-produtos! (:produtos @app-state))))

(defn update-produto! [id produto-data]
  (swap! app-state update :produtos 
         (fn [produtos]
           (mapv #(if (= (:id %) id)
                    (merge % produto-data)
                    %) produtos)))
  (storage/save-produtos! (:produtos @app-state))
  (swap! app-state assoc :editing-produto nil))

(defn delete-produto! [id]
  (swap! app-state update :produtos 
         (fn [produtos]
           (filterv #(not= (:id %) id) produtos)))
  (storage/save-produtos! (:produtos @app-state)))

(defn start-edit-produto! [produto]
  (swap! app-state assoc :editing-produto produto))

(defn cancel-edit-produto! []
  (swap! app-state assoc :editing-produto nil))

;; Componente principal da aplicação
(defn app []
  (let [state @app-state]
    [:div.container
     [:div.header
      [:h1 "🚀 CRUD Clojure"]
      [:p "Sistema de Cadastro de Clientes e Produtos"]]
     
     [:div.content
      ;; Abas de navegação
      [:div.tabs
       [:button.tab 
        {:class (when (= (:current-tab state) :clientes) "active")
         :on-click #(set-tab! :clientes)}
        "👥 Clientes"]
       [:button.tab 
        {:class (when (= (:current-tab state) :produtos) "active")
         :on-click #(set-tab! :produtos)}
        "📦 Produtos"]]
      
      ;; Conteúdo das abas
      (case (:current-tab state)
        :clientes [components/clientes-tab state]
        :produtos [components/produtos-tab state])]]))

;; Função de inicialização
(defn init! []
  (rdom/render [app] (.getElementById js/document "app")))

;; Recarregamento a quente para desenvolvimento
(defn ^:dev/after-load reload! []
  (init!))