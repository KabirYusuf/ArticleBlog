<template>
   <div class="profileHeader">
      <NavBar />
      <div class="profileHeader__content">
         <slot>

         </slot>
      </div>
   </div>
</template>

<script setup>
import NavBar from './NavBar.vue'
</script>