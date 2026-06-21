# Project Athena

---

## Table of Contents

- [1. Mission](#1-mission)
- [2. Core Design Principles](#2-core-design-principles)
- [3. System Architecture (The 6 Layers)](#3-system-architecture-the-6-layers)
- [4. Master Database Schema (DuckDB)](#4-master-database-schema-duckdb)
    - [Layer 1: Data Layer](#layer-1-data-layer-the-source-of-truth)
    - [Layer 2: Knowledge Layer](#layer-2-knowledge-layer-athenas-memory)
    - [Layer 3: Deterministic Analysis Layer](#layer-3-deterministic-analysis-layer-pure-math)
    - [Layer 4: AI Layer](#layer-4-ai-layer-interpretation)
    - [Layer 5: Decision Layer](#layer-5-decision-layer-where-investing-happens)
    - [Layer 6: Output Layer](#layer-6-output-layer-what-you-actually-use)
- [5. Build Roadmap (12 Weeks)](#5-build-roadmap-12-weeks)
    - [Phase 1: Foundation (Weeks 1–2)](#phase-1-foundation-weeks-12)
    - [Phase 2: Deterministic Engines (Weeks 3–4)](#phase-2-deterministic-engines-weeks-34)
    - [Phase 3: Thesis System (Weeks 5–6)](#phase-3-thesis-system-weeks-56)
    - [Phase 4: AI Extraction (Weeks 7–8)](#phase-4-ai-extraction-weeks-78)
    - [Phase 5: Validation Engine (Weeks 9–10)](#phase-5-validation-engine-weeks-910)
    - [Phase 6: Research Feed (Weeks 11–12)](#phase-6-research-feed-weeks-1112)
- [6. Final Success Criteria](#6-final-success-criteria)
- [7. Enhancements for Multibagger Detection](#7-enhancements-for-multibagger-detection)
    - [Enhancement 1: Operating Leverage Tracker](#enhancement-1-operating-leverage-tracker)
    - [Enhancement 2: Capital Allocation Quality Score](#enhancement-2-capital-allocation-quality-score)
    - [Enhancement 3: TAM Expansion Signal](#enhancement-3-tam-expansion-signal)
    - [Enhancement 4: Management Quality Score](#enhancement-4-management-quality-score)
    - [Enhancement 5: Cycle Positioning Detector](#enhancement-5-cycle-positioning-detector)
    - [Enhancement 6: Silent Compounder Screen](#enhancement-6-silent-compounder-screen)
    - [Enhancement 7: Price–Business Divergence Alert](#enhancement-7-pricebusiness-divergence-alert)
    - [Enhancement 8: Annual Report Insights](#enhancement-8-annual-report-insights)
    - [Enhancement 9: Vector DB + RAG (ChromaDB) — Optional](#enhancement-9-vector-db--rag-chromadb--optional)
    - [Enhancement Summary](#enhancement-summary)
    - [Additional Columns Needed in Existing Tables](#additional-columns-needed-in-existing-tables)
- [8. Data Sourcing & Execution Steps](#8-data-sourcing--execution-steps)
- [9. Tech Stack & Data Flow](#9-tech-stack--data-flow)
    - [Tech Stack](#tech-stack)
    - [End-to-End Data Flow Diagram](#end-to-end-data-flow)
    - [Local Folder Structure](#local-folder-structure)
    - [Data Sources](#data-sources)
    - [Step 0: Build Your Universe](#step-0-build-your-universe-screenerin-screen-export)
    - [Step 1: Load Financials (yfinance)](#step-1-financials_loaderpy-yfinance)
    - [Step 1b: BSE XBRL — Latest Quarter + Deep History](#step-1b-bse-xbrl--latest-quarter--deep-history)
    - [Step 2: Load Shareholding (NSE Bulk CSV)](#step-2-shareholding_loaderpy-nse-bulk-csv)
    - [Step 3: Load Prices (yfinance)](#step-3-price_loaderpy)
    - [Step 4: Legacy / Manual Shareholding Fallback](#step-4-legacy--manual-fallback-shareholding_loaderpy)
    - [Step 5: Load Corporate Actions](#step-5-corporate_actions_loaderpy)
    - [Step 6: Register Documents (document_tracker)](#step-6-document_trackerpy)
    - [Step 7: Run Layer 3 Engines (Pure SQL)](#step-7-layer-3-engines-pure-sql)
    - [Step 8: Thesis Input (Streamlit UI)](#step-8-thesis-input-streamlit-ui)
    - [Top 20 Investment Opportunities — Home Screen](#top-20-investment-opportunities--home-screen)
    - [Gemini Model & Cost Breakdown](#gemini-model--cost-breakdown)
    - [Hybrid Model Strategy (Flash vs Flash-Lite)](#hybrid-model-strategy-best-cost--quality-balance)
    - [Step 9: AI Extraction](#step-9-ai-extraction-transcript_parserpy)
    - [Step 10: Daily Orchestration (Prefect)](#step-10-daily-orchestration-prefect)
    - [Critical Gotchas Summary](#critical-gotchas-summary)
- [10. Extended Build Roadmap — Missing Use Cases](#10-extended-build-roadmap--missing-use-cases)
    - [Phase 7: Portfolio P&L + Data Freshness (Weeks 13–14)](#phase-7-portfolio-pl--data-freshness-weeks-1314)
    - [Phase 8: Notifications + Pledge Alerting (Weeks 15–16)](#phase-8-notifications--pledge-alerting-weeks-1516)
    - [Phase 9: Earnings Calendar + News Ingestion (Weeks 17–18)](#phase-9-earnings-calendar--news-ingestion-weeks-1718)
    - [Phase 10: Peer & Sector Relative Ranking (Weeks 19–20)](#phase-10-peer--sector-relative-ranking-weeks-1920)
    - [Phase 11: Signal Backtesting (Weeks 21–22)](#phase-11-signal-backtesting-weeks-2122)
    - [Phase 12: Macro Context Layer + Mobile Digest (Weeks 23–24)](#phase-12-macro-context-layer--mobile-digest-weeks-2324)
    - [Extended Roadmap Summary](#extended-roadmap-summary)
- [11. Use Case Coverage, Gaps, and Recommended Improvements](#11-use-case-coverage-gaps-and-recommended-improvements)
    - [A. Use Cases Athena Already Solves Well](#a-use-cases-athena-already-solves-well)
    - [B. Important Use Cases That Are Missing or Underdeveloped](#b-important-use-cases-that-are-missing-or-underdeveloped)
    - [C. Design Improvements Needed in the Current Spec](#c-design-improvements-needed-in-the-current-spec)
    - [D. Highest-Impact Next Enhancements](#d-highest-impact-next-enhancements)
    - [E. Bottom-Line Assessment](#e-bottom-line-assessment)

---

## 1. Mission

Build a personal, institutional-grade research platform that answers:

- **Daily:** What changed?
- **Weekly:** Which businesses improved? Which businesses deteriorated?
- **Quarterly:** Is my investment thesis still valid?
- **Yearly:** What have I learned about this company over time?

[↑ Back to TOC](#table-of-contents)

---

## 2. Core Design Principles

- **Principle 1: Separate Facts from Opinions.**
  Revenue growth is a fact. "Business improving" is an opinion. Store them separately.

- **Principle 2: Separate Math from AI.**
  PE, ROCE, and Promoter Change are deterministic (Math). Identifying risk changes or thesis strengthening is
  probabilistic (AI).

- **Principle 3: AI never owns truth. AI only interprets truth.**
  Truth lives purely in Financials, Shareholding, Prices, and Events.

[↑ Back to TOC](#table-of-contents)

---

## 3. System Architecture (The 6 Layers)

```
                ATHENA
                  UI
                   │
   ┌─────────────┴─────────────┐
   │                           │
   Research Feed         Deep Research
   │                           │
   └─────────────┬─────────────┘
                 │
         Layer 5: Decision
                 │
   ┌──────────────┼──────────────┐
   │              │              │
   Conviction  Thesis Engine  Alert Engine
   │              │              │
   └──────────────┼──────────────┘
                  │
        Layer 4: AI & Layer 3: Math
                  │
   ┌─────────┬──────┴──┬─────────┬───────┐
   │         │         │         │       │
Financial Ownership Valuation   AI    Events
 Engine    Engine    Engine   Engine  Engine
                  │
         Layer 2: Knowledge
                  │
   ┌───────────────┼───────────────┐
   │               │               │
Business         Events        Documents
Profiles
                  │
         Layer 1: Data (Truth)
```

[↑ Back to TOC](#table-of-contents)

---

## 4. Master Database Schema (DuckDB)

### Layer 1: Data Layer (The Source of Truth)

> No AI here. Pure ingestion.

```sql
CREATE TABLE companies (
    company_id      INTEGER PRIMARY KEY,
    symbol          VARCHAR UNIQUE,
    name            VARCHAR,
    sector          VARCHAR,
    industry        VARCHAR,
    market_cap_cr   DOUBLE,
    active          BOOLEAN
);

CREATE TABLE quarterly_financials (
    company_id  INTEGER,
    quarter     VARCHAR,
    revenue     DOUBLE,
    ebitda      DOUBLE,
    pat         DOUBLE,
    eps         DOUBLE,
    roe         DOUBLE,
    roce        DOUBLE,
    debt        DOUBLE,
    cash        DOUBLE,
    ocf         DOUBLE,
    fcf         DOUBLE,
    PRIMARY KEY (company_id, quarter)
);

CREATE TABLE shareholding (
    company_id  INTEGER,
    quarter     VARCHAR,
    promoter    DOUBLE,
    fii         DOUBLE,
    dii         DOUBLE,
    public      DOUBLE,
    pledged     DOUBLE,
    PRIMARY KEY (company_id, quarter)
);

CREATE TABLE daily_prices (
    company_id      INTEGER,
    date            DATE,
    close           DOUBLE,
    adjusted_close  DOUBLE,
    volume          BIGINT,
    PRIMARY KEY (company_id, date)
);

CREATE TABLE corporate_actions (
    company_id  INTEGER,
    action_type VARCHAR, -- split, bonus, dividend
    action_date DATE,
    split       DOUBLE,
    bonus       DOUBLE,
    dividend    DOUBLE,
    PRIMARY KEY (company_id, action_type, action_date)
);

CREATE TABLE documents (
    document_id     INTEGER PRIMARY KEY,
    company_id      INTEGER,
    document_type   VARCHAR,
    quarter         VARCHAR,
    file_path       VARCHAR,
    checksum        VARCHAR,
    processed       BOOLEAN
);
```

---

### Layer 2: Knowledge Layer (Athena's Memory)

```sql
CREATE TABLE business_profile (
    company_id      INTEGER PRIMARY KEY,
    business_model  TEXT,
    moat            TEXT,
    growth_drivers  TEXT,
    risks           TEXT,
    competitors     TEXT
);

CREATE TABLE company_events (
    event_id    INTEGER PRIMARY KEY,
    company_id  INTEGER,
    event_date  DATE,
    category    VARCHAR, -- e.g., CAPEX, ORDER_WIN, CEO_CHANGE
    description TEXT,
    source      VARCHAR
);

CREATE TABLE research_notes (
    note_id      INTEGER PRIMARY KEY,
    company_id   INTEGER,
    created_date DATE,
    note_type    VARCHAR,
    content      TEXT
);
```

---

### Layer 3: Deterministic Analysis Layer (Pure Math)

> No LLMs allowed here.

```sql
CREATE TABLE quarterly_metrics (
    company_id      INTEGER,
    quarter         VARCHAR,
    revenue_growth  DOUBLE,
    pat_growth      DOUBLE,
    fcf_growth      DOUBLE,
    debt_to_equity  DOUBLE,
    fcf_margin      DOUBLE,
    promoter_change DOUBLE,
    PRIMARY KEY (company_id, quarter)
);

CREATE TABLE valuation_history (
    company_id    INTEGER,
    date          DATE,
    pe            DOUBLE,
    pb            DOUBLE,
    ev_ebitda     DOUBLE,
    pe_percentile DOUBLE,
    PRIMARY KEY (company_id, date)
);

CREATE TABLE ownership_scores (
    company_id      INTEGER,
    quarter         VARCHAR,
    ownership_score DOUBLE,
    PRIMARY KEY (company_id, quarter)
);

CREATE TABLE financial_scores (
    company_id     INTEGER,
    quarter        VARCHAR,
    business_score DOUBLE,
    PRIMARY KEY (company_id, quarter)
);
```

---

### Layer 4: AI Layer (Interpretation)

> AI does not calculate; it interprets text.

```sql
CREATE TABLE ai_extractions (
    company_id      INTEGER,
    quarter         VARCHAR,
    capex_guidance  TEXT,
    margin_guidance TEXT,
    demand_outlook  TEXT,
    quotes          TEXT,
    PRIMARY KEY (company_id, quarter)
);

CREATE TABLE change_analysis (
    company_id       INTEGER,
    quarter          VARCHAR,
    positive_changes TEXT,
    negative_changes TEXT,
    new_risks        TEXT,
    PRIMARY KEY (company_id, quarter)
);

CREATE TABLE thesis_validation (
    company_id  INTEGER,
    quarter     VARCHAR,
    status      VARCHAR, -- 'Strengthening', 'Weakening', 'Broken', 'Needs Review'
    explanation TEXT,
    PRIMARY KEY (company_id, quarter)
);
```

---

### Layer 5: Decision Layer (Where Investing Happens)

```sql
CREATE TABLE investment_thesis (
    thesis_id            INTEGER PRIMARY KEY,
    company_id           INTEGER,
    valid_from           DATE,
    valid_to             DATE,
    is_active            BOOLEAN,
    bull_case            TEXT,
    bear_case            TEXT,
    invalidation_metric  TEXT,
    post_mortem          TEXT
);

CREATE TABLE conviction_history (
    company_id      INTEGER,
    date            DATE,
    business_score  DOUBLE,
    growth_score    DOUBLE,
    ownership_score DOUBLE,
    thesis_score    DOUBLE,
    total_score     DOUBLE,
    PRIMARY KEY (company_id, date)
);

CREATE TABLE watchlist (
    company_id          INTEGER,
    watch_type          VARCHAR,
    target_price        DOUBLE,
    desired_conviction  DOUBLE,
    PRIMARY KEY (company_id, watch_type)
);
```

---

### Layer 6: Output Layer (What You Actually Use)

```sql
CREATE TABLE research_feed (
    feed_date         DATE,
    company_id        INTEGER,
    title             VARCHAR,
    summary           TEXT,
    conviction_change DOUBLE,
    priority          INTEGER,
    PRIMARY KEY (feed_date, company_id)
);

CREATE TABLE alert_log (
    alert_id           INTEGER PRIMARY KEY,
    company_id         INTEGER,
    alert_type         VARCHAR, -- e.g., THESIS_BROKEN, VALUATION_OPPORTUNITY
    payload            TEXT,
    notification_sent  BOOLEAN
);
```

[↑ Back to TOC](#table-of-contents)

---

## 5. Build Roadmap (12 Weeks)

### Phase 1: Foundation (Weeks 1–2)

- **Build:** `companies`, `quarterly_financials`, `daily_prices`, `shareholding`
- **Deliverable:** `SELECT * FROM quarterly_financials` works cleanly.

### Phase 2: Deterministic Engines (Weeks 3–4)

- **Build:** `quarterly_metrics`, `valuation_history`, `financial_scores`, `ownership_scores`
- **Deliverable:** `SELECT * FROM valuation_history` shows the PE percentile accurately.

### Phase 3: Thesis System (Weeks 5–6)

- **Build:** `investment_thesis`, `watchlist`, `research_notes`
- **Deliverable:** You can write "Why am I interested in Persistent?" and Athena stores it safely.

### Phase 4: AI Extraction (Weeks 7–8)

- **Build:** `ai_extractions`, `change_analysis`
- **Deliverable:** Asking "What changed this quarter?" yields a structured, accurate JSON response comparing Q1 vs Q2.

### Phase 5: Validation Engine (Weeks 9–10)

- **Build:** `thesis_validation`, `conviction_history`
- **Deliverable:** The system can reliably output if an active thesis is "Strengthening" or "Weakening".

### Phase 6: Research Feed (Weeks 11–12)

- **Build:** `research_feed`, `alert_log`
- **Deliverable:** Open Athena and see Top Positive Changes, Top Negative Changes, Theses Breaking, and Valuation
  Opportunities in under 60 seconds.

[↑ Back to TOC](#table-of-contents)

---

## 6. Final Success Criteria

Athena succeeds if every **Saturday morning** it can answer these six questions instantly:

1. What improved?
2. What deteriorated?
3. Which thesis is breaking?
4. Which quality companies are cheap?
5. What have I forgotten about this company?
6. **Which quality company just entered early-cycle recovery with an under-owned stock?** ← *The hunter question*

> Questions 1–5 make Athena a great **portfolio monitor**. Question 6 makes it a great **stock picker**.

[↑ Back to TOC](#table-of-contents)

---

## 7. Enhancements for Multibagger Detection

> These enhancements extend the core 6-layer architecture without breaking it.
> Each one plugs into an existing layer and answers a question the base system cannot.
>
> **Design rule stays the same:** Math is math. AI is AI. Facts are facts. Opinions are opinions.

---

### Enhancement 1: Operating Leverage Tracker

**Layer:** 3 (Deterministic Math) | **Priority:** 🔴 High | **Build in:** Phase 2 (Weeks 3–4)

#### Why It Matters

The single most reliable early signal of a multibagger is revenue growing **faster** than costs — operating leverage
kicking in. Your `quarterly_metrics` table tracks growth rates but not the *structure* of that growth. A company with
20% revenue growth and 5% cost growth is in a completely different universe from one with 20% revenue growth and 19%
cost growth. The first one compounds. The second one grinds.

#### Schema

```sql
CREATE TABLE operating_leverage (
    company_id           INTEGER,
    quarter              VARCHAR,
    revenue_growth_yoy   DOUBLE,   -- revenue growth YoY %
    fixed_cost_growth    DOUBLE,   -- employee cost + depreciation growth YoY %
    ebitda_margin        DOUBLE,   -- current quarter EBITDA margin
    ebitda_margin_delta  DOUBLE,   -- ebitda_margin(Q) - ebitda_margin(Q-4), YoY basis
    pat_margin           DOUBLE,
    pat_margin_delta     DOUBLE,   -- PAT margin expansion YoY
    op_leverage_score    DOUBLE,   -- revenue_growth / cost_growth; >1.5 is strong signal
    PRIMARY KEY (company_id, quarter)
);
```

#### How to Implement

1. **Data source:** All inputs come from `quarterly_financials` — no new ingestion needed.
2. **Calculation logic (run after every quarterly ingest):**
   ```python
   # Pseudo-code — runs in Layer 3 pipeline
   def compute_operating_leverage(company_id, quarter):
       current = get_financials(company_id, quarter)
       prior   = get_financials(company_id, quarter_minus_4(quarter))  # same quarter last year

       revenue_growth   = (current.revenue - prior.revenue) / prior.revenue
       ebitda_margin    = current.ebitda / current.revenue
       prior_margin     = prior.ebitda / prior.revenue
       ebitda_margin_delta = ebitda_margin - prior_margin

       # Fixed cost proxy: employee_cost + depreciation (add these to quarterly_financials)
       fixed_cost_growth = (current.fixed_cost - prior.fixed_cost) / prior.fixed_cost
       op_leverage_score = revenue_growth / fixed_cost_growth if fixed_cost_growth > 0 else None

       insert_into_operating_leverage(...)
   ```
3. **Add two columns to `quarterly_financials`:** `employee_cost DOUBLE` and `depreciation DOUBLE` — these come directly
   from the P&L and need to be ingested in Phase 1.
4. **Alert trigger:** If `op_leverage_score > 1.5` AND `ebitda_margin_delta > 0.02` for **2 consecutive quarters**, fire
   a `MARGIN_INFLECTION` alert.

#### Signal to Watch

| `op_leverage_score` | Interpretation                                    |
|---------------------|---------------------------------------------------|
| > 2.0               | Strong operating leverage — business scaling well |
| 1.2 – 2.0           | Healthy — costs controlled as revenue grows       |
| 0.8 – 1.2           | Neutral — margin stable                           |
| < 0.8               | Cost inflation — margin at risk                   |

---

### Enhancement 2: Capital Allocation Quality Score

**Layer:** 3 (Deterministic Math) | **Priority:** 🔴 High | **Build in:** Phase 2 (Weeks 3–4)

#### Why It Matters

Your system tracks ROCE as a snapshot but not as a *trend of management decisions*. The biggest predictor of long-term
multibaggers is what management does with profits. Businesses that earn high ROIC **and** can reinvest at high ROIC for
a long runway are the true compounders — Titan, Bajaj Finance, Asian Paints. Businesses that earn high ROIC but have no
reinvestment runway are just cash cows.

#### Schema

```sql
CREATE TABLE capital_allocation (
    company_id            INTEGER,
    quarter               VARCHAR,
    capex                 DOUBLE,   -- from cash flow statement
    capex_to_cfo          DOUBLE,   -- capex / operating cash flow; <0.5 = asset light
    capex_growth_yoy      DOUBLE,   -- is investment accelerating?
    nopat                 DOUBLE,   -- Net Operating Profit After Tax = EBIT * (1 - tax_rate)
    invested_capital      DOUBLE,   -- total equity + debt - excess cash
    roic                  DOUBLE,   -- nopat / invested_capital
    roic_trend_4q         DOUBLE,   -- roic(Q) - roic(Q-4); positive = improving
    dividend_payout_ratio DOUBLE,   -- dividends / PAT
    buyback_amount        DOUBLE,   -- from corporate announcements
    acquisition_spend     DOUBLE,   -- cash paid for acquisitions (from cash flow)
    PRIMARY KEY (company_id, quarter)
);
```

#### How to Implement

1. **Data sources:**
    - `quarterly_financials` → for EBIT, tax, debt, cash, OCF
    - `corporate_actions` → for dividends and buybacks
    - `company_events` (category = `ACQUISITION`) → for acquisition spend
2. **ROIC calculation:**
   ```python
   def compute_roic(company_id, quarter):
       f = get_financials(company_id, quarter)

       ebit        = f.ebitda - f.depreciation
       tax_rate    = 0.25  # or derive from (PAT / PBT) if PBT is tracked
       nopat       = ebit * (1 - tax_rate)

       # Invested capital = Total Assets - Excess Cash - Non-interest-bearing liabilities
       invested_capital = (f.debt + get_equity(company_id, quarter)) - f.cash

       roic = nopat / invested_capital if invested_capital > 0 else None

       prior_roic   = get_roic(company_id, quarter_minus_4(quarter))
       roic_trend   = roic - prior_roic if prior_roic else None

       insert_into_capital_allocation(...)
   ```
3. **Add to `quarterly_financials`:** `depreciation DOUBLE`, `pbt DOUBLE` (Profit Before Tax), `tax DOUBLE`.
4. **Score to surface in conviction:** Weight ROIC trend in `conviction_history` — a company with ROIC > 20% and ROIC
   improving is worth a higher conviction score than ROIC > 20% but flat.

#### Thresholds

| ROIC   | Interpretation                             |
|--------|--------------------------------------------|
| > 25%  | Exceptional — very likely a compounder     |
| 18–25% | Good — track reinvestment opportunity      |
| 12–18% | Average — need volume growth to compensate |
| < 12%  | Likely a value trap — be cautious          |

---

### Enhancement 3: TAM Expansion Signal

**Layer:** 2 (Knowledge) + 4 (AI) | **Priority:** 🟡 Medium | **Build in:** Phase 4 (Weeks 7–8)

#### Why It Matters

Multibaggers are almost always companies whose **total addressable market just got significantly larger** — a new
regulation, a new geography, an import substitution wave, or a product adjacency. This is where 10-baggers are born, and
it is almost entirely invisible in financial statements until 6–8 quarters later. The only place it shows up early is in
concall transcripts and management commentary.

#### Schema

```sql
-- Layer 2: Structured storage of TAM signals
CREATE TABLE tam_signals (
    signal_id        INTEGER PRIMARY KEY,
    company_id       INTEGER,
    signal_date      DATE,
    signal_type      VARCHAR,   -- 'NEW_GEOGRAPHY', 'NEW_PRODUCT', 'NEW_CUSTOMER_SEGMENT',
                                --  'REGULATORY_TAILWIND', 'IMPORT_SUBSTITUTION', 'EXPORT_OPPORTUNITY'
    description      TEXT,
    estimated_tam_cr DOUBLE,   -- estimated TAM in crores, if quantified by management
    confidence       VARCHAR,  -- 'HIGH', 'MEDIUM', 'LOW'
    source           VARCHAR,  -- 'CONCALL', 'ANNUAL_REPORT', 'NEWS', 'MANUAL'
    validated        BOOLEAN   -- have you personally verified this signal?
);
```

**Add to `ai_extractions` table** (new columns):

```sql
ALTER TABLE ai_extractions ADD COLUMN new_market_mentions    TEXT;  -- geographic / segment expansion
ALTER TABLE ai_extractions ADD COLUMN import_sub_mentions    TEXT;  -- China+1, PLI, make-in-India signals
ALTER TABLE ai_extractions ADD COLUMN regulatory_tailwind    TEXT;  -- new policies benefiting the company
ALTER TABLE ai_extractions ADD COLUMN tam_expansion_score    DOUBLE; -- 0-5, AI-assigned confidence
```

#### How to Implement

1. **AI prompt template for concall extraction (Phase 4 pipeline):**
   ```
   You are analyzing a concall transcript for [COMPANY].
   Extract the following and return as JSON:

   1. new_market_mentions: Any mention of new geographies, new countries, new cities, new customer segments
   2. import_sub_mentions: Any mention of import substitution, China+1, PLI scheme, local manufacturing replacing imports
   3. regulatory_tailwind: New government policies, regulations, or schemes that benefit this company
   4. tam_expansion_score: On a scale of 0-5, how significantly is the company's addressable market expanding this quarter?

   Be conservative. Only flag what management explicitly states.
   ```
2. **Auto-create `tam_signals` row** when `tam_expansion_score >= 3` from AI extraction — mark `validated = FALSE` for
   your manual review.
3. **Feed into `research_feed`** with priority boost: any unvalidated TAM signal gets surfaced in your Saturday review.

---

### Enhancement 4: Management Quality Score

**Layer:** 2 (Knowledge) + 4 (AI) | **Priority:** 🟡 Medium | **Build in:** Phase 4 (Weeks 7–8)

#### Why It Matters

Shareholding data tells you *what* promoters own. This tells you *how good* they are at running the business. Titan,
HDFC Bank, and Pidilite all had one thing in common early — management that told you exactly what they were building and
then built it, quarter after quarter. Evasive or over-promising management is the silent killer of multibaggers.

#### Schema

```sql
-- Layer 2: Static management quality profile (updated manually + AI)
CREATE TABLE management_quality (
    company_id                INTEGER PRIMARY KEY,
    ceo_name                  VARCHAR,
    ceo_tenure_years          DOUBLE,
    guidance_accuracy_score   DOUBLE,   -- math-derived: % of guidance targets met over last 8 quarters (0-10)
    related_party_risk        VARCHAR,  -- 'LOW', 'MEDIUM', 'HIGH' — manual assessment
    capital_allocation_rating VARCHAR,  -- 'EXCELLENT', 'GOOD', 'AVERAGE', 'POOR'
    founder_led               BOOLEAN,
    promoter_holding_pct      DOUBLE,   -- synced from shareholding table
    last_updated              DATE
);

-- Layer 4: Per-quarter AI analysis of concall tone
CREATE TABLE concall_tone_analysis (
    company_id           INTEGER,
    quarter              VARCHAR,
    forward_guidance     TEXT,      -- what they explicitly promised this quarter
    prior_guidance_kept  TEXT,      -- did they deliver on last quarter's promise? (AI comparison)
    accountability_score DOUBLE,    -- 1-10: did they explain misses honestly?
    evasion_signals      TEXT,      -- AI flags: blame macro, vague answers, topic deflection
    optimism_score       DOUBLE,    -- 1-10: overall tone
    credibility_score    DOUBLE,    -- rolling average of accountability_score over 4 quarters
    PRIMARY KEY (company_id, quarter)
);
```

#### How to Implement

1. **Guidance accuracy (Math, Layer 3):**
   ```python
   def compute_guidance_accuracy(company_id):
       # Pull forward_guidance from concall_tone_analysis for Q-1
       # Pull actual revenue/margin from quarterly_financials for Q
       # Score: if actual >= 90% of guided → hit, else miss
       # guidance_accuracy_score = hits / total_guidance_points over last 8 quarters * 10
       pass
   ```
2. **Concall tone AI prompt:**
   ```
   Compare this quarter's concall transcript with last quarter's forward_guidance for [COMPANY].

   Return JSON:
   - prior_guidance_kept: What did management promise last quarter, and did they deliver? Be specific.
   - accountability_score: 1-10. Did they explain misses honestly, or deflect to macro/external factors?
   - evasion_signals: List any phrases that signal evasion (e.g., "challenging environment", "industry headwinds" without specifics)
   - forward_guidance: What are they explicitly promising for next quarter?
   - optimism_score: 1-10. Is the tone grounded or overly promotional?
   ```
3. **Surface in Deep Research UI:** When you open a company, the management quality score should be the first thing you
   see — before any financial data.

---

### Enhancement 5: Cycle Positioning Detector

**Layer:** 3 (Deterministic Math) | **Priority:** 🔴 High | **Build in:** Phase 2 (Weeks 3–4)

#### Why It Matters

Most multibaggers in cyclical sectors (chemicals, capital goods, real estate, metals, textiles) are found by buying a *
*great business near trough margins after a multi-year downcycle**. Your system currently has no concept of where a
company sits in its business cycle. Buying a great chemicals company when margins are at 8% and recovering is a
completely different bet from buying the same company when margins are at 22% and peaking.

#### Schema

```sql
CREATE TABLE cycle_positioning (
    company_id            INTEGER,
    quarter               VARCHAR,
    revenue_vs_peak       DOUBLE,    -- current revenue / max revenue in last 12 quarters
    ebitda_margin_current DOUBLE,
    ebitda_margin_vs_peak DOUBLE,    -- current margin / best margin in last 12 quarters
    ebitda_margin_vs_trough DOUBLE,  -- current margin / worst margin in last 12 quarters
    capacity_utilization  DOUBLE,    -- AI-extracted from concalls (NULL if not mentioned)
    order_book_cr         DOUBLE,    -- AI-extracted order book value in crores
    order_book_growth     DOUBLE,    -- QoQ change in order book %
    cycle_stage           VARCHAR,   -- derived: 'TROUGH', 'EARLY_RECOVERY', 'MID_CYCLE', 'LATE_CYCLE', 'PEAK'
    PRIMARY KEY (company_id, quarter)
);
```

#### How to Implement

1. **Math derivation for `cycle_stage` (pure Python, no AI):**
   ```python
   def derive_cycle_stage(company_id, quarter, lookback_quarters=12):
       history = get_ebitda_margins(company_id, lookback_quarters)
       current = history[-1]
       peak    = max(history)
       trough  = min(history)
       trend   = current - history[-4]  # delta vs 4 quarters ago

       pct_of_range = (current - trough) / (peak - trough) if peak != trough else 0.5

       if pct_of_range < 0.20:
           return 'TROUGH'
       elif pct_of_range < 0.45 and trend > 0:
           return 'EARLY_RECOVERY'   # ← The multibagger entry zone
       elif pct_of_range < 0.75:
           return 'MID_CYCLE'
       elif pct_of_range < 0.90:
           return 'LATE_CYCLE'
       else:
           return 'PEAK'
   ```
2. **Add to AI extraction prompt:** "What is the current capacity utilization level? What is the current order book
   value in crores?" — store in `cycle_positioning`.
3. **Alert trigger:** If `cycle_stage = 'EARLY_RECOVERY'` AND `business_score > 7` AND
   `valuation below 5-year median PE` → fire `CYCLE_RECOVERY_OPPORTUNITY` alert. This is the highest-conviction alert in
   the entire system.

#### The Entry Zone

> `EARLY_RECOVERY` + High Business Quality + Below Median Valuation = **The Multibagger Setup**

This is how 5–10x returns happen in capital goods, chemicals, and real estate companies.

---

### Enhancement 6: Silent Compounder Screen

**Layer:** 5 (Decision) | **Priority:** 🔴 High | **Build in:** Phase 5 (Weeks 9–10)

#### Why It Matters

Your current watchlist is **reactive** — you add companies you already know about. This screen is **proactive** — it
hunts your 150–200 universe every week and ranks companies by how many early-compounder fingerprints they show. A
company scoring 7/8 flags deserves 2 hours of immediate deep research. A company at 2/8 can wait.

#### Schema

```sql
CREATE TABLE multibagger_screen (
    company_id             INTEGER,
    screen_date            DATE,
    roce_gt_20             BOOLEAN,   -- ROCE > 20% for 3+ consecutive years
    roce_improving         BOOLEAN,   -- ROCE trend positive last 4 quarters
    debt_free              BOOLEAN,   -- debt_to_equity < 0.3
    promoter_buying        BOOLEAN,   -- promoter % increased in last 2 quarters (not pledging)
    margin_expanding       BOOLEAN,   -- EBITDA margin growing YoY for 2+ quarters
    revenue_accelerating   BOOLEAN,   -- revenue growth(Q) > revenue growth(Q-4)
    pe_below_own_median    BOOLEAN,   -- PE < its own 5-year median PE
    low_institutional      BOOLEAN,   -- FII + DII < 20% (undiscovered by institutions)
    composite_score        INTEGER,   -- COUNT of TRUE flags (0–8)
    PRIMARY KEY (company_id, screen_date)
);
```

#### How to Implement

1. **Run every Sunday night** as a batch job after weekly data refresh.
2. **All flags are pure math** — derive from existing tables:

   ```python
   def run_multibagger_screen(screen_date):
       for company in get_active_companies():
           metrics   = get_latest_metrics(company.id)
           valuation = get_valuation_history(company.id, years=5)
           ownership = get_latest_shareholding(company.id)
           history   = get_quarterly_metrics(company.id, quarters=12)

           flags = {
               'roce_gt_20':           all(q.roce > 20 for q in history[-12:]),
               'roce_improving':       metrics.roce > get_avg_roce(company.id, 4),
               'debt_free':            metrics.debt_to_equity < 0.3,
               'promoter_buying':      is_promoter_increasing(company.id, quarters=2),
               'margin_expanding':     is_margin_expanding(company.id, quarters=2),
               'revenue_accelerating': is_growth_accelerating(company.id),
               'pe_below_own_median':  valuation.current_pe < valuation.median_pe_5yr,
               'low_institutional':    (ownership.fii + ownership.dii) < 20,
           }

           score = sum(flags.values())
           insert_multibagger_screen(company.id, screen_date, flags, score)

   ```

3. **Surface in Research Feed:** Every Monday morning, show the **Top 10 companies by composite_score** that are NOT
   already in your `watchlist` or portfolio. These are your research candidates for the week.
4. **Threshold for action:**
    - Score **6–8** → Add to watchlist, schedule deep research this week
    - Score **4–5** → Flag for review next month
    - Score **< 4** → No action

---

### Enhancement 7: Price–Business Divergence Alert

**Layer:** 6 (Output) | **Priority:** 🔴 High | **Build in:** Phase 6 (Weeks 11–12)

#### Why It Matters

The best time to buy a multibagger is when the **business is visibly improving but the stock price hasn't reacted yet
** — the market is asleep. Your system tracks price and business quality in separate tables but never directly compares
their trajectories. This alert closes that gap.

#### New Alert Types

Add the following to `alert_log.alert_type`:

| Alert Type                   | Trigger Condition                                                             | Meaning                                            |
|------------------------------|-------------------------------------------------------------------------------|----------------------------------------------------|
| `PRICE_BUSINESS_DIVERGENCE`  | `business_score` up 2+ quarters but stock down 15%+ from 52-week high         | Market hasn't noticed the improvement yet          |
| `HIDDEN_COMPOUNDER`          | ROCE > 20%, market cap < ₹5,000 Cr, FII + DII < 5%                            | Genuinely undiscovered by institutions             |
| `MARGIN_INFLECTION`          | EBITDA margin crossed a 3-year high this quarter                              | Structural improvement, not one-off                |
| `EARLY_RERATING`             | PE expanding while EPS also growing YoY                                       | Double engine — both earnings and multiple growing |
| `CYCLE_RECOVERY_OPPORTUNITY` | `cycle_stage = 'EARLY_RECOVERY'` + `business_score > 7` + PE below 5yr median | The highest conviction cyclical setup              |
| `PROMOTER_CONVICTION`        | Promoter buying open market shares (not ESOPs) 2+ quarters in a row           | Insider buying at scale                            |

#### How to Implement

1. **Add alert generation to end-of-week pipeline** (runs every Saturday morning before your review):
   ```python
   def generate_divergence_alerts(date):
       for company in get_watchlist_and_portfolio():
           score_now  = get_business_score(company.id, current_quarter)
           score_prev = get_business_score(company.id, prev_quarter)
           score_2ago = get_business_score(company.id, two_quarters_ago)

           price_now  = get_price(company.id, date)
           price_52wh = get_52_week_high(company.id, date)

           improving  = score_now > score_prev > score_2ago
           price_down = (price_52wh - price_now) / price_52wh > 0.15

           if improving and price_down:
               create_alert(company.id, 'PRICE_BUSINESS_DIVERGENCE', {
                   'business_score_trend': [score_2ago, score_prev, score_now],
                   'price_drop_from_52wh': round((price_52wh - price_now) / price_52wh * 100, 1),
               })
   ```
2. **Priority ranking:** `PRICE_BUSINESS_DIVERGENCE` and `CYCLE_RECOVERY_OPPORTUNITY` alerts should always appear at the
   **top of the Research Feed**, above thesis validation alerts.
3. **Do not alert for already-owned positions** — these are *entry* signals, not monitoring signals.

---

### Enhancement 8: Annual Report Insights

**Layer:** 2 (Knowledge) | **Priority:** 🟢 Later | **Build in:** Phase 4 (Weeks 7–8, alongside AI extraction)

#### Why It Matters

Annual reports — specifically the **Chairman's Letter and MD&A section** — contain the most honest, forward-looking, and
unscripted communication from Indian promoters. Unlike concalls (which are reactive Q&A), the annual letter is where
management tells you their multi-year vision. Tracking this over years reveals if management is consistent, evolving, or
just repeating platitudes.

#### Schema

```sql
CREATE TABLE annual_report_insights (
    company_id           INTEGER,
    year                 INTEGER,
    chairman_letter_text TEXT,    -- raw text, extracted from PDF
    stated_priorities    TEXT,    -- AI: top 3-5 things they say they'll focus on
    prior_promises_kept  TEXT,    -- AI comparison: last year's priorities vs what actually happened
    guidance_kept        BOOLEAN, -- math-derived: did revenue/margin meet what was implied?
    new_initiatives      TEXT,    -- AI: new businesses, new products, new geographies mentioned
    red_flags            TEXT,    -- AI: unusually defensive tone, blame shifting, vague language
    PRIMARY KEY (company_id, year)
);
```

#### How to Implement

1. **PDF ingestion:** Add `document_type = 'ANNUAL_REPORT'` to your `documents` table. Use `pdfplumber` or `pypdf` to
   extract text from the Chairman's Letter section (usually pages 4–20).
2. **AI prompt for annual report:**
   ```
   You are analyzing the Chairman's Letter and MD&A from [COMPANY]'s [YEAR] Annual Report.

   Return JSON:
   - stated_priorities: List the top 3-5 strategic priorities management explicitly states for the coming year.
   - new_initiatives: Any new products, geographies, customer segments, or business lines mentioned.
   - red_flags: Any language that signals defensiveness, blame-shifting, or unusual vagueness.
   ```
3. **Year-over-year comparison (next AI call):**
   ```
   Compare [COMPANY]'s stated_priorities from [YEAR-1] with actual financial and operational outcomes in [YEAR].
   For each priority, assess: Delivered / Partial / Not Delivered.
   Return as JSON with evidence from the current annual report.
   ```
4. **Store result in `prior_promises_kept`** and update `management_quality.guidance_accuracy_score` based on it.

---

### Enhancement 9: Vector DB + RAG (ChromaDB) — Optional

**Layer:** 2 (Knowledge) + 4 (AI) | **Priority:** 🟢 Optional | **Build in:** Phase 4+ (only when Deep Research UI needs
conversational search)

#### Why It's Optional — The Honest Reason

`gemini-2.0-flash` has a **1 million token context window**. A typical Indian concall transcript is ~20,000 tokens. You
can fit **50 full transcripts in one API call**. The main historical reason RAG existed was to work around 4K–32K token
limits of older models. That constraint is largely gone.

> **Current pipeline** (batch structured extraction) → send 1 document → get structured JSON back → store in DuckDB.
> This does NOT need RAG.

> **The only case that needs RAG:** asking free-form questions across hundreds of documents — the Deep Research
> conversational UI.

#### The Trigger: When to Add It

Add ChromaDB **only when you want to type this in the Deep Research tab:**

- *"What has TCS management said about their BFSI segment across the last 6 quarters?"*
- *"Which companies in my universe have mentioned China+1 as a tailwind?"*
- *"Find all times any management blamed macro headwinds instead of owning the miss."*
- *"Show me every mention of capacity expansion across capital goods companies this year."*

These questions require **searching meaning across hundreds of documents**. Without RAG, you'd need to send all 150
companies × 4 quarters = 600 transcripts to Gemini in one session — impossible even at 1M tokens. With RAG, you retrieve
only the 5–10 most relevant chunks and send those.

#### Architecture: How It Fits In

```
DuckDB (athena.duckdb)          ChromaDB (data/chroma_db/)
───────────────────────         ──────────────────────────
Layer 1–6: All structured       Transcript chunks (embeddings)
           facts and scores     Annual report sections
                                Research note embeddings

Rule: DuckDB owns Truth & Structure.
      ChromaDB owns Semantic Search.
      Gemini owns Interpretation.
```

#### What Changes Are Needed

**1. New folder:**

```
data/
└── chroma_db/    ← ChromaDB's persistent storage (auto-created)
```

**2. New DuckDB metadata table** (tracks which chunks are indexed — optional but useful):

```sql
CREATE TABLE document_chunks (
    chunk_id     VARCHAR PRIMARY KEY,  -- e.g. "TCS_Sep2024_concall_chunk_12"
    document_id  INTEGER,              -- FK to documents table
    company_id   INTEGER,
    quarter      VARCHAR,
    doc_type     VARCHAR,
    chunk_index  INTEGER,
    token_count  INTEGER,
    indexed_at   TIMESTAMP
);
```

**3. Install ChromaDB:**

```bash
pip install chromadb
```

#### Implementation

```python
# loaders/vector_indexer.py
import chromadb
import duckdb
import pdfplumber

con = duckdb.connect("athena.duckdb")
client = chromadb.PersistentClient(path="data/chroma_db")
collection = client.get_or_create_collection(
    name="athena_documents",
    metadata={"hnsw:space": "cosine"}  # cosine similarity for text
)

CHUNK_SIZE = 800  # tokens (~600 words) per chunk
CHUNK_OVERLAP = 100  # overlap between chunks to preserve context


def chunk_text(text: str, chunk_size: int = CHUNK_SIZE, overlap: int = CHUNK_OVERLAP) -> list[str]:
    """Split text into overlapping chunks by approximate word count."""
    words = text.split()
    chunks = []
    step = chunk_size - overlap
    for i in range(0, len(words), step):
        chunk = " ".join(words[i: i + chunk_size])
        if chunk:
            chunks.append(chunk)
    return chunks


def index_document(document_id: int, company_id: int, symbol: str,
                   quarter: str, doc_type: str, filepath: str):
    # Extract text from PDF
    with pdfplumber.open(filepath) as pdf:
        text = "\n".join(page.extract_text() or "" for page in pdf.pages)

    chunks = chunk_text(text)

    for i, chunk in enumerate(chunks):
        chunk_id = f"{symbol}_{quarter}_{doc_type}_chunk_{i}"

        # Skip if already indexed
        existing = collection.get(ids=[chunk_id])
        if existing["ids"]:
            continue

        collection.add(
            documents=[chunk],
            metadatas=[{
                "company_id": company_id,
                "symbol": symbol,
                "quarter": quarter,
                "doc_type": doc_type,  # CONCALL_TRANSCRIPT or ANNUAL_REPORT
                "chunk_index": i,
            }],
            ids=[chunk_id]
        )

        # Track in DuckDB
        con.execute("""
            INSERT OR IGNORE INTO document_chunks
                (chunk_id, document_id, company_id, quarter, doc_type, chunk_index, indexed_at)
            VALUES (?, ?, ?, ?, ?, ?, now())
        """, [chunk_id, document_id, company_id, quarter, doc_type, i])

    print(f"Indexed {len(chunks)} chunks: {symbol} {quarter} {doc_type}")


def index_all_unindexed():
    """Index any documents in the documents table that haven't been chunked yet."""
    docs = con.execute("""
        SELECT d.document_id, d.company_id, c.symbol, d.quarter, d.document_type, d.file_path
        FROM documents d
        JOIN companies c USING (company_id)
        WHERE d.processed = TRUE
          AND d.document_id NOT IN (SELECT DISTINCT document_id FROM document_chunks)
    """).fetchall()

    for doc_id, company_id, symbol, quarter, doc_type, filepath in docs:
        index_document(doc_id, company_id, symbol, quarter, doc_type, filepath)
```

#### Querying the Vector DB (Deep Research UI)

```python
# Used in app.py Streamlit Deep Research tab
def semantic_search(query: str, symbol: str = None,
                    doc_type: str = None, n_results: int = 8) -> list[dict]:
    """
    Find the most semantically relevant document chunks for a free-form question.
    Optionally filter by company symbol or document type.
    """
    where_filter = {}
    if symbol:
        where_filter["symbol"] = symbol
    if doc_type:
        where_filter["doc_type"] = doc_type

    results = collection.query(
        query_texts=[query],
        n_results=n_results,
        where=where_filter if where_filter else None
    )

    chunks = []
    for i, doc in enumerate(results["documents"][0]):
        chunks.append({
            "text": doc,
            "symbol": results["metadatas"][0][i]["symbol"],
            "quarter": results["metadatas"][0][i]["quarter"],
            "doc_type": results["metadatas"][0][i]["doc_type"],
            "score": 1 - results["distances"][0][i],  # cosine similarity
        })

    return chunks


def ask_across_documents(question: str, symbol: str = None) -> str:
    """
    RAG pipeline: retrieve relevant chunks, then ask Gemini to answer using them.
    """
    import google.generativeai as genai

    chunks = semantic_search(question, symbol=symbol, n_results=8)
    context = "\n\n---\n\n".join(
        f"[{c['symbol']} | {c['quarter']} | {c['doc_type']}]\n{c['text']}"
        for c in chunks
    )

    model = genai.GenerativeModel("gemini-2.0-flash")
    response = model.generate_content(f"""
    You are a financial research assistant analyzing Indian listed companies.
    Answer the following question using ONLY the document excerpts provided below.
    Cite the company and quarter for each claim.

    Question: {question}

    Document excerpts:
    {context}
    """)

    return response.text
```

#### Example Streamlit Usage

```python
# In app.py Deep Research tab
st.subheader("Ask Anything")
query = st.text_input("Ask a question across all documents",
                      placeholder="What has management said about margin recovery?")
symbol = st.selectbox("Filter by company (optional)", ["All"] + get_symbols())

if st.button("Search") and query:
    answer = ask_across_documents(query, symbol=None if symbol == "All" else symbol)
    st.markdown(answer)
```

#### Cost Impact of Adding RAG

| Scenario                                          | API calls                                 | Cost       |
|---------------------------------------------------|-------------------------------------------|------------|
| Indexing 150 companies × 4 transcripts (one-time) | 0 — ChromaDB uses its own embedding model | **$0.00**  |
| Each Deep Research question (8 chunks retrieved)  | 1 Gemini call, ~10K tokens                | **$0.001** |
| 20 questions on a Saturday morning                | 20 calls                                  | **$0.02**  |

> ChromaDB uses its **own built-in embedding model** (`all-MiniLM-L6-v2`) by default — no Gemini tokens consumed for
> indexing. Only the final answer generation uses Gemini.

---

### Enhancement Summary

| # | Enhancement                     | Layer | Priority    | Build Phase | Key Signal                                               |
|---|---------------------------------|-------|-------------|-------------|----------------------------------------------------------|
| 1 | Operating Leverage Tracker      | 3     | 🔴 High     | Phase 2     | Margin inflection before market notices                  |
| 2 | Capital Allocation / ROIC Trend | 3     | 🔴 High     | Phase 2     | Separates compounders from pretenders                    |
| 3 | TAM Expansion Signal            | 2 + 4 | 🟡 Medium   | Phase 4     | Catches 10-baggers before consensus                      |
| 4 | Management Quality Score        | 2 + 4 | 🟡 Medium   | Phase 4     | Filters value traps via concall honesty                  |
| 5 | Cycle Positioning Detector      | 3     | 🔴 High     | Phase 2     | Times entry in cyclicals and capital goods               |
| 6 | Silent Compounder Screen        | 5     | 🔴 High     | Phase 5     | Proactive hunting engine across the universe             |
| 7 | Price–Business Divergence Alert | 6     | 🔴 High     | Phase 6     | Surfaces the best asymmetric entry setups                |
| 8 | Annual Report Insights          | 2     | 🟢 Later    | Phase 4     | Long-term management consistency tracker                 |
| 9 | Vector DB + RAG (ChromaDB)      | 2 + 4 | 🟢 Optional | Phase 4+    | Cross-document conversational search in Deep Research UI |

---

### Additional Columns Needed in Existing Tables

To support all enhancements above, add the following to existing **Layer 1 tables** during Phase 1 ingestion:

```sql
-- Add to quarterly_financials
ALTER TABLE quarterly_financials ADD COLUMN employee_cost  DOUBLE;  -- for operating leverage
ALTER TABLE quarterly_financials ADD COLUMN depreciation   DOUBLE;  -- for ROIC and op leverage
ALTER TABLE quarterly_financials ADD COLUMN pbt            DOUBLE;  -- Profit Before Tax
ALTER TABLE quarterly_financials ADD COLUMN tax            DOUBLE;  -- for effective tax rate
ALTER TABLE quarterly_financials ADD COLUMN capex          DOUBLE;  -- from cash flow statement

-- Add to companies
ALTER TABLE companies ADD COLUMN market_cap_cr   DOUBLE;  -- in crores, updated daily
ALTER TABLE companies ADD COLUMN listing_date    DATE;
ALTER TABLE companies ADD COLUMN is_cyclical     BOOLEAN; -- manual flag for cycle detection
```

[↑ Back to TOC](#table-of-contents)

---

## 8. Data Sourcing & Execution Steps

### Local Folder Structure

```
project_athena/
├── data/
│   ├── universe/           # universe.csv from Screener.in screen export
│   ├── transcripts/        # Concall PDFs/TXTs  (SYMBOL_QXFYXX.pdf)
│   └── annual_reports/     # Annual report PDFs (SYMBOL_YYYY.pdf)
├── loaders/
│   ├── universe_loader.py          # Step 0: populate companies table from universe.csv
│   ├── financials_loader.py        # Step 1: quarterly financials via yfinance
│   ├── price_loader.py             # Step 2: daily prices via yfinance
│   ├── shareholding_loader.py      # Step 3: shareholding via NSE bulk CSV
│   ├── corporate_actions_loader.py # Step 4: splits/dividends via yfinance
│   └── document_tracker.py        # Step 5: register PDFs into documents table
├── engines/
│   ├── growth_engine.py
│   └── valuation_engine.py
├── ai/
│   └── transcript_parser.py
├── flows.py
└── app.py
```

---

### Data Sources

| Table                     | Source                    | Method                                                                           | Restriction                                       |
|---------------------------|---------------------------|----------------------------------------------------------------------------------|---------------------------------------------------|
| `companies`               | Screener.in Screen Export | Run a screen → Export to Excel → save as `universe.csv`                          | Free, works (screen export ≠ company page export) |
| `quarterly_financials`    | yfinance                  | `Ticker.quarterly_financials` + `quarterly_balance_sheet` + `quarterly_cashflow` | Free, no login                                    |
| `shareholding`            | NSE bulk CSV              | Download quarterly ZIP from NSE archives                                         | Free, official                                    |
| `daily_prices`            | yfinance                  | `Ticker.history()`                                                               | Free, no login                                    |
| `corporate_actions`       | yfinance                  | `Ticker.splits`, `Ticker.dividends`                                              | Free, no login                                    |
| Latest quarter supplement | BSE XBRL                  | Official filing, available same day as results                                   | Free, official                                    |
| `documents`               | Manual download           | Drop PDFs into `data/` folders                                                   | Manual                                            |

> **Important distinction:** Screener.in **blocks scraping of individual company pages** but the **Screen Results export
works fine** — it exports a summary row per company (market cap, ROCE, growth rates etc.) for all companies matching
> your filter. This is exactly what you need to populate the `companies` table and nothing more. Full quarterly financial
> history still comes from yfinance.

---

### Step 0: Build Your Universe (Screener.in Screen Export)

> **This is the true first step.** Before loading any financial data, you need to answer: *which 150–200 companies
should Athena monitor?* Screener.in's screen builder is the best free tool for this, and its **screen results export
works** (unlike individual company page exports).

#### Create the Screen

1. Go to [screener.in](https://www.screener.in) and **log in** (free account).
2. Click **Screens** in the top navigation → **Create New Screen**.
3. Paste the following query into the text box:

```
Market Capitalization > 1000 AND
Return on capital employed > 15 AND
Average return on capital employed 5Years > 15 AND
Debt to equity < 0.5 AND
Interest Coverage Ratio > 5 AND
Sales growth 5Years > 10 AND
Profit growth 5Years > 10 AND
Operating Cashflow > 0 AND
Promoter holding > 50 AND
Pledged percentage < 10
```

#### Why Each Filter

| Filter                                 | Reason                                                                                                          |
|----------------------------------------|-----------------------------------------------------------------------------------------------------------------|
| `Market Cap > 1000`                    | Removes illiquid micro-caps where one large sell order crashes the price                                        |
| `ROCE > 15` AND `Avg ROCE 5Y > 15`     | Consistent capital efficiency — one good year doesn't qualify, you need 5 years of proof                        |
| `Debt/Equity < 0.5`                    | Low leverage — the business funds its own growth, not banks                                                     |
| `Interest Coverage > 5`                | Even if operating profit drops 80%, the company can still pay interest without defaulting                       |
| `Sales + Profit growth 5Y > 10`        | Proves actual scaling, not just cost-cutting to boost margins temporarily                                       |
| **`Operating Cashflow > 0`** ← *added* | Filters out companies with good accounting profit but poor cash conversion — catches accrual manipulation early |
| `Promoter holding > 50`                | Founders have real skin in the game                                                                             |
| `Pledged < 10%`                        | Founders haven't mortgaged their shares for personal loans — a major red flag if high                           |

> **Expected result:** 80–200 companies. If you get fewer than 80, relax `Sales growth` to `> 8` or `Market Cap > 500`.
> If you get more than 200, tighten `ROCE > 18` or add `Profit growth 5Years > 15`.

#### Export and Save

1. Click **Run this query** → wait for results.
2. Click **Save** (top right) — name it `Athena Universe`. Screener will update it automatically.
3. Click **Export to Excel** (top right of results table).
4. Open the downloaded `.xlsx`, go to the data sheet, **Save As CSV** → name it `universe.csv`.
5. Drop `universe.csv` into `data/universe/` in your project.

> ⚠️ **What this CSV contains:** A summary snapshot per company — Name, NSE Symbol, Market Cap, ROCE, P/E, Sales Growth,
> etc. It does **not** contain quarterly financial history. That comes from yfinance in Step 1.

> 🔁 **Re-run quarterly:** After each earnings season (May, Aug, Nov, Feb), re-run the screen and re-export. New
> companies that enter your criteria get added to the universe. Companies that fall off can be archived.

---

#### universe_loader.py

```python
import pandas as pd
import duckdb

con = duckdb.connect("athena.duckdb")


def load_universe(filepath: str = "data/universe/universe.csv"):
    df = pd.read_csv(filepath)

    # Screener export column names — print df.columns to verify yours
    # Common columns: Name, NSE Symbol (or just Symbol), Market Cap, ROCE, Sector
    print("Columns in universe.csv:", df.columns.tolist())

    rename_map = {
        "Name": "name",
        "NSE Symbol": "symbol",  # adjust if Screener uses just "Symbol"
        "Market Cap": "market_cap_cr",
        "Sector": "sector",
        "Industry": "industry",
    }
    df.rename(columns=rename_map, inplace=True)

    # Ensure required columns exist
    if "symbol" not in df.columns:
        raise ValueError("Could not find NSE Symbol column. Check rename_map against your CSV headers.")

    df["active"] = True
    df["market_cap_cr"] = pd.to_numeric(df.get("market_cap_cr"), errors="coerce")

    # Only keep companies we don't already have
    existing = con.execute("SELECT symbol FROM companies").df()["symbol"].tolist()
    new_companies = df[~df["symbol"].isin(existing)]

    if new_companies.empty:
        print("No new companies to add.")
        return

    con.execute("""
        INSERT INTO companies (symbol, name, sector, industry, market_cap_cr, active)
        SELECT symbol, name, sector, industry, market_cap_cr, active
        FROM new_companies
    """)
    print(f"Added {len(new_companies)} companies to universe.")
    print(new_companies[["symbol", "name"]].to_string(index=False))


if __name__ == "__main__":
    load_universe()
```

> After running `universe_loader.py`, your `companies` table is seeded. Every subsequent loader (`financials_loader.py`,
`price_loader.py`, etc.) reads from `companies WHERE active = TRUE` — so this step must come first.

---

### Step 1: financials_loader.py (yfinance)

> **The most underused yfinance feature:** Beyond prices, `yfinance` also exposes quarterly P&L, balance sheet, and cash
> flow statements for NSE-listed stocks via Yahoo Finance. This covers ~8 quarters of history which is sufficient for all
> Layer 3 calculations.

```python
import yfinance as yf
import pandas as pd
import duckdb
import time

con = duckdb.connect("athena.duckdb")


def load_financials(company_id: int, symbol: str):
    ticker = yf.Ticker(f"{symbol}.NS")

    try:
        pl = ticker.quarterly_financials.T  # P&L  — rows=quarters, cols=line items
        bs = ticker.quarterly_balance_sheet.T  # Balance Sheet
        cf = ticker.quarterly_cashflow.T  # Cash Flow

        if pl.empty:
            print(f"No financial data for {symbol}")
            return

        # Merge all three on the quarter index (datetime)
        df = pl.join(bs, how="left", lsuffix="", rsuffix="_bs")
        df = df.join(cf, how="left", lsuffix="", rsuffix="_cf")
        df = df.reset_index().rename(columns={"index": "quarter"})

        # Format quarter as string: "Mar 2024"
        df["quarter"] = df["quarter"].dt.strftime("%b %Y")
        df["company_id"] = company_id

        # yfinance column names → your schema
        # Note: yfinance uses title-case with spaces. Print df.columns to verify for your stocks.
        rename_map = {
            "Total Revenue": "revenue",
            "EBITDA": "ebitda",
            "Net Income": "pat",
            "Basic EPS": "eps",
            "Total Debt": "debt",
            "Cash And Cash Equivalents": "cash",
            "Operating Cash Flow": "ocf",
            "Capital Expenditure": "capex",
            "Reconciled Depreciation": "depreciation",
        }
        df.rename(columns=rename_map, inplace=True)

        # Derive calculated fields
        df["fcf"] = df.get("ocf", pd.Series(dtype=float)) + df.get("capex", pd.Series(dtype=float))
        # Note: yfinance reports capex as negative in cash flow — adding gives FCF

        # Derive EBITDA if not directly available
        if "ebitda" not in df.columns:
            ebit = df.get("EBIT", 0)
            dep = df.get("depreciation", 0)
            df["ebitda"] = ebit + dep

        # Numeric coercion
        numeric_cols = ["revenue", "ebitda", "pat", "eps", "debt", "cash", "ocf", "fcf", "capex", "depreciation"]
        for col in numeric_cols:
            if col in df.columns:
                df[col] = pd.to_numeric(df[col], errors="coerce")

        con.execute("""
            INSERT OR REPLACE INTO quarterly_financials
                (company_id, quarter, revenue, ebitda, pat, eps,
                 debt, cash, ocf, fcf, capex, depreciation)
            SELECT company_id, quarter, revenue, ebitda, pat, eps,
                   debt, cash, ocf, fcf, capex, depreciation
            FROM df
        """)
        print(f"Loaded financials: {symbol} ({len(df)} quarters)")

    except Exception as e:
        print(f"Error loading financials for {symbol}: {e}")

    time.sleep(1)


def load_all_financials():
    companies = con.execute("SELECT company_id, symbol FROM companies WHERE active = TRUE").fetchall()
    for company_id, symbol in companies:
        load_financials(company_id, symbol)
```

> ⚠️ **Coverage:** yfinance typically returns the last 4–8 quarters of data. For deeper history (10 years), supplement
> with BSE XBRL bulk download (see Step 1b below).

> ⚠️ **Column names vary:** Run `print(ticker.quarterly_financials.index.tolist())` for any stock to see the exact row
> labels before mapping. Adjust `rename_map` if needed.

---

### Step 1b: BSE XBRL — Latest Quarter + Deep History

> BSE India publishes every quarterly result as an **official XBRL filing** the same day results are announced — faster
> than any other source. Also provides bulk historical data going back 10+ years as downloadable ZIP files.

**Two ways to use it:**

**Option A — API for a single company's recent results:**

```python
import requests

HEADERS = {"User-Agent": "Mozilla/5.0"}


def fetch_bse_latest_quarter(bse_code: str, company_id: int):
    """
    bse_code: The 6-digit BSE scrip code, e.g. 532540 for TCS.
    Add a bse_code column to your companies table.
    """
    url = (
        f"https://api.bseindia.com/BseIndiaAPI/api/FinancialResults/w"
        f"?scripcode={bse_code}&type=Quarterly"
    )
    resp = requests.get(url, headers=HEADERS, timeout=10)
    data = resp.json()

    for row in data.get("Table", []):
        quarter = row.get("QUARTER")  # e.g. "Sep 2024"
        revenue = row.get("NET_SALES_INCOME_FROM_OPERATIONS")
        ebitda = row.get("PBDT")  # Profit Before Dep & Tax ≈ EBITDA
        pat = row.get("NET_PROFIT_LOSS_FOR_THE_PERIOD")
        eps = row.get("BASIC_EPS_AFTER_EO_ITEM")
        dep = row.get("DEPRECIATION")

        con.execute("""
            INSERT OR REPLACE INTO quarterly_financials
                (company_id, quarter, revenue, ebitda, pat, eps, depreciation)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """, [company_id, quarter, revenue, ebitda, pat, eps, dep])
```

**Option B — Bulk historical ZIP (one-time initial load):**

1. Go to `bseindia.com` → **Corporates** → **Financial Results** → **Bulk Download**
2. Download ZIP for each year → extract XMLs → parse with `xmltodict`

```python
import zipfile, xmltodict, glob


def load_bse_bulk_zip(zip_path: str):
    with zipfile.ZipFile(zip_path) as z:
        for name in z.namelist():
            if name.endswith(".xml"):
                with z.open(name) as f:
                    data = xmltodict.parse(f.read())
                    # Navigate the XBRL tree to extract line items
                    # Structure varies by filing type — inspect one file first
                    print(data.keys())  # explore structure
```

---

### Step 2: shareholding_loader.py (NSE Bulk CSV)

> NSE publishes **quarterly bulk shareholding pattern files** — free, official, no login needed.

**Download URL pattern:**

```
https://nsearchives.nseindia.com/corporate/shareholding/SHP_<QUARTER>.zip
# Example: SHP_012024.zip = January 2024 quarter
```

```python
import requests, zipfile, io
import pandas as pd


def download_nse_shareholding(quarter_code: str):
    """
    quarter_code format: MMYYYY — e.g. '012024' for Q3 FY24 (Jan 2024)
    NSE releases: 012024, 042024, 072024, 102024
    """
    url = f"https://nsearchives.nseindia.com/corporate/shareholding/SHP_{quarter_code}.zip"
    resp = requests.get(url, headers={"User-Agent": "Mozilla/5.0"}, timeout=30)
    z = zipfile.ZipFile(io.BytesIO(resp.content))

    # NSE ZIP contains one CSV
    csv_name = [n for n in z.namelist() if n.endswith(".csv")][0]
    df = pd.read_csv(z.open(csv_name))

    # NSE columns: Symbol, Promoter_and_Promoter_Group, FII, DII, Public, Pledged_Shares_Pct
    rename_map = {
        "Symbol": "symbol",
        "Promoter_and_Promoter_Group": "promoter",
        "FII": "fii",
        "DII": "dii",
        "Public": "public",
        "Pledged_Shares_Pct": "pledged",
    }
    df.rename(columns=rename_map, inplace=True)

    # Map quarter_code to readable quarter string
    from datetime import datetime
    dt = datetime.strptime(quarter_code, "%m%Y")
    quarter_str = dt.strftime("%b %Y")

    # Join with companies to get company_id
    companies = con.execute("SELECT company_id, symbol FROM companies").df()
    df = df.merge(companies, on="symbol", how="inner")
    df["quarter"] = quarter_str

    for col in ["promoter", "fii", "dii", "public", "pledged"]:
        df[col] = pd.to_numeric(df[col], errors="coerce")

    con.execute("""
        INSERT OR REPLACE INTO shareholding (company_id, quarter, promoter, fii, dii, public, pledged)
        SELECT company_id, quarter, promoter, fii, dii, public, pledged FROM df
    """)
    print(f"Loaded shareholding for {quarter_str}: {len(df)} companies")


def load_shareholding_history():
    # Load last 12 quarters
    quarters = ["012022", "042022", "072022", "102022",
                "012023", "042023", "072023", "102023",
                "012024", "042024", "072024", "102024"]
    for q in quarters:
        print(f"Loading shareholding {q}...")
        download_nse_shareholding(q)
```

---

### Recommended Strategy

```
Quarterly financials (last 8Q)   →  yfinance  (ticker.quarterly_financials)
Quarterly financials (deep 10yr) →  BSE XBRL bulk ZIP download (one-time)
Latest quarter (same day)        →  BSE XBRL API
Daily prices                     →  yfinance  (ticker.history, .NS suffix)
Corporate actions                →  yfinance  (ticker.splits, ticker.dividends)
Shareholding history             →  NSE bulk CSV  (quarterly ZIP per quarter)
Concall PDFs / Annual Reports    →  Manual download → data/ folder
```

---

### Step 3: price_loader.py

> ⚠️ **Critical Gotcha — Symbol format:** yfinance requires NSE symbols with a `.NS` suffix (e.g., `INFY.NS`, `TCS.NS`).
> Store `symbol` in your `companies` table **without** the suffix and append it at fetch time.

> ⚠️ **Rate Limiting:** For 150–200 stocks, add `time.sleep(0.5)` between each fetch to avoid being blocked.

```python
import yfinance as yf
import duckdb
import time

con = duckdb.connect("athena.duckdb")


def load_prices():
    companies = con.execute("SELECT company_id, symbol FROM companies WHERE active = TRUE").fetchall()

    for company_id, symbol in companies:
        ticker = yf.Ticker(f"{symbol}.NS")
        hist = ticker.history(period="10y", auto_adjust=True)

        if hist.empty:
            print(f"No data for {symbol}")
            continue

        hist = hist.reset_index()[["Date", "Close", "Volume"]]
        hist.columns = ["date", "close", "volume"]
        hist["adjusted_close"] = hist["close"]  # auto_adjust=True already adjusts
        hist["company_id"] = company_id

        con.execute("""
            INSERT OR REPLACE INTO daily_prices (company_id, date, close, adjusted_close, volume)
            SELECT company_id, date, close, adjusted_close, volume FROM hist
        """)

        time.sleep(0.5)  # rate limit
```

---

### Step 4 (Legacy / Manual Fallback): shareholding_loader.py

> Use this only if you want to parse shareholding from a manual Screener.in export. The **primary** shareholding path for
> Athena is **Step 2: NSE Bulk CSV**, because it is official, more complete, and easier to backfill historically.

```python
def load_shareholding(filepath: str, company_id: int):
    df = pd.read_csv(filepath, index_col=0).T.reset_index()
    df.rename(columns={"index": "quarter"}, inplace=True)
    df.columns = [c.strip().lower().replace(" ", "_") for c in df.columns]
    df["company_id"] = company_id

    rename_map = {
        "promoter_holding": "promoter",
        "fii_holding": "fii",
        "dii_holding": "dii",
        "public_holding": "public",
        "pledged_percentage": "pledged",
    }
    df.rename(columns=rename_map, inplace=True)

    con.execute("""
        INSERT OR REPLACE INTO shareholding (company_id, quarter, promoter, fii, dii, public, pledged)
        SELECT company_id, quarter, promoter, fii, dii, public, pledged FROM df
    """)
```

---

### Step 5: corporate_actions_loader.py

> yfinance provides splits and dividends directly — no manual effort needed.

```python
def load_corporate_actions(company_id: int, symbol: str):
    ticker = yf.Ticker(f"{symbol}.NS")

    splits = ticker.splits.reset_index()
    splits.columns = ["date", "split_ratio"]
    for _, row in splits.iterrows():
        con.execute("""
            INSERT OR REPLACE INTO corporate_actions (company_id, action_type, split)
            VALUES (?, 'SPLIT', ?)
        """, [company_id, row["split_ratio"]])

    dividends = ticker.dividends.reset_index()
    dividends.columns = ["date", "amount"]
    for _, row in dividends.iterrows():
        con.execute("""
            INSERT OR REPLACE INTO corporate_actions (company_id, action_type, dividend)
            VALUES (?, 'DIVIDEND', ?)
        """, [company_id, row["amount"]])

    time.sleep(0.3)
```

---

### Step 6: document_tracker.py

> Tracks PDFs dropped into your local folders into the `documents` table. Uses a SHA-256 checksum to avoid re-processing
> the same file twice.

```python
import hashlib
import os
from pathlib import Path


def get_checksum(filepath: str) -> str:
    with open(filepath, "rb") as f:
        return hashlib.sha256(f.read()).hexdigest()


def scan_and_register_documents():
    folders = {
        "data/transcripts": "CONCALL_TRANSCRIPT",
        "data/annual_reports": "ANNUAL_REPORT",
    }

    for folder, doc_type in folders.items():
        for filepath in Path(folder).glob("*.pdf"):
            checksum = get_checksum(str(filepath))

            existing = con.execute(
                "SELECT document_id FROM documents WHERE checksum = ?", [checksum]
            ).fetchone()

            if existing:
                continue  # already registered

            # Filename convention: SYMBOL_QUARTER.pdf e.g. TCS_Q1FY25.pdf
            parts = filepath.stem.split("_")
            symbol = parts[0]
            quarter = parts[1] if len(parts) > 1 else None

            company = con.execute(
                "SELECT company_id FROM companies WHERE symbol = ?", [symbol]
            ).fetchone()

            if not company:
                print(f"Unknown symbol in filename: {filepath.name}")
                continue

            con.execute("""
                INSERT INTO documents (company_id, document_type, quarter, file_path, checksum, processed)
                VALUES (?, ?, ?, ?, ?, FALSE)
            """, [company[0], doc_type, quarter, str(filepath), checksum])
            print(f"Registered: {filepath.name}")
```

---

### Step 7: Layer 3 Engines (Pure SQL)

```python
# growth_engine.py — no AI, pure DuckDB window functions
def run_growth_engine():
    con.execute("""
        INSERT OR REPLACE INTO quarterly_metrics
        SELECT
            company_id,
            quarter,
            (revenue - LAG(revenue, 4) OVER w) / LAG(revenue, 4) OVER w AS revenue_growth,
            (pat    - LAG(pat,    4) OVER w) / LAG(pat,    4) OVER w AS pat_growth,
            (fcf    - LAG(fcf,    4) OVER w) / NULLIF(LAG(fcf, 4) OVER w, 0) AS fcf_growth,
            debt / NULLIF(shareholder_equity, 0) AS debt_to_equity,
            fcf  / NULLIF(revenue, 0) AS fcf_margin,
            (promoter - LAG(promoter, 1) OVER w) AS promoter_change
        FROM quarterly_financials
        JOIN shareholding USING (company_id, quarter)
        WINDOW w AS (PARTITION BY company_id ORDER BY quarter)
    """)
```

> ⚠️ **Important:** `debt_to_equity` is only correct if shareholder equity is ingested from the balance sheet into
> `quarterly_financials` (or joined from a separate balance-sheet table). Using PAT here would be mathematically wrong.

---

### Step 8: Thesis Input (Streamlit UI)

```python
# app.py — Streamlit form for manual thesis entry
import streamlit as st

st.title("Athena — Investment Thesis")
company = st.selectbox("Company", get_companies())
bull = st.text_area("Bull Case")
bear = st.text_area("Bear Case")
invalid = st.text_input("Invalidation Metric (e.g. ROCE drops below 15%)")

if st.button("Save Thesis"):
    con.execute("""
        INSERT INTO investment_thesis (company_id, valid_from, is_active, bull_case, bear_case, invalidation_metric)
        VALUES (?, today(), TRUE, ?, ?, ?)
    """, [company.id, bull, bear, invalid])
    st.success("Thesis saved.")
```

---

### Top 20 Investment Opportunities — Home Screen

> **Every time you open Athena, the first thing you see is the 20 best investment ideas your own research has surfaced —
ranked, scored, and explained. No gut feel. Pure signal from your data.**

#### The Investment Score Formula

The composite score is computed entirely from data already in DuckDB — no AI call at render time, instant load.

```
Investment Score =
    (business_score  × 0.25)   +   -- Layer 3: financial quality (0–10)
    (conviction_score × 0.20)  +   -- Layer 5: overall conviction (0–10)
    (ownership_score × 0.10)   +   -- Layer 3: promoter/FII behaviour (0–10)
    valuation_bonus            +   -- +2.0 if PE below own 5-year median
    multibagger_bonus          +   -- composite_score / 2.0 (0–4 from 0–8 flags)
    cycle_bonus                +   -- +3.0 EARLY_RECOVERY, +1.5 TROUGH, -2.0 PEAK
    thesis_bonus               +   -- +2.0 Strengthening, -2.0 Weakening, -10 Broken
    leverage_bonus             +   -- +1.5 if op_leverage_score > 1.5
    tam_bonus                  +   -- +1.5 if validated TAM signal in last 90 days
    divergence_bonus               -- +2.0 if PRICE_BUSINESS_DIVERGENCE alert exists
```

Companies with a broken thesis (`thesis_status = 'Broken'`) are automatically excluded.

#### DuckDB Composite Scoring Query

```python
# engines/top20_engine.py
import duckdb

con = duckdb.connect("athena.duckdb")

TOP20_QUERY = """
WITH latest_q AS (
    SELECT company_id, MAX(quarter) AS q
    FROM quarterly_metrics
    GROUP BY company_id
),
latest_vh AS (
    SELECT company_id, pe, pe_percentile
    FROM valuation_history
    WHERE date = (SELECT MAX(date) FROM valuation_history)
),
latest_ms AS (
    SELECT company_id, composite_score
    FROM multibagger_screen
    WHERE screen_date = (SELECT MAX(screen_date) FROM multibagger_screen)
),
latest_tv AS (
    SELECT mt.company_id, tv.status
    FROM (SELECT company_id, MAX(quarter) AS q FROM thesis_validation GROUP BY company_id) mt
    JOIN thesis_validation tv ON tv.company_id = mt.company_id AND tv.quarter = mt.q
),
latest_ch AS (
    SELECT mc.company_id, ch.total_score, ch.business_score, ch.ownership_score
    FROM (SELECT company_id, MAX(date) AS d FROM conviction_history GROUP BY company_id) mc
    JOIN conviction_history ch ON ch.company_id = mc.company_id AND ch.date = mc.d
),
recent_tam AS (
    SELECT DISTINCT company_id
    FROM tam_signals
    WHERE validated = TRUE
      AND signal_date >= CURRENT_DATE - INTERVAL '90 days'
),
recent_div AS (
    SELECT DISTINCT company_id
    FROM alert_log
    WHERE alert_type = 'PRICE_BUSINESS_DIVERGENCE'
      AND alert_id IN (SELECT MAX(alert_id) FROM alert_log GROUP BY company_id)
),
scored AS (
    SELECT
        c.company_id,
        c.symbol,
        c.name,
        c.sector,

        -- Component scores
        COALESCE(ch.business_score,  0)                  AS business_score,
        COALESCE(ch.total_score,     0)                  AS conviction_score,
        COALESCE(ch.ownership_score, 0)                  AS ownership_score,
        COALESCE(ms.composite_score, 0)                  AS multibagger_flags,

        -- Key display metrics
        COALESCE(qm.revenue_growth, 0)                   AS revenue_growth,
        COALESCE(qm.pat_growth,     0)                   AS pat_growth,
        COALESCE(vh.pe,             0)                   AS pe,
        COALESCE(vh.pe_percentile,  50)                  AS pe_percentile,
        COALESCE(cp.cycle_stage,    'UNKNOWN')           AS cycle_stage,
        COALESCE(tv.status,         'No Thesis')         AS thesis_status,
        COALESCE(ol.op_leverage_score, 0)                AS op_leverage_score,

        -- Bonus components
        CASE WHEN vh.pe < vh.pe_percentile          THEN 2.0  ELSE 0.0 END AS valuation_bonus,
        COALESCE(ms.composite_score, 0) / 2.0                              AS multibagger_bonus,
        CASE cp.cycle_stage
            WHEN 'EARLY_RECOVERY' THEN 3.0
            WHEN 'TROUGH'         THEN 1.5
            WHEN 'MID_CYCLE'      THEN 0.5
            WHEN 'LATE_CYCLE'     THEN -0.5
            WHEN 'PEAK'           THEN -2.0
            ELSE 0.0 END                                                    AS cycle_bonus,
        CASE tv.status
            WHEN 'Strengthening'  THEN 2.0
            WHEN 'Needs Review'   THEN 0.0
            WHEN 'Weakening'      THEN -2.0
            WHEN 'Broken'         THEN -10.0
            ELSE 0.0 END                                                    AS thesis_bonus,
        CASE WHEN ol.op_leverage_score > 1.5        THEN 1.5  ELSE 0.0 END AS leverage_bonus,
        CASE WHEN rt.company_id IS NOT NULL         THEN 1.5  ELSE 0.0 END AS tam_bonus,
        CASE WHEN rd.company_id IS NOT NULL         THEN 2.0  ELSE 0.0 END AS divergence_bonus

    FROM companies c
    JOIN latest_q lq ON lq.company_id = c.company_id
    JOIN quarterly_metrics qm ON qm.company_id = c.company_id AND qm.quarter = lq.q
    LEFT JOIN latest_vh  vh ON vh.company_id  = c.company_id
    LEFT JOIN latest_ms  ms ON ms.company_id  = c.company_id
    LEFT JOIN latest_tv  tv ON tv.company_id  = c.company_id
    LEFT JOIN latest_ch  ch ON ch.company_id  = c.company_id
    LEFT JOIN cycle_positioning cp
           ON cp.company_id = c.company_id AND cp.quarter = lq.q
    LEFT JOIN operating_leverage ol
           ON ol.company_id = c.company_id AND ol.quarter = lq.q
    LEFT JOIN recent_tam rt ON rt.company_id  = c.company_id
    LEFT JOIN recent_div rd ON rd.company_id  = c.company_id
    WHERE c.active = TRUE
      AND COALESCE(tv.status, '') != 'Broken'   -- hard exclude broken theses
),
final AS (
    SELECT *,
        ROUND(
            (business_score  * 0.25) +
            (conviction_score * 0.20) +
            (ownership_score * 0.10) +
            valuation_bonus + multibagger_bonus + cycle_bonus +
            thesis_bonus + leverage_bonus + tam_bonus + divergence_bonus
        , 2) AS investment_score,

        -- Top signal: the single biggest contributing factor for this stock
        CASE
            WHEN cycle_stage     = 'EARLY_RECOVERY'    THEN '🔄 Early Cycle Recovery'
            WHEN divergence_bonus > 0                  THEN '📈 Business up, Price lagging'
            WHEN tam_bonus        > 0                  THEN '🌐 New Market Opportunity'
            WHEN multibagger_flags >= 6                THEN '💎 Strong Compounder Profile'
            WHEN thesis_status    = 'Strengthening'    THEN '✅ Thesis Strengthening'
            WHEN valuation_bonus  > 0                  THEN '💰 Cheap vs Own History'
            WHEN leverage_bonus   > 0                  THEN '⚙️ Operating Leverage Kicking In'
            ELSE '📊 Solid Fundamentals'
        END AS top_signal

    FROM scored
)
SELECT
    symbol, name, sector, investment_score, top_signal,
    business_score, conviction_score, multibagger_flags,
    revenue_growth, pat_growth, pe, pe_percentile,
    cycle_stage, thesis_status, op_leverage_score,

    -- Human-readable reason: concatenates only active signals, NULLs auto-skipped
    CONCAT_WS(' · ',
        CASE WHEN revenue_growth  > 0.15 THEN 'Revenue ▲' || ROUND(revenue_growth  * 100)::TEXT || '%' END,
        CASE WHEN pat_growth      > 0.15 THEN 'PAT ▲'     || ROUND(pat_growth      * 100)::TEXT || '%' END,
        CASE WHEN valuation_bonus > 0    THEN 'PE below 5yr avg'            END,
        CASE WHEN cycle_stage = 'EARLY_RECOVERY' THEN 'Early cycle recovery' END,
        CASE WHEN leverage_bonus  > 0    THEN 'Margins expanding'            END,
        CASE WHEN multibagger_flags >= 6 THEN multibagger_flags::TEXT || '/8 compounder flags' END,
        CASE WHEN tam_bonus        > 0   THEN 'New market opportunity'       END,
        CASE WHEN divergence_bonus > 0   THEN 'Business ↑ while price lags' END,
        CASE WHEN thesis_status = 'Strengthening' THEN 'Thesis strengthening' END
    ) AS reason_summary

FROM final
ORDER BY investment_score DESC
LIMIT 20
"""


def get_top20() -> list[dict]:
    return con.execute(TOP20_QUERY).df().to_dict(orient="records")
```

#### Streamlit Home Screen (app.py)

```python
import streamlit as st
import pandas as pd
import duckdb
from engines.top20_engine import get_top20

st.set_page_config(page_title="Athena", layout="wide", page_icon="🦉")
con = duckdb.connect("athena.duckdb")

# ── Global CSS ─────────────────────────────────────────────────────────────────
CARD_CSS = """
<style>
.athena-card {
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    padding: 16px 18px;
    margin-bottom: 14px;
    background: #ffffff;
    box-shadow: 0 1px 4px rgba(0,0,0,0.07);
    font-family: -apple-system, BlinkMacSystemFont, sans-serif;
}
.card-header { display: flex; justify-content: space-between; align-items: flex-start; }
.card-rank   { font-size: 11px; color: #94a3b8; font-weight: 600; letter-spacing: 0.05em; }
.card-symbol { font-size: 18px; font-weight: 800; color: #0f172a; }
.card-name   { font-size: 13px; color: #475569; margin-top: 1px; }
.card-sector { font-size: 11px; color: #94a3b8; }
.score-badge-green  { background:#dcfce7; color:#15803d; padding:5px 14px;
                      border-radius:20px; font-weight:800; font-size:16px; }
.score-badge-amber  { background:#fef9c3; color:#854d0e; padding:5px 14px;
                      border-radius:20px; font-weight:800; font-size:16px; }
.score-badge-red    { background:#fee2e2; color:#991b1b; padding:5px 14px;
                      border-radius:20px; font-weight:800; font-size:16px; }
.signal-pill {
    display:inline-block; padding:3px 10px; border-radius:14px;
    font-size:12px; font-weight:600; margin: 6px 0;
    background:#f0fdf4; color:#166534; border: 1px solid #bbf7d0;
}
.metrics-row { display:flex; gap:18px; margin:10px 0 8px; }
.metric-box  { flex:1; }
.metric-label { font-size:10px; color:#94a3b8; text-transform:uppercase;
                letter-spacing:0.06em; font-weight:600; }
.metric-value { font-size:15px; font-weight:700; color:#0f172a; }
.metric-value.up   { color: #15803d; }
.metric-value.down { color: #dc2626; }
.pill-row { display:flex; flex-wrap:wrap; gap:5px; margin: 6px 0 10px; }
.pill { padding:2px 9px; border-radius:10px; font-size:11px; font-weight:600; }
.pill-green  { background:#dcfce7; color:#15803d; }
.pill-amber  { background:#fef9c3; color:#854d0e; }
.pill-red    { background:#fee2e2; color:#991b1b; }
.pill-blue   { background:#dbeafe; color:#1d4ed8; }
.pill-gray   { background:#f1f5f9; color:#475569; }
.reason-box {
    border-top: 1px solid #f1f5f9; padding-top: 8px; margin-top: 4px;
    font-size: 12px; color: #475569; line-height: 1.5;
}
.reason-label { font-weight: 700; color: #0f172a; }
</style>
"""


# ── Helpers ────────────────────────────────────────────────────────────────────
def score_badge(score: float) -> str:
    cls = "score-badge-green" if score >= 12 else ("score-badge-amber" if score >= 8 else "score-badge-red")
    return f'<span class="{cls}">{score:.1f}</span>'


def thesis_pill(status: str) -> str:
    style = {"Strengthening": "pill-green", "Weakening": "pill-amber",
             "Broken": "pill-red", "Needs Review": "pill-blue"}.get(status, "pill-gray")
    icons = {"Strengthening": "✅", "Weakening": "⚠️", "Broken": "❌", "Needs Review": "🔍"}
    return f'<span class="pill {style}">{icons.get(status, "")} {status}</span>'


def cycle_pill(stage: str) -> str:
    style = {"EARLY_RECOVERY": "pill-green", "TROUGH": "pill-amber",
             "MID_CYCLE": "pill-gray", "PEAK": "pill-red"}.get(stage, "pill-gray")
    labels = {"EARLY_RECOVERY": "🔄 Early Recovery", "TROUGH": "📉 Trough",
              "MID_CYCLE": "➡️ Mid Cycle", "PEAK": "⛰️ Peak", "UNKNOWN": "— Cycle N/A"}
    return f'<span class="pill {style}">{labels.get(stage, stage)}</span>'


def flags_pill(flags: int) -> str:
    style = "pill-green" if flags >= 6 else ("pill-amber" if flags >= 4 else "pill-gray")
    return f'<span class="pill {style}">🚩 {flags}/8 flags</span>'


def pct(val: float, good_if_positive: bool = True) -> str:
    cls = ("up" if val >= 0 else "down") if good_if_positive else ("down" if val >= 0 else "up")
    arrow = "▲" if val >= 0 else "▼"
    return f'<span class="metric-value {cls}">{arrow} {abs(val) * 100:.1f}%</span>'


def pe_display(pe: float, percentile: float) -> str:
    cls = "up" if percentile <= 40 else ("down" if percentile >= 70 else "")
    note = "cheap" if percentile <= 40 else ("expensive" if percentile >= 70 else "fair")
    return f'<span class="metric-value {cls}">{pe:.1f}x</span> <span style="font-size:10px;color:#94a3b8;">({percentile:.0f}th %ile · {note})</span>'


def build_card(rank: int, row: dict) -> str:
    lev_pill = ('<span class="pill pill-green">⚙️ Op. Leverage</span>'
                if row.get("op_leverage_score", 0) > 1.5 else "")
    reason = row.get("reason_summary") or "Solid multi-factor fundamentals"
    return f"""
<div class="athena-card">
  <div class="card-header">
    <div>
      <div class="card-rank">#{rank}</div>
      <div class="card-symbol">{row['symbol']}</div>
      <div class="card-name">{row['name']}</div>
      <div class="card-sector">{row['sector']}</div>
    </div>
    <div style="text-align:right">
      {score_badge(row['investment_score'])}
      <div style="font-size:10px;color:#94a3b8;margin-top:4px;">score</div>
    </div>
  </div>

  <div style="margin-top:8px;">
    <span class="signal-pill">{row['top_signal']}</span>
  </div>

  <div class="metrics-row">
    <div class="metric-box">
      <div class="metric-label">Revenue Growth</div>
      <div>{pct(row['revenue_growth'])}</div>
    </div>
    <div class="metric-box">
      <div class="metric-label">PAT Growth</div>
      <div>{pct(row['pat_growth'])}</div>
    </div>
    <div class="metric-box">
      <div class="metric-label">Valuation</div>
      <div>{pe_display(row['pe'], row['pe_percentile'])}</div>
    </div>
    <div class="metric-box">
      <div class="metric-label">Conviction</div>
      <div><span class="metric-value">{row['conviction_score']:.1f}/10</span></div>
    </div>
  </div>

  <div class="pill-row">
    {cycle_pill(row['cycle_stage'])}
    {thesis_pill(row['thesis_status'])}
    {flags_pill(int(row['multibagger_flags']))}
    {lev_pill}
  </div>

  <div class="reason-box">
    <span class="reason-label">Why ranked #{rank}: </span>{reason}
  </div>
</div>
"""


# ── Main render ────────────────────────────────────────────────────────────────
def render_home():
    st.markdown(CARD_CSS, unsafe_allow_html=True)
    st.title("🦉 Athena — Top 20 Investment Opportunities")
    st.caption("Ranked by composite score · business quality · valuation · cycle · thesis · management")

    rows = get_top20()
    if not rows:
        st.warning("No data yet — run your loaders and engines first (Sections 8 Steps 0–7).")
        return

    df = pd.DataFrame(rows)

    # ── KPI summary row ────────────────────────────────────────────────────────
    k1, k2, k3, k4, k5 = st.columns(5)
    k1.metric("Universe",
              con.execute("SELECT COUNT(*) FROM companies WHERE active=TRUE").fetchone()[0],
              help="Total active companies monitored")
    k2.metric("Avg Score (Top 20)", f"{df['investment_score'].mean():.1f}")
    k3.metric("🔄 Early Recovery", int((df['cycle_stage'] == 'EARLY_RECOVERY').sum()))
    k4.metric("✅ Thesis Strengthening", int((df['thesis_status'] == 'Strengthening').sum()))
    k5.metric("💰 Cheap vs History", int((df['pe_percentile'] < 40).sum()))

    st.markdown("---")

    # ── Card grid — 2 columns ──────────────────────────────────────────────────
    left_col, right_col = st.columns(2)
    for i, row in enumerate(rows):
        col = left_col if i % 2 == 0 else right_col
        with col:
            st.markdown(build_card(i + 1, row), unsafe_allow_html=True)

    # ── Compact reference table (toggle) ──────────────────────────────────────
    st.markdown("---")
    with st.expander("📋 View as compact table"):
        tbl = df[[
            "symbol", "name", "investment_score", "top_signal",
            "revenue_growth", "pat_growth", "pe", "pe_percentile",
            "multibagger_flags", "cycle_stage", "thesis_status"
        ]].copy()
        tbl.index = range(1, len(tbl) + 1)
        tbl.columns = [
            "Symbol", "Company", "Score", "Top Signal",
            "Rev ▲", "PAT ▲", "PE", "PE %ile",
            "🚩 Flags", "Cycle", "Thesis"
        ]

        THESIS_COLORS = {
            "Strengthening": "background-color:#dcfce7;color:#15803d",
            "Weakening": "background-color:#fef9c3;color:#854d0e",
            "Broken": "background-color:#fee2e2;color:#991b1b",
            "Needs Review": "background-color:#dbeafe;color:#1d4ed8",
        }
        CYCLE_COLORS = {
            "EARLY_RECOVERY": "background-color:#dcfce7;color:#15803d;font-weight:bold",
            "TROUGH": "background-color:#fef9c3;color:#854d0e",
            "PEAK": "background-color:#fee2e2;color:#991b1b",
        }

        styled = (
            tbl.style
            .format({
                "Score": "{:.1f}",
                "Rev ▲": "{:.1%}",
                "PAT ▲": "{:.1%}",
                "PE": "{:.1f}x",
                "PE %ile": "{:.0f}",
            })
            .applymap(lambda v: THESIS_COLORS.get(v, ""), subset=["Thesis"])
            .applymap(lambda v: CYCLE_COLORS.get(v, ""), subset=["Cycle"])
            .applymap(lambda v: "font-weight:bold;color:#15803d" if v >= 12
            else ("color:#854d0e" if v >= 8 else "color:#991b1b"),
                      subset=["Score"])
            .set_table_styles([{
                "selector": "th",
                "props": [("background", "#0f172a"), ("color", "white"),
                          ("font-weight", "bold"), ("padding", "8px 10px")]
            }])
        )
        st.dataframe(styled, use_container_width=True)


# ── App entry point ────────────────────────────────────────────────────────────
page = st.sidebar.selectbox(
    "Navigate",
    ["🏠 Top 20", "📋 Research Feed", "🔍 Deep Research", "📝 Thesis Input"]
)
if page == "🏠 Top 20":
    render_home()
elif page == "📋 Research Feed":
    st.write("Research Feed — coming soon")
elif page == "🔍 Deep Research":
    st.write("Deep Research — coming soon")
elif page == "📝 Thesis Input":
    st.write("Thesis Input form here")
```

> **Every component of this score already exists in your DuckDB schema.** As you add more data (more quarters, more
> concall AI extractions, more thesis entries), the score automatically becomes more accurate. On Day 1 with only
> financial data, it ranks by fundamentals. By Month 6 with full AI and thesis data, it ranks by everything.

---

### Gemini Model & Cost Breakdown

> **The AI layer of Athena runs entirely on `gemini-2.0-flash` — one of the most capable and cheapest LLMs available.
For most weeks, it runs completely free.**

#### Model: gemini-2.0-flash (via Google AI Studio)

**Get your free API key:** [aistudio.google.com](https://aistudio.google.com) → Sign in → Create API Key → copy into
your project.

```python
# Set once in your environment
import google.generativeai as genai

genai.configure(api_key="YOUR_GOOGLE_AI_STUDIO_KEY")
```

#### Free Tier Limits

| Limit                     | Value     | What it means for Athena                                        |
|---------------------------|-----------|-----------------------------------------------------------------|
| Requests Per Minute (RPM) | 15        | Process 15 transcripts per minute — fine for batch weekend runs |
| Requests Per Day (RPD)    | 1,500     | 1,500 AI calls per day — far more than you'll ever need         |
| Input tokens per minute   | 1,000,000 | Entire concall transcript in one call, no chunking needed       |
| Cost on free tier         | **$0.00** | Zero. Free. No credit card needed unless you exceed the above   |

#### If You Go Over (Pay-as-you-go) — Full Model Comparison

| Model                          | Input cost             | Output cost | Free tier         | Best for                                                 |
|--------------------------------|------------------------|-------------|-------------------|----------------------------------------------------------|
| `gemini-2.0-flash-lite`        | **$0.025 / 1M tokens** | $0.10 / 1M  | 30 RPM, 1,500 RPD | Simple structured extraction — **4× cheaper than flash** |
| `gemini-2.0-flash`             | $0.10 / 1M tokens      | $0.40 / 1M  | 15 RPM, 1,500 RPD | Complex reasoning, thesis validation, tone analysis      |
| `gemini-1.5-flash-8b`          | $0.0375 / 1M tokens    | $0.15 / 1M  | 15 RPM, 1,500 RPD | Lightweight backup — older but cheap                     |
| `gpt-4o-mini` (OpenAI)         | $0.15 / 1M tokens      | $0.60 / 1M  | None              | 1.5× more expensive than flash, no free tier             |
| `claude-haiku-3.5` (Anthropic) | $0.80 / 1M tokens      | $4.00 / 1M  | None              | 8× more expensive — not recommended for Athena           |

> **Recommendation: Use `gemini-2.0-flash-lite` for everything first. Switch only specific tasks to `gemini-2.0-flash`
when quality isn't sufficient.** For this project, flash-lite handles ~80% of tasks perfectly well.

---

#### Hybrid Model Strategy (Best Cost / Quality Balance)

Route each AI task to the appropriate model based on complexity:

| Task                                          | Model                   | Reason                                                              |
|-----------------------------------------------|-------------------------|---------------------------------------------------------------------|
| Structured extraction (capex, margin, demand) | `gemini-2.0-flash-lite` | Fixed Pydantic schema — model just needs to follow instructions     |
| Change analysis (Q vs Q-1 diff)               | `gemini-2.0-flash-lite` | Comparison is mechanical — no deep reasoning needed                 |
| TAM signal extraction                         | `gemini-2.0-flash-lite` | Keyword + context matching                                          |
| Concall tone analysis                         | `gemini-2.0-flash`      | Requires nuance — detecting evasion vs honesty needs stronger model |
| Thesis validation                             | `gemini-2.0-flash`      | Requires multi-step reasoning against thesis criteria               |
| Annual report year-over-year comparison       | `gemini-2.0-flash`      | Long-context cross-document reasoning                               |
| RAG answer generation (Deep Research)         | `gemini-2.0-flash`      | Free-form financial analysis needs full capability                  |

```python
# ai/model_router.py
import google.generativeai as genai

# Task complexity levels
LITE_TASKS = {
    "structured_extraction",  # capex, margin, demand from concall
    "change_analysis",  # Q vs Q-1 diff
    "tam_extraction",  # TAM signal keywords
}

FLASH_TASKS = {
    "tone_analysis",  # concall credibility / evasion detection
    "thesis_validation",  # multi-step reasoning
    "annual_report_compare",  # year-over-year promise tracking
    "rag_answer",  # Deep Research free-form questions
}


def get_model(task: str) -> genai.GenerativeModel:
    """Return the appropriate Gemini model for a given task type."""
    if task in LITE_TASKS:
        return genai.GenerativeModel("gemini-2.0-flash-lite")
    elif task in FLASH_TASKS:
        return genai.GenerativeModel("gemini-2.0-flash")
    else:
        # Default to lite — upgrade manually if quality insufficient
        return genai.GenerativeModel("gemini-2.0-flash-lite")
```

**Revised annual cost with hybrid routing:**

| Period                       | Model mix            | Tokens | Cost                    |
|------------------------------|----------------------|--------|-------------------------|
| Earnings season week (4×/yr) | 70% lite + 30% flash | ~2.5M  | **≈ $0.09/week**        |
| Non-earnings week            | None                 | 0      | $0.00                   |
| **Full year total**          |                      |        | **≈ $0.36/year (~₹30)** |

#### Real Cost Per Document

| Document                                   | Approx. tokens        | Input cost             |
|--------------------------------------------|-----------------------|------------------------|
| Concall transcript (60 min)                | ~20,000 tokens        | **$0.002** (0.2 paise) |
| Quarterly result PDF                       | ~10,000 tokens        | **$0.001** (0.1 paise) |
| Annual report (200 pages)                  | ~100,000 tokens       | **$0.01** (1 paise)    |
| Annual report with chain-of-thought output | ~120,000 tokens total | **$0.015**             |

#### Weekly Usage Estimate for 150 Companies

| Task                                        | Frequency         | API calls      | Tokens (est.)    | Cost             |
|---------------------------------------------|-------------------|----------------|------------------|------------------|
| Concall extraction (earnings season, 4x/yr) | 150 calls/quarter | ~38/week avg   | ~750K/week       | $0.075/week      |
| Change analysis (compare Q vs Q-1)          | 150 calls/quarter | ~38/week avg   | ~1.5M/week       | $0.15/week       |
| Thesis validation                           | 150 calls/quarter | ~38/week avg   | ~300K/week       | $0.03/week       |
| Daily alert generation (no AI — pure math)  | Daily             | 0              | 0                | $0.00            |
| **Total (earnings season week)**            |                   | **~114 calls** | **~2.5M tokens** | **≈ $0.25/week** |
| **Total (non-earnings week)**               |                   | **0 calls**    | **0**            | **$0.00**        |

> **Bottom line:** During earnings season (4 weeks a year), you'll spend roughly **₹20/week** on AI. The other 48 weeks
> of the year: **₹0**. Annual total: under **₹100** (~$1.20) even at full pay-as-you-go pricing. Most developers will
> never leave the free tier.

#### Rate Limit Handling

```python
import time
from google.api_core.exceptions import ResourceExhausted


def call_gemini_with_retry(model, prompt: str, max_retries: int = 3):
    for attempt in range(max_retries):
        try:
            return model.generate_content(prompt)
        except ResourceExhausted:
            wait = 60 * (attempt + 1)  # 60s, 120s, 180s
            print(f"Rate limit hit. Waiting {wait}s before retry {attempt + 1}/{max_retries}...")
            time.sleep(wait)
    raise RuntimeError("Gemini rate limit exceeded after max retries.")
```

---

### Step 9: AI Extraction (transcript_parser.py)

```python
import google.generativeai as genai
from pydantic import BaseModel


class ConcallExtraction(BaseModel):
    capex_guidance: str
    margin_guidance: str
    demand_outlook: str
    quotes: str
    positive_changes: str
    negative_changes: str
    new_risks: str
    new_market_mentions: str
    tam_expansion_score: float


def parse_transcript(company_id: int, quarter: str, filepath: str):
    text = open(filepath).read()

    model = genai.GenerativeModel("gemini-2.0-flash")
    prompt = f"""
    Analyze this concall transcript and return a JSON matching this schema exactly:
    {ConcallExtraction.model_json_schema()}

    Transcript:
    {text}
    """

    response = model.generate_content(prompt)
    data = ConcallExtraction.model_validate_json(response.text)

    con.execute("""
        INSERT OR REPLACE INTO ai_extractions
        (company_id, quarter, capex_guidance, margin_guidance, demand_outlook, quotes,
         new_market_mentions, tam_expansion_score)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """, [company_id, quarter, data.capex_guidance, data.margin_guidance,
          data.demand_outlook, data.quotes, data.new_market_mentions, data.tam_expansion_score])

    con.execute("""
        INSERT OR REPLACE INTO change_analysis
        (company_id, quarter, positive_changes, negative_changes, new_risks)
        VALUES (?, ?, ?, ?, ?)
    """, [company_id, quarter, data.positive_changes, data.negative_changes, data.new_risks])

    # Mark document as processed
    con.execute("UPDATE documents SET processed = TRUE WHERE company_id = ? AND quarter = ?",
                [company_id, quarter])
```

---

### Step 10: Daily Orchestration (Prefect)

```python
# flows.py
from prefect import flow, task
from prefect.schedules import CronSchedule


@task
def task_load_prices():          load_prices()


@task
def task_load_corporate_actions(): load_all_corporate_actions()


@task
def task_run_growth_engine():    run_growth_engine()


@task
def task_run_valuation_engine(): run_valuation_engine()


@task
def task_scan_documents():       scan_and_register_documents()


@task
def task_parse_new_transcripts(): parse_all_unprocessed_documents()


@task
def task_generate_alerts():      generate_all_alerts()


@flow(schedule=CronSchedule(cron="0 18 * * 1-5"))  # 6 PM weekdays
def daily_athena_flow():
    task_load_prices()
    task_load_corporate_actions()
    task_run_growth_engine()
    task_run_valuation_engine()
    task_scan_documents()
    task_parse_new_transcripts()
    task_generate_alerts()
```

---

### Critical Gotchas Summary

| Gotcha                                             | Fix                                                       |
|----------------------------------------------------|-----------------------------------------------------------|
| Screener exports wide format (quarters as columns) | Use `df.T` to transpose, then `melt()` or rename directly |
| yfinance needs `.NS` suffix for NSE stocks         | Append `.NS` at fetch time: `f"{symbol}.NS"`              |
| 150–200 stocks will hit rate limits                | Add `time.sleep(0.5)` between each yfinance call          |
| Re-processing the same PDF twice                   | Use SHA-256 checksum in `document_tracker.py`             |
| Screener column names differ from your schema      | Use `rename_map` dict in every loader                     |
| Gemini may return malformed JSON                   | Wrap `model_validate_json()` in try/except, log and skip  |

[↑ Back to TOC](#table-of-contents)

---

## 9. Tech Stack & Data Flow

### Tech Stack

| Category                  | Tool / Library                         | Version  | Purpose                                                                                     |
|---------------------------|----------------------------------------|----------|---------------------------------------------------------------------------------------------|
| **Database**              | DuckDB                                 | `>=0.10` | Local analytical database — stores all 6 layers, runs window functions for Layer 3          |
| **Language**              | Python                                 | `>=3.10` | All loaders, engines, AI parsing, orchestration                                             |
| **Data — Financials**     | yfinance                               | `>=0.2`  | Quarterly P&L, balance sheet, cash flow, prices, splits, dividends for NSE stocks           |
| **Data — Shareholding**   | NSE Archives (HTTP)                    | —        | Quarterly bulk shareholding ZIP files, official and free                                    |
| **Data — Latest Quarter** | BSE XBRL API (HTTP)                    | —        | Same-day quarterly result filings in structured JSON/XML                                    |
| **Data — Universe**       | Screener.in (manual export)            | —        | Screen results CSV to seed the `companies` table                                            |
| **Data Manipulation**     | pandas                                 | `>=2.0`  | DataFrame reshaping, type coercion, merging across sources                                  |
| **AI / LLM**              | google-generativeai (Gemini 2.0 Flash) | `>=0.5`  | Concall transcript parsing, TAM signal extraction, tone analysis — free tier: 1,500 req/day |
| **Structured AI Output**  | Pydantic                               | `>=2.0`  | Forces Gemini to return schema-validated JSON — prevents silent data corruption             |
| **PDF Parsing**           | pdfplumber                             | `>=0.10` | Extract text from concall PDFs and annual reports                                           |
| **Orchestration**         | Prefect                                | `>=2.0`  | Schedules and manages the daily pipeline (6 PM weekdays)                                    |
| **UI**                    | Streamlit                              | `>=1.30` | Research feed dashboard + thesis input forms                                                |
| **XML Parsing**           | xmltodict                              | `>=0.13` | Parses BSE XBRL XML bulk download files                                                     |
| **Checksums**             | hashlib                                | stdlib   | SHA-256 fingerprinting for PDF deduplication in document_tracker                            |
| **HTTP Requests**         | requests                               | `>=2.31` | BSE/NSE API calls and shareholding ZIP downloads                                            |

---

### Install All Dependencies

```bash
pip install duckdb pandas yfinance google-generativeai pydantic \
            pdfplumber prefect streamlit xmltodict requests \
            beautifulsoup4
```

---

### End-to-End Data Flow

```
╔══════════════════════════════════════════════════════════════════════════════╗
║                        ATHENA — END-TO-END DATA FLOW                        ║
╚══════════════════════════════════════════════════════════════════════════════╝

 ┌─────────────────────────────────────────────────────────────────────────┐
 │  STEP 0 — UNIVERSE SEEDING (one-time + quarterly refresh)               │
 │                                                                         │
 │  Screener.in Screen Export (universe.csv)                               │
 │          │                                                              │
 │          ▼                                                              │
 │  universe_loader.py  ──────────────────►  companies  (Layer 1)         │
 └─────────────────────────────────────────────────────────────────────────┘
                  │
                  ▼  companies table seeded → all loaders below activate
 ┌─────────────────────────────────────────────────────────────────────────┐
 │  STEPS 1–6 — LAYER 1 INGESTION (daily / quarterly)                     │
 │                                                                         │
 │  yfinance (.NS)                                                         │
 │  ├── quarterly_financials ──►  financials_loader.py  ──►  Layer 1      │
 │  ├── daily prices         ──►  price_loader.py       ──►  Layer 1      │
 │  └── splits / dividends   ──►  corp_actions_loader   ──►  Layer 1      │
 │                                                                         │
 │  NSE Archives (ZIP)                                                     │
 │  └── shareholding pattern ──►  shareholding_loader   ──►  Layer 1      │
 │                                                                         │
 │  BSE XBRL API                                                           │
 │  └── latest quarter       ──►  bse_loader.py         ──►  Layer 1      │
 │                                                                         │
 │  Manual PDFs (data/transcripts, data/annual_reports)                   │
 │  └── document_tracker.py  ──►  documents             (Layer 2)         │
 └─────────────────────────────────────────────────────────────────────────┘
                  │
                  ▼  Layer 1 complete → trigger Layer 3 engines
 ┌─────────────────────────────────────────────────────────────────────────┐
 │  STEP 7 — LAYER 3 DETERMINISTIC ENGINES (pure Python + DuckDB SQL)     │
 │  No AI. No LLMs. Pure math.                                             │
 │                                                                         │
 │  growth_engine.py      ──►  quarterly_metrics    (YoY growth, margins) │
 │  valuation_engine.py   ──►  valuation_history    (PE, PB, percentiles) │
 │  leverage_engine.py    ──►  operating_leverage   (margin inflection)   │
 │  capital_engine.py     ──►  capital_allocation   (ROIC trend)          │
 │  cycle_engine.py       ──►  cycle_positioning    (trough/recovery)     │
 │  screen_engine.py      ──►  multibagger_screen   (composite flags)     │
 │  score_engine.py       ──►  financial_scores,    ownership_scores      │
 └─────────────────────────────────────────────────────────────────────────┘
                  │
                  ▼  Layer 3 complete + unprocessed PDFs → trigger AI
 ┌─────────────────────────────────────────────────────────────────────────┐
 │  STEP 9 — LAYER 4 AI INTERPRETATION (Gemini 1.5 Pro + Pydantic)        │
 │  AI interprets text. AI does not calculate.                             │
 │                                                                         │
 │  transcript_parser.py                                                   │
 │  ├──►  ai_extractions         (capex, margin, demand guidance)         │
 │  ├──►  change_analysis        (positives, negatives, new risks)        │
 │  ├──►  concall_tone_analysis  (credibility score, evasion flags)       │
 │  └──►  tam_signals            (new markets, import substitution)       │
 │                                                                         │
 │  annual_report_parser.py                                               │
 │  └──►  annual_report_insights (priorities, promises kept, red flags)   │
 │                                                                         │
 │  thesis_validator.py                                                    │
 │  └──►  thesis_validation      (Strengthening / Weakening / Broken)     │
 └─────────────────────────────────────────────────────────────────────────┘
                  │
                  ▼  Layer 3 + Layer 4 complete → trigger Decision engines
 ┌─────────────────────────────────────────────────────────────────────────┐
 │  STEP 8 — LAYER 5 DECISION (Conviction + Thesis)                       │
 │                                                                         │
 │  conviction_engine.py  ──►  conviction_history  (composite score)      │
 │  Streamlit UI (manual) ──►  investment_thesis   (bull/bear/invalidate) │
 │  Streamlit UI (manual) ──►  watchlist           (target prices)        │
 └─────────────────────────────────────────────────────────────────────────┘
                  │
                  ▼  All layers complete → generate output
 ┌─────────────────────────────────────────────────────────────────────────┐
 │  STEP 10 — LAYER 6 OUTPUT (Alerts + Research Feed)                     │
 │                                                                         │
 │  alert_engine.py                                                        │
 │  ├──►  PRICE_BUSINESS_DIVERGENCE  (business up, price down)            │
 │  ├──►  CYCLE_RECOVERY_OPPORTUNITY (early recovery + cheap)             │
 │  ├──►  MARGIN_INFLECTION          (3-year EBITDA margin high)          │
 │  ├──►  HIDDEN_COMPOUNDER          (ROCE>20%, low institutional)        │
 │  ├──►  THESIS_BROKEN              (invalidation metric breached)       │
 │  └──►  PROMOTER_CONVICTION        (open market buying 2Q+)             │
 │                                                                         │
 │  feed_engine.py  ──►  research_feed  (ranked, prioritised, dated)      │
 └─────────────────────────────────────────────────────────────────────────┘
                  │
                  ▼  Saturday morning
 ┌─────────────────────────────────────────────────────────────────────────┐
 │  STREAMLIT DASHBOARD (app.py)                                           │
 │                                                                         │
 │  ┌─────────────────────┐    ┌──────────────────────────────────────┐   │
 │  │   Research Feed     │    │         Deep Research                │   │
 │  │─────────────────────│    │──────────────────────────────────────│   │
 │  │ Top Improvements    │    │ Company snapshot                     │   │
 │  │ Top Deteriorations  │    │ Thesis status + conviction score     │   │
 │  │ Theses Breaking     │    │ Management quality + tone            │   │
 │  │ Valuation Opps      │    │ TAM signals + cycle stage            │   │
 │  │ Multibagger Flags   │    │ Concall extractions (AI)             │   │
 │  └─────────────────────┘    └──────────────────────────────────────┘   │
 └─────────────────────────────────────────────────────────────────────────┘

 ┌─────────────────────────────────────────────────────────────────────────┐
 │  ORCHESTRATION — Prefect (flows.py)                                     │
 │  Cron: 0 18 * * 1-5  (6 PM, Monday–Friday)                             │
 │                                                                         │
 │  price_loader → corp_actions → growth_engine → valuation_engine        │
 │       → leverage_engine → cycle_engine → screen_engine                 │
 │       → scan_documents → parse_transcripts → thesis_validator          │
 │       → conviction_engine → alert_engine → feed_engine                 │
 └─────────────────────────────────────────────────────────────────────────┘
```

[↑ Back to TOC](#table-of-contents)

---

## 10. Extended Build Roadmap — Missing Use Cases

> These phases extend Athena beyond the core 12-week build. Each phase is self-contained and can be built independently.

---

### Phase 7: Portfolio P&L + Data Freshness (Weeks 13–14)

**Problems solved:** No record of what you actually own. No way to know if data is stale.

#### Schema

```sql
CREATE TABLE portfolio (
    company_id       INTEGER,
    buy_date         DATE,
    buy_price        DOUBLE,
    quantity         INTEGER,
    sell_date        DATE,
    sell_price       DOUBLE,
    is_open          BOOLEAN,
    PRIMARY KEY (company_id, buy_date)
);

CREATE TABLE data_freshness (
    company_id           INTEGER PRIMARY KEY,
    prices_updated       TIMESTAMP,
    financials_updated   TIMESTAMP,
    shareholding_updated TIMESTAMP,
    is_stale             BOOLEAN  -- TRUE if any source > 7 days old
);
```

#### Implementation

```python
# engines/portfolio_engine.py
import duckdb

con = duckdb.connect("athena.duckdb")


def get_portfolio_pnl():
    return con.execute("""
        SELECT
            c.symbol,
            c.name,
            p.buy_date,
            p.buy_price,
            p.quantity,
            dp.close AS current_price,
            ROUND((dp.close - p.buy_price) / p.buy_price * 100, 2) AS pnl_pct,
            ROUND((dp.close - p.buy_price) * p.quantity, 2)         AS unrealized_pnl,
            (CURRENT_DATE - p.buy_date)                             AS holding_days
        FROM portfolio p
        JOIN companies c USING (company_id)
        JOIN daily_prices dp
            ON dp.company_id = p.company_id
            AND dp.date = (
                SELECT MAX(date) FROM daily_prices WHERE company_id = p.company_id
            )
        WHERE p.is_open = TRUE
        ORDER BY pnl_pct DESC
    """).df()


def update_freshness():
    """Run after every loader — marks companies with stale data."""
    con.execute("""
        INSERT OR REPLACE INTO data_freshness
        SELECT
            c.company_id,
            MAX(dp.date)::TIMESTAMP   AS prices_updated,
            MAX(qf.quarter)::TIMESTAMP AS financials_updated,
            MAX(s.quarter)::TIMESTAMP  AS shareholding_updated,
            MAX(dp.date) < CURRENT_DATE - INTERVAL '7 days' AS is_stale
        FROM companies c
        LEFT JOIN daily_prices        dp  USING (company_id)
        LEFT JOIN quarterly_financials qf USING (company_id)
        LEFT JOIN shareholding         s  USING (company_id)
        WHERE c.active = TRUE
        GROUP BY c.company_id
    """)
```

#### Streamlit Addition

```python
# In app.py — show stale data warning in sidebar
stale = con.execute(
    "SELECT COUNT(*) FROM data_freshness WHERE is_stale = TRUE"
).fetchone()[0]
if stale > 0:
    st.sidebar.warning(f"⚠️ {stale} companies have stale data (>7 days)")
```

**Deliverable:** `SELECT * FROM portfolio` shows real-time P&L per position. Sidebar warns you if any company's data has
gone stale.

---

### Phase 8: Notifications + Pledge Alerting (Weeks 15–16)

**Problems solved:** Alerts exist in DuckDB but never reach you. Pledge increases go unnoticed.

#### Schema Addition

```sql
-- alert_type is VARCHAR — no schema change needed for new types
-- Add notification config table
CREATE TABLE notification_config (
    channel     VARCHAR PRIMARY KEY,  -- 'telegram', 'email'
    enabled     BOOLEAN,
    config_json TEXT  -- {"bot_token": "...", "chat_id": "..."}
);
```

#### Implementation

```python
# notifications/notifier.py
import requests, json, duckdb

con = duckdb.connect("athena.duckdb")


def send_telegram(message: str):
    cfg = json.loads(
        con.execute(
            "SELECT config_json FROM notification_config "
            "WHERE channel='telegram' AND enabled=TRUE"
        ).fetchone()[0]
    )
    url = f"https://api.telegram.org/bot{cfg['bot_token']}/sendMessage"
    requests.post(url, json={
        "chat_id": cfg["chat_id"],
        "text": message,
        "parse_mode": "Markdown"
    })


def dispatch_unsent_alerts():
    alerts = con.execute("""
        SELECT al.alert_id, c.symbol, al.alert_type, al.payload
        FROM alert_log al
        JOIN companies c USING (company_id)
        WHERE al.notification_sent = FALSE
        ORDER BY al.alert_id
    """).fetchall()

    for alert_id, symbol, alert_type, payload in alerts:
        msg = f"*{alert_type}* — {symbol}\n{payload}"
        send_telegram(msg)
        con.execute(
            "UPDATE alert_log SET notification_sent = TRUE WHERE alert_id = ?",
            [alert_id]
        )
```

```python
# engines/pledge_engine.py
def detect_pledge_increases():
    """Fire alert if pledged % rises > 10 percentage points QoQ."""
    con.execute("""
        INSERT INTO alert_log (company_id, alert_type, payload, notification_sent)
        SELECT
            s.company_id,
            'PROMOTER_PLEDGE_INCREASE',
            'Pledged jumped from ' || ROUND(prev.pledged, 1) || '% to '
                || ROUND(s.pledged, 1) || '% QoQ',
            FALSE
        FROM shareholding s
        JOIN shareholding prev
            ON  prev.company_id = s.company_id
            AND prev.quarter = (
                SELECT MAX(quarter) FROM shareholding
                WHERE company_id = s.company_id AND quarter < s.quarter
            )
        WHERE s.quarter = (
                SELECT MAX(quarter) FROM shareholding
                WHERE company_id = s.company_id
              )
          AND (s.pledged - prev.pledged) > 10
          AND NOT EXISTS (
              SELECT 1 FROM alert_log
              WHERE company_id = s.company_id
                AND alert_type = 'PROMOTER_PLEDGE_INCREASE'
          )
    """)
```

**Deliverable:** Every new alert fires a Telegram message within minutes of the daily pipeline run. Pledge spikes
surface automatically.

---

### Phase 9: Earnings Calendar + News Ingestion (Weeks 17–18)

**Problems solved:** No awareness of when companies report. Breaking news is invisible to Athena.

#### Schema

```sql
CREATE TABLE earnings_calendar (
    company_id      INTEGER,
    expected_date   DATE,
    result_type     VARCHAR,   -- 'QUARTERLY', 'ANNUAL'
    actual_date     DATE,
    results_loaded  BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (company_id, expected_date)
);

CREATE TABLE news_feed (
    news_id      INTEGER PRIMARY KEY,
    company_id   INTEGER,
    published_at TIMESTAMP,
    headline     TEXT,
    url          TEXT,
    sentiment    VARCHAR,  -- 'POSITIVE', 'NEUTRAL', 'NEGATIVE'
    severity     INTEGER,  -- 1–5
    processed    BOOLEAN DEFAULT FALSE
);
```

#### Implementation

```python
# loaders/earnings_calendar_loader.py
import requests, duckdb

con = duckdb.connect("athena.duckdb")


def fetch_bse_board_meetings():
    """BSE publishes upcoming board meeting dates (when results will be announced)."""
    url = "https://api.bseindia.com/BseIndiaAPI/api/DefaultData/w?category=BM"
    resp = requests.get(url, headers={"User-Agent": "Mozilla/5.0"}, timeout=10)
    for row in resp.json().get("Table", []):
        symbol = row.get("NSE_SYMBOL")
        bm_date = row.get("MEETING_DATE")
        if not symbol or not bm_date:
            continue
        company = con.execute(
            "SELECT company_id FROM companies WHERE symbol = ?", [symbol]
        ).fetchone()
        if company:
            con.execute("""
                INSERT OR IGNORE INTO earnings_calendar
                    (company_id, expected_date, result_type)
                VALUES (?, ?, 'QUARTERLY')
            """, [company[0], bm_date])
```

```python
# loaders/news_loader.py
import feedparser, json, duckdb
import google.generativeai as genai

con = duckdb.connect("athena.duckdb")
model = genai.GenerativeModel("gemini-2.0-flash-lite")


def fetch_news_for_company(symbol: str, company_id: int):
    feed = feedparser.parse(
        f"https://news.google.com/rss/search"
        f"?q={symbol}+NSE+India&hl=en-IN&gl=IN&ceid=IN:en"
    )
    for entry in feed.entries[:5]:
        headline = entry.title
        resp = model.generate_content(
            f"Classify this financial news headline for a stock investor. "
            f"Return JSON only: {{\"sentiment\": \"POSITIVE|NEUTRAL|NEGATIVE\", \"severity\": 1-5}}\n"
            f"Headline: {headline}"
        )
        try:
            data = json.loads(resp.text)
        except Exception:
            data = {"sentiment": "NEUTRAL", "severity": 1}

        con.execute("""
            INSERT OR IGNORE INTO news_feed
                (company_id, published_at, headline, url, sentiment, severity)
            VALUES (?, ?, ?, ?, ?, ?)
        """, [company_id, entry.published, headline, entry.link,
              data["sentiment"], data["severity"]])

        # Fire alert for high severity negative news
        if data["sentiment"] == "NEGATIVE" and data["severity"] >= 4:
            con.execute("""
                INSERT INTO alert_log (company_id, alert_type, payload, notification_sent)
                VALUES (?, 'NEGATIVE_NEWS', ?, FALSE)
            """, [company_id, headline])
```

**Deliverable:** Athena knows when every company in your universe is reporting. Severity-4+ negative news fires an
immediate Telegram alert.

---

### Phase 10: Peer & Sector Relative Ranking (Weeks 19–20)

**Problems solved:** All scoring is absolute — a score of 7/10 means nothing without sector context.

#### Schema

```sql
CREATE TABLE sector_benchmarks (
    sector              VARCHAR,
    quarter             VARCHAR,
    avg_revenue_growth  DOUBLE,
    avg_pat_growth      DOUBLE,
    avg_roce            DOUBLE,
    avg_pe              DOUBLE,
    avg_business_score  DOUBLE,
    PRIMARY KEY (sector, quarter)
);

CREATE TABLE peer_rankings (
    company_id        INTEGER,
    quarter           VARCHAR,
    sector_rank       INTEGER,  -- rank within sector by business_score
    sector_total      INTEGER,  -- total companies in that sector
    sector_percentile DOUBLE,   -- 100 = best in sector
    PRIMARY KEY (company_id, quarter)
);
```

#### Implementation

```python
# engines/peer_ranking_engine.py
def run_peer_ranking_engine():
    # Step 1: sector benchmarks
    con.execute("""
        INSERT OR REPLACE INTO sector_benchmarks
        SELECT
            c.sector,
            qm.quarter,
            AVG(qm.revenue_growth) AS avg_revenue_growth,
            AVG(qm.pat_growth)     AS avg_pat_growth,
            AVG(qf.roce)           AS avg_roce,
            AVG(vh.pe)             AS avg_pe,
            AVG(fs.business_score) AS avg_business_score
        FROM quarterly_metrics qm
        JOIN companies           c  USING (company_id)
        JOIN quarterly_financials qf USING (company_id, quarter)
        LEFT JOIN valuation_history vh
            ON  vh.company_id = qm.company_id
            AND vh.date = (SELECT MAX(date) FROM valuation_history
                           WHERE company_id = qm.company_id)
        LEFT JOIN financial_scores fs USING (company_id, quarter)
        GROUP BY c.sector, qm.quarter
    """)

    # Step 2: per-company rank within sector
    con.execute("""
        INSERT OR REPLACE INTO peer_rankings
        SELECT
            fs.company_id,
            fs.quarter,
            RANK() OVER w                              AS sector_rank,
            COUNT(*) OVER w                            AS sector_total,
            ROUND(
                (1.0 - RANK() OVER w
                       / COUNT(*) OVER w::DOUBLE) * 100
            , 1)                                       AS sector_percentile
        FROM financial_scores fs
        JOIN companies c USING (company_id)
        WINDOW w AS (PARTITION BY c.sector, fs.quarter
                     ORDER BY fs.business_score DESC)
    """)
```

#### Streamlit Addition — Company Card

```python
# Add to build_card() in app.py
peer = con.execute("""
    SELECT sector_rank, sector_total
    FROM peer_rankings
    WHERE company_id = ? ORDER BY quarter DESC LIMIT 1
""", [row["company_id"]]).fetchone()

if peer:
    rank_pill = f'#{peer[0]} of {peer[1]} in sector'
```

**Deliverable:** Every company card shows "#2 of 18 in Chemicals" — you instantly know if it is a sector leader or
laggard.

---

### Phase 11: Signal Backtesting (Weeks 21–22)

**Problems solved:** No way to validate whether any alert type has actual historical edge.

#### Schema

```sql
CREATE TABLE signal_backtest (
    company_id      INTEGER,
    signal_date     DATE,
    signal_type     VARCHAR,
    price_at_signal DOUBLE,
    price_3m_later  DOUBLE,
    price_1yr_later DOUBLE,
    return_3m       DOUBLE,
    return_1yr      DOUBLE,
    PRIMARY KEY (company_id, signal_date, signal_type)
);
```

#### Implementation

```python
# engines/backtest_engine.py
from datetime import timedelta


def get_price_on_date(company_id: int, date):
    row = con.execute("""
        SELECT close FROM daily_prices
        WHERE company_id = ? AND date <= ?
        ORDER BY date DESC LIMIT 1
    """, [company_id, date]).fetchone()
    return row[0] if row else None


def run_backtest():
    """For every historical alert, look up price 3M and 1Y later."""
    alerts = con.execute("""
        SELECT DISTINCT company_id, alert_type,
               DATE_TRUNC('day', MIN(rowid::VARCHAR)::DATE) AS signal_date
        FROM alert_log
        WHERE notification_sent = TRUE
        GROUP BY company_id, alert_type
    """).fetchall()

    for company_id, signal_type, signal_date in alerts:
        p0 = get_price_on_date(company_id, signal_date)
        p3m = get_price_on_date(company_id, signal_date + timedelta(days=90))
        p1y = get_price_on_date(company_id, signal_date + timedelta(days=365))

        if not p0:
            continue

        con.execute("""
            INSERT OR IGNORE INTO signal_backtest
                (company_id, signal_date, signal_type, price_at_signal,
                 price_3m_later, price_1yr_later, return_3m, return_1yr)
            VALUES (?, ?, ?, ?, ?, ?,
                ROUND((? - ?) / ? * 100, 2),
                ROUND((? - ?) / ? * 100, 2))
        """, [company_id, signal_date, signal_type, p0,
              p3m, p1y,
              p3m, p0, p0,
              p1y, p0, p0])


def backtest_summary():
    """Returns a dataframe showing win-rate and average return per signal type."""
    return con.execute("""
        SELECT
            signal_type,
            COUNT(*)                       AS total_signals,
            ROUND(AVG(return_3m),  1)      AS avg_return_3m_pct,
            ROUND(AVG(return_1yr), 1)      AS avg_return_1yr_pct,
            ROUND(
                SUM(CASE WHEN return_1yr > 0 THEN 1 ELSE 0 END)
                * 100.0 / COUNT(*), 1
            )                              AS win_rate_1yr_pct
        FROM signal_backtest
        WHERE price_1yr_later IS NOT NULL
        GROUP BY signal_type
        ORDER BY avg_return_1yr_pct DESC
    """).df()
```

**Deliverable:** One query shows which alert types have historically delivered the best 1-year returns and win rates —
so you know which ones to act on immediately vs ignore.

---

### Phase 12: Macro Context Layer + Mobile Digest (Weeks 23–24)

**Problems solved:** No macro overlay on signals. No mobile-readable summary for Saturday morning review.

#### Schema

```sql
CREATE TABLE macro_indicators (
    indicator_date      DATE PRIMARY KEY,
    repo_rate           DOUBLE,   -- RBI repo rate %
    cpi_inflation       DOUBLE,   -- CPI YoY %
    iip_growth          DOUBLE,   -- IIP YoY %
    nifty_pe            DOUBLE,   -- Nifty 50 trailing PE
    nifty_pe_percentile DOUBLE,   -- vs 10-year history (0-100)
    usd_inr             DOUBLE    -- USD/INR exchange rate
);

CREATE TABLE sector_macro_sensitivity (
    sector           VARCHAR PRIMARY KEY,
    rate_sensitive   BOOLEAN,   -- TRUE for NBFC, Real Estate, etc.
    export_sensitive BOOLEAN,   -- TRUE for IT, Pharma, Textiles
    commodity_linked BOOLEAN,   -- TRUE for Metals, Chemicals, Oil & Gas
    manual_note      TEXT
);
```

#### Implementation

```python
# loaders/macro_loader.py
import requests, duckdb

con = duckdb.connect("athena.duckdb")


def fetch_macro_indicators():
    """Fetch Nifty PE from NSE India API."""
    try:
        resp = requests.get(
            "https://www.nseindia.com/api/allIndices",
            headers={"User-Agent": "Mozilla/5.0",
                     "Referer": "https://www.nseindia.com"},
            timeout=10
        )
        nifty = next(
            (x for x in resp.json()["data"] if x["index"] == "NIFTY 50"), {}
        )
        pe = float(nifty.get("pe", 0)) or None
    except Exception:
        pe = None

    con.execute("""
        INSERT OR IGNORE INTO macro_indicators (indicator_date, nifty_pe)
        VALUES (CURRENT_DATE, ?)
    """, [pe])

    # Compute PE percentile vs trailing 10 years
    con.execute("""
        UPDATE macro_indicators
        SET nifty_pe_percentile = (
            SELECT ROUND(
                PERCENT_RANK() OVER (ORDER BY nifty_pe) * 100, 1
            )
            FROM macro_indicators m2
            WHERE m2.indicator_date = macro_indicators.indicator_date
        )
        WHERE indicator_date = CURRENT_DATE
    """)
    print(f"Macro loaded: Nifty PE = {pe}")
```

```python
# notifications/weekly_digest.py
from notifications.notifier import send_telegram
import duckdb

con = duckdb.connect("athena.duckdb")


def send_weekly_digest():
    """Fires every Saturday at 8 AM — readable in one Telegram screen."""

    # Top 5 ideas
    top5 = con.execute("""
        SELECT c.symbol, c.name, ROUND(ch.total_score, 1) AS score
        FROM conviction_history ch
        JOIN companies c USING (company_id)
        WHERE ch.date = (SELECT MAX(date) FROM conviction_history)
        ORDER BY ch.total_score DESC
        LIMIT 5
    """).fetchall()

    # Unread alerts
    alerts = con.execute("""
        SELECT c.symbol, al.alert_type
        FROM alert_log al
        JOIN companies c USING (company_id)
        WHERE al.notification_sent = FALSE
        LIMIT 5
    """).fetchall()

    # Macro snapshot
    macro = con.execute("""
        SELECT nifty_pe, nifty_pe_percentile
        FROM macro_indicators
        ORDER BY indicator_date DESC LIMIT 1
    """).fetchone()

    lines = ["*🦉 Athena — Saturday Digest*", ""]

    if macro and macro[0]:
        pe, pct = macro
        mood = "🟢 Cheap" if (pct or 50) < 40 else (
            "🔴 Expensive" if (pct or 50) > 70 else "🟡 Fair")
        lines.append(f"*Market:* Nifty PE {pe:.1f}x — {mood} ({pct:.0f}th %ile)")
        lines.append("")

    lines.append("*Top 5 Ideas:*")
    for sym, name, score in top5:
        lines.append(f"  • {sym} — Score {score}")

    if alerts:
        lines.append("")
        lines.append("*New Alerts:*")
        for sym, atype in alerts:
            lines.append(f"  ⚡ {sym}: {atype.replace('_', ' ')}")

    send_telegram("\n".join(lines))
```

#### Prefect: Add Saturday Weekly Flow

```python
# Add to flows.py
from prefect import flow, task
from prefect.schedules import CronSchedule


@task
def task_fetch_macro():       fetch_macro_indicators()


@task
def task_pledge_check():      detect_pledge_increases()


@task
def task_dispatch_alerts():   dispatch_unsent_alerts()


@task
def task_peer_ranking():      run_peer_ranking_engine()


@task
def task_update_freshness():  update_freshness()


@task
def task_weekly_digest():     send_weekly_digest()


@flow(schedule=CronSchedule(cron="0 8 * * 6"))  # 8 AM every Saturday
def weekly_athena_flow():
    task_fetch_macro()
    task_pledge_check()
    task_peer_ranking()
    task_update_freshness()
    task_dispatch_alerts()
    task_weekly_digest()
```

**Deliverable:** Every Saturday at 8 AM a mobile-readable Telegram digest lands with Top 5 ideas, new alerts, and Nifty
PE context — zero manual effort.

---

### Extended Roadmap Summary

| Phase | Weeks | What It Adds                                     | Priority        |
|-------|-------|--------------------------------------------------|-----------------|
| 7     | 13–14 | Portfolio P&L tracking + Data freshness warnings | 🔴 High         |
| 8     | 15–16 | Telegram notifications + Pledge spike alerting   | 🔴 High         |
| 9     | 17–18 | Earnings calendar + News ingestion + sentiment   | 🟡 Medium       |
| 10    | 19–20 | Peer & sector relative ranking on every score    | 🟡 Medium       |
| 11    | 21–22 | Signal backtesting — which alerts have real edge | 🟢 Long-term    |
| 12    | 23–24 | Macro context layer + Saturday mobile digest     | 🟢 Nice-to-have |

[↑ Back to TOC](#table-of-contents)

---

## 11. Use Case Coverage, Gaps, and Recommended Improvements

> This section is the fastest way to answer three practical questions:
>
> 1. **What is Athena already designed to solve well?**
> 2. **Which use cases are still weak or missing?**
> 3. **What should be improved next for maximum investing edge?**

---

### A. Use Cases Athena Already Solves Well

| Use Case | Current Coverage | Where It Lives | Why It Matters |
|----------|------------------|----------------|----------------|
| Daily portfolio monitoring | ✅ Strong | Layers 1, 3, 6 | Tracks what changed in price, ownership, alerts, and documents without manual effort |
| Weekly research review | ✅ Strong | `research_feed`, `alert_log`, Top 20 UI | Gives a prioritized weekend review instead of scattered notes and dashboards |
| Quarterly thesis validation | ✅ Strong | `investment_thesis`, `thesis_validation`, `change_analysis` | Forces every holding to be re-checked against the original reason for owning it |
| Long-term company memory | ✅ Strong | `business_profile`, `research_notes`, `annual_report_insights` | Prevents forgetting key facts, risks, and management promises over time |
| Valuation tracking | ✅ Strong | `valuation_history` | Surfaces when a high-quality business becomes cheap versus its own history |
| Financial quality tracking | ✅ Strong | `quarterly_financials`, `quarterly_metrics`, `financial_scores` | Captures growth, margins, cash flow, debt, and operating quality over time |
| Ownership quality tracking | ✅ Strong | `shareholding`, `ownership_scores` | Detects promoter buying, institutional participation, and pledge-related risk |
| Multibagger hunting | ✅ Strong | `operating_leverage`, `capital_allocation`, `cycle_positioning`, `multibagger_screen` | Goes beyond screening for quality and tries to catch early compounding signals |
| Price vs business disconnects | ✅ Strong | `alert_log`, divergence alerts | Finds the best setup: improving business, sleepy stock price |
| Management commentary extraction | ✅ Strong | `ai_extractions`, `concall_tone_analysis` | Converts transcripts into structured insight instead of one-time reading effort |
| Annual report intelligence | ✅ Strong | `annual_report_insights` | Tracks strategy, consistency, and red flags in long-form management communication |
| New idea ranking | ✅ Strong | Top 20 home screen, composite scoring | Gives a single ranked list of best current opportunities |
| Portfolio P&L visibility | 🟡 Planned | `portfolio`, `portfolio_engine.py` | Connects research with actual capital deployed |
| News and earnings awareness | 🟡 Planned | `earnings_calendar`, `news_feed` | Adds event awareness so you know what needs attention before and after results |
| Mobile digest / notifications | 🟡 Planned | Telegram notifier, weekly digest | Ensures important changes reach you without opening the full app |

---

### B. Important Use Cases That Are Missing or Underdeveloped

| Missing / Weak Use Case | Current State | Why It Matters | Recommended Fix | Priority |
|-------------------------|---------------|----------------|-----------------|----------|
| Portfolio construction | ❌ Missing | Athena finds ideas but does not help decide position size, concentration, or capital allocation | Add target weights, max position size, sector caps, and cash allocation rules | 🔴 High |
| Sell discipline | ❌ Missing | Finding good buys is only half the system; exits and trims drive long-term returns too | Add sell triggers: overvaluation, thesis achieved, governance break, better alternative | 🔴 High |
| Risk management framework | ❌ Weak | Current design is stronger on upside than downside protection | Add governance risk, accounting risk, liquidity risk, customer concentration, forex risk, and balance-sheet stress checks | 🔴 High |
| Forensic accounting / fraud checks | ❌ Missing | Many value traps look fine on growth and ROCE until accounting quality is examined | Add accrual checks, receivables stretch, CFO/PAT mismatch, auditor changes, and promoter red flags | 🔴 High |
| Decision journaling | ❌ Partial | Thesis is stored, but buy/add/reduce/sell decisions are not fully journaled | Add transaction notes, decision snapshots, and post-review templates | 🟡 Medium |
| Scenario valuation | ❌ Missing | Historical valuation is useful, but investing decisions require forward payoff ranges | Add base / bull / bear assumptions with expected IRR and valuation sensitivity | 🟡 Medium |
| Industry / peer intelligence | 🟡 Partial | Current peer ranking is useful, but not enough to understand industry structure or market share shifts | Add peer commentary comparison, sector dashboards, and leader/laggard tables | 🟡 Medium |
| Benchmark attribution | ❌ Missing | You need to know whether the process is beating the index or just riding the market | Add portfolio vs benchmark returns, alpha attribution, and signal attribution | 🟡 Medium |
| Leading indicators / alternative data | ❌ Missing | The earliest signs of inflection often appear before reported financials | Add order inflow, import/export trends, hiring activity, capacity announcements, and insider trades | 🟢 Later |
| Exportable research briefs | ❌ Missing | Personal systems still benefit from one-click company summaries and review packets | Add company memo export as Markdown / PDF / HTML | 🟢 Later |

---

### C. Design Improvements Needed in the Current Spec

> These are not new product ideas. These are quality improvements to make the existing architecture more reliable,
> easier to build, and safer to trust.

| Improvement Area | Issue in Current Spec | Recommended Change | Priority |
|------------------|-----------------------|--------------------|----------|
| Quarter storage | Many tables use `quarter VARCHAR`, which is fragile for sorting, joins, and lag calculations | Use `period_end_date DATE` plus optional `fiscal_year` and `fiscal_quarter` columns | 🔴 High |
| Corporate actions schema | `PRIMARY KEY (company_id, action_type)` allows only one dividend / split / bonus per company | Add `action_date` and use a time-aware primary key such as `(company_id, action_type, action_date)` | 🔴 High |
| Formula correctness | Example: `debt_to_equity` is currently shown as `debt / pat`, which is not debt-to-equity | Add actual equity data and compute ratios using proper balance sheet fields | 🔴 High |
| Market cap consistency | Spec uses both `market_cap` and `market_cap_cr` in different places | Standardize on one field and one unit across all loaders, engines, and UI | 🔴 High |
| Alert lifecycle | `alert_log` is too simple for deduplication, severity, and resolution tracking | Add `created_at`, `status`, `severity`, `dedupe_key`, `resolved_at`, and `first_seen_at` | 🟡 Medium |
| AI auditability | AI-derived data has no explicit prompt version, confidence score, or manual review status | Add metadata columns: `model_name`, `prompt_version`, `confidence`, `validated`, `validated_by`, `validated_at` | 🔴 High |
| Snapshot views | The UI relies on repeated “latest row” queries, which will get messy as tables grow | Add convenience views like `latest_financials`, `latest_metrics`, `latest_alerts`, and `company_snapshot` | 🟡 Medium |
| Data reconciliation | Multiple sources exist for similar fields, but overwrite rules are not clearly defined | Define source precedence rules, freshness rules, and field-level confidence hierarchy | 🟡 Medium |
| Failure recovery | Pipeline design exists, but not enough is defined for partial failures or retries | Add run logs, loader status tables, retry strategy, and failed-record quarantine tables | 🟡 Medium |
| Secrets / configuration | API key handling is mentioned casually but not formalized | Add `.env`-based config pattern and central configuration loading | 🟢 Later |

---

### D. Highest-Impact Next Enhancements

If the goal is to maximize practical value after the current roadmap, build these next in order:

| Rank | Enhancement | Why It Should Come Next | Priority |
|------|-------------|-------------------------|----------|
| 1 | Risk Framework | Athena is already very good at finding opportunities; the next edge is avoiding permanent capital loss | 🔴 High |
| 2 | Sell Discipline Engine | A complete investing system must know when to exit, trim, or rotate capital | 🔴 High |
| 3 | Portfolio Construction Layer | Ranked ideas become truly useful only when connected to sizing and allocation | 🔴 High |
| 4 | AI Confidence + Validation Metadata | The more AI influences scoring and alerts, the more auditability matters | 🔴 High |
| 5 | Scenario Valuation Model | Helps convert “good business” into “good expected return from this price” | 🟡 Medium |
| 6 | Sector / Peer Intelligence Dashboard | Makes scoring more context-aware and improves industry-level understanding | 🟡 Medium |
| 7 | Benchmark Attribution | Lets you measure whether Athena is actually producing excess return over time | 🟡 Medium |
| 8 | Alternative Data Layer | Improves early signal detection once the core engine is stable | 🟢 Later |

---

### E. Bottom-Line Assessment

Athena is already designed to solve the **hardest and most valuable core investing problems**:

- building a curated universe,
- tracking business quality and valuation,
- validating thesis over time,
- extracting insight from management communication,
- surfacing asymmetric opportunities before they become obvious.

What it still needs is the other half of a complete investing operating system:

- **risk management,**
- **sell discipline,**
- **portfolio construction,** and
- **higher trust in AI-derived signals through validation and auditability.**

> In short: the current Athena spec is already a very strong **research and idea-generation machine**. The next step is to
> make it a stronger **capital-allocation and risk-control machine**.

[↑ Back to TOC](#table-of-contents)

