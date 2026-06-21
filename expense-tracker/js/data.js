/**
 * data.js — LocalStorage Data Layer
 * All read/write operations for categories and expenses live here.
 * Data persists across page refreshes via localStorage.
 */

const DB = (() => {
  const KEYS = {
    CATEGORIES: 'spendly_categories',
    EXPENSES:   'spendly_expenses',
  };

  // ── Default categories shipped with the app ──────────────────────────
  const DEFAULT_CATEGORIES = [
    { id: 'food',          name: 'Food',          icon: '🍔', color: '#ff6b6b', isDefault: true },
    { id: 'transport',     name: 'Transport',     icon: '🚌', color: '#ffa94d', isDefault: true },
    { id: 'shopping',      name: 'Shopping',      icon: '🛍️', color: '#a78bfa', isDefault: true },
    { id: 'entertainment', name: 'Fun',           icon: '🎬', color: '#f06595', isDefault: true },
    { id: 'health',        name: 'Health',        icon: '🏥', color: '#51cf66', isDefault: true },
    { id: 'bills',         name: 'Bills',         icon: '⚡', color: '#ffd43b', isDefault: true },
    { id: 'education',     name: 'Education',     icon: '📚', color: '#4dabf7', isDefault: true },
    { id: 'travel',        name: 'Travel',        icon: '✈️', color: '#38d9a9', isDefault: true },
    { id: 'coffee',        name: 'Coffee',        icon: '☕', color: '#a0522d', isDefault: true },
    { id: 'groceries',     name: 'Groceries',     icon: '🛒', color: '#74c0fc', isDefault: true },
    { id: 'fitness',       name: 'Fitness',       icon: '🏋️', color: '#63e6be', isDefault: true },
    { id: 'other',         name: 'Other',         icon: '💼', color: '#868e96', isDefault: true },
  ];

  // ── Internal helpers ──────────────────────────────────────────────────
  function _load(key, fallback) {
    try {
      const raw = localStorage.getItem(key);
      return raw ? JSON.parse(raw) : fallback;
    } catch {
      return fallback;
    }
  }

  function _save(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
  }

  function _uid() {
    return Date.now().toString(36) + Math.random().toString(36).slice(2, 7);
  }

  // ── Initialise (seeds defaults only once) ────────────────────────────
  function init() {
    if (!localStorage.getItem(KEYS.CATEGORIES)) {
      _save(KEYS.CATEGORIES, DEFAULT_CATEGORIES);
    }
    if (!localStorage.getItem(KEYS.EXPENSES)) {
      _save(KEYS.EXPENSES, []);
    }
  }

  // ── Categories ────────────────────────────────────────────────────────
  function getCategories() {
    return _load(KEYS.CATEGORIES, DEFAULT_CATEGORIES);
  }

  function addCategory({ name, icon, color }) {
    const cats = getCategories();
    const newCat = {
      id:        _uid(),
      name:      name.trim(),
      icon,
      color,
      isDefault: false,
    };
    cats.push(newCat);
    _save(KEYS.CATEGORIES, cats);
    return newCat;
  }

  function deleteCategory(id) {
    const cats = getCategories().filter(c => c.id !== id);
    _save(KEYS.CATEGORIES, cats);
    // Orphan expenses get assigned to 'other'
    const expenses = getExpenses().map(e =>
      e.categoryId === id ? { ...e, categoryId: 'other' } : e
    );
    _save(KEYS.EXPENSES, expenses);
  }

  function getCategoryById(id) {
    return getCategories().find(c => c.id === id) || null;
  }

  // ── Expenses ─────────────────────────────────────────────────────────
  function getExpenses() {
    return _load(KEYS.EXPENSES, []);
  }

  /**
   * Returns expenses for a given YYYY-MM string (e.g. "2024-06")
   */
  function getExpensesByMonth(yearMonth) {
    return getExpenses().filter(e => e.date.startsWith(yearMonth));
  }

  /**
   * Returns expenses for a given YYYY string (e.g. "2024")
   */
  function getExpensesByYear(year) {
    return getExpenses().filter(e => e.date.startsWith(year));
  }

  function addExpense({ amount, note, date, categoryId }) {
    const expenses = getExpenses();
    const newExp = {
      id:         _uid(),
      amount:     parseFloat(amount),
      note:       note.trim(),
      date,           // "YYYY-MM-DD"
      categoryId,
      createdAt:  Date.now(),
    };
    expenses.push(newExp);
    _save(KEYS.EXPENSES, expenses);
    return newExp;
  }

  function deleteExpense(id) {
    const expenses = getExpenses().filter(e => e.id !== id);
    _save(KEYS.EXPENSES, expenses);
  }

  // ── Aggregate helpers ─────────────────────────────────────────────────

  /**
   * Returns { categoryId -> totalAmount } map for a set of expenses
   */
  function sumByCategory(expenses) {
    return expenses.reduce((acc, e) => {
      acc[e.categoryId] = (acc[e.categoryId] || 0) + e.amount;
      return acc;
    }, {});
  }

  /**
   * Returns { "YYYY-MM-DD" -> totalAmount } map
   */
  function sumByDay(expenses) {
    return expenses.reduce((acc, e) => {
      acc[e.date] = (acc[e.date] || 0) + e.amount;
      return acc;
    }, {});
  }

  /**
   * Returns { "MM" -> totalAmount } map for a year's expenses
   */
  function sumByMonth(expenses) {
    return expenses.reduce((acc, e) => {
      const month = e.date.slice(5, 7); // "MM"
      acc[month] = (acc[month] || 0) + e.amount;
      return acc;
    }, {});
  }

  /**
   * Hard reset — wipes all data (manual action only)
   */
  function resetAll() {
    localStorage.removeItem(KEYS.CATEGORIES);
    localStorage.removeItem(KEYS.EXPENSES);
    init();
  }

  return {
    init,
    getCategories,
    addCategory,
    deleteCategory,
    getCategoryById,
    getExpenses,
    getExpensesByMonth,
    getExpensesByYear,
    addExpense,
    deleteExpense,
    sumByCategory,
    sumByDay,
    sumByMonth,
    resetAll,
  };
})();
