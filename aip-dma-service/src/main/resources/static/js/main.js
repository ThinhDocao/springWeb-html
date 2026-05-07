// ========== MOBILE NAV TOGGLE ==========
document.addEventListener('DOMContentLoaded', function() {
  const toggle = document.querySelector('.nav-toggle');
  const menu = document.querySelector('.nav-menu');
  if (toggle && menu) {
    toggle.addEventListener('click', function() {
      menu.classList.toggle('active');
    });
  }

  // ========== CAROUSEL ==========
  initCarousel();

  // ========== TABS ==========
  initTabs();

  // ========== SCROLL ANIMATIONS ==========
  initScrollAnimations();

  // ========== PRODUCT DETAIL TABS ==========
  initProductTabs();

  // ========== MOBILE SUBMENU TOGGLE ==========
  if (window.innerWidth <= 768) {
    const subToggles = document.querySelectorAll('.has-submenu > a');
    subToggles.forEach(function(st) {
      st.addEventListener('click', function(e) {
        e.preventDefault();
        const sub = st.nextElementSibling;
        if (sub) {
          sub.style.display = (sub.style.display === 'block') ? 'none' : 'block';
        }
      });
    });
  }
});

function initProductTabs() {
  const links = document.querySelectorAll('.tab-link');
  const panes = document.querySelectorAll('.tab-pane');
  if (links.length === 0) return;

  links.forEach(function(link) {
    link.addEventListener('click', function() {
      links.forEach(function(l) { l.classList.remove('active'); });
      panes.forEach(function(p) { p.classList.remove('active'); });
      link.classList.add('active');
      const target = document.getElementById(link.getAttribute('data-tab'));
      if (target) target.classList.add('active');
    });
  });
}

function initCarousel() {
  const track = document.querySelector('.carousel-track');
  const prevBtn = document.querySelector('.carousel-btn.prev');
  const nextBtn = document.querySelector('.carousel-btn.next');
  if (!track) return;

  const cards = track.querySelectorAll('.product-card');
  if (cards.length === 0) return;

  let index = 0;
  const gap = 20;

  function getVisibleCount() {
    if (window.innerWidth <= 768) return 2;
    if (window.innerWidth <= 992) return 3;
    return 4;
  }

  function getCardWidth() {
    return cards[0].offsetWidth + gap;
  }

  function slide(dir) {
    const maxIndex = Math.max(0, cards.length - getVisibleCount());
    index = Math.min(Math.max(index + dir, 0), maxIndex);
    track.style.transform = 'translateX(-' + (index * getCardWidth()) + 'px)';
  }

  if (prevBtn) prevBtn.addEventListener('click', function() { slide(-1); });
  if (nextBtn) nextBtn.addEventListener('click', function() { slide(1); });

  // Auto slide
  let autoSlide = setInterval(function() {
    const maxIndex = Math.max(0, cards.length - getVisibleCount());
    if (index >= maxIndex) { index = -1; }
    slide(1);
  }, 4000);

  track.addEventListener('mouseenter', function() { clearInterval(autoSlide); });
  track.addEventListener('mouseleave', function() {
    autoSlide = setInterval(function() {
      const maxIndex = Math.max(0, cards.length - getVisibleCount());
      if (index >= maxIndex) { index = -1; }
      slide(1);
    }, 4000);
  });
}

function initTabs() {
  const tabBtns = document.querySelectorAll('.tab-btn');
  const tabContents = document.querySelectorAll('.tab-content');

  tabBtns.forEach(function(btn) {
    btn.addEventListener('click', function() {
      tabBtns.forEach(function(b) { b.classList.remove('active'); });
      tabContents.forEach(function(c) { c.classList.remove('active'); });
      btn.classList.add('active');
      var target = document.getElementById(btn.getAttribute('data-tab'));
      if (target) target.classList.add('active');
    });
  });
}

function initScrollAnimations() {
  var elements = document.querySelectorAll('.product-card, .news-card, .testimonial-card, .hero-cat-card');
  var observer = new IntersectionObserver(function(entries) {
    entries.forEach(function(entry) {
      if (entry.isIntersecting) {
        entry.target.style.opacity = '1';
        entry.target.style.transform = 'translateY(0)';
        observer.unobserve(entry.target);
      }
    });
  }, { threshold: 0.1 });

  elements.forEach(function(el) {
    el.style.opacity = '0';
    el.style.transform = 'translateY(20px)';
    el.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
    observer.observe(el);
  });
}
