(function () {
  const sidebar = document.getElementById('adminSidebar');
  const sidebarToggle = document.getElementById('sidebarToggle');

  if (sidebar && sidebarToggle) {
    sidebarToggle.addEventListener('click', function () {
      sidebar.classList.toggle('open');
    });
  }

  // Auto-hide flash messages after 5 seconds
  document.querySelectorAll('.admin-flash').forEach(function (flash) {
    setTimeout(function () {
      flash.style.transition = 'opacity 0.5s ease';
      flash.style.opacity = '0';
      setTimeout(function () {
        const wrap = flash.parentElement;
        flash.remove();
        if (wrap && wrap.classList.contains('admin-flash-wrap') && wrap.children.length === 0) {
          wrap.style.display = 'none';
        }
      }, 500);
    }, 5000);
  });

  document.querySelectorAll('form[data-confirm]').forEach(function (form) {
    form.addEventListener('submit', function (event) {
      const message = form.getAttribute('data-confirm') || 'Bạn chắc chắn muốn tiếp tục?';
      if (!window.confirm(message)) {
        event.preventDefault();
      }
    });
  });

  const activeEditors = [];

  const EditorConstructor = window.CKEDITOR && window.CKEDITOR.ClassicEditor
    ? window.CKEDITOR.ClassicEditor
    : window.ClassicEditor;

  if (EditorConstructor) {
    document.querySelectorAll('.rich-editor').forEach(function (textarea) {
      EditorConstructor.create(textarea, {
        extraPlugins: [function (editor) {
          adminUploadAdapterPlugin(editor, textarea);
        }],
        removePlugins: [
          'AIAssistant',
          'CKBox',
          'CKFinder',
          'EasyImage',
          'Base64UploadAdapter',
          'RealTimeCollaborativeComments',
          'RealTimeCollaborativeTrackChanges',
          'RealTimeCollaborativeRevisionHistory',
          'PresenceList',
          'Comments',
          'TrackChanges',
          'TrackChangesData',
          'RevisionHistory',
          'Pagination',
          'WProofreader',
          'MathType',
          'SlashCommand',
          'Template',
          'DocumentOutline',
          'FormatPainter',
          'TableOfContents',
          'PasteFromOfficeEnhanced',
          'CaseChange',
          'MultiLevelList',
          'ExportPdf',
          'ExportWord',
          'ImportWord',
          'ImportPdf'
        ],
        toolbar: {
          items: [
            'heading',
            '|',
            'bold',
            'italic',
            'underline',
            'strikethrough',
            'removeFormat',
            '|',
            'fontFamily',
            'fontSize',
            'fontColor',
            'fontBackgroundColor',
            '|',
            'alignment',
            'outdent',
            'indent',
            '|',
            'link',
            'bulletedList',
            'numberedList',
            'todoList',
            '|',
            'uploadImage',
            'resizeImage',
            'insertTable',
            'horizontalLine',
            'blockQuote',
            'mediaEmbed',
            'specialCharacters',
            '|',
            'undo',
            'redo'
          ],
          shouldNotGroupWhenFull: false
        },
        fontFamily: {
          supportAllValues: true
        },
        fontSize: {
          options: [
            12,
            14,
            'default',
            18,
            20,
            24,
            28,
            32,
            36
          ],
          supportAllValues: true
        },
        alignment: {
          options: [
            'left',
            'center',
            'right',
            'justify'
          ]
        },
        table: {
          contentToolbar: [
            'tableColumn',
            'tableRow',
            'mergeTableCells',
            'tableProperties',
            'tableCellProperties'
          ]
        },
        image: {
          styles: {
            options: [
              'inline',
              'alignLeft',
              'alignCenter',
              'alignRight',
              'block',
              'side'
            ]
          },
          resizeUnit: '%',
          resizeOptions: [
            {
              name: 'resizeImage:original',
              value: null,
              label: 'Gốc'
            },
            {
              name: 'resizeImage:25',
              value: '25',
              label: '25%'
            },
            {
              name: 'resizeImage:50',
              value: '50',
              label: '50%'
            },
            {
              name: 'resizeImage:75',
              value: '75',
              label: '75%'
            }
          ],
          toolbar: [
            'imageTextAlternative',
            'toggleImageCaption',
            '|',
            'imageStyle:inline',
            'imageStyle:alignLeft',
            'imageStyle:alignCenter',
            'imageStyle:alignRight',
            'imageStyle:block',
            'imageStyle:side',
            '|',
            'resizeImage'
          ]
        }
      }).then(function (editor) {
        activeEditors.push(editor);
      }).catch(function (error) {
        if (window.console) {
          console.error('CKEditor init failed', error);
        }
        textarea.classList.add('editor-fallback');
      });
    });
  }

  document.querySelectorAll('form').forEach(function (form) {
    form.addEventListener('submit', function () {
      activeEditors.forEach(function (editor) {
        if (typeof editor.updateSourceElement === 'function') {
          editor.updateSourceElement();
        }
      });
    });
  });

  document.querySelectorAll('input[type="file"][data-preview-target]').forEach(function (input) {
    input.addEventListener('change', function () {
      renderImagePreviews(input);
    });
  });

  document.querySelectorAll('.sortable-images').forEach(function (list) {
    let dragging = null;

    list.querySelectorAll('.image-item').forEach(function (item) {
      item.addEventListener('dragstart', function () {
        dragging = item;
        item.classList.add('dragging');
      });

      item.addEventListener('dragend', function () {
        item.classList.remove('dragging');
        dragging = null;
        refreshSortOrders(list);
      });

      item.addEventListener('dragover', function (event) {
        event.preventDefault();
        if (!dragging || dragging === item) {
          return;
        }
        const rect = item.getBoundingClientRect();
        const after = event.clientY > rect.top + rect.height / 2;
        if (after) {
          item.after(dragging);
        } else {
          item.before(dragging);
        }
      });
    });
  });

  document.querySelectorAll('.remove-image-btn').forEach(function (button) {
    button.addEventListener('click', function () {
      const imageId = button.getAttribute('data-image-id');
      const item = button.closest('.image-item');
      const list = item ? item.closest('.sortable-images') : null;
      const form = button.closest('form');
      const removedInputs = form ? form.querySelector('.removed-images-inputs') : null;

      if (!imageId || !item || !removedInputs) {
        return;
      }
      if (!window.confirm('Xóa ảnh này khỏi sản phẩm?')) {
        return;
      }

      const hiddenInput = document.createElement('input');
      hiddenInput.type = 'hidden';
      hiddenInput.name = 'removeImageIds';
      hiddenInput.value = imageId;
      removedInputs.appendChild(hiddenInput);

      const removedPrimary = item.querySelector('input[name="primaryImageId"]:checked');
      item.remove();

      if (list) {
        refreshSortOrders(list);
        if (removedPrimary) {
          const nextPrimary = list.querySelector('input[name="primaryImageId"]');
          if (nextPrimary) {
            nextPrimary.checked = true;
          }
        }
      }
    });
  });

  function refreshSortOrders(list) {
    list.querySelectorAll('.image-item').forEach(function (item, index) {
      const input = item.querySelector('.sort-order-input');
      if (input) {
        input.value = index;
      }
    });
  }

  function renderImagePreviews(input) {
    const targetId = input.getAttribute('data-preview-target');
    const target = targetId ? document.getElementById(targetId) : null;
    if (!target) {
      return;
    }

    target.querySelectorAll('img[data-preview-url]').forEach(function (img) {
      URL.revokeObjectURL(img.getAttribute('data-preview-url'));
    });
    target.innerHTML = '';

    const files = Array.prototype.slice.call(input.files || []);
    const imageFiles = files.filter(function (file) {
      return file.type && file.type.indexOf('image/') === 0;
    });
    if (!imageFiles.length) {
      target.hidden = true;
      return;
    }

    target.hidden = false;
    imageFiles.forEach(function (file) {
      const previewUrl = URL.createObjectURL(file);
      const item = document.createElement('div');
      item.className = 'upload-preview-item';

      const img = document.createElement('img');
      img.src = previewUrl;
      img.alt = file.name;
      img.setAttribute('data-preview-url', previewUrl);

      const caption = document.createElement('span');
      caption.textContent = file.name;

      item.appendChild(img);
      item.appendChild(caption);
      target.appendChild(item);
    });
  }

  function adminUploadAdapterPlugin(editor, textarea) {
    editor.plugins.get('FileRepository').createUploadAdapter = function (loader) {
      return new AdminUploadAdapter(loader, textarea);
    };
  }

  class AdminUploadAdapter {
    constructor(loader, textarea) {
      this.loader = loader;
      this.textarea = textarea;
      this.xhr = null;
    }

    upload() {
      return this.loader.file.then((file) => {
        return new Promise((resolve, reject) => {
          this.initRequest();
          this.initListeners(resolve, reject, file);
          this.sendRequest(file);
        });
      });
    }

    abort() {
      if (this.xhr) {
        this.xhr.abort();
      }
    }

    initRequest() {
      const xhr = this.xhr = new XMLHttpRequest();
      xhr.open('POST', '/admin/uploads/ckeditor', true);
      xhr.responseType = 'json';
    }

    initListeners(resolve, reject, file) {
      const xhr = this.xhr;
      const loader = this.loader;
      const genericErrorText = 'Không thể upload ảnh: ' + file.name;

      xhr.addEventListener('error', function () {
        reject(genericErrorText);
      });
      xhr.addEventListener('abort', function () {
        reject();
      });
      xhr.addEventListener('load', function () {
        const response = xhr.response;

        if (!response || xhr.status < 200 || xhr.status >= 300 || !response.url) {
          reject(response && response.message ? response.message : genericErrorText);
          return;
        }

        resolve({
          default: response.url
        });
      });

      if (xhr.upload) {
        xhr.upload.addEventListener('progress', function (event) {
          if (event.lengthComputable) {
            loader.uploadTotal = event.total;
            loader.uploaded = event.loaded;
          }
        });
      }
    }

    sendRequest(file) {
      const data = new FormData();
      data.append('upload', file);
      const folder = getEditorUploadFolder(this.textarea);
      if (folder) {
        data.append('folder', folder);
      }
      this.xhr.send(data);
    }
  }

  function getEditorUploadFolder(textarea) {
    const form = textarea ? textarea.closest('form') : null;
    if (!form) {
      return 'ckeditor';
    }

    const uploadType = form.getAttribute('data-upload-type') || 'ckeditor';
    const currentSlug = form.getAttribute('data-current-slug') || '';
    const slugInput = form.querySelector('input[name="slug"]');
    const nameInput = form.querySelector('input[name="name"], input[name="title"]');
    const slug = toAdminSlug(
      (slugInput && slugInput.value) ||
      (nameInput && nameInput.value) ||
      currentSlug ||
      'draft'
    );

    return uploadType + '/' + slug + '/content';
  }

  function toAdminSlug(value) {
    const raw = (value || 'draft').toString();
    let normalized = raw;
    if (typeof normalized.normalize === 'function') {
      normalized = normalized.normalize('NFD').replace(/[\u0300-\u036f]/g, '');
    }
    normalized = normalized
      .replace(/đ/g, 'd')
      .replace(/Đ/g, 'D')
      .toLowerCase()
      .replace(/[^a-z0-9\s-]/g, '')
      .trim()
      .replace(/\s+/g, '-')
      .replace(/-+/g, '-');
    return normalized || 'draft';
  }
})();
