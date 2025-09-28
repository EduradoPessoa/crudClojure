# 🚀 CRUD Clojure

Sistema simples de cadastro de clientes e produtos desenvolvido em ClojureScript com Reagent.

## 📋 Funcionalidades

- ✅ Cadastro, edição e exclusão de clientes
- ✅ Cadastro, edição e exclusão de produtos
- ✅ Busca e filtros em tempo real
- ✅ Persistência local com localStorage
- ✅ Interface responsiva e moderna
- ✅ Validação de formulários

## 🛠️ Tecnologias Utilizadas

- **ClojureScript** - Linguagem principal
- **Reagent** - Interface reativa baseada em React
- **Shadow-CLJS** - Build tool e desenvolvimento
- **HTML5 LocalStorage** - Persistência de dados

## 🚀 Como Executar

### Pré-requisitos

- Java 8+ (para Clojure)
- Node.js 14+ (para shadow-cljs)

### Instalação

1. Clone ou baixe o projeto
2. Navegue até a pasta do projeto:
   ```bash
   cd crudClojure
   ```

3. Instale as dependências:
   ```bash
   npm install
   ```

### Desenvolvimento

Para iniciar o servidor de desenvolvimento:

```bash
npm run dev
```

Acesse: http://localhost:8080

### Build de Produção

Para gerar os arquivos otimizados:

```bash
npm run build
```

## 📁 Estrutura do Projeto

```
crudClojure/
├── src/
│   └── crud_clojure/
│       ├── core.cljs          # Arquivo principal da aplicação
│       ├── models.cljs        # Modelos de dados e validações
│       ├── storage.cljs       # Gerenciamento do localStorage
│       └── components.cljs    # Componentes da interface
├── public/
│   └── index.html            # Página HTML principal
├── deps.edn                  # Dependências Clojure
├── shadow-cljs.edn          # Configuração do build
└── package.json             # Scripts NPM
```

## 🎯 Como Usar

### Clientes

1. Clique na aba "👥 Clientes"
2. Clique em "➕ Novo Cliente"
3. Preencha os dados obrigatórios (Nome e Email)
4. Clique em "Cadastrar"

### Produtos

1. Clique na aba "📦 Produtos"
2. Clique em "➕ Novo Produto"
3. Preencha os dados obrigatórios (Nome, Código e Preço)
4. Clique em "Cadastrar"

### Busca

- Use a caixa de busca para filtrar clientes ou produtos
- A busca funciona em tempo real
- Para clientes: busca por nome, email ou cidade
- Para produtos: busca por nome, categoria ou código

## 💾 Persistência de Dados

Os dados são salvos automaticamente no localStorage do navegador:
- Não há necessidade de banco de dados externo
- Os dados persistem entre sessões
- Para limpar os dados, use as ferramentas de desenvolvedor do navegador

## 🎨 Características do Código Clojure

Este projeto demonstra conceitos importantes do Clojure:

- **Imutabilidade**: Todos os dados são imutáveis
- **Funções Puras**: Lógica de negócio sem efeitos colaterais
- **Namespaces**: Organização modular do código
- **Atoms**: Gerenciamento de estado reativo
- **Destructuring**: Extração elegante de dados
- **Threading Macros**: Composição de transformações

## 🔧 Desenvolvimento

### Estrutura dos Namespaces

- `crud-clojure.core` - Ponto de entrada e gerenciamento de estado
- `crud-clojure.models` - Modelos de dados e validações
- `crud-clojure.storage` - Persistência no localStorage
- `crud-clojure.components` - Componentes da interface

### Hot Reload

O projeto suporta hot reload durante o desenvolvimento. Mudanças no código são refletidas automaticamente no navegador.

## 📝 Licença

MIT License - Sinta-se livre para usar e modificar este código para aprendizado.