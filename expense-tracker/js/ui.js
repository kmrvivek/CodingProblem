/**
 * ui.js — UI Rendering & Interaction Layer
 * Responsible for building and updating all DOM elements.
 * Reads from DB, never writes to localStorage directly.
 */

const UI = (() => {

  // ─── Currency formatter ───────────────────────────────────────────────
  const fmt = new Intl.NumberFormat('en-IN', {
    style:    'currency',
    currency: 'INR',
    minimumFractionDigits: 2,
  });

  // ─── Icons available in the category icon picker ─────────────────────
  const ICON_OPTIONS = [
    '🍕','🍣','🥗','🍜','🌮','🍦','🥤','🍷',
    '🚗','🚇','🛵','✈️','🚢','🚲','⛽','🅿️',
    '👗','👟','💄','🎁','🛒','📱','💻','🎮',
    '💊','🏥','💉','🧘','🏋️','🩺','🛁','🌿',
    '⚡','📡','🏠','🔧','🧹','💈','📦','🔑',
    '🎬','🎵','🎨','📷','🏖️','🎯','🎲','🎤',
    '📚','🖊️','🎓','🔬','🏫','📐','🗂️','🧮',
    '☕','🍵','🧃','🥛','🍺','🧋','🥂','🍾',
    '💰','🏦','💳','📈','💼','🤝','🧾','📊',
  ];

  // ─── Colour palette for new categories ───────────────────────────────
  const COLOR_OPTIONS = [
    '#ff6b6b','#ffa94d','#ffd43b','#a9e34b','#63e6be',
    '#74c0fc','#a78bfa','#f06595','#38d9a9','#ff8787',
    '#66d9e8','#da77f2','#4dabf7','#51cf66','#868e96',
    '#a0522d','#228be6','#e64980','#fab005','#37b24d',
  ];

  // ── State for new-category form ───────────────────────────────────────
  let _selectedIcon  = ICON_OPTIONS[0];
  let _selectedColor = COLOR_OPTIONS[0];

  // ─────────────────────────────────────────────────────────────────────
  //  CATEGORY CHIPS  (top of expense page)
  // ─────────────────────────────────────────────────────────────────────
  function renderCategoryChips(activeCatId, onSelect) {
    const wrap = document.getElementById('categoryChips');
    const cats = DB.getCategories();

    // "All" chip
    const allChip = _el('button', 'chip' + (activeCatId === null ? ' active' : ''));
    allChip.textContent = '🔖 All';
    allChip.addEventListener('click', () => onSelect(null));
    wrap.innerHTML = '';
    wrap.appendChild(allChip);

    cats.forEach(cat => {
      const chip = _el('button', 'chip' + (activeCatId === cat.id ? ' active' : ''));
      chip.textContent = `${cat.icon} ${cat.name}`;
      chip.addEventListener('click', () => onSelect(cat.id));
      wrap.appendChild(chip);
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  EXPENSE LIST
  // ─────────────────────────────────────────────────────────────────────
  function renderExpenseList(yearMonth, activeCatId, onDelete) {
    const list   = document.getElementById('expenseList');
    const empty  = document.getElementById('emptyExpenses');
    let expenses = DB.getExpensesByMonth(yearMonth);

    if (activeCatId) {
      expenses = expenses.filter(e => e.categoryId === activeCatId);
    }

    // Sort newest first
    expenses.sort((a, b) => b.date.localeCompare(a.date) || b.createdAt - a.createdAt);

    list.innerHTML = '';

    if (!expenses.length) {
      empty.classList.remove('hidden');
      list.classList.add('hidden');
      return;
    }

    empty.classList.add('hidden');
    list.classList.remove('hidden');

    // Group by date
    const groups = {};
    expenses.forEach(e => {
      if (!groups[e.date]) groups[e.date] = [];
      groups[e.date].push(e);
    });

    Object.keys(groups).sort((a, b) => b.localeCompare(a)).forEach(date => {
      // Date header
      const header = _el('div', 'date-header');
      header.style.cssText = 'font-size:.75rem;font-weight:700;color:var(--text-muted);text-transform:uppercase;letter-spacing:.5px;padding:4px 0 6px';
      header.textContent = _formatDateHeader(date);
      list.appendChild(header);

      groups[date].forEach(exp => {
        const cat = DB.getCategoryById(exp.categoryId) || { icon: '💼', color: '#868e96', name: 'Other' };
        const item = _el('div', 'expense-item');

        const iconWrap = _el('div', 'exp-icon');
        iconWrap.style.background = cat.color + '22';
        iconWrap.textContent = cat.icon;

        const info = _el('div', 'exp-info');
        const note = _el('div', 'exp-note');
        note.textContent = exp.note || cat.name;
        const meta = _el('div', 'exp-meta');
        meta.textContent = cat.name;

        info.appendChild(note);
        info.appendChild(meta);

        const right = _el('div', 'exp-right');
        const amount = _el('div', 'exp-amount');
        amount.textContent = fmt.format(exp.amount);

        const del = _el('button', 'exp-delete');
        del.title = 'Delete';
        del.innerHTML = '🗑️';
        del.addEventListener('click', () => onDelete(exp.id));

        right.appendChild(amount);
        right.appendChild(del);

        item.appendChild(iconWrap);
        item.appendChild(info);
        item.appendChild(right);
        list.appendChild(item);
      });
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  SUMMARY CARD
  // ─────────────────────────────────────────────────────────────────────
  function renderSummary(yearMonth, activeCatId) {
    let expenses = DB.getExpensesByMonth(yearMonth);
    if (activeCatId) expenses = expenses.filter(e => e.categoryId === activeCatId);

    const total = expenses.reduce((s, e) => s + e.amount, 0);
    document.getElementById('totalSpent').textContent = fmt.format(total);
    document.getElementById('totalTxn').textContent   = expenses.length;

    const label = activeCatId
      ? (DB.getCategoryById(activeCatId)?.name || 'Category')
      : 'All Categories';
    document.getElementById('activeFilterLabel').textContent = label;
  }

  // ─────────────────────────────────────────────────────────────────────
  //  CATEGORY GRID  (Add Expense form)
  // ─────────────────────────────────────────────────────────────────────
  function renderCategoryGrid(selectedId, onSelect) {
    const grid = document.getElementById('categoryGrid');
    grid.innerHTML = '';
    DB.getCategories().forEach(cat => {
      const btn = _el('button', 'cat-btn' + (cat.id === selectedId ? ' selected' : ''));
      const icon = _el('span', 'cat-btn-icon');
      icon.textContent = cat.icon;
      const name = _el('span', '');
      name.textContent = cat.name;
      btn.appendChild(icon);
      btn.appendChild(name);
      btn.addEventListener('click', () => onSelect(cat.id));
      grid.appendChild(btn);
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  ICON PICKER  (Category form)
  // ─────────────────────────────────────────────────────────────────────
  function renderIconPicker() {
    const picker = document.getElementById('iconPicker');
    picker.innerHTML = '';
    ICON_OPTIONS.forEach(icon => {
      const opt = _el('span', 'icon-opt' + (icon === _selectedIcon ? ' selected' : ''));
      opt.textContent = icon;
      opt.addEventListener('click', () => {
        _selectedIcon = icon;
        picker.querySelectorAll('.icon-opt').forEach(o => o.classList.remove('selected'));
        opt.classList.add('selected');
        document.getElementById('selectedIconPreview').textContent = icon;
      });
      picker.appendChild(opt);
    });
    document.getElementById('selectedIconPreview').textContent = _selectedIcon;
  }

  // ─────────────────────────────────────────────────────────────────────
  //  COLOUR PICKER  (Category form)
  // ─────────────────────────────────────────────────────────────────────
  function renderColorPicker() {
    const picker = document.getElementById('colorPicker');
    picker.innerHTML = '';
    COLOR_OPTIONS.forEach(color => {
      const opt = _el('span', 'color-opt' + (color === _selectedColor ? ' selected' : ''));
      opt.style.background = color;
      opt.title = color;
      opt.addEventListener('click', () => {
        _selectedColor = color;
        picker.querySelectorAll('.color-opt').forEach(o => o.classList.remove('selected'));
        opt.classList.add('selected');
      });
      picker.appendChild(opt);
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  CATEGORY MANAGE LIST
  // ─────────────────────────────────────────────────────────────────────
  function renderCatManageList(onDelete) {
    const list = document.getElementById('catManageList');
    list.innerHTML = '';
    const allExp = DB.getExpenses();

    DB.getCategories().forEach(cat => {
      const count   = allExp.filter(e => e.categoryId === cat.id).length;
      const item    = _el('div', 'cat-manage-item');

      const iconWrap = _el('div', 'cat-manage-icon');
      iconWrap.style.background = cat.color + '22';
      iconWrap.textContent = cat.icon;

      const info   = _el('div', 'cat-manage-info');
      const name   = _el('div', 'cat-manage-name');
      name.textContent = cat.name;
      const badge  = _el('div', 'cat-manage-badge');
      badge.textContent = `${count} expense${count !== 1 ? 's' : ''}`;
      info.appendChild(name);
      info.appendChild(badge);

      const del = _el('button', 'cat-manage-delete');
      del.title = cat.isDefault ? 'Default categories cannot be deleted' : 'Delete category';
      del.innerHTML = cat.isDefault ? '🔒' : '🗑️';
      if (!cat.isDefault) {
        del.addEventListener('click', () => onDelete(cat.id));
      } else {
        del.style.cursor = 'default';
      }

      item.appendChild(iconWrap);
      item.appendChild(info);
      item.appendChild(del);
      list.appendChild(item);
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  GET FORM VALUES  (Add Category)
  // ─────────────────────────────────────────────────────────────────────
  function getNewCategoryFormValues() {
    return {
      name:  document.getElementById('catName').value.trim(),
      icon:  _selectedIcon,
      color: _selectedColor,
    };
  }

  function resetNewCategoryForm() {
    document.getElementById('catName').value = '';
    _selectedIcon  = ICON_OPTIONS[0];
    _selectedColor = COLOR_OPTIONS[0];
    renderIconPicker();
    renderColorPicker();
  }

  // ─────────────────────────────────────────────────────────────────────
  //  TOAST
  // ─────────────────────────────────────────────────────────────────────
  let _toastTimer = null;
  function showToast(msg) {
    const toast = document.getElementById('toast');
    toast.textContent = msg;
    toast.classList.remove('hidden');
    clearTimeout(_toastTimer);
    _toastTimer = setTimeout(() => toast.classList.add('hidden'), 2500);
  }

  // ─────────────────────────────────────────────────────────────────────
  //  DELETE MODAL
  // ─────────────────────────────────────────────────────────────────────
  function showDeleteModal(onConfirm) {
    const overlay = document.getElementById('deleteModal');
    overlay.classList.remove('hidden');

    const confirmBtn = document.getElementById('confirmDeleteBtn');
    const cancelBtn  = document.getElementById('cancelDeleteBtn');

    function cleanup() {
      overlay.classList.add('hidden');
      confirmBtn.replaceWith(confirmBtn.cloneNode(true));
      cancelBtn.replaceWith(cancelBtn.cloneNode(true));
    }

    document.getElementById('confirmDeleteBtn').addEventListener('click', () => {
      onConfirm();
      cleanup();
    });
    document.getElementById('cancelDeleteBtn').addEventListener('click', cleanup);
    overlay.addEventListener('click', e => { if (e.target === overlay) cleanup(); }, { once: true });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  PRIVATE HELPERS
  // ─────────────────────────────────────────────────────────────────────
  function _el(tag, className) {
    const el = document.createElement(tag);
    if (className) el.className = className;
    return el;
  }

  function _formatDateHeader(dateStr) {
    const d     = new Date(dateStr + 'T00:00:00');
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const diff  = (today - d) / 86400000;
    if (diff === 0) return 'Today';
    if (diff === 1) return 'Yesterday';
    return d.toLocaleDateString('en-IN', { weekday: 'short', day: 'numeric', month: 'short' });
  }

  // ─────────────────────────────────────────────────────────────────────
  return {
    fmt,
    renderCategoryChips,
    renderExpenseList,
    renderSummary,
    renderCategoryGrid,
    renderIconPicker,
    renderColorPicker,
    renderCatManageList,
    getNewCategoryFormValues,
    resetNewCategoryForm,
    showToast,
    showDeleteModal,
  };
})();
