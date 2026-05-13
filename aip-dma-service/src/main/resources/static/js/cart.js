/* ===== CART MANAGER (LocalStorage) ===== */
const CartManager = {
    key: 'FINE_ART_BRONZES_CART',

    getItems() {
        const cart = localStorage.getItem(this.key);
        return cart ? JSON.parse(cart) : [];
    },

    saveItems(items) {
        localStorage.setItem(this.key, JSON.stringify(items));
        this.updateBadge();
    },

    addItem(product) {
        const items = this.getItems();
        const existing = items.find(item => item.id == product.id);
        
        if (existing) {
            existing.quantity += parseInt(product.quantity || 1);
        } else {
            items.push({
                id: product.id,
                name: product.name,
                price: product.price, // string like "15.800.000₫"
                image: product.image,
                slug: product.slug,
                quantity: parseInt(product.quantity || 1)
            });
        }
        this.saveItems(items);
        this.renderDrawer();
    },

    removeItem(id) {
        const items = this.getItems().filter(item => item.id != id);
        this.saveItems(items);
        this.renderDrawer();
        if (window.location.pathname === '/xac-nhan-don-hang') this.renderCheckout();
    },

    updateQuantity(id, delta) {
        const items = this.getItems();
        const item = items.find(item => item.id == id);
        if (item) {
            item.quantity += delta;
            if (item.quantity <= 0) {
                this.removeItem(id);
                return;
            }
        }
        this.saveItems(items);
        this.renderDrawer();
        if (window.location.pathname === '/xac-nhan-don-hang') this.renderCheckout();
    },

    updateBadge() {
        const count = this.getItems().reduce((total, item) => total + item.quantity, 0);
        const badge = document.getElementById('cartBadge');
        if (badge) {
            badge.textContent = count;
            badge.style.display = count > 0 ? 'flex' : 'none';
        }
        const titleCount = document.getElementById('cartCountTitle');
        if (titleCount) titleCount.textContent = `(${count})`;
    },

    formatPrice(num) {
        if (num === 0) return "Liên hệ";
        return new Intl.NumberFormat('vi-VN').format(num) + '₫';
    },

    parsePrice(priceStr) {
        if (!priceStr || priceStr === "Liên hệ") return 0;
        return parseInt(priceStr.replace(/[^\d]/g, ''));
    },

    calculateTotals() {
        const items = this.getItems();
        let total = 0;
        items.forEach(item => {
            total += this.parsePrice(item.price) * item.quantity;
        });
        return total;
    },

    renderDrawer() {
        const items = this.getItems();
        const body = document.getElementById('cartDrawerBody');
        const footer = document.getElementById('cartDrawerFooter');
        
        if (!body) return;

        if (items.length === 0) {
            body.innerHTML = `
                <div class="cart-empty-state">
                    <div class="empty-cart-icon">🛒</div>
                    <p>Giỏ hàng của bạn đang trống.</p>
                    <a href="/san-pham" class="btn btn-outline">Tiếp tục mua sắm</a>
                </div>
            `;
            if (footer) footer.style.display = 'none';
        } else {
            body.innerHTML = items.map(item => `
                <div class="cart-item">
                    <div class="cart-item-img">
                        <img src="${item.image}" alt="${item.name}">
                    </div>
                    <div class="cart-item-info">
                        <div>
                            <a href="/san-pham/${item.slug}" class="cart-item-name">${item.name}</a>
                            <div class="cart-item-price">${item.price}</div>
                        </div>
                        <div class="cart-item-actions">
                            <div class="qty-selector">
                                <button class="qty-btn" onclick="CartManager.updateQuantity(${item.id}, -1)">-</button>
                                <span class="qty-val">${item.quantity}</span>
                                <button class="qty-btn" onclick="CartManager.updateQuantity(${item.id}, 1)">+</button>
                            </div>
                            <span class="remove-item" onclick="CartManager.removeItem(${item.id})">Xóa</span>
                        </div>
                    </div>
                </div>
            `).join('');
            
            if (footer) {
                footer.style.display = 'block';
                const total = this.calculateTotals();
                document.getElementById('cartSubtotal').textContent = this.formatPrice(total);
                document.getElementById('cartTotal').textContent = this.formatPrice(total);
            }
        }
    },

    renderCheckout() {
        const items = this.getItems();
        const list = document.getElementById('checkoutItemsList');
        if (!list) return;

        if (items.length === 0) {
            window.location.href = '/san-pham';
            return;
        }

        list.innerHTML = items.map(item => `
            <div class="checkout-item">
                <div class="checkout-item-img">
                    <img src="${item.image}" alt="${item.name}">
                </div>
                <div class="checkout-item-info">
                    <span class="checkout-item-name">${item.name}</span>
                    <div class="checkout-item-meta">${item.quantity} x ${item.price}</div>
                </div>
            </div>
        `).join('');

        const total = this.calculateTotals();
        document.getElementById('checkoutSubtotal').textContent = this.formatPrice(total);
        document.getElementById('checkoutTotal').textContent = this.formatPrice(total);
    }
};

/* ===== UI LOGIC ===== */
document.addEventListener('DOMContentLoaded', () => {
    CartManager.updateBadge();
    CartManager.renderDrawer();
    if (window.location.pathname === '/xac-nhan-don-hang') CartManager.renderCheckout();

    // Drawer Toggle
    const drawer = document.getElementById('cartDrawer');
    const triggers = document.querySelectorAll('.cart-trigger');
    const closeBtn = document.getElementById('cartDrawerClose');
    const continueBtn = document.getElementById('continueShopping');

    const openDrawer = () => {
        drawer.classList.add('open');
        document.body.style.overflow = 'hidden';
        CartManager.renderDrawer();
    };

    const closeDrawer = () => {
        drawer.classList.remove('open');
        document.body.style.overflow = '';
    };

    triggers.forEach(t => t.addEventListener('click', openDrawer));
    if (closeBtn) closeBtn.addEventListener('click', closeDrawer);
    if (continueBtn) continueBtn.addEventListener('click', closeDrawer);
    
    if (drawer) {
        drawer.addEventListener('click', (e) => {
            if (e.target === drawer) closeDrawer();
        });
    }

    // Add to Cart Buttons
    document.addEventListener('click', (e) => {
        const btn = e.target.closest('.add-to-cart');
        if (btn) {
            e.preventDefault();
            e.stopPropagation();
            const product = {
                id: btn.dataset.id,
                name: btn.dataset.name,
                price: btn.dataset.price,
                image: btn.dataset.image,
                slug: btn.dataset.slug,
                quantity: 1
            };
            CartManager.addItem(product);
            openDrawer();
        }

        const buyNowBtn = e.target.closest('.buy-now');
        if (buyNowBtn) {
            e.preventDefault();
            e.stopPropagation();
            const product = {
                id: buyNowBtn.dataset.id,
                name: buyNowBtn.dataset.name,
                price: buyNowBtn.dataset.price,
                image: buyNowBtn.dataset.image,
                slug: buyNowBtn.dataset.slug,
                quantity: 1
            };
            CartManager.addItem(product);
            window.location.href = '/xac-nhan-don-hang';
        }
    });

    // Checkout Confirmation Methods
    const methodCards = document.querySelectorAll('.confirmation-card');
    const methodInput = document.getElementById('confirmationMethod');
    methodCards.forEach(card => {
        card.addEventListener('click', () => {
            methodCards.forEach(c => c.classList.remove('active'));
            card.classList.add('active');
            if (methodInput) methodInput.value = card.dataset.method;
        });
    });

    // Submit Order
    const checkoutForm = document.getElementById('checkoutForm');
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const submitBtn = document.getElementById('submitOrderBtn');
            submitBtn.disabled = true;
            submitBtn.textContent = 'Đang xử lý...';

            const items = CartManager.getItems().map(item => ({
                productId: item.id,
                quantity: item.quantity
            }));

            const formData = new FormData(checkoutForm);
            const data = {
                fullName: formData.get('fullName'),
                phone: formData.get('phone'),
                email: formData.get('email'),
                address: formData.get('address'),
                confirmationMethod: formData.get('confirmationMethod'),
                note: formData.get('note'),
                items: items
            };

            try {
                const response = await fetch('/api/order', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });
                const result = await response.json();
                
                if (result.success) {
                    CartManager.saveItems([]); // Clear cart
                    window.location.href = `/dat-hang-thanh-cong?code=${result.orderCode}&method=${data.confirmationMethod}`;
                } else {
                    alert('Đã có lỗi xảy ra: ' + result.message);
                    submitBtn.disabled = false;
                    submitBtn.textContent = 'Gửi yêu cầu đặt hàng';
                }
            } catch (err) {
                console.error(err);
                alert('Không thể kết nối đến máy chủ.');
                submitBtn.disabled = false;
                submitBtn.textContent = 'Gửi yêu cầu đặt hàng';
            }
        });
    }
});
