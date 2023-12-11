<script setup>
import { defineProps } from 'vue'
defineProps(['type', 'placeholder', 'value', 'checked', 'id'])

</script>

<template>

<input v-if="type === 'checkbox'" :id="id" :type="type" :checked="checked" v-bind="$attrs"
    @change="$emit('update:checked', $event.target.checked )" />


  <input v-else :id="id" :type="type" :placeholder="placeholder" :value="value" v-bind="$attrs"
    @input="$emit('update:value', $event.target.value)" />

</template>