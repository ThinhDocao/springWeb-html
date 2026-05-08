document.addEventListener('DOMContentLoaded', function() {
  // === SCROLL ANIMATIONS ===
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) { entry.target.classList.add('visible'); }
    });
  }, { threshold: 0.1, rootMargin: '0px 0px -50px 0px' });
  document.querySelectorAll('.fade-up').forEach(el => observer.observe(el));

  // === HEADER SCROLL ===
  const header = document.querySelector('.site-header');
  if (header) {
    window.addEventListener('scroll', () => {
      header.classList.toggle('scrolled', window.scrollY > 50);
    });
  }

  // === MOBILE MENU ===
  const toggle = document.querySelector('.mobile-toggle');
  const nav = document.querySelector('.main-nav');
  if (toggle && nav) {
    toggle.addEventListener('click', () => nav.classList.toggle('open'));
    document.addEventListener('click', (e) => {
      if (!nav.contains(e.target) && !toggle.contains(e.target)) nav.classList.remove('open');
    });
  }

  // === PRODUCT TABS (Homepage) ===
  const tabs = document.querySelectorAll('.product-tab');
  const grids = document.querySelectorAll('.tab-content');
  tabs.forEach(tab => {
    tab.addEventListener('click', () => {
      tabs.forEach(t => t.classList.remove('active'));
      grids.forEach(g => g.style.display = 'none');
      tab.classList.add('active');
      const target = document.getElementById(tab.dataset.tab);
      if (target) target.style.display = 'grid';
    });
  });

  // === DETAIL PAGE TABS ===
  const dtBtns = document.querySelectorAll('.detail-tab-btn');
  const dtContents = document.querySelectorAll('.detail-tab-content');
  dtBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      dtBtns.forEach(b => b.classList.remove('active'));
      dtContents.forEach(c => c.classList.remove('active'));
      btn.classList.add('active');
      const t = document.getElementById(btn.dataset.tab);
      if (t) t.classList.add('active');
    });
  });

  // === GALLERY THUMBNAILS ===
  const mainImg = document.querySelector('.gallery-main img');
  const thumbs = document.querySelectorAll('.gallery-thumb');
  thumbs.forEach(thumb => {
    thumb.addEventListener('click', () => {
      thumbs.forEach(t => t.classList.remove('active'));
      thumb.classList.add('active');
      if (mainImg) mainImg.src = thumb.querySelector('img').src;
    });
  });

  // === GALLERY ZOOM ===
  const galleryMain = document.querySelector('.gallery-main');
  if (galleryMain && mainImg) {
    let zoomTimeout;
    galleryMain.addEventListener('mouseenter', () => {
      mainImg.style.transition = 'transform 0.3s ease';
      clearTimeout(zoomTimeout);
      zoomTimeout = setTimeout(() => {
        mainImg.style.transition = 'none';
      }, 300);
    });
    galleryMain.addEventListener('mousemove', (e) => {
      const rect = galleryMain.getBoundingClientRect();
      const x = ((e.clientX - rect.left) / rect.width) * 100;
      const y = ((e.clientY - rect.top) / rect.height) * 100;
      mainImg.style.transformOrigin = `${x}% ${y}%`;
    });
    galleryMain.addEventListener('mouseleave', () => {
      clearTimeout(zoomTimeout);
      mainImg.style.transition = 'transform 0.3s ease';
      mainImg.style.transformOrigin = 'center center';
    });
  }

  // === FAQ ACCORDION ===
  document.querySelectorAll('.faq-question').forEach(q => {
    q.addEventListener('click', () => {
      q.closest('.faq-item').classList.toggle('open');
    });
  });

  // === SIDEBAR CATEGORY TOGGLE ===
  document.querySelectorAll('.toggle-sub').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      const sub = btn.parentElement.nextElementSibling;
      if (sub && sub.classList.contains('sub-categories')) {
        sub.classList.toggle('open');
        btn.textContent = sub.classList.contains('open') ? '-' : '+';
      }
    });
  });

  // === BACK TO TOP ===
  const btt = document.querySelector('.back-to-top');
  if (btt) {
    window.addEventListener('scroll', () => {
      btt.classList.toggle('visible', window.scrollY > 400);
    });
    btt.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));
  }

  // === BLOG FILTERS ===
  document.querySelectorAll('.blog-filter').forEach(f => {
    f.addEventListener('click', () => {
      document.querySelectorAll('.blog-filter').forEach(b => b.classList.remove('active'));
      f.classList.add('active');
    });
  });

  // === WISHLIST TOGGLE ===
  document.querySelectorAll('.wishlist-btn').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      e.stopPropagation();
      btn.textContent = btn.textContent === '♡' ? '♥' : '♡';
      btn.style.color = btn.textContent === '♥' ? '#E74C3C' : '';
    });
  });
});
