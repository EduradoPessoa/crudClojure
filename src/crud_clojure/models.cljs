(ns crud-clojure.models)

;; Função para gerar IDs únicos
(defn generate-id []
  (str (random-uuid)))

;; Função para obter timestamp atual
(defn current-timestamp []
  (.toISOString (js/Date.)))

;; Modelo de Cliente
(defn create-cliente 
  [{:keys [nome email telefone endereco cidade estado cep]}]
  {:id (generate-id)
   :nome (or nome "")
   :email (or email "")
   :telefone (or telefone "")
   :endereco (or endereco "")
   :cidade (or cidade "")
   :estado (or estado "")
   :cep (or cep "")
   :created-at (current-timestamp)
   :updated-at (current-timestamp)})

;; Validação de cliente
(defn valid-cliente? [cliente]
  (and (not-empty (:nome cliente))
       (not-empty (:email cliente))
       (re-matches #"^[^\s@]+@[^\s@]+\.[^\s@]+$" (:email cliente))))

;; Modelo de Produto
(defn create-produto 
  [{:keys [nome descricao preco categoria estoque codigo]}]
  {:id (generate-id)
   :nome (or nome "")
   :descricao (or descricao "")
   :preco (or preco 0)
   :categoria (or categoria "")
   :estoque (or estoque 0)
   :codigo (or codigo "")
   :created-at (current-timestamp)
   :updated-at (current-timestamp)})

;; Validação de produto
(defn valid-produto? [produto]
  (and (not-empty (:nome produto))
       (not-empty (:codigo produto))
       (> (:preco produto) 0)
       (>= (:estoque produto) 0)))

;; Função para filtrar clientes por termo de busca
(defn filter-clientes [clientes search-term]
  (if (empty? search-term)
    clientes
    (let [term (clojure.string/lower-case search-term)]
      (filter #(or (clojure.string/includes? 
                    (clojure.string/lower-case (:nome %)) term)
                   (clojure.string/includes? 
                    (clojure.string/lower-case (:email %)) term)
                   (clojure.string/includes? 
                    (clojure.string/lower-case (:cidade %)) term))
              clientes))))

;; Função para filtrar produtos por termo de busca
(defn filter-produtos [produtos search-term]
  (if (empty? search-term)
    produtos
    (let [term (clojure.string/lower-case search-term)]
      (filter #(or (clojure.string/includes? 
                    (clojure.string/lower-case (:nome %)) term)
                   (clojure.string/includes? 
                    (clojure.string/lower-case (:categoria %)) term)
                   (clojure.string/includes? 
                    (clojure.string/lower-case (:codigo %)) term))
              produtos))))

;; Função para formatar preço
(defn format-price [price]
  (str "R$ " (.toFixed price 2)))

;; Função para formatar data
(defn format-date [date-string]
  (let [date (js/Date. date-string)]
    (.toLocaleDateString date "pt-BR")))