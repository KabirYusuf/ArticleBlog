<template>
    <button
    class="form__submitButton"
      :class="buttonClass"
      @click="$emit('click')"
    >
      <slot></slot>
    </button>
</template>

<script setup>
import { defineProps } from 'vue'
defineProps(['buttonClass'])
</script>
  