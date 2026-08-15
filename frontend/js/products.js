/**
 * products.js — Product Fetching and Rendering
 *
 * This file:
 * 1. Fetches products from the Spring Boot REST API
 * 2. Renders them as product cards
 * 3. Handles filtering by category
 * 4. Shows a product detail modal
 *
 * If the backend is offline, it falls back to sample data so the
 * frontend still works during development.
 */

const API_BASE = 'http://localhost:8080/api';

// ----------------------------------------------------------------
// SAMPLE DATA — used when the backend is not running
// ----------------------------------------------------------------

const SAMPLE_PRODUCTS = [
    {
        id: 1,
        name: 'Classic Fried Hito',
        description: 'Crispy whole catfish deep-fried to golden perfection. Served with sawsawan and steamed rice.',
        price: 150,
        category: 'Fried Hito',
        imageUrl: null,
        available: true
    },
    {
        id: 2,
        name: 'Grilled Hito Special',
        description: 'Juicy catfish marinated in our secret blend of spices and grilled over charcoal.',
        price: 175,
        category: 'Grilled Hito',
        imageUrl: null,
        available: true
    },
    {
        id: 3,
        name: 'Fresh Hito (Whole)',
        description: 'Fresh pond-raised catfish, cleaned and ready to cook. Sold per kilo.',
        price: 120,
        category: 'Fresh Hito',
        imageUrl: null,
        available: true
    },
    {
        id: 4,
        name: 'Hito Sinabawang Set',
        description: 'Tender catfish in a rich savory broth with vegetables. A complete comfort meal.',
        price: 195,
        category: 'Hito Meals',
        imageUrl: null,
        available: true
    },
    {
        id: 5,
        name: 'Spicy Hito Sisig',
        description: 'Our famous sizzling Hito sisig — spicy, crispy, and absolutely addictive.',
        price: 220,
        category: 'Hito Specials',
        imageUrl: null,
        available: true
    },
    {
        id: 6,
        name: 'Fried Hito Fillet',
        description: 'Boneless catfish fillet, lightly battered and fried. Perfect for kids!',
        price: 180,
        category: 'Fried Hito',
        imageUrl: null,
        available: true
    },
    {
        id: 7,
        name: 'Hito Kare-Kare',
        description: 'Catfish in rich peanut sauce with vegetables and bagoong on the side.',
        price: 250,
        category: 'Hito Specials',
        imageUrl: null,
        available: true
    },
    {
        id: 8,
        name: 'Hito Meal Box',
        description: 'Fried Hito + 2 cups of rice + sawsawan. The best value meal deal.',
        price: 199,
        category: 'Hito Meals',
        imageUrl: null,
        available: true
    }
];

// All products cached after the first fetch
let allProducts = [];

// ----------------------------------------------------------------
// Fetching from the API
// ----------------------------------------------------------------

/**
 * Fetch products from the backend.
 * Falls back to SAMPLE_PRODUCTS if the backend is unreachable.
 */
async function fetchProducts() {
    try {
        const response = await fetch(`${API_BASE}/products`, {
            signal: AbortSignal.timeout(5000) // 5-second timeout
        });

        if (!response.ok) throw new Error(`HTTP ${response.status}`);

        const data = await response.json();
        // Use sample data if backend returns empty (DB not seeded yet)
        return data.length > 0 ? data : SAMPLE_PRODUCTS;

    } catch (err) {
        console.warn('Backend not reachable, using sample data.', err.message);
        return SAMPLE_PRODUCTS;
    }
}

// ----------------------------------------------------------------
// Building a Product Card
// ----------------------------------------------------------------

/**
 * Creates the HTML string for one product card.
 * Called for every product in the grid.
 */
function buildProductCard(product) {
    const imgContent = product.imageUrl
        ? `<img src="${product.imageUrl}" alt="${product.name}" loading="lazy">`
        : `<span style="font-size:3.5rem;">🐟</span>`;

    const available = product.available !== false;

    return `
        <div class="product-card" onclick="openProductModal(${product.id})">
            <div class="product-img">
                ${imgContent}
                <span class="product-badge">${product.category}</span>
                ${!available ? '<span class="product-badge" style="left:auto;right:12px;background:#6b7280;">Unavailable</span>' : ''}
            </div>
            <div class="product-body">
                <div class="product-category">${product.category}</div>
                <h3 class="product-name">${product.name}</h3>
                <p class="product-desc">${product.description}</p>
                <div class="product-footer">
                    <span class="product-price">₱${parseFloat(product.price).toFixed(2)}</span>
                    ${available
                        ? `<button class="add-to-cart-btn"
                              onclick="event.stopPropagation(); addToCart(${JSON.stringify(product).replace(/"/g, '&quot;')})"
                              title="Add to Cart">
                              +
                           </button>`
                        : `<span style="font-size:.8rem;color:#6b7280;">Unavailable</span>`
                    }
                </div>
            </div>
        </div>
    `;
}

// ----------------------------------------------------------------
// Load Featured Products (Home Page — max 4)
// ----------------------------------------------------------------

async function loadFeaturedProducts() {
    const grid = document.getElementById('featuredProductsGrid');
    if (!grid) return;

    allProducts = await fetchProducts();
    const featured = allProducts.slice(0, 4);

    grid.innerHTML = featured.map(buildProductCard).join('');
}

// ----------------------------------------------------------------
// Load All Products (Menu Page)
// ----------------------------------------------------------------

async function loadMenuProducts() {
    const grid = document.getElementById('menuProductsGrid');
    if (!grid) return;

    grid.innerHTML = `<div class="loading-state"><div class="spinner"></div><p>Loading products...</p></div>`;

    allProducts = await fetchProducts();

    // If using sample data, show a notice
    if (allProducts === SAMPLE_PRODUCTS) {
        const errEl = document.getElementById('menuError');
        if (errEl) errEl.style.display = 'block';
    }

    renderProductGrid(allProducts);
}

/** Render a list of products into the menu grid */
function renderProductGrid(products) {
    const grid = document.getElementById('menuProductsGrid');
    if (!grid) return;

    if (products.length === 0) {
        grid.innerHTML = `
            <div class="loading-state" style="grid-column:1/-1;">
                <p>No products found in this category.</p>
            </div>
        `;
        return;
    }

    grid.innerHTML = products.map(buildProductCard).join('');
}

// ----------------------------------------------------------------
// Category Filtering
// ----------------------------------------------------------------

/**
 * Called when a filter button is clicked.
 * @param {string} category - 'all' or a specific category name
 * @param {HTMLElement} btn - the clicked button element
 */
function filterProducts(category, btn) {
    // Update active button state
    document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');

    // Filter and re-render
    const filtered = category === 'all'
        ? allProducts
        : allProducts.filter(p => p.category === category);

    renderProductGrid(filtered);
}

// ----------------------------------------------------------------
// Product Detail Modal
// ----------------------------------------------------------------

function openProductModal(productId) {
    const product = allProducts.find(p => p.id === productId);
    if (!product) return;

    const modal   = document.getElementById('productModal');
    const content = document.getElementById('modalContent');
    if (!modal || !content) return;

    const imgContent = product.imageUrl
        ? `<img src="${product.imageUrl}" alt="${product.name}" style="width:100%;height:280px;object-fit:cover;">`
        : `<div style="width:100%;height:200px;background:linear-gradient(135deg,#d0e8da,#a8d5bc);display:flex;align-items:center;justify-content:center;font-size:5rem;">🐟</div>`;

    content.innerHTML = `
        ${imgContent}
        <div style="padding:2rem;">
            <div style="font-size:.78rem;color:var(--color-accent);font-weight:600;letter-spacing:1.5px;text-transform:uppercase;margin-bottom:.4rem;">
                ${product.category}
            </div>
            <h2 style="font-family:var(--font-heading);color:var(--color-primary);margin-bottom:.6rem;">
                ${product.name}
            </h2>
            <p style="color:var(--color-text-muted);margin-bottom:1.5rem;line-height:1.7;">
                ${product.description}
            </p>

            <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:1.5rem;">
                <span style="font-size:1.6rem;font-weight:700;color:var(--color-primary);font-family:var(--font-heading);">
                    ₱${parseFloat(product.price).toFixed(2)}
                </span>

                <!-- Quantity selector inside modal -->
                <div style="display:flex;align-items:center;gap:.75rem;">
                    <button onclick="modalChangeQty(-1)" class="qty-btn">−</button>
                    <span id="modalQty" style="font-weight:700;min-width:24px;text-align:center;">1</span>
                    <button onclick="modalChangeQty(1)" class="qty-btn">+</button>
                </div>
            </div>

            <div style="display:flex;gap:1rem;">
                ${product.available !== false
                    ? `<button onclick="addToCartFromModal(${product.id})" class="btn btn-primary" style="flex:1;justify-content:center;">
                           Add to Cart 🛒
                       </button>`
                    : `<span style="color:#6b7280;">Currently Unavailable</span>`
                }
                <button onclick="closeProductModal()" class="btn btn-outline">Close</button>
            </div>
        </div>
    `;

    modal.style.display = 'flex';
    document.body.style.overflow = 'hidden';
}

function closeProductModal() {
    const modal = document.getElementById('productModal');
    if (modal) modal.style.display = 'none';
    document.body.style.overflow = '';
}

// Close modal when clicking outside the content box
document.addEventListener('click', (e) => {
    const modal = document.getElementById('productModal');
    if (modal && e.target === modal) closeProductModal();
});

let modalQty = 1;

function modalChangeQty(delta) {
    modalQty = Math.max(1, modalQty + delta);
    const el = document.getElementById('modalQty');
    if (el) el.textContent = modalQty;
}

function addToCartFromModal(productId) {
    const product = allProducts.find(p => p.id === productId);
    if (!product) return;

    for (let i = 0; i < modalQty; i++) {
        addToCart(product);
    }
    modalQty = 1;
    closeProductModal();
}
