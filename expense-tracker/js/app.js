/**
 * app.js — Application Bootstrap & Page Routing
 * Wires together DB, UI and Charts modules.
 * Handles navigation, month navigation, and all user events.
 */

(() => {
  // ── App State ──────────────────────────────────────────────────────────
  const state = {
    currentPage:      'expenses',   // 'expenses' | 'add' | 'stats' | 'categories'
    currentMonth:     '',           // "YYYY-MM"
    currentYear:      0,
    activeCategoryId: null,         // filter on expenses page
    selectedCatId:    null,         // selected on add-expense form
    statsPeriod:      'monthly',    // 'monthly' | 'yearly'
    pendingDeleteId:  null,         // expense id awaiting delete confirm
  };

  // ── Initialise ──────────────────────────────────────────────────────────
  function init() {
    DB.init();
    _setCurrentMonth(new Date());
    _bindNav();
    _bindMonthNav();
    _bindAddExpenseForm();
    _bindAddCategoryForm();
    _bindStatsToggle();
    _bindFab();
    _showPage('expenses');
  }

  // ─────────────────────────────────────────────────────────────────────
  //  NAVIGATION
  // ─────────────────────────────────────────────────────────────────────
  function _bindNav() {
    document.querySelectorAll('.nav-item').forEach(btn => {
      btn.addEventListener('click', () => {
        const page = btn.dataset.page;
        _showPage(page);
        document.querySelectorAll('.nav-item').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
      });
    });
  }

  function _showPage(pageId) {
    state.currentPage = pageId;

    // Toggle pages
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
    document.getElementById(`page-${pageId}`).classList.add('active');

    // Show/hide FAB (only on expenses page)
    const fab = document.getElementById('fabBtn');
    fab.classList.toggle('hidden', pageId !== 'expenses');

    // Show/hide month picker in header (not on categories)
    const monthPicker = document.getElementById('headerMonthPicker');
    monthPicker.style.visibility = pageId === 'categories' ? 'hidden' : 'visible';

    // Per-page actions
    switch (pageId) {
      case 'expenses':
        _refreshExpensesPage();
        break;
      case 'add':
        _initAddForm();
        break;
      case 'stats':
        _refreshStatsPage();
        break;
      case 'categories':
        UI.renderIconPicker();
        UI.renderColorPicker();
        UI.renderCatManageList(_onDeleteCategory);
        break;
    }
  }

  // ─────────────────────────────────────────────────────────────────────
  //  MONTH NAVIGATION
  // ─────────────────────────────────────────────────────────────────────
  function _setCurrentMonth(date) {
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    state.currentMonth = `${y}-${m}`;
    state.currentYear  = y;
    _updateMonthLabel();
  }

  function _updateMonthLabel() {
    const [y, m]   = state.currentMonth.split('-');
    const date     = new Date(Number(y), Number(m) - 1, 1);
    const label    = date.toLocaleDateString('en-IN', { month: 'long', year: 'numeric' });
    document.getElementById('monthLabel').textContent = label;
  }

  function _bindMonthNav() {
    document.getElementById('prevMonth').addEventListener('click', () => {
      const [y, m] = state.currentMonth.split('-').map(Number);
      const d      = new Date(y, m - 2, 1); // go back one month
      _setCurrentMonth(d);
      state.activeCategoryId = null;
      if (state.currentPage === 'expenses') _refreshExpensesPage();
      if (state.currentPage === 'stats')    _refreshStatsPage();
    });

    document.getElementById('nextMonth').addEventListener('click', () => {
      const [y, m] = state.currentMonth.split('-').map(Number);
      const d      = new Date(y, m, 1); // go forward one month
      _setCurrentMonth(d);
      state.activeCategoryId = null;
      if (state.currentPage === 'expenses') _refreshExpensesPage();
      if (state.currentPage === 'stats')    _refreshStatsPage();
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  FAB
  // ─────────────────────────────────────────────────────────────────────
  function _bindFab() {
    document.getElementById('fabBtn').addEventListener('click', () => {
      _showPage('add');
      // Sync bottom nav highlight
      document.querySelectorAll('.nav-item').forEach(b => {
        b.classList.toggle('active', b.dataset.page === 'add');
      });
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  EXPENSES PAGE
  // ─────────────────────────────────────────────────────────────────────
  function _refreshExpensesPage() {
    UI.renderSummary(state.currentMonth, state.activeCategoryId);
    UI.renderCategoryChips(state.activeCategoryId, _onCategoryChipSelect);
    UI.renderExpenseList(state.currentMonth, state.activeCategoryId, _onDeleteExpenseRequest);
  }

  function _onCategoryChipSelect(catId) {
    state.activeCategoryId = catId;
    _refreshExpensesPage();
  }

  function _onDeleteExpenseRequest(id) {
    state.pendingDeleteId = id;
    UI.showDeleteModal(() => {
      DB.deleteExpense(state.pendingDeleteId);
      state.pendingDeleteId = null;
      _refreshExpensesPage();
      UI.showToast('Expense deleted');
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  ADD EXPENSE FORM
  // ─────────────────────────────────────────────────────────────────────
  function _initAddForm() {
    // Default date = today
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('expDate').value = today;

    // Reset fields
    document.getElementById('expAmount').value = '';
    document.getElementById('expNote').value   = '';

    // Default to first category
    if (!state.selectedCatId) {
      const cats = DB.getCategories();
      state.selectedCatId = cats.length ? cats[0].id : null;
    }
    UI.renderCategoryGrid(state.selectedCatId, _onCatGridSelect);
  }

  function _onCatGridSelect(catId) {
    state.selectedCatId = catId;
    UI.renderCategoryGrid(catId, _onCatGridSelect);
  }

  function _bindAddExpenseForm() {
    document.getElementById('saveExpenseBtn').addEventListener('click', () => {
      const amount = parseFloat(document.getElementById('expAmount').value);
      const note   = document.getElementById('expNote').value.trim();
      const date   = document.getElementById('expDate').value;
      const catId  = state.selectedCatId;

      if (!amount || amount <= 0) {
        UI.showToast('⚠️ Please enter a valid amount');
        return;
      }
      if (!date) {
        UI.showToast('⚠️ Please pick a date');
        return;
      }
      if (!catId) {
        UI.showToast('⚠️ Please select a category');
        return;
      }

      DB.addExpense({ amount, note, date, categoryId: catId });
      UI.showToast('✅ Expense saved!');

      // Go to home
      _showPage('expenses');
      document.querySelectorAll('.nav-item').forEach(b => {
        b.classList.toggle('active', b.dataset.page === 'expenses');
      });
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  CATEGORIES PAGE
  // ─────────────────────────────────────────────────────────────────────
  function _bindAddCategoryForm() {
    document.getElementById('saveCategoryBtn').addEventListener('click', () => {
      const { name, icon, color } = UI.getNewCategoryFormValues();
      if (!name) {
        UI.showToast('⚠️ Please enter a category name');
        return;
      }
      // Duplicate check
      const existing = DB.getCategories().find(c => c.name.toLowerCase() === name.toLowerCase());
      if (existing) {
        UI.showToast('⚠️ Category already exists');
        return;
      }

      DB.addCategory({ name, icon, color });
      UI.resetNewCategoryForm();
      UI.renderCatManageList(_onDeleteCategory);
      UI.showToast(`✅ "${name}" added!`);
    });
  }

  function _onDeleteCategory(id) {
    const cat = DB.getCategoryById(id);
    if (!cat || cat.isDefault) return;
    UI.showDeleteModal(() => {
      DB.deleteCategory(id);
      UI.renderCatManageList(_onDeleteCategory);
      UI.showToast('Category deleted');
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  STATS PAGE
  // ─────────────────────────────────────────────────────────────────────
  function _refreshStatsPage() {
    if (state.statsPeriod === 'monthly') {
      Charts.renderMonthly(state.currentMonth);
    } else {
      Charts.renderYearly(state.currentYear);
    }
  }

  function _bindStatsToggle() {
    document.querySelectorAll('.stats-tab').forEach(tab => {
      tab.addEventListener('click', () => {
        state.statsPeriod = tab.dataset.period;
        document.querySelectorAll('.stats-tab').forEach(t => t.classList.remove('active'));
        tab.classList.add('active');

        document.getElementById('statsMonthly').classList.toggle('hidden', state.statsPeriod !== 'monthly');
        document.getElementById('statsYearly').classList.toggle('hidden', state.statsPeriod !== 'yearly');

        _refreshStatsPage();
      });
    });
  }

  // ── Boot ────────────────────────────────────────────────────────────────
  document.addEventListener('DOMContentLoaded', init);
})();
