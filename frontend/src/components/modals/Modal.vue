<script setup>
import { useModalStore } from '../../store/modalStore'
import { X } from 'lucide-vue-next';


const modalStore = useModalStore();
const stopPropagation = (e) => {
    e.stopPropagation();
}

</script>
<template>
    <div class="modal__container" @click="modalStore.closeModal">
        <div class="modalContent__container" @click="stopPropagation">
            <span class="closeModal__symbol" @click="modalStore.closeModal">
                <X />
            </span>
            <slot />
        </div>
    </div>
</template>