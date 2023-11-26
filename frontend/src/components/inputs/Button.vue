<template>
    <button @click="$emit('click')">
        <slot></slot>
    </button>
</template>

<script setup>
defineProps([])
</script>
  