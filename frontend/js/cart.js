/**
 * cart.js — Shopping Cart Logic
 *
 * The cart is stored in localStorage so it persists across page refreshes.
 * Each cart item looks like:
 *   { id, name, price, quantity, imageUrl, category }
 */

const CART_KEY = 'hito_cart';

// ----------------------------------------------------------------
// Core cart operations
// ----------------------------------------------------------------

/** Read cart from localStorage */
function getCart() {
    try {
        return JSON.parse(localStorage.getItem(CART_KEY)) || [];
    } catch {
        return [];
    }
}

/** Write cart to localStorage */
function saveCart(cart) {
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
}

/**
 * Add a product to the cart.
 * If it already exists, just increase the quantity.
 */
function addToCart(product) {
    const cart = getCart();
    const existingIndex = cart.findIndex(item => item.id === product.id);

    if (existingIndex !== -1) {
        cart[existingIndex].quantity += 1;
    } else {
        cart.push({
            id:       product.id,
            name:     product.name,
            price:    parseFloat(product.price),
            quantity: 1,
            imageUrl: product.imageUrl || null,
            category: product.category
        });
    }

    saveCart(cart);
    updateCartUI();
    showToast(`✅ ${product.name} added to cart!`, 'success');
}

/** Increase quantity of a cart item */
function increaseQty(productId) {
    const cart = getCart();
    const item = cart.find(i => i.id === productId);
    if (item) {
        item.quantity += 1;
        saveCart(cart);
        updateCartUI();
    }
}

/** Decrease quantity — removes item if quantity reaches 0 */
function decreaseQty(productId) {
    let cart = getCart();
    const item = cart.find(i => i.id === productId);
    if (!item) return;

    if (item.quantity <= 1) {
        removeFromCart(productId);
    } else {
        item.quantity -= 1;
        saveCart(cart);
        updateCartUI();
    }
}

/** Remove an item from cart entirely */
function removeFromCart(productId) {
    let cart = getCart().filter(i => i.id !== productId);
    saveCart(cart);
    updateCartUI();
}

/** Clear all items from cart */
function clearCart() {
    saveCart([]);
    updateCartUI();
}

// ----------------------------------------------------------------
// Cart UI Rendering
// ----------------------------------------------------------------

/**
 * Sync all cart-related UI elements on the page:
 * - Cart count badge on the nav button
 * - Cart sidebar items list
 * - Totals
 */
function updateCartUI() {
    const cart = getCart();
    const totalQty = cart.reduce((sum, i) => sum + i.quantity, 0);
    const totalPrice = cart.reduce((sum, i) => sum + (i.price * i.quantity), 0);

    // Update cart count badge(s)
    document.querySelectorAll('#cartCount').forEach(el => {
        el.textContent = totalQty;
    });

    // Update totals
    const fmt = n => `₱${n.toFixed(2)}`;
    document.querySelectorAll('#cartSubtotal, #cartTotal').forEach(el => {
        el.textContent = fmt(totalPrice);
    });

    // Render items in sidebar
    renderCartItems(cart, totalPrice);
}

/** Render the cart item list inside the sidebar */
function renderCartItems(cart, totalPrice) {
    const list = document.getElementById('cartItemsList');
    if (!list) return;

    if (cart.length === 0) {
        list.innerHTML = `
            <div class="cart-empty-msg">
                <div class="empty-icon">🛒</div>
                <p>Your cart is empty.</p>
                <a href="menu.html" class="btn btn-outline btn-sm" style="margin-top:1rem;">
                    Browse Menu
                </a>
            </div>
        `;
        return;
    }

    list.innerHTML = cart.map(item => `
        <div class="cart-item">
            <div class="cart-item-img">
                ${item.imageUrl
                    ? `<img src="${item.imageUrl}" alt="${item.name}">`
                    : '🐟'
                }
            </div>
            <div class="cart-item-info">
                <div class="cart-item-name">${item.name}</div>
                <div class="cart-item-price">₱${(item.price * item.quantity).toFixed(2)}</div>
            </div>
            <div class="cart-item-qty">
                <button class="qty-btn" onclick="decreaseQty(${item.id})">−</button>
                <span class="qty-display">${item.quantity}</span>
                <button class="qty-btn" onclick="increaseQty(${item.id})">+</button>
            </div>
            <button class="cart-remove-btn" onclick="removeFromCart(${item.id})" title="Remove">
                🗑️
            </button>
        </div>
    `).join('');
}

// ----------------------------------------------------------------
// Cart Sidebar Toggle
// ----------------------------------------------------------------

function toggleCart() {
    const sidebar  = document.getElementById('cartSidebar');
    const overlay  = document.getElementById('cartOverlay');
    if (!sidebar || !overlay) return;

    sidebar.classList.toggle('open');
    overlay.classList.toggle('open');
    // Prevent background scroll when cart is open
    document.body.style.overflow = sidebar.classList.contains('open') ? 'hidden' : '';
}
