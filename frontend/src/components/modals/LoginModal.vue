<script setup>
import { useModalStore } from '@/stores/modalStore';
import Modal from '@/components/Modal'; 

    function openModal() {
      useModalStore().openModal();
    }
 
</script>
<template>
    <div >
    <button @click="openModal">Open Modal</button>
    <Modal>
      <!-- <h2>Hello, I'm a modal!</h2> -->
    </Modal>
  </div>
</template>