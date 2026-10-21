document.addEventListener('DOMContentLoaded', () => {
  const api = '/api';
  const grid = document.getElementById('productGrid');
  const cartItems = [];
  const money = value => 'S/ ' + Number(value).toFixed(2);
  let searchTimer;

  async function request(url, options = {}) {
    const response = await fetch(url, { headers: { 'Content-Type': 'application/json', ...(options.headers || {}) }, ...options });
    if (!response.ok) {
      let message = `Error ${response.status}`;
      try { message = (await response.json()).mensaje || message; } catch (_) {}
      throw new Error(message);
    }
    return response.status === 204 ? null : response.json();
  }

  async function loadProducts(term = '') {
    grid.innerHTML = '<p class="text-center">Cargando productos…</p>';
    try {
      const products = await request(term ? `${api}/productos/buscar?nombre=${encodeURIComponent(term)}` : `${api}/productos`);
      grid.replaceChildren();
      if (!products.length) { grid.innerHTML = '<p class="text-center">No encontramos productos con ese nombre.</p>'; return; }
      products.forEach(product => grid.appendChild(productCard(product)));
    } catch (error) { grid.innerHTML = `<p class="text-center text-danger">No se pudieron cargar los productos: ${escapeText(error.message)}</p>`; }
  }

  function productCard(p) {
    const col = document.createElement('div'); col.className = 'col-12 col-sm-6 col-lg-3';
    const card = document.createElement('article'); card.className = 'card producto-card h-100';
    const wrap = document.createElement('div'); wrap.className = 'producto-img-wrap';
    const img = document.createElement('img'); img.className = 'card-img-top producto-img'; img.alt = p.nombre || 'Producto marino';
    img.src = p.imagen ? `/img/${encodeURIComponent(p.imagen)}` : '/img/placeholder.svg'; img.onerror = () => { img.onerror = null; img.src = '/img/placeholder.svg'; };
    wrap.appendChild(img);
    const body = document.createElement('div'); body.className = 'card-body d-flex flex-column';
    const title = document.createElement('h3'); title.className = 'h5 card-title producto-nombre'; title.textContent = p.nombre;
    const desc = document.createElement('p'); desc.className = 'card-text producto-desc'; desc.textContent = p.descripcion || `${p.tipo || 'Marisco'} · ${p.origen || 'Costa peruana'}`;
    const details = document.createElement('p'); details.className = 'small text-muted'; details.textContent = `${p.presentacion || ''}${p.origen ? ' · ' + p.origen : ''} · Stock: ${p.stock}`;
    const row = document.createElement('div'); row.className = 'd-flex justify-content-between align-items-center mt-auto mb-3';
    const price = document.createElement('span'); price.className = 'producto-precio'; price.textContent = money(p.precio); row.appendChild(price);
    const button = document.createElement('button'); button.type = 'button'; button.className = 'btn btn-agregar w-100'; button.dataset.productId = p.idProducto; button.textContent = p.stock > 0 ? 'Agregar al carrito' : 'Sin stock'; button.disabled = p.stock <= 0;
    body.append(title, desc, details, row, button); card.append(wrap, body); col.appendChild(card); return col;
  }

  document.getElementById('searchInput').addEventListener('input', event => {
    clearTimeout(searchTimer); searchTimer = setTimeout(() => loadProducts(event.target.value.trim()), 250);
  });
  grid.addEventListener('click', async event => {
    const button = event.target.closest('[data-product-id]'); if (!button) return;
    try {
      const p = await request(`${api}/productos/${button.dataset.productId}`);
      const existing = cartItems.find(item => item.idProducto === p.idProducto);
      if (existing) { if (existing.cantidad >= p.stock) return showToast('No hay más unidades disponibles.'); existing.cantidad++; }
      else cartItems.push({ idProducto: p.idProducto, nombre: p.nombre, precio: Number(p.precio), imagen: p.imagen, cantidad: 1, stock: p.stock });
      renderCart(); showToast(`${p.nombre} agregado al carrito.`);
    } catch (error) { showToast(error.message); }
  });

  function renderCart() {
    const list = document.getElementById('cartItems'); list.replaceChildren();
    let count = 0, total = 0;
    cartItems.forEach(item => {
      count += item.cantidad; total += item.precio * item.cantidad;
      const li = document.createElement('li'); li.className = 'carrito-item';
      const img = document.createElement('img'); img.src = item.imagen ? `/img/${encodeURIComponent(item.imagen)}` : '/img/placeholder.svg'; img.onerror = () => { img.onerror = null; img.src = '/img/placeholder.svg'; }; img.alt = item.nombre;
      const info = document.createElement('div'); info.className = 'carrito-item-info';
      const name = document.createElement('div'); name.className = 'carrito-item-nombre'; name.textContent = item.nombre;
      const price = document.createElement('div'); price.className = 'carrito-item-precio'; price.textContent = `${money(item.precio)} c/u`;
      const qty = document.createElement('div'); qty.className = 'carrito-cantidad';
      const dec = cartAction('−', () => { item.cantidad--; if (!item.cantidad) cartItems.splice(cartItems.indexOf(item), 1); renderCart(); });
      const num = document.createElement('span'); num.textContent = item.cantidad;
      const inc = cartAction('+', () => { if (item.cantidad >= item.stock) return showToast('No hay más unidades disponibles.'); item.cantidad++; renderCart(); });
      qty.append(dec, num, inc); info.append(name, price, qty);
      const subtotal = document.createElement('strong'); subtotal.className = 'carrito-item-subtotal'; subtotal.textContent = money(item.precio * item.cantidad);
      li.append(img, info, subtotal); list.appendChild(li);
    });
    document.getElementById('cartCount').textContent = count;
    document.getElementById('cartTotal').textContent = money(total);
    document.getElementById('cartEmpty').hidden = cartItems.length > 0;
    document.getElementById('cartSummary').hidden = cartItems.length === 0;
  }
  function cartAction(label, action) { const b = document.createElement('button'); b.type = 'button'; b.textContent = label; b.addEventListener('click', action); return b; }
  document.getElementById('clearCart').addEventListener('click', () => { cartItems.length = 0; renderCart(); });
  document.getElementById('checkoutButton').addEventListener('click', () => { document.getElementById('checkoutForm').hidden = false; document.getElementById('orderMessage').textContent = ''; });
  document.getElementById('checkoutForm').addEventListener('submit', async event => {
    event.preventDefault(); const form = event.currentTarget; if (!form.reportValidity() || cartItems.length === 0) return;
    const message = document.getElementById('orderMessage');
    const payload = { nombreCliente: document.getElementById('orderName').value.trim(), telefono: document.getElementById('orderPhone').value.trim(), tipoEntrega: document.getElementById('orderDelivery').value, items: cartItems.map(({ idProducto, cantidad }) => ({ idProducto, cantidad })) };
    try { const result = await request(`${api}/pedidos`, { method: 'POST', body: JSON.stringify(payload) }); message.className = 'text-success mt-2'; message.textContent = `Pedido #${result.idPedido} registrado. Total: ${money(result.total)}.`; cartItems.length = 0; renderCart(); form.reset(); loadProducts(document.getElementById('searchInput').value.trim()); }
    catch (error) { message.className = 'text-danger mt-2'; message.textContent = error.message; }
  });

  document.getElementById('toggleAdmin').addEventListener('click', () => { const section = document.getElementById('adminSection'); section.hidden = !section.hidden; if (!section.hidden) section.scrollIntoView({ behavior: 'smooth' }); });
  document.getElementById('adminForm').addEventListener('submit', async event => {
    event.preventDefault(); const f = event.currentTarget; if (!f.reportValidity()) return;
    const data = new FormData(f); const product = Object.fromEntries(data.entries()); product.precio = Number(product.precio); product.stock = Number(product.stock); product.activo = true;
    try { await request(`${api}/productos`, { method: 'POST', body: JSON.stringify(product) }); document.getElementById('adminMessage').className = 'ms-3 text-success'; document.getElementById('adminMessage').textContent = 'Producto guardado.'; f.reset(); loadProducts(); }
    catch (error) { document.getElementById('adminMessage').className = 'ms-3 text-danger'; document.getElementById('adminMessage').textContent = error.message; }
  });
  document.getElementById('contactForm').addEventListener('submit', async event => {
    event.preventDefault();
    const f = event.currentTarget;
    const reglas = {
        cNombre: valor => valor !== '',
        cEmail: valor => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor),
        cAsunto: valor => valor !== '',
        cMensaje: valor => valor !== ''
    };
    let valido = true;

    Object.keys(reglas).forEach(id => {
        const campo = document.getElementById(id);
        const error = f.querySelector(`[data-error="${id}"]`);
        const correcto = reglas[id](campo.value.trim());
        campo.classList.toggle('is-invalid', !correcto);
        error.style.display = correcto ? 'none' : 'block';
        if (!correcto) valido = false;
    });

    if (!valido) return;

    const message = document.getElementById('contactMessage');
    const payload = {
        nombre: document.getElementById('cNombre').value.trim(),
        email: document.getElementById('cEmail').value.trim(),
        asunto: document.getElementById('cAsunto').value.trim(),
        mensaje: document.getElementById('cMensaje').value.trim()
    };
    try {
        await request(`${api}/contacto`, { method: 'POST', body: JSON.stringify(payload) });
        message.className = 'text-success mt-3';
        message.textContent = 'Mensaje enviado. Gracias por escribirnos.';
        f.reset();
        Object.keys(reglas).forEach(id => {
            document.getElementById(id).classList.remove('is-invalid');
            f.querySelector(`[data-error="${id}"]`).style.display = 'none';
        });
    } catch (error) {
        message.className = 'text-danger mt-3';
        message.textContent = error.message;
    }
});
  function showToast(text) { const element = document.getElementById('shopToast'); document.getElementById('shopToastText').textContent = text; bootstrap.Toast.getOrCreateInstance(element, { delay: 1800 }).show(); }
  function escapeText(value) { const node = document.createElement('span'); node.textContent = value; return node.innerHTML; }
  loadProducts(); renderCart();
});
