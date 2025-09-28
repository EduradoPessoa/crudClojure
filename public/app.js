// CRUD Clojure - Implementação JavaScript com conceitos funcionais
// Inspirado na filosofia funcional do Clojure

// Estado da aplicação (imutável)
let appState = {
    clientes: [],
    produtos: [],
    currentTab: 'clientes',
    editingCliente: null,
    editingProduto: null
};

// Funções utilitárias (puras)
const generateId = () => Date.now().toString(36) + Math.random().toString(36).substr(2);
const currentTimestamp = () => new Date().toISOString();

// Funções de validação (puras)
const isValidEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
const isValidCliente = (cliente) => cliente.nome && cliente.nome.trim() && cliente.email && isValidEmail(cliente.email);
const isValidProduto = (produto) => produto.nome && produto.nome.trim() && produto.codigo && produto.codigo.trim() && produto.preco > 0;

// Funções de formatação (puras)
const formatPrice = (price) => `R$ ${parseFloat(price).toFixed(2)}`;
const formatDate = (dateString) => new Date(dateString).toLocaleDateString('pt-BR');

// Funções de localStorage
const storage = {
    save: (key, data) => {
        try {
            localStorage.setItem(key, JSON.stringify(data));
        } catch (e) {
            console.error('Erro ao salvar no localStorage:', e);
        }
    },
    
    load: (key, defaultValue = []) => {
        try {
            const data = localStorage.getItem(key);
            return data ? JSON.parse(data) : defaultValue;
        } catch (e) {
            console.error('Erro ao carregar do localStorage:', e);
            return defaultValue;
        }
    },
    
    clear: (key) => localStorage.removeItem(key)
};

// Funções para gerenciar estado (imutáveis)
const updateState = (newState) => {
    appState = { ...appState, ...newState };
    render();
};

const addCliente = (clienteData) => {
    const newCliente = {
        id: generateId(),
        nome: clienteData.nome || '',
        email: clienteData.email || '',
        telefone: clienteData.telefone || '',
        endereco: clienteData.endereco || '',
        cidade: clienteData.cidade || '',
        createdAt: currentTimestamp()
    };
    
    const newClientes = [...appState.clientes, newCliente];
    storage.save('crud-clojure-clientes', newClientes);
    updateState({ clientes: newClientes });
};

const updateCliente = (id, clienteData) => {
    const newClientes = appState.clientes.map(cliente => 
        cliente.id === id 
            ? { ...cliente, ...clienteData, updatedAt: currentTimestamp() }
            : cliente
    );
    
    storage.save('crud-clojure-clientes', newClientes);
    updateState({ clientes: newClientes, editingCliente: null });
};

const deleteCliente = (id) => {
    const newClientes = appState.clientes.filter(cliente => cliente.id !== id);
    storage.save('crud-clojure-clientes', newClientes);
    updateState({ clientes: newClientes });
};

const addProduto = (produtoData) => {
    const newProduto = {
        id: generateId(),
        nome: produtoData.nome || '',
        codigo: produtoData.codigo || '',
        descricao: produtoData.descricao || '',
        preco: parseFloat(produtoData.preco) || 0,
        estoque: parseInt(produtoData.estoque) || 0,
        categoria: produtoData.categoria || '',
        createdAt: currentTimestamp()
    };
    
    const newProdutos = [...appState.produtos, newProduto];
    storage.save('crud-clojure-produtos', newProdutos);
    updateState({ produtos: newProdutos });
};

const updateProduto = (id, produtoData) => {
    const newProdutos = appState.produtos.map(produto => 
        produto.id === id 
            ? { 
                ...produto, 
                ...produtoData, 
                preco: parseFloat(produtoData.preco) || produto.preco,
                estoque: parseInt(produtoData.estoque) || produto.estoque,
                updatedAt: currentTimestamp() 
            }
            : produto
    );
    
    storage.save('crud-clojure-produtos', newProdutos);
    updateState({ produtos: newProdutos, editingProduto: null });
};

const deleteProduto = (id) => {
    const newProdutos = appState.produtos.filter(produto => produto.id !== id);
    storage.save('crud-clojure-produtos', newProdutos);
    updateState({ produtos: newProdutos });
};

// Funções de filtro (puras)
const filterClientes = (clientes, searchTerm) => {
    if (!searchTerm) return clientes;
    const term = searchTerm.toLowerCase();
    return clientes.filter(cliente => 
        cliente.nome.toLowerCase().includes(term) ||
        cliente.email.toLowerCase().includes(term) ||
        cliente.cidade.toLowerCase().includes(term)
    );
};

const filterProdutos = (produtos, searchTerm) => {
    if (!searchTerm) return produtos;
    const term = searchTerm.toLowerCase();
    return produtos.filter(produto => 
        produto.nome.toLowerCase().includes(term) ||
        produto.categoria.toLowerCase().includes(term) ||
        produto.codigo.toLowerCase().includes(term)
    );
};

// Funções de interface
const showTab = (tab) => {
    // Atualizar abas
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    document.getElementById(`${tab}-tab`).classList.add('active');
    
    // Mostrar conteúdo
    document.querySelectorAll('.tab-content').forEach(content => content.style.display = 'none');
    document.getElementById(`${tab}-content`).style.display = 'block';
    
    updateState({ currentTab: tab });
};

const showClienteForm = (cliente = null) => {
    const form = document.getElementById('cliente-form');
    const title = document.getElementById('cliente-form-title');
    
    if (cliente) {
        title.textContent = 'Editar Cliente';
        document.getElementById('cliente-id').value = cliente.id;
        document.getElementById('cliente-nome').value = cliente.nome;
        document.getElementById('cliente-email').value = cliente.email;
        document.getElementById('cliente-telefone').value = cliente.telefone;
        document.getElementById('cliente-endereco').value = cliente.endereco;
        document.getElementById('cliente-cidade').value = cliente.cidade;
        updateState({ editingCliente: cliente });
    } else {
        title.textContent = 'Novo Cliente';
        document.getElementById('cliente-form').querySelector('form').reset();
        updateState({ editingCliente: null });
    }
    
    form.style.display = 'block';
};

const hideClienteForm = () => {
    document.getElementById('cliente-form').style.display = 'none';
    document.getElementById('cliente-form').querySelector('form').reset();
    updateState({ editingCliente: null });
};

const showProdutoForm = (produto = null) => {
    const form = document.getElementById('produto-form');
    const title = document.getElementById('produto-form-title');
    
    if (produto) {
        title.textContent = 'Editar Produto';
        document.getElementById('produto-id').value = produto.id;
        document.getElementById('produto-nome').value = produto.nome;
        document.getElementById('produto-codigo').value = produto.codigo;
        document.getElementById('produto-descricao').value = produto.descricao;
        document.getElementById('produto-preco').value = produto.preco;
        document.getElementById('produto-estoque').value = produto.estoque;
        document.getElementById('produto-categoria').value = produto.categoria;
        updateState({ editingProduto: produto });
    } else {
        title.textContent = 'Novo Produto';
        document.getElementById('produto-form').querySelector('form').reset();
        updateState({ editingProduto: null });
    }
    
    form.style.display = 'block';
};

const hideProdutoForm = () => {
    document.getElementById('produto-form').style.display = 'none';
    document.getElementById('produto-form').querySelector('form').reset();
    updateState({ editingProduto: null });
};

const saveCliente = (event) => {
    event.preventDefault();
    
    const clienteData = {
        nome: document.getElementById('cliente-nome').value,
        email: document.getElementById('cliente-email').value,
        telefone: document.getElementById('cliente-telefone').value,
        endereco: document.getElementById('cliente-endereco').value,
        cidade: document.getElementById('cliente-cidade').value
    };
    
    if (!isValidCliente(clienteData)) {
        alert('Por favor, preencha os campos obrigatórios corretamente.');
        return;
    }
    
    const id = document.getElementById('cliente-id').value;
    
    if (id) {
        updateCliente(id, clienteData);
    } else {
        addCliente(clienteData);
    }
    
    hideClienteForm();
};

const saveProduto = (event) => {
    event.preventDefault();
    
    const produtoData = {
        nome: document.getElementById('produto-nome').value,
        codigo: document.getElementById('produto-codigo').value,
        descricao: document.getElementById('produto-descricao').value,
        preco: document.getElementById('produto-preco').value,
        estoque: document.getElementById('produto-estoque').value,
        categoria: document.getElementById('produto-categoria').value
    };
    
    if (!isValidProduto(produtoData)) {
        alert('Por favor, preencha os campos obrigatórios corretamente.');
        return;
    }
    
    const id = document.getElementById('produto-id').value;
    
    if (id) {
        updateProduto(id, produtoData);
    } else {
        addProduto(produtoData);
    }
    
    hideProdutoForm();
};

// Funções de renderização
const renderClientes = () => {
    const searchTerm = document.getElementById('search-clientes').value;
    const filteredClientes = filterClientes(appState.clientes, searchTerm);
    const container = document.getElementById('clientes-list');
    
    if (filteredClientes.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <h3>Nenhum cliente encontrado</h3>
                <p>Adicione seu primeiro cliente ou ajuste os filtros de busca.</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = `
        <div class="grid">
            ${filteredClientes.map(cliente => `
                <div class="list-item">
                    <h3>${cliente.nome}</h3>
                    <p>📧 ${cliente.email}</p>
                    ${cliente.telefone ? `<p>📞 ${cliente.telefone}</p>` : ''}
                    ${cliente.cidade ? `<p>📍 ${cliente.cidade}</p>` : ''}
                    <div class="actions">
                        <button class="btn btn-primary" onclick="editCliente('${cliente.id}')">Editar</button>
                        <button class="btn btn-danger" onclick="confirmDeleteCliente('${cliente.id}')">Excluir</button>
                    </div>
                </div>
            `).join('')}
        </div>
    `;
};

const renderProdutos = () => {
    const searchTerm = document.getElementById('search-produtos').value;
    const filteredProdutos = filterProdutos(appState.produtos, searchTerm);
    const container = document.getElementById('produtos-list');
    
    if (filteredProdutos.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <h3>Nenhum produto encontrado</h3>
                <p>Adicione seu primeiro produto ou ajuste os filtros de busca.</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = `
        <div class="grid">
            ${filteredProdutos.map(produto => `
                <div class="list-item">
                    <h3>${produto.nome}</h3>
                    <p>💰 ${formatPrice(produto.preco)}</p>
                    <p>📦 Estoque: ${produto.estoque}</p>
                    ${produto.categoria ? `<p>🏷️ ${produto.categoria}</p>` : ''}
                    ${produto.codigo ? `<p>🔖 Código: ${produto.codigo}</p>` : ''}
                    <div class="actions">
                        <button class="btn btn-primary" onclick="editProduto('${produto.id}')">Editar</button>
                        <button class="btn btn-danger" onclick="confirmDeleteProduto('${produto.id}')">Excluir</button>
                    </div>
                </div>
            `).join('')}
        </div>
    `;
};

const render = () => {
    if (appState.currentTab === 'clientes') {
        renderClientes();
    } else {
        renderProdutos();
    }
};

// Funções auxiliares para edição e exclusão
const editCliente = (id) => {
    const cliente = appState.clientes.find(c => c.id === id);
    if (cliente) {
        showClienteForm(cliente);
    }
};

const editProduto = (id) => {
    const produto = appState.produtos.find(p => p.id === id);
    if (produto) {
        showProdutoForm(produto);
    }
};

const confirmDeleteCliente = (id) => {
    if (confirm('Tem certeza que deseja excluir este cliente?')) {
        deleteCliente(id);
    }
};

const confirmDeleteProduto = (id) => {
    if (confirm('Tem certeza que deseja excluir este produto?')) {
        deleteProduto(id);
    }
};

// Funções de filtro em tempo real
const searchClientes = () => {
    renderClientes();
};

const searchProdutos = () => {
    renderProdutos();
};

// Inicialização da aplicação
const init = () => {
    // Carregar dados do localStorage
    const clientes = storage.load('crud-clojure-clientes', []);
    const produtos = storage.load('crud-clojure-produtos', []);
    
    updateState({ clientes, produtos });
    
    // Renderizar interface inicial
    showTab('clientes');
    
    console.log('🚀 CRUD Clojure inicializado com sucesso!');
    console.log('📊 Estatísticas:', {
        clientes: clientes.length,
        produtos: produtos.length
    });
};

// Inicializar quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', init);