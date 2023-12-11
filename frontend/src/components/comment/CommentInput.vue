<template>

    <div class="writeComment__container">
        <div class="writeComment__inner">
            <img src="../../../public/card_images/BlogImage3.jpg" alt="commenter image" class="commenter__image">
            <InputField  type="text" placeholder="Write a comment..." 
            v-model="comment" class="writeComment__inputField"/>
            <Button @click="sendComment" class="writeComment__submitButton"> Send </Button>

        </div>
    </div>

</template>

<script setup>
import InputField from '../inputs/InputField.vue'
import Button from '../inputs/Button.vue'
</script>