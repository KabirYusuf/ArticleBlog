<template>
    <div class="image-upload-crop">
      <img
        v-if="!imgDataUrl"
        @click="toggleUploader"
        src="../../public/profile-avatar.webp"
        alt="profile-image"
        class="profileHeader__image clickable"
      />
      <img
        v-else
        @click="toggleUploader"
        :src="imgDataUrl"
        alt="Cropped image"
        class="profileHeader__image clickable"
      />
      <my-upload
        v-if="showUploader"
        :width="300"
        :height="300"
        :url="uploadUrl"
        :params="params"
        :headers="headers"
        :img-format="imgFormat"
        @crop-success="cropSuccess"
        @crop-upload-success="cropUploadSuccess"
        @crop-upload-fail="cropUploadFail"
      />
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import myUpload from 'vue-image-crop-upload';
  
  const showUploader = ref(false);
  const imgDataUrl = ref('');
  const uploadUrl = '/upload';
  const imgFormat = 'png';
  const params = ref({});
  const headers = ref({});
  
  function toggleUploader() {
    showUploader.value = !showUploader.value;
  }
  
  function cropSuccess(imgDataUrlParam, field) {
    imgDataUrl.value = imgDataUrlParam;
    showUploader.value = false; 
  }
  
  function cropUploadSuccess(jsonData, field) {
    console.log('Upload success:', jsonData);
  }
  
  function cropUploadFail(status, field) {
    console.error('Upload fail:', status);
  }
  </script>
  
  <style scoped>
  .clickable {
    cursor: pointer;
  }
  
  </style>
  