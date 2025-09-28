# 📊 Estrutura de Dados - CRUD Clojure

## 📋 Visão Geral
Este documento descreve a estrutura de dados do sistema CRUD desenvolvido em ClojureScript, incluindo todas as tabelas (entidades) e seus respectivos campos.

---

## 🏢 Tabela: CLIENTES

### 📝 Descrição
Armazena informações dos clientes cadastrados no sistema.

### 🔑 Campos

| Campo | Tipo | Obrigatório | Descrição | Validação |
|-------|------|-------------|-----------|-----------|
| `id` | String (UUID) | ✅ | Identificador único do cliente | Gerado automaticamente |
| `nome` | String | ✅ | Nome completo do cliente | Não pode estar vazio |
| `email` | String | ✅ | Endereço de e-mail | Formato de e-mail válido |
| `telefone` | String | ❌ | Número de telefone | - |
| `endereco` | String | ❌ | Endereço completo | - |
| `cidade` | String | ❌ | Cidade de residência | - |
| `estado` | String | ❌ | Estado/UF | - |
| `cep` | String | ❌ | Código postal | - |
| `created-at` | String (ISO) | ✅ | Data/hora de criação | Timestamp ISO 8601 |
| `updated-at` | String (ISO) | ✅ | Data/hora da última atualização | Timestamp ISO 8601 |

### 🔍 Campos de Busca
- `nome` (busca parcial, case-insensitive)
- `email` (busca parcial, case-insensitive)
- `cidade` (busca parcial, case-insensitive)

### ✅ Regras de Validação
```clojure
(defn valid-cliente? [cliente]
  (and (not-empty (:nome cliente))
       (not-empty (:email cliente))
       (re-matches #"^[^\s@]+@[^\s@]+\.[^\s@]+$" (:email cliente))))
```

---

## 📦 Tabela: PRODUTOS

### 📝 Descrição
Armazena informações dos produtos cadastrados no sistema.

### 🔑 Campos

| Campo | Tipo | Obrigatório | Descrição | Validação |
|-------|------|-------------|-----------|-----------|
| `id` | String (UUID) | ✅ | Identificador único do produto | Gerado automaticamente |
| `nome` | String | ✅ | Nome do produto | Não pode estar vazio |
| `descricao` | String | ❌ | Descrição detalhada do produto | - |
| `preco` | Number | ✅ | Preço do produto | Deve ser maior que 0 |
| `categoria` | String | ❌ | Categoria do produto | - |
| `estoque` | Number | ✅ | Quantidade em estoque | Deve ser >= 0 |
| `codigo` | String | ✅ | Código único do produto | Não pode estar vazio |
| `created-at` | String (ISO) | ✅ | Data/hora de criação | Timestamp ISO 8601 |
| `updated-at` | String (ISO) | ✅ | Data/hora da última atualização | Timestamp ISO 8601 |

### 🔍 Campos de Busca
- `nome` (busca parcial, case-insensitive)
- `categoria` (busca parcial, case-insensitive)
- `codigo` (busca parcial, case-insensitive)

### ✅ Regras de Validação
```clojure
(defn valid-produto? [produto]
  (and (not-empty (:nome produto))
       (not-empty (:codigo produto))
       (> (:preco produto) 0)
       (>= (:estoque produto) 0)))
```

---

## 💾 Persistência de Dados

### 🗄️ LocalStorage
Os dados são persistidos no navegador usando localStorage:

| Chave | Conteúdo | Formato |
|-------|----------|---------|
| `crud-clojure-clientes` | Array de clientes | EDN (Extensible Data Notation) |
| `crud-clojure-produtos` | Array de produtos | EDN (Extensible Data Notation) |

### 🔧 Funções de Storage

#### Clientes
- `load-clientes` - Carrega clientes do localStorage
- `save-clientes!` - Salva clientes no localStorage
- `clear-clientes!` - Remove todos os clientes

#### Produtos
- `load-produtos` - Carrega produtos do localStorage
- `save-produtos!` - Salva produtos no localStorage
- `clear-produtos!` - Remove todos os produtos

#### Geral
- `clear-all-data!` - Remove todos os dados do sistema

---

## 🛠️ Funções Utilitárias

### 🆔 Geração de IDs
```clojure
(defn generate-id []
  (str (random-uuid)))
```

### ⏰ Timestamps
```clojure
(defn current-timestamp []
  (.toISOString (js/Date.)))
```

### 💰 Formatação de Preço
```clojure
(defn format-price [price]
  (str "R$ " (.toFixed price 2)))
```

### 📅 Formatação de Data
```clojure
(defn format-date [date-string]
  (let [date (js/Date. date-string)]
    (.toLocaleDateString date "pt-BR")))
```

---

## 📊 Exemplo de Dados

### Cliente
```clojure
{:id "550e8400-e29b-41d4-a716-446655440000"
 :nome "João Silva"
 :email "joao@email.com"
 :telefone "(11) 99999-9999"
 :endereco "Rua das Flores, 123"
 :cidade "São Paulo"
 :estado "SP"
 :cep "01234-567"
 :created-at "2024-01-15T10:30:00.000Z"
 :updated-at "2024-01-15T10:30:00.000Z"}
```

### Produto
```clojure
{:id "550e8400-e29b-41d4-a716-446655440001"
 :nome "Notebook Dell"
 :descricao "Notebook Dell Inspiron 15 3000"
 :preco 2500.00
 :categoria "Informática"
 :estoque 10
 :codigo "NB-DELL-001"
 :created-at "2024-01-15T10:30:00.000Z"
 :updated-at "2024-01-15T10:30:00.000Z"}
```

---

## 🔄 Operações CRUD

### Clientes
- **Create**: `create-cliente` - Cria novo cliente
- **Read**: `load-clientes` - Lista todos os clientes
- **Update**: Atualização via estado da aplicação
- **Delete**: Remoção via estado da aplicação
- **Search**: `filter-clientes` - Busca clientes

### Produtos
- **Create**: `create-produto` - Cria novo produto
- **Read**: `load-produtos` - Lista todos os produtos
- **Update**: Atualização via estado da aplicação
- **Delete**: Remoção via estado da aplicação
- **Search**: `filter-produtos` - Busca produtos

---

## 📈 Estatísticas do Sistema

- **Total de Entidades**: 2 (Clientes e Produtos)
- **Total de Campos**: 18 campos únicos
- **Campos Obrigatórios**: 8 campos
- **Campos com Validação**: 6 campos
- **Campos Pesquisáveis**: 6 campos
- **Tipo de Persistência**: LocalStorage (Client-side)
- **Formato de Dados**: EDN (Extensible Data Notation)