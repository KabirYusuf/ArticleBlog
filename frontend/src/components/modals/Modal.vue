<script setup>
import {useModalStore} from '../../store/modalStore'

const modalStore = useModalStore();
const stopPropagation = (e)=>{
    e.stopPropagation();  
}

</script>
<template>
    <div v-if="modalStore.showModal" class="modal__container" @click="modalStore.closeModal">
            <div class="modalContent__container"  @click="stopPropagation">
                <span class="closeModal__symbol" @click="modalStore.closeModal">X</span>
                 <slot/>  
            </div>
    </div>
</template>