<script>
import {defineProps} from 'vue'
defineProps(['type', 'placeHolder', 'value', 'class'])
</script>

<template>

<input class="input__field"
    :id="id"
    :type="type"
    :placeholder="placeholder"
    :value="value"
    @input="$emit('update:value', $event.target.value)"
    :class="inputClass"
  />

</template>