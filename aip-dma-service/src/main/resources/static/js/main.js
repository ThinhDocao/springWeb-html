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
      if (!nav.contains(e.target) && !toggle.contains(e.target) && !e.target.closest('#mobileSearchTrigger')) {
        nav.classList.remove('open');
      }
    });
  }

  // === SEARCH DRAWER ===
  const searchDrawer = document.getElementById('searchDrawer');
  const searchTriggers = document.querySelectorAll('.search-trigger');
  const searchClose = document.getElementById('searchDrawerClose');
  const searchInput = document.getElementById('searchDrawerInput');
  const searchClear = document.getElementById('searchDrawerClear');
  const searchDefault = document.getElementById('searchDefault');
  const searchResults = document.getElementById('searchResults');
  const searchNoResults = document.getElementById('searchNoResults');
  const searchTags = document.querySelectorAll('.search-tag');

  const openSearch = () => {
    searchDrawer.classList.add('open');
    document.body.style.overflow = 'hidden';
    setTimeout(() => searchInput.focus(), 300);
  };

  const closeSearch = () => {
    searchDrawer.classList.remove('open');
    document.body.style.overflow = '';
  };

  searchTriggers.forEach(btn => btn.addEventListener('click', openSearch));
  if (searchClose) searchClose.addEventListener('click', closeSearch);

  // Close on Escape or click outside
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && searchDrawer.classList.contains('open')) closeSearch();
    if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
      e.preventDefault();
      openSearch();
    }
  });

  searchDrawer.addEventListener('click', (e) => {
    if (e.target === searchDrawer) closeSearch();
  });

  // Live Search with Debounce
  let searchTimeout;
  const performSearch = (query) => {
    if (query.length < 2) {
      searchDefault.style.display = 'block';
      searchResults.style.display = 'none';
      searchNoResults.style.display = 'none';
      searchClear.style.display = 'none';
      return;
    }

    searchClear.style.display = 'block';
    
    fetch(`/api/search?q=${encodeURIComponent(query)}`)
      .then(res => res.json())
      .then(data => {
        searchDefault.style.display = 'none';
        if (data.length > 0) {
          searchResults.style.display = 'grid';
          searchNoResults.style.display = 'none';
          searchResults.innerHTML = data.map(p => `
            <a href="/san-pham/${p.slug}" class="search-prod-card">
              <div class="prod-img">
                <img src="${p.imageUrl}" alt="${p.name}">
              </div>
              <div class="prod-info">
                <div class="prod-name">${p.name}</div>
                <div class="prod-price">${p.price}</div>
              </div>
            </a>
          `).join('');
        } else {
          searchResults.style.display = 'none';
          searchNoResults.style.display = 'block';
        }
      })
      .catch(err => console.error('Search error:', err));
  };

  searchInput.addEventListener('input', (e) => {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(() => performSearch(e.target.value), 300);
  });

  searchClear.addEventListener('click', () => {
    searchInput.value = '';
    performSearch('');
    searchInput.focus();
  });

  searchTags.forEach(tag => {
    tag.addEventListener('click', () => {
      searchInput.value = tag.textContent;
      performSearch(tag.textContent);
      searchInput.focus();
    });
  });

  // === MOBILE DROPDOWN TOGGLE ===
  const dropdownToggles = document.querySelectorAll('.mobile-dropdown-toggle');
  dropdownToggles.forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault(); // Prevent default link behavior if any
      const parentLi = btn.closest('li.has-dropdown');
      if (parentLi) {
        parentLi.classList.toggle('show');
      }
    });
  });

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

  // === PRODUCT CAROUSEL ===
  const carousel = document.getElementById('productCarousel');
  if (carousel) {
    const items = carousel.querySelectorAll('.carousel-item');
    const thumbs = document.querySelectorAll('.gallery-thumb');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    let currentIndex = 0;
    let carouselInterval;

    const showSlide = (index) => {
      if (index < 0) index = items.length - 1;
      if (index >= items.length) index = 0;
      
      items.forEach(item => item.classList.remove('active'));
      thumbs.forEach(thumb => thumb.classList.remove('active'));
      
      items[index].classList.add('active');
      const targetThumb = document.querySelector(`.gallery-thumb[data-index="${index}"]`);
      if (targetThumb) targetThumb.classList.add('active');
      
      currentIndex = index;
    };

    const startCarousel = () => {
      if (items.length <= 1) return;
      carouselInterval = setInterval(() => showSlide(currentIndex + 1), 4000);
    };

    const resetCarousel = () => {
      clearInterval(carouselInterval);
      startCarousel();
    };

    if (prevBtn) prevBtn.addEventListener('click', () => { showSlide(currentIndex - 1); resetCarousel(); });
    if (nextBtn) nextBtn.addEventListener('click', () => { showSlide(currentIndex + 1); resetCarousel(); });

    thumbs.forEach(thumb => {
      thumb.addEventListener('click', () => {
        showSlide(parseInt(thumb.dataset.index));
        resetCarousel();
      });
    });

    startCarousel();
  }

  // === GALLERY ZOOM ===
  const galleryMain = document.querySelector('.gallery-main');
  if (galleryMain) {
    let zoomTimeout;
    galleryMain.addEventListener('mouseenter', () => {
      const activeImg = galleryMain.querySelector('.carousel-item.active img');
      if (!activeImg) return;
      activeImg.style.transition = 'transform 0.3s ease';
      clearTimeout(zoomTimeout);
      zoomTimeout = setTimeout(() => {
        activeImg.style.transition = 'none';
      }, 300);
    });
    galleryMain.addEventListener('mousemove', (e) => {
      const activeImg = galleryMain.querySelector('.carousel-item.active img');
      if (!activeImg) return;
      const rect = galleryMain.getBoundingClientRect();
      const x = ((e.clientX - rect.left) / rect.width) * 100;
      const y = ((e.clientY - rect.top) / rect.height) * 100;
      activeImg.style.transformOrigin = `${x}% ${y}%`;
      activeImg.style.transform = 'scale(1.5)';
    });
    galleryMain.addEventListener('mouseleave', () => {
      const activeImg = galleryMain.querySelector('.carousel-item.active img');
      if (!activeImg) return;
      clearTimeout(zoomTimeout);
      activeImg.style.transition = 'transform 0.3s ease';
      activeImg.style.transformOrigin = 'center center';
      activeImg.style.transform = 'scale(1)';
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



  // === WISHLIST TOGGLE ===
  document.querySelectorAll('.wishlist-btn').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      e.stopPropagation();
      btn.textContent = btn.textContent === '♡' ? '♥' : '♡';
      btn.style.color = btn.textContent === '♥' ? '#E74C3C' : '';
    });
  });

  // === CATEGORY FILTER DRAWER ===
  const filterBtn = document.getElementById('filterTrigger');
  const sidebar = document.getElementById('categorySidebar');
  const sidebarClose = document.getElementById('sidebarClose');
  const sidebarOverlay = document.getElementById('sidebarOverlay');

  if (filterBtn && sidebar) {
    filterBtn.addEventListener('click', () => {
      sidebar.classList.add('open');
      if (sidebarOverlay) sidebarOverlay.classList.add('open');
      document.body.style.overflow = 'hidden';
    });

    const closeSidebar = () => {
      sidebar.classList.remove('open');
      if (sidebarOverlay) sidebarOverlay.classList.remove('open');
      document.body.style.overflow = '';
    };

    if (sidebarClose) sidebarClose.addEventListener('click', closeSidebar);
    if (sidebarOverlay) sidebarOverlay.addEventListener('click', closeSidebar);
  }
});
