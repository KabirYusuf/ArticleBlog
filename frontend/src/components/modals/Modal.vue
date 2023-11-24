<script setup>

import { useModalStore } from "@/store/modalStore"; 
    const modalStore = useModalStore();

    const closeModal = () => {
      modalStore.closeModal();
    };

    const closeModalFromOutside = (event) => {
      if (event.target.classList.contains("modal__container")) {
        closeModal();
      }
    };


</script>
<template>
    <div
        class="modal__container"
        :class="{ showModal: modalStore.showModal }"
        @click="closeModalFromOutside"
    >
        <div class="modal__content">
            
        </div>
    </div>
</template>