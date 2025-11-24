// Admin Panel JavaScript
class AdminPanel {
    constructor() {
        this.currentSection = 'dashboard';
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.loadDashboardData();
        this.setupNavigation();
    }

    setupEventListeners() {
        // Mobile menu toggle
        document.getElementById('menuToggle').addEventListener('click', () => {
            document.getElementById('sidebar').classList.toggle('active');
        });

        // Search functionality
        const searchInput = document.querySelector('.admin-search input');
        searchInput.addEventListener('input', (e) => {
            this.handleSearch(e.target.value);
        });
    }

    setupNavigation() {
        const navItems = document.querySelectorAll('.nav-item[data-section]');
        navItems.forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                const section = item.getAttribute('data-section');
                this.showSection(section);
                
                // Update active nav item
                navItems.forEach(nav => nav.classList.remove('active'));
                item.classList.add('active');
            });
        });
    }

    showSection(section) {
        // Hide all sections
        document.querySelectorAll('.content-section').forEach(sec => {
            sec.classList.remove('active');
        });

        // Show selected section
        const targetSection = document.getElementById(`${section}-section`);
        if (targetSection) {
            targetSection.classList.add('active');
            this.currentSection = section;
            this.updatePageTitle(section);
            this.loadSectionData(section);
        }
    }

    updatePageTitle(section) {
        const titles = {
            'dashboard': 'Dashboard',
            'products': 'Product Management',
            'orders': 'Order Management',
            'analytics': 'Analytics',
            'customers': 'Customer Management',
            'settings': 'Settings'
        };

        document.getElementById('page-title').textContent = titles[section] || 'Admin Panel';
    }

    async loadSectionData(section) {
        switch(section) {
            case 'dashboard':
                await this.loadDashboardData();
                break;
            case 'products':
                await this.loadProducts();
                break;
            case 'orders':
                await this.loadOrders();
                break;
        }
    }

    async loadDashboardData() {
        try {
            this.showLoading('stats-grid');
            
            // Load today's stats
            const statsResponse = await this.apiCall('/api/admin/stats/today');
            this.renderStats(statsResponse.data);
            
            // Load recent orders
            await this.loadRecentOrders();
            
        } catch (error) {
            console.error('Error loading dashboard data:', error);
            this.showError('stats-grid', 'Failed to load dashboard data');
        }
    }

    async loadProducts() {
        try {
            this.showLoading('products-body');
            
            const response = await this.apiCall('/api/admin/products/all');
            this.renderProducts(response.data);
            
            // Update products count in nav
            document.getElementById('products-count').textContent = response.data.length;
            
        } catch (error) {
            console.error('Error loading products:', error);
            this.showError('products-body', 'Failed to load products');
        }
    }

    async loadOrders() {
        try {
            this.showLoading('orders-body');
            this.showLoading('recent-orders-body');
            
            const response = await this.apiCall('/api/admin/orders/all');
            this.renderOrders(response.data);
            this.renderRecentOrders(response.data.slice(0, 5));
            
            // Update orders count in nav
            document.getElementById('orders-count').textContent = response.data.length;
            
        } catch (error) {
            console.error('Error loading orders:', error);
            this.showError('orders-body', 'Failed to load orders');
        }
    }

    async loadOrderDetail(orderId) {
        try {
            this.showLoading('order-detail-content');
            
            const response = await this.apiCall(`/api/admin/orders/${orderId}`);
            this.renderOrderDetail(response.data);
            this.showSection('order-detail');
            
        } catch (error) {
            console.error('Error loading order details:', error);
            this.showError('order-detail-content', 'Failed to load order details');
        }
    }

    renderStats(stats) {
        const statsGrid = document.getElementById('stats-grid');
        
        const statsData = [
            {
                icon: 'fas fa-dollar-sign',
                class: 'icon-revenue',
                value: `₹${stats.revenue?.toLocaleString() || '0'}`,
                label: 'Today\'s Revenue',
                change: stats.revenueChange || 0
            },
            {
                icon: 'fas fa-shopping-cart',
                class: 'icon-orders',
                value: stats.orders?.toString() || '0',
                label: 'Today\'s Orders',
                change: stats.ordersChange || 0
            },
            {
                icon: 'fas fa-users',
                class: 'icon-customers',
                value: stats.customers?.toString() || '0',
                label: 'New Customers',
                change: stats.customersChange || 0
            },
            {
                icon: 'fas fa-box',
                class: 'icon-products',
                value: stats.products?.toString() || '0',
                label: 'Total Products',
                change: stats.productsChange || 0
            }
        ];

        statsGrid.innerHTML = statsData.map(stat => `
            <div class="stat-card">
                <div class="stat-icon ${stat.class}">
                    <i class="${stat.icon}"></i>
                </div>
                <div class="stat-info">
                    <h3>${stat.value}</h3>
                    <p>${stat.label}</p>
                    <div class="stat-change ${stat.change >= 0 ? 'change-positive' : 'change-negative'}">
                        <i class="fas fa-${stat.change >= 0 ? 'arrow-up' : 'arrow-down'}"></i>
                        ${Math.abs(stat.change)}%
                    </div>
                </div>
            </div>
        `).join('');
    }

    renderProducts(products) {
        const productsBody = document.getElementById('products-body');
        
        productsBody.innerHTML = products.map(product => `
            <tr>
                <td>#${product.id}</td>
                <td>
                    <div style="display: flex; align-items: center; gap: 10px;">
                        <img src="${product.image}" alt="${product.name}" style="width: 40px; height: 40px; border-radius: 6px; object-fit: cover;">
                        <div>
                            <div style="font-weight: 500;">${product.name}</div>
                            <div style="font-size: 12px; color: var(--muted-text);">${product.category}</div>
                        </div>
                    </div>
                </td>
                <td>${product.category}</td>
                <td>₹${product.price}</td>
                <td>${product.stock}</td>
                <td>
                    <span class="status-badge ${product.status === 'active' ? 'status-processing' : 'status-cancelled'}">
                        ${product.status}
                    </span>
                </td>
                <td>
                    <div class="action-buttons">
                        <button class="btn-icon btn-view" onclick="adminPanel.viewProduct(${product.id})">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn-icon btn-edit" onclick="adminPanel.editProduct(${product.id})">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-icon btn-delete" onclick="adminPanel.deleteProduct(${product.id})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');
    }

    renderOrders(orders) {
        const ordersBody = document.getElementById('orders-body');
        
        ordersBody.innerHTML = orders.map(order => `
            <tr>
                <td>#${order.id}</td>
                <td>${order.customerName}</td>
                <td>${new Date(order.date).toLocaleDateString()}</td>
                <td>${order.items.length} items</td>
                <td>₹${order.totalAmount}</td>
                <td>
                    <span class="status-badge ${this.getStatusClass(order.status)}">
                        ${order.status}
                    </span>
                </td>
                <td>
                    <div class="action-buttons">
                        <button class="btn-icon btn-view" onclick="adminPanel.loadOrderDetail(${order.id})">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn-icon btn-edit" onclick="adminPanel.editOrder(${order.id})">
                            <i class="fas fa-edit"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');
    }

    renderRecentOrders(orders) {
        const recentOrdersBody = document.getElementById('recent-orders-body');
        
        recentOrdersBody.innerHTML = orders.map(order => `
            <tr>
                <td>#${order.id}</td>
                <td>${order.customerName}</td>
                <td>${new Date(order.date).toLocaleDateString()}</td>
                <td>₹${order.totalAmount}</td>
                <td>
                    <span class="status-badge ${this.getStatusClass(order.status)}">
                        ${order.status}
                    </span>
                </td>
                <td>
                    <button class="btn-icon btn-view" onclick="adminPanel.loadOrderDetail(${order.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    }

    renderOrderDetail(order) {
        const orderDetailContent = document.getElementById('order-detail-content');
        
        orderDetailContent.innerHTML = `
            <div style="padding: 25px;">
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 30px; margin-bottom: 30px;">
                    <div>
                        <h3 style="margin-bottom: 15px; color: var(--primary-color);">Order Information</h3>
                        <div style="background: #f8f9fa; padding: 20px; border-radius: 8px;">
                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px; font-size: 14px;">
                                <div><strong>Order ID:</strong> #${order.id}</div>
                                <div><strong>Date:</strong> ${new Date(order.date).toLocaleString()}</div>
                                <div><strong>Status:</strong> 
                                    <span class="status-badge ${this.getStatusClass(order.status)}">
                                        ${order.status}
                                    </span>
                                </div>
                                <div><strong>Total Amount:</strong> ₹${order.totalAmount}</div>
                            </div>
                        </div>
                    </div>
                    
                    <div>
                        <h3 style="margin-bottom: 15px; color: var(--primary-color);">Customer Information</h3>
                        <div style="background: #f8f9fa; padding: 20px; border-radius: 8px;">
                            <div style="font-size: 14px;">
                                <div style="margin-bottom: 8px;"><strong>Name:</strong> ${order.customerName}</div>
                                <div style="margin-bottom: 8px;"><strong>Email:</strong> ${order.customerEmail}</div>
                                <div style="margin-bottom: 8px;"><strong>Phone:</strong> ${order.customerPhone}</div>
                                <div><strong>Address:</strong> ${order.shippingAddress}</div>
                            </div>
                        </div>
                    </div>
                </div>

                <h3 style="margin-bottom: 15px; color: var(--primary-color);">Order Items</h3>
                <div class="table-container">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${order.items.map(item => `
                                <tr>
                                    <td>
                                        <div style="display: flex; align-items: center; gap: 10px;">
                                            <img src="${item.image}" alt="${item.name}" style="width: 40px; height: 40px; border-radius: 6px; object-fit: cover;">
                                            <div>${item.name}</div>
                                        </div>
                                    </td>
                                    <td>₹${item.price}</td>
                                    <td>${item.quantity}</td>
                                    <td>₹${item.price * item.quantity}</td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
    }

    getStatusClass(status) {
        const statusClasses = {
            'pending': 'status-pending',
            'processing': 'status-processing',
            'completed': 'status-completed',
            'cancelled': 'status-cancelled'
        };
        return statusClasses[status.toLowerCase()] || 'status-pending';
    }

    async apiCall(endpoint) {
        // Simulate API call - replace with actual API calls
        return new Promise((resolve) => {
            setTimeout(() => {
                // Mock data - replace with actual API response
                const mockData = this.getMockData(endpoint);
                resolve({ data: mockData });
            }, 1000);
        });
    }

    getMockData(endpoint) {
        const mockData = {
            '/api/admin/stats/today': {
                revenue: 125000,
                revenueChange: 12.5,
                orders: 45,
                ordersChange: 8.2,
                customers: 23,
                customersChange: 15.7,
                products: 156,
                productsChange: 3.2
            },
            '/api/admin/products/all': [
                {
                    id: 1,
                    name: "Premium Laptop",
                    category: "Electronics",
                    price: 55000,
                    stock: 15,
                    status: "active",
                    image: "../Assets/product1.jpg"
                },
                {
                    id: 2,
                    name: "Wireless Headphones",
                    category: "Electronics",
                    price: 3500,
                    stock: 42,
                    status: "active",
                    image: "../Assets/product2.jpg"
                },
                {
                    id: 3,
                    name: "Running Shoes",
                    category: "Footwear",
                    price: 2800,
                    stock: 28,
                    status: "active",
                    image: "../Assets/product3.jpg"
                }
            ],
            '/api/admin/orders/all': [
                {
                    id: 1,
                    customerName: "John Doe",
                    customerEmail: "john@example.com",
                    customerPhone: "+91 9876543210",
                    date: "2024-01-15T10:30:00Z",
                    totalAmount: 58500,
                    status: "completed",
                    shippingAddress: "123 Main St, Mumbai, Maharashtra 400001",
                    items: [
                        { name: "Premium Laptop", price: 55000, quantity: 1, image: "../Assets/product1.jpg" },
                        { name: "Wireless Headphones", price: 3500, quantity: 1, image: "../Assets/product2.jpg" }
                    ]
                },
                {
                    id: 2,
                    customerName: "Jane Smith",
                    customerEmail: "jane@example.com",
                    customerPhone: "+91 9876543211",
                    date: "2024-01-14T14:20:00Z",
                    totalAmount: 5600,
                    status: "processing",
                    shippingAddress: "456 Park Ave, Delhi, Delhi 110001",
                    items: [
                        { name: "Running Shoes", price: 2800, quantity: 2, image: "../Assets/product3.jpg" }
                    ]
                }
            ]
        };

        const orderDetailMatch = endpoint.match(/\/api\/admin\/orders\/(\d+)/);
        if (orderDetailMatch) {
            const orderId = parseInt(orderDetailMatch[1]);
            const orders = mockData['/api/admin/orders/all'];
            return orders.find(order => order.id === orderId) || orders[0];
        }

        return mockData[endpoint] || [];
    }

    showLoading(elementId) {
        const element = document.getElementById(elementId);
        if (element) {
            element.classList.add('loading');
        }
    }

    hideLoading(elementId) {
        const element = document.getElementById(elementId);
        if (element) {
            element.classList.remove('loading');
        }
    }

    showError(elementId, message) {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = `<div style="text-align: center; padding: 40px; color: var(--muted-text);">
                <i class="fas fa-exclamation-triangle" style="font-size: 48px; margin-bottom: 20px;"></i>
                <p>${message}</p>
                <button class="btn btn-primary" onclick="adminPanel.loadSectionData('${this.currentSection}')">
                    Try Again
                </button>
            </div>`;
        }
    }

    handleSearch(query) {
        // Implement search functionality
        console.log('Searching for:', query);
    }

    // Product management methods
    viewProduct(productId) {
        console.log('View product:', productId);
        // Implement product view
    }

    editProduct(productId) {
        console.log('Edit product:', productId);
        // Implement product edit
    }

    deleteProduct(productId) {
        if (confirm('Are you sure you want to delete this product?')) {
            console.log('Delete product:', productId);
            // Implement product deletion
        }
    }

    // Order management methods
    editOrder(orderId) {
        console.log('Edit order:', orderId);
        // Implement order edit
    }
}

// Initialize admin panel when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.adminPanel = new AdminPanel();
});

// Global functions for HTML onclick handlers
function showSection(section) {
    if (window.adminPanel) {
        window.adminPanel.showSection(section);
    }
}

function loadOrders() {
    if (window.adminPanel) {
        window.adminPanel.loadOrders();
    }
}

function showAddProductForm() {
    // Implement add product form
    alert('Add product form would open here');
}