/**
 * charts.js — Statistics & Chart Rendering
 * Uses Chart.js (MIT license) loaded via CDN.
 * All chart instances are cached so they can be properly destroyed
 * before re-rendering to avoid the "canvas already in use" error.
 */

const Charts = (() => {

  // Cache active Chart.js instances
  const _instances = {};

  // ─── Chart defaults ───────────────────────────────────────────────────
  Chart.defaults.font.family = "'Segoe UI', system-ui, sans-serif";
  Chart.defaults.color       = '#7a7a9d';

  const MONTH_NAMES = [
    'Jan','Feb','Mar','Apr','May','Jun',
    'Jul','Aug','Sep','Oct','Nov','Dec',
  ];

  // ─────────────────────────────────────────────────────────────────────
  //  MONTHLY VIEW
  // ─────────────────────────────────────────────────────────────────────

  /**
   * Donut chart — spending by category for the given month
   */
  function renderMonthlyDonut(yearMonth) {
    const expenses  = DB.getExpensesByMonth(yearMonth);
    const catTotals = DB.sumByCategory(expenses);
    const cats      = DB.getCategories();

    const labels  = [];
    const data    = [];
    const colors  = [];

    cats.forEach(cat => {
      if (catTotals[cat.id]) {
        labels.push(`${cat.icon} ${cat.name}`);
        data.push(parseFloat(catTotals[cat.id].toFixed(2)));
        colors.push(cat.color);
      }
    });

    _destroyChart('donut');

    if (!data.length) {
      _showEmpty('donutChart', 'No data for this month');
      return;
    }

    const ctx = document.getElementById('donutChart').getContext('2d');
    _instances.donut = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels,
        datasets: [{
          data,
          backgroundColor: colors,
          borderWidth:     3,
          borderColor:     '#ffffff',
          hoverOffset:     10,
        }],
      },
      options: {
        responsive:          true,
        maintainAspectRatio: false,
        cutout:              '65%',
        plugins: {
          legend: {
            position:  'bottom',
            labels:    { padding: 12, boxWidth: 12, font: { size: 11 } },
          },
          tooltip: {
            callbacks: {
              label: ctx => ` ₹${ctx.parsed.toLocaleString('en-IN', { minimumFractionDigits: 2 })}`,
            },
          },
        },
      },
    });
  }

  /**
   * Bar chart — daily spending for the given month
   */
  function renderDailyBar(yearMonth) {
    const [year, month] = yearMonth.split('-').map(Number);
    const daysInMonth   = new Date(year, month, 0).getDate();
    const expenses      = DB.getExpensesByMonth(yearMonth);
    const dayTotals     = DB.sumByDay(expenses);

    const labels = [];
    const data   = [];
    const colors = [];

    for (let d = 1; d <= daysInMonth; d++) {
      const key = `${yearMonth}-${String(d).padStart(2, '0')}`;
      labels.push(d);
      const val = dayTotals[key] || 0;
      data.push(parseFloat(val.toFixed(2)));
      colors.push(val > 0 ? '#6c63ff' : '#e4e4f0');
    }

    _destroyChart('dailyBar');

    const ctx = document.getElementById('barChart').getContext('2d');
    _instances.dailyBar = new Chart(ctx, {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          label:           'Daily Spend (₹)',
          data,
          backgroundColor: colors,
          borderRadius:    6,
          borderSkipped:   false,
        }],
      },
      options: {
        responsive:          true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: ctx => ` ₹${ctx.parsed.y.toLocaleString('en-IN', { minimumFractionDigits: 2 })}`,
            },
          },
        },
        scales: {
          x: { grid: { display: false }, ticks: { font: { size: 10 } } },
          y: {
            beginAtZero: true,
            grid:        { color: '#f0f2f7' },
            ticks: {
              font:      { size: 10 },
              callback:  v => '₹' + v,
            },
          },
        },
      },
    });
  }

  /**
   * Category breakdown bars (text list with progress bars)
   */
  function renderCategoryBreakdown(yearMonth) {
    const expenses  = DB.getExpensesByMonth(yearMonth);
    const catTotals = DB.sumByCategory(expenses);
    const cats      = DB.getCategories();
    const total     = expenses.reduce((s, e) => s + e.amount, 0);
    const wrap      = document.getElementById('categoryBreakdown');
    wrap.innerHTML  = '';

    const sorted = cats
      .filter(c => catTotals[c.id])
      .sort((a, b) => catTotals[b.id] - catTotals[a.id]);

    if (!sorted.length) {
      wrap.innerHTML = '<p style="color:var(--text-muted);font-size:.85rem;text-align:center;padding:10px">No expenses this month.</p>';
      return;
    }

    sorted.forEach(cat => {
      const amt     = catTotals[cat.id];
      const pct     = total ? ((amt / total) * 100).toFixed(1) : 0;
      const row     = document.createElement('div');
      row.className = 'breakdown-item';
      row.innerHTML = `
        <div class="breakdown-dot" style="background:${cat.color}"></div>
        <span class="breakdown-name">${cat.icon} ${cat.name}</span>
        <div class="breakdown-bar-wrap">
          <div class="breakdown-bar" style="width:${pct}%;background:${cat.color}"></div>
        </div>
        <span class="breakdown-amount">₹${amt.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</span>
      `;
      wrap.appendChild(row);
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  YEARLY VIEW
  // ─────────────────────────────────────────────────────────────────────

  /**
   * Bar chart — monthly spending for the given year
   */
  function renderYearlyBar(year) {
    document.getElementById('yearLabel').textContent = year;
    const expenses   = DB.getExpensesByYear(String(year));
    const monthTotals = DB.sumByMonth(expenses);

    const data = MONTH_NAMES.map((_, i) => {
      const key = String(i + 1).padStart(2, '0');
      return parseFloat((monthTotals[key] || 0).toFixed(2));
    });

    _destroyChart('yearBar');

    const ctx = document.getElementById('yearBarChart').getContext('2d');
    _instances.yearBar = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: MONTH_NAMES,
        datasets: [{
          label:           'Monthly Spend (₹)',
          data,
          backgroundColor: data.map(v => v > 0 ? '#a78bfa' : '#e4e4f0'),
          borderRadius:    8,
          borderSkipped:   false,
        }],
      },
      options: {
        responsive:          true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: ctx => ` ₹${ctx.parsed.y.toLocaleString('en-IN', { minimumFractionDigits: 2 })}`,
            },
          },
        },
        scales: {
          x: { grid: { display: false }, ticks: { font: { size: 11 } } },
          y: {
            beginAtZero: true,
            grid:        { color: '#f0f2f7' },
            ticks: {
              font:     { size: 10 },
              callback: v => '₹' + v,
            },
          },
        },
      },
    });
  }

  /**
   * Donut — category split for the whole year
   */
  function renderYearlyDonut(year) {
    const expenses  = DB.getExpensesByYear(String(year));
    const catTotals = DB.sumByCategory(expenses);
    const cats      = DB.getCategories();

    const labels = [];
    const data   = [];
    const colors = [];

    cats.forEach(cat => {
      if (catTotals[cat.id]) {
        labels.push(`${cat.icon} ${cat.name}`);
        data.push(parseFloat(catTotals[cat.id].toFixed(2)));
        colors.push(cat.color);
      }
    });

    _destroyChart('yearDonut');

    if (!data.length) {
      _showEmpty('yearDonutChart', 'No data for this year');
      return;
    }

    const ctx = document.getElementById('yearDonutChart').getContext('2d');
    _instances.yearDonut = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels,
        datasets: [{
          data,
          backgroundColor: colors,
          borderWidth:     3,
          borderColor:     '#ffffff',
          hoverOffset:     10,
        }],
      },
      options: {
        responsive:          true,
        maintainAspectRatio: false,
        cutout:              '65%',
        plugins: {
          legend: {
            position: 'bottom',
            labels:   { padding: 12, boxWidth: 12, font: { size: 11 } },
          },
          tooltip: {
            callbacks: {
              label: ctx => ` ₹${ctx.parsed.toLocaleString('en-IN', { minimumFractionDigits: 2 })}`,
            },
          },
        },
      },
    });
  }

  // ─────────────────────────────────────────────────────────────────────
  //  HELPERS
  // ─────────────────────────────────────────────────────────────────────
  function _destroyChart(key) {
    if (_instances[key]) {
      _instances[key].destroy();
      delete _instances[key];
    }
  }

  function _showEmpty(canvasId, msg) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) return;
    const parent = canvas.parentElement;
    canvas.style.display = 'none';
    const p = parent.querySelector('.chart-empty') || document.createElement('p');
    p.className    = 'chart-empty';
    p.style.cssText = 'color:var(--text-muted);font-size:.85rem;text-align:center;padding:30px 0';
    p.textContent  = msg;
    if (!parent.querySelector('.chart-empty')) parent.appendChild(p);
  }

  function resetCanvas(canvasId) {
    const canvas = document.getElementById(canvasId);
    if (canvas) canvas.style.display = '';
    const parent = canvas?.parentElement;
    const empty  = parent?.querySelector('.chart-empty');
    if (empty) empty.remove();
  }

  /**
   * Render all monthly charts
   */
  function renderMonthly(yearMonth) {
    ['donutChart','barChart'].forEach(resetCanvas);
    renderMonthlyDonut(yearMonth);
    renderDailyBar(yearMonth);
    renderCategoryBreakdown(yearMonth);
  }

  /**
   * Render all yearly charts
   */
  function renderYearly(year) {
    ['yearBarChart','yearDonutChart'].forEach(resetCanvas);
    renderYearlyBar(year);
    renderYearlyDonut(year);
  }

  return {
    renderMonthly,
    renderYearly,
  };
})();
