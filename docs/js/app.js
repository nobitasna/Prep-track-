/**
 * PREP TRACK — Official Website Scripts
 * Plan • Focus • Track • Achieve
 */

document.addEventListener('DOMContentLoaded', () => {
  initNavbar();
  initMobileNav();
  initShowcaseTabs();
  initFaqAccordion();
});

/* ==========================================================================
   1. Navbar Scroll Blur & Shadow
   ========================================================================== */
function initNavbar() {
  const nav = document.querySelector('.site-nav');
  if (!nav) return;

  window.addEventListener('scroll', () => {
    if (window.scrollY > 40) {
      nav.classList.add('scrolled');
    } else {
      nav.classList.remove('scrolled');
    }
  }, { passive: true });
}

/* ==========================================================================
   2. Mobile Drawer Navigation
   ========================================================================== */
function initMobileNav() {
  const toggleBtn = document.getElementById('mobileNavToggle');
  const drawer = document.getElementById('mobileNavDrawer');
  if (!toggleBtn || !drawer) return;

  toggleBtn.addEventListener('click', () => {
    const isOpen = drawer.classList.contains('open');
    if (isOpen) {
      drawer.classList.remove('open');
      toggleBtn.setAttribute('aria-expanded', 'false');
      toggleBtn.innerHTML = '☰';
    } else {
      drawer.classList.add('open');
      toggleBtn.setAttribute('aria-expanded', 'true');
      toggleBtn.innerHTML = '✕';
    }
  });

  // Close drawer when any mobile nav link is clicked
  const mobileLinks = drawer.querySelectorAll('a');
  mobileLinks.forEach(link => {
    link.addEventListener('click', () => {
      drawer.classList.remove('open');
      toggleBtn.setAttribute('aria-expanded', 'false');
      toggleBtn.innerHTML = '☰';
    });
  });
}

/* ==========================================================================
   3. App Showcase Tab Switcher
   ========================================================================== */
const showcaseData = {
  dashboard: {
    title: 'Mission Dashboard',
    desc: 'An intelligent control hub displaying your target exam countdown, active streak, daily workload breakdown, and quick access to study modules.',
    checklist: [
      'Target Exam Countdown & Year Tracker',
      'Daily 2-Hour Study Timetable preview',
      'Continuous daily streak tracking',
      'Quick-action access to Focus Timer & Syllabus'
    ],
    mockup: `
      <div style="padding: 12px; background: #131C31; border-radius: 16px; margin-bottom: 12px; border: 1px solid #223254;">
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <span style="font-size:0.75rem; color:#38BDF8; font-weight:700;">CURRENT TARGET</span>
          <span style="font-size:0.7rem; background:rgba(251,191,36,0.15); color:#FBBF24; padding:2px 8px; border-radius:6px; font-weight:800;">NEET 2026</span>
        </div>
        <div style="font-size:1.15rem; font-weight:800; color:#fff; margin-top:4px;">184 Days Remaining</div>
        <div style="font-size:0.8rem; color:#94A3B8;">Target Goal: 680+ Score Target</div>
      </div>
      <div style="display:grid; grid-template-columns:1fr 1fr; gap:8px; margin-bottom:12px;">
        <div style="background:#16223B; padding:10px; border-radius:12px; border:1px solid #223254;">
          <div style="font-size:0.7rem; color:#94A3B8;">TODAY'S WORKLOAD</div>
          <div style="font-size:1.1rem; font-weight:800; color:#38BDF8;">2h 00m</div>
          <div style="font-size:0.7rem; color:#10B981;">Fixed Reference</div>
        </div>
        <div style="background:#16223B; padding:10px; border-radius:12px; border:1px solid #223254;">
          <div style="font-size:0.7rem; color:#94A3B8;">ACTIVE STREAK</div>
          <div style="font-size:1.1rem; font-weight:800; color:#F97316;">14 Days 🔥</div>
          <div style="font-size:0.7rem; color:#94A3B8;">Consistent</div>
        </div>
      </div>
      <div style="background:#16223B; padding:12px; border-radius:12px; border:1px solid #223254;">
        <div style="font-size:0.75rem; font-weight:700; color:#CBD5E1; margin-bottom:6px;">NEXT UP TODAY</div>
        <div style="font-size:0.85rem; font-weight:700; color:#fff;">Physics • Electrostatics</div>
        <div style="font-size:0.75rem; color:#38BDF8;">Lecture 04 • 45m Watch Time (at 1.25x)</div>
      </div>
    `
  },
  plan: {
    title: 'Study Planner Engine',
    desc: 'Automates realistic daily milestones. Configured around a fixed 2-hour daily reference workload with personalized playback speed, notes, and revision ratios.',
    checklist: [
      'Fixed 2-hour daily reference pace',
      'Lecture playback speed multiplier (1.0x - 2.0x)',
      'Configurable notes & practice time allocation',
      'Weekly timetable with planned rest days'
    ],
    mockup: `
      <div style="padding: 12px; background: #131C31; border-radius: 16px; margin-bottom: 12px; border: 1px solid #223254;">
        <div style="font-size:0.75rem; color:#38BDF8; font-weight:700;">DAILY STUDY PACING</div>
        <div style="font-size:1.1rem; font-weight:800; color:#fff; margin-top:2px;">Fixed 2h Workload</div>
        <div style="font-size:0.75rem; color:#94A3B8;">Estimated actual time: ~2h 20m</div>
      </div>
      <div style="background:#16223B; padding:10px; border-radius:12px; margin-bottom:8px; border:1px solid #223254;">
        <div style="display:flex; justify-content:space-between; font-size:0.8rem; font-weight:700; color:#fff;">
          <span>Video Lecture</span>
          <span style="color:#38BDF8;">60 min</span>
        </div>
        <div style="font-size:0.7rem; color:#94A3B8; margin-top:2px;">Watch at 1.25x speed (~48 mins)</div>
      </div>
      <div style="background:#16223B; padding:10px; border-radius:12px; margin-bottom:8px; border:1px solid #223254;">
        <div style="display:flex; justify-content:space-between; font-size:0.8rem; font-weight:700; color:#fff;">
          <span>Self Notes & Key Formulas</span>
          <span style="color:#FBBF24;">30 min</span>
        </div>
        <div style="font-size:0.7rem; color:#94A3B8; margin-top:2px;">Structured summary creation</div>
      </div>
      <div style="background:#16223B; padding:10px; border-radius:12px; border:1px solid #223254;">
        <div style="display:flex; justify-content:space-between; font-size:0.8rem; font-weight:700; color:#fff;">
          <span>Problem Practice / PYQs</span>
          <span style="color:#10B981;">30 min</span>
        </div>
        <div style="font-size:0.7rem; color:#94A3B8; margin-top:2px;">Target: 15-20 Questions</div>
      </div>
    `
  },
  syllabus: {
    title: 'Syllabus Manager',
    desc: 'Full hierarchical tracking of subjects, chapters, and topics. Pre-loaded with official syllabi for NEET, JEE, and CBSE, or completely customizable.',
    checklist: [
      'Comprehensive subject & chapter catalogs',
      'Lecture count & completion checkboxes',
      'Chapter-wise notes attachment & revisions',
      'Color-coded completion progress bars'
    ],
    mockup: `
      <div style="padding: 10px 14px; background: #131C31; border-radius: 14px; margin-bottom: 10px; border: 1px solid #223254; display:flex; justify-content:space-between; align-items:center;">
        <div>
          <div style="font-size:0.85rem; font-weight:800; color:#fff;">PHYSICS</div>
          <div style="font-size:0.75rem; color:#38BDF8;">18 of 28 Chapters Done</div>
        </div>
        <div style="font-size:0.9rem; font-weight:800; color:#10B981;">64%</div>
      </div>
      <div style="padding: 10px 14px; background: #131C31; border-radius: 14px; margin-bottom: 10px; border: 1px solid #223254; display:flex; justify-content:space-between; align-items:center;">
        <div>
          <div style="font-size:0.85rem; font-weight:800; color:#fff;">CHEMISTRY</div>
          <div style="font-size:0.75rem; color:#38BDF8;">22 of 30 Chapters Done</div>
        </div>
        <div style="font-size:0.9rem; font-weight:800; color:#10B981;">73%</div>
      </div>
      <div style="padding: 10px 14px; background: #131C31; border-radius: 14px; border: 1px solid #223254; display:flex; justify-content:space-between; align-items:center;">
        <div>
          <div style="font-size:0.85rem; font-weight:800; color:#fff;">BIOLOGY / MATHS</div>
          <div style="font-size:0.75rem; color:#38BDF8;">28 of 38 Chapters Done</div>
        </div>
        <div style="font-size:0.9rem; font-weight:800; color:#10B981;">74%</div>
      </div>
    `
  },
  focus: {
    title: 'Focus Mode & Timer',
    desc: 'An independent Pomodoro-style timer to track real deep work without distractions. Features full-screen mode, distraction shield, and quick-preset durations.',
    checklist: [
      '25m, 45m, 60m, and custom presets',
      'Distraction Shield mode',
      'Automatic session logging to analytics',
      'Independent timer — not tied to video playback'
    ],
    mockup: `
      <div style="text-align:center; padding: 20px 10px; background: #131C31; border-radius: 20px; border: 1px solid #223254;">
        <div style="width: 130px; height: 130px; margin: 0 auto; border-radius: 50%; border: 6px solid #0A84FF; border-top-color: #38BDF8; display:flex; flex-direction:column; align-items:center; justify-content:center;">
          <div style="font-size: 1.8rem; font-weight: 900; color: #fff; font-family: monospace;">23:45</div>
          <div style="font-size: 0.65rem; color: #38BDF8; font-weight: 700;">FOCUS SESSION</div>
        </div>
        <div style="font-size: 0.85rem; font-weight: 700; color: #fff; margin-top: 14px;">Organic Chemistry • Aldehydes</div>
        <div style="display:flex; justify-content:center; gap:8px; margin-top:12px;">
          <span style="font-size:0.7rem; background:#1E293B; padding:3px 8px; border-radius:6px; color:#CBD5E1;">25m</span>
          <span style="font-size:0.7rem; background:#0A84FF; padding:3px 8px; border-radius:6px; color:#fff; font-weight:700;">45m</span>
          <span style="font-size:0.7rem; background:#1E293B; padding:3px 8px; border-radius:6px; color:#CBD5E1;">60m</span>
        </div>
      </div>
    `
  },
  analytics: {
    title: 'Learning Analytics',
    desc: 'Visualizes your weekly study distribution, completed focus hours, reference pacing, and syllabus velocity to keep you honest and accountable.',
    checklist: [
      'Weekly reference hours completed',
      'Subject distribution breakdown',
      'Daily consistency & streak records',
      'Estimated syllabus completion projected date'
    ],
    mockup: `
      <div style="padding: 12px; background: #131C31; border-radius: 16px; margin-bottom: 12px; border: 1px solid #223254;">
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <span style="font-size:0.75rem; color:#38BDF8; font-weight:700;">THIS WEEK'S STUDY</span>
          <span style="font-size:0.8rem; font-weight:800; color:#10B981;">14.5 Hours</span>
        </div>
        <div style="display:flex; align-items:flex-end; gap:6px; height:60px; margin-top:12px; padding:0 4px;">
          <div style="flex:1; background:#0A84FF; height:70%; border-radius:4px 4px 0 0;"></div>
          <div style="flex:1; background:#0A84FF; height:90%; border-radius:4px 4px 0 0;"></div>
          <div style="flex:1; background:#0A84FF; height:60%; border-radius:4px 4px 0 0;"></div>
          <div style="flex:1; background:#0A84FF; height:100%; border-radius:4px 4px 0 0;"></div>
          <div style="flex:1; background:#0A84FF; height:85%; border-radius:4px 4px 0 0;"></div>
          <div style="flex:1; background:#38BDF8; height:45%; border-radius:4px 4px 0 0;"></div>
          <div style="flex:1; background:#334155; height:15%; border-radius:4px 4px 0 0;"></div>
        </div>
        <div style="display:flex; justify-content:space-between; font-size:0.65rem; color:#64748B; margin-top:4px;">
          <span>M</span><span>T</span><span>W</span><span>T</span><span>F</span><span>S</span><span>S</span>
        </div>
      </div>
      <div style="background:#16223B; padding:10px 14px; border-radius:12px; border:1px solid #223254; display:flex; justify-content:space-between;">
        <span style="font-size:0.75rem; color:#94A3B8;">Projected Completion:</span>
        <span style="font-size:0.75rem; font-weight:800; color:#FBBF24;">15 Dec 2026 (On Track)</span>
      </div>
    `
  }
};

function initShowcaseTabs() {
  const tabs = document.querySelectorAll('.showcase-tab');
  const titleEl = document.getElementById('showcaseTitle');
  const descEl = document.getElementById('showcaseDesc');
  const listEl = document.getElementById('showcaseList');
  const mockupEl = document.getElementById('showcaseMockupView');

  if (!tabs.length || !titleEl || !descEl || !listEl || !mockupEl) return;

  tabs.forEach(tab => {
    tab.addEventListener('click', () => {
      tabs.forEach(t => t.classList.remove('active'));
      tab.classList.add('active');

      const key = tab.getAttribute('data-tab');
      const data = showcaseData[key];
      if (!data) return;

      titleEl.textContent = data.title;
      descEl.textContent = data.desc;
      
      listEl.innerHTML = data.checklist
        .map(item => `
          <li>
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
              <path d="M20 6L9 17l-5-5"/>
            </svg>
            <span>${item}</span>
          </li>
        `)
        .join('');

      mockupEl.innerHTML = data.mockup;
    });
  });
}

/* ==========================================================================
   4. FAQ Accordion
   ========================================================================== */
function initFaqAccordion() {
  const faqItems = document.querySelectorAll('.faq-item');
  if (!faqItems.length) return;

  faqItems.forEach(item => {
    const questionBtn = item.querySelector('.faq-question');
    if (!questionBtn) return;

    questionBtn.addEventListener('click', () => {
      const isActive = item.classList.contains('active');
      
      // Close all other items
      faqItems.forEach(otherItem => {
        if (otherItem !== item) {
          otherItem.classList.remove('active');
          const btn = otherItem.querySelector('.faq-question');
          if (btn) btn.setAttribute('aria-expanded', 'false');
        }
      });

      // Toggle current
      if (isActive) {
        item.classList.remove('active');
        questionBtn.setAttribute('aria-expanded', 'false');
      } else {
        item.classList.add('active');
        questionBtn.setAttribute('aria-expanded', 'true');
      }
    });
  });
}
