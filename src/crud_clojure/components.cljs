(ns crud-clojure.components
  (:require [reagent.core :as r]
            [crud-clojure.models :as models]))

;; Estado local para formulários
(defonce cliente-form (r/atom {}))
(defonce produto-form (r/atom {}))

;; Componente de campo de entrada
(defn input-field [label type value on-change & [placeholder]]
  [:div.form-group
   [:label label]
   [:input {:type type
            :value value
            :placeholder (or placeholder "")
            :on-change #(on-change (-> % .-target .-value))}]])

;; Componente de área de texto
(defn textarea-field [label value on-change & [placeholder]]
  [:div.form-group
   [:label label]
   [:textarea {:value value
               :placeholder (or placeholder "")
               :rows 3
               :on-change #(on-change (-> % .-target .-value))}]])

;; Componente de select
(defn select-field [label value options on-change]
  [:div.form-group
   [:label label]
   [:select {:value value
             :on-change #(on-change (-> % .-target .-value))}
    (for [option options]
      [:option {:key (:value option) :value (:value option)} (:label option)])]])

;; Componente de busca
(defn search-box [search-term on-change placeholder]
  [:div.search-box
   [:input {:type "text"
            :placeholder placeholder
            :value search-term
            :on-change #(on-change (-> % .-target .-value))}]])

;; Formulário de cliente
(defn cliente-form-component [editing-cliente on-submit on-cancel]
  (let [form-data (r/atom (or editing-cliente {}))]
    (fn []
      [:div
       [:h3 (if editing-cliente "Editar Cliente" "Novo Cliente")]
       [:form {:on-submit (fn [e]
                           (.preventDefault e)
                           (when (models/valid-cliente? @form-data)
                             (on-submit @form-data)
                             (reset! form-data {})))}
        
        [input-field "Nome *" "text" (:nome @form-data)
         #(swap! form-data assoc :nome %) "Digite o nome completo"]
        
        [input-field "Email *" "email" (:email @form-data)
         #(swap! form-data assoc :email %) "exemplo@email.com"]
        
        [input-field "Telefone" "tel" (:telefone @form-data)
         #(swap! form-data assoc :telefone %) "(11) 99999-9999"]
        
        [input-field "Endereço" "text" (:endereco @form-data)
         #(swap! form-data assoc :endereco %) "Rua, número, bairro"]
        
        [:div {:style {:display "grid" :grid-template-columns "1fr 1fr 1fr" :gap "15px"}}
         [input-field "Cidade" "text" (:cidade @form-data)
          #(swap! form-data assoc :cidade %) "São Paulo"]
         
         [select-field "Estado" (:estado @form-data)
          [{:value "" :label "Selecione..."}
           {:value "AC" :label "Acre"}
           {:value "AL" :label "Alagoas"}
           {:value "AP" :label "Amapá"}
           {:value "AM" :label "Amazonas"}
           {:value "BA" :label "Bahia"}
           {:value "CE" :label "Ceará"}
           {:value "DF" :label "Distrito Federal"}
           {:value "ES" :label "Espírito Santo"}
           {:value "GO" :label "Goiás"}
           {:value "MA" :label "Maranhão"}
           {:value "MT" :label "Mato Grosso"}
           {:value "MS" :label "Mato Grosso do Sul"}
           {:value "MG" :label "Minas Gerais"}
           {:value "PA" :label "Pará"}
           {:value "PB" :label "Paraíba"}
           {:value "PR" :label "Paraná"}
           {:value "PE" :label "Pernambuco"}
           {:value "PI" :label "Piauí"}
           {:value "RJ" :label "Rio de Janeiro"}
           {:value "RN" :label "Rio Grande do Norte"}
           {:value "RS" :label "Rio Grande do Sul"}
           {:value "RO" :label "Rondônia"}
           {:value "RR" :label "Roraima"}
           {:value "SC" :label "Santa Catarina"}
           {:value "SP" :label "São Paulo"}
           {:value "SE" :label "Sergipe"}
           {:value "TO" :label "Tocantins"}]
          #(swap! form-data assoc :estado %)]
         
         [input-field "CEP" "text" (:cep @form-data)
          #(swap! form-data assoc :cep %) "00000-000"]]
        
        [:div {:style {:margin-top "20px"}}
         [:button.btn.btn-primary {:type "submit"
                                   :disabled (not (models/valid-cliente? @form-data))}
          (if editing-cliente "Atualizar" "Cadastrar")]
         [:button.btn.btn-secondary {:type "button" :on-click on-cancel}
          "Cancelar"]]]])))

;; Formulário de produto
(defn produto-form-component [editing-produto on-submit on-cancel]
  (let [form-data (r/atom (or editing-produto {}))]
    (fn []
      [:div
       [:h3 (if editing-produto "Editar Produto" "Novo Produto")]
       [:form {:on-submit (fn [e]
                           (.preventDefault e)
                           (when (models/valid-produto? @form-data)
                             (on-submit @form-data)
                             (reset! form-data {})))}
        
        [:div {:style {:display "grid" :grid-template-columns "2fr 1fr" :gap "15px"}}
         [input-field "Nome *" "text" (:nome @form-data)
          #(swap! form-data assoc :nome %) "Nome do produto"]
         
         [input-field "Código *" "text" (:codigo @form-data)
          #(swap! form-data assoc :codigo %) "SKU123"]]
        
        [textarea-field "Descrição" (:descricao @form-data)
         #(swap! form-data assoc :descricao %) "Descrição detalhada do produto"]
        
        [:div {:style {:display "grid" :grid-template-columns "1fr 1fr 1fr" :gap "15px"}}
         [input-field "Preço *" "number" (:preco @form-data)
          #(swap! form-data assoc :preco (js/parseFloat %)) "0.00"]
         
         [input-field "Estoque" "number" (:estoque @form-data)
          #(swap! form-data assoc :estoque (js/parseInt %)) "0"]
         
         [select-field "Categoria" (:categoria @form-data)
          [{:value "" :label "Selecione..."}
           {:value "Eletrônicos" :label "Eletrônicos"}
           {:value "Roupas" :label "Roupas"}
           {:value "Casa e Jardim" :label "Casa e Jardim"}
           {:value "Esportes" :label "Esportes"}
           {:value "Livros" :label "Livros"}
           {:value "Beleza" :label "Beleza"}
           {:value "Alimentação" :label "Alimentação"}
           {:value "Outros" :label "Outros"}]
          #(swap! form-data assoc :categoria %)]]
        
        [:div {:style {:margin-top "20px"}}
         [:button.btn.btn-primary {:type "submit"
                                   :disabled (not (models/valid-produto? @form-data))}
          (if editing-produto "Atualizar" "Cadastrar")]
         [:button.btn.btn-secondary {:type "button" :on-click on-cancel}
          "Cancelar"]]]])))

;; Item da lista de clientes
(defn cliente-item [cliente on-edit on-delete]
  [:div.list-item
   [:h3 (:nome cliente)]
   [:p (str "📧 " (:email cliente))]
   (when (not-empty (:telefone cliente))
     [:p (str "📞 " (:telefone cliente))])
   (when (not-empty (:cidade cliente))
     [:p (str "📍 " (:cidade cliente) (when (not-empty (:estado cliente)) (str ", " (:estado cliente))))])
   [:div.actions
    [:button.btn.btn-primary {:on-click #(on-edit cliente)} "Editar"]
    [:button.btn.btn-danger {:on-click #(when (js/confirm "Tem certeza que deseja excluir este cliente?")
                                          (on-delete (:id cliente)))} "Excluir"]]])

;; Item da lista de produtos
(defn produto-item [produto on-edit on-delete]
  [:div.list-item
   [:h3 (:nome produto)]
   [:p (str "💰 " (models/format-price (:preco produto)))]
   [:p (str "📦 Estoque: " (:estoque produto))]
   (when (not-empty (:categoria produto))
     [:p (str "🏷️ " (:categoria produto))])
   (when (not-empty (:codigo produto))
     [:p (str "🔖 Código: " (:codigo produto))])
   [:div.actions
    [:button.btn.btn-primary {:on-click #(on-edit produto)} "Editar"]
    [:button.btn.btn-danger {:on-click #(when (js/confirm "Tem certeza que deseja excluir este produto?")
                                          (on-delete (:id produto)))} "Excluir"]]])

;; Aba de clientes
(defn clientes-tab [state]
  (let [filtered-clientes (models/filter-clientes (:clientes state) (:search-term state))]
    [:div
     ;; Busca
     [search-box (:search-term state) 
      #(js/setTimeout (fn [] ((js/eval "crud_clojure.core.set_search_term_BANG_") %)) 0)
      "Buscar clientes por nome, email ou cidade..."]
     
     ;; Formulário (se estiver editando ou adicionando)
     (when (or (:editing-cliente state) (:show-cliente-form state))
       [cliente-form-component (:editing-cliente state)
        (if (:editing-cliente state)
          #((js/eval "crud_clojure.core.update_cliente_BANG_") (:id (:editing-cliente state)) %)
          #((js/eval "crud_clojure.core.add_cliente_BANG_") %))
        #((js/eval "crud_clojure.core.cancel_edit_cliente_BANG_"))])
     
     ;; Botão para adicionar novo cliente
     (when (not (:editing-cliente state))
       [:div {:style {:margin-bottom "20px"}}
        [:button.btn.btn-primary 
         {:on-click #(js/setTimeout (fn [] ((js/eval "crud_clojure.core.start_edit_cliente_BANG_") {})) 0)}
         "➕ Novo Cliente"]])
     
     ;; Lista de clientes
     (if (empty? filtered-clientes)
       [:div.empty-state
        [:h3 "Nenhum cliente encontrado"]
        [:p "Adicione seu primeiro cliente ou ajuste os filtros de busca."]]
       [:div.grid
        (for [cliente filtered-clientes]
          ^{:key (:id cliente)}
          [cliente-item cliente
           #((js/eval "crud_clojure.core.start_edit_cliente_BANG_") %)
           #((js/eval "crud_clojure.core.delete_cliente_BANG_") %)])])]))

;; Aba de produtos
(defn produtos-tab [state]
  (let [filtered-produtos (models/filter-produtos (:produtos state) (:search-term state))]
    [:div
     ;; Busca
     [search-box (:search-term state) 
      #(js/setTimeout (fn [] ((js/eval "crud_clojure.core.set_search_term_BANG_") %)) 0)
      "Buscar produtos por nome, categoria ou código..."]
     
     ;; Formulário (se estiver editando ou adicionando)
     (when (or (:editing-produto state) (:show-produto-form state))
       [produto-form-component (:editing-produto state)
        (if (:editing-produto state)
          #((js/eval "crud_clojure.core.update_produto_BANG_") (:id (:editing-produto state)) %)
          #((js/eval "crud_clojure.core.add_produto_BANG_") %))
        #((js/eval "crud_clojure.core.cancel_edit_produto_BANG_"))])
     
     ;; Botão para adicionar novo produto
     (when (not (:editing-produto state))
       [:div {:style {:margin-bottom "20px"}}
        [:button.btn.btn-primary 
         {:on-click #(js/setTimeout (fn [] ((js/eval "crud_clojure.core.start_edit_produto_BANG_") {})) 0)}
         "➕ Novo Produto"]])
     
     ;; Lista de produtos
     (if (empty? filtered-produtos)
       [:div.empty-state
        [:h3 "Nenhum produto encontrado"]
        [:p "Adicione seu primeiro produto ou ajuste os filtros de busca."]]
       [:div.grid
        (for [produto filtered-produtos]
          ^{:key (:id produto)}
          [produto-item produto
           #((js/eval "crud_clojure.core.start_edit_produto_BANG_") %)
           #((js/eval "crud_clojure.core.delete_produto_BANG_") %)])])]))