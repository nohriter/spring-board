// 필요한 CKEditor 플러그인 import
import {
  ClassicEditor,
  Essentials,
  Bold,
  Italic,
  Font,
  Paragraph,
  SimpleUploadAdapter,
  Image,
  ImageToolbar,
  ImageStyle,
  ImageUpload,
  FileRepository
} from 'ckeditor5';

// MyUploadAdapter 클래스
class MyUploadAdapter {
  constructor(loader) {
    this.loader = loader; // CKEditor에서 파일을 로드하는 객체
  }

  upload() {
    return this.loader.file
    .then(file => {
      const formData = new FormData();
      formData.append('upload', file);

      return fetch('/image/upload', {
        method: 'POST',
        body: formData,
      })
      .then(response => {
        if (!response.ok) {
          return response.json().then(errorData => {
            throw new Error(errorData.message || `Upload failed with status: ${response.status}`);
          });
        }
        return response.json();
      })      .then(result => {
        if (result.url) {
          ImageManager.addImage(result.imageId, result.url);
          return { default: result.url }; // 이미지 URL 반환
        }
        throw new Error('Upload failed');
      })
      .catch(error => {
        return Promise.reject(error.message || 'Upload failed');
      });
    });
  }
}

function MyCustomUploadAdapterPlugin(editor) {
  editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
    return new MyUploadAdapter(loader);
  };
}

export const ImageManager = {
  uploadedImages: [],

  updateImages(images) {
    this.uploadedImages = images.map(image => ({
      imageId: image.id,
      url: image.url || ""
    }))
  },

  addImage(imageId, url) {
    if (!this.uploadedImages.some(image => image.imageId === imageId)) {
      this.uploadedImages.push({ imageId, url });
    }
  },

  getImageIds() {
    return this.uploadedImages.map(image => image.imageId);
  },

  getImageUrls() {
    return this.uploadedImages.map(image => image.url);
  },

  removeImageByUrl(url) {
    this.uploadedImages = this.uploadedImages.filter(image => image.url !== url);
    console.log('Updated Images after deletion:', this.uploadedImages);
  }
};

export function initializeEditor(selector) {
  return ClassicEditor.create(document.querySelector(selector), {
    extraPlugins: [MyCustomUploadAdapterPlugin],
    plugins: [Essentials, Bold, Italic, Font, Paragraph, SimpleUploadAdapter, Image, ImageToolbar, ImageStyle, ImageUpload, FileRepository],
    toolbar: ['undo', 'redo', '|', 'bold', 'italic', 'imageUpload', '|', 'fontSize', 'fontFamily', 'fontColor', 'fontBackgroundColor'],
    image: {
      toolbar: ['imageStyle:inline', 'imageStyle:alignLeft', 'imageStyle:alignRight', '|', 'imageTextAlternative']
    }
  })
  .then(editor => {
    editor.editing.view.change(writer => {
      writer.setStyle('min-height', '300px', editor.editing.view.document.getRoot());
      writer.setStyle('max-height', '300px', editor.editing.view.document.getRoot());
      writer.setStyle('overflow', 'auto', editor.editing.view.document.getRoot());
    });

    editor.plugins.get('FileRepository').on('uploadFailed', (e, data) => {
      const errorMessage = data.message || 'File upload failed';
      alert(errorMessage);
    });

    return editor;
  })
  .catch(error => {
    console.error("Error initializing editor:", error);
  });
}

export function handlePostSubmit(editor) {
  const editorData = editor.getData();
  const parser = new DOMParser();
  const doc = parser.parseFromString(editorData, 'text/html');
  const imagesInEditor = Array.from(doc.querySelectorAll('img')).map(img => img.src);

  const currentImageUrls = ImageManager.getImageUrls();
  currentImageUrls.forEach(imageUrl => {
    if (!imagesInEditor.includes(imageUrl)) {
      ImageManager.removeImageByUrl(imageUrl);
    }
  });
  console.log("send image: ", ImageManager.getImageIds());
  document.getElementById('hidden-content').value = editorData;
}
