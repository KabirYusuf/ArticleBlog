<template>
    <div class="writeComment__container">
        <form @submit.prevent="sendComment" class="writeComment__form">
            <div class="writeComment__inner">
                <img src="../../../public/card_images/BlogImage3.jpg" alt="commenter image" class="commenter__image">
                <InputField type="text" placeholder="Write a comment..." v-model:value="content" class="writeComment__inputField"/>
                <Button type="submit" class="writeComment__submitButton">Send</Button>
            </div>
        </form>
    </div>
</template>

<script setup>
import InputField from '../inputs/InputField.vue'
import Button from '../inputs/Button.vue'
import { postComment } from '../../utility/Http'
import { ref } from 'vue'
import { useArticleStore } from '@/store/articleStore'

const articleStore = useArticleStore()
const articleId = ref(articleStore.currentArticle.id)
const content = ref('')

const sendComment = async () => {
    console.log('Content:', content.value); 

    const addCommentRequest = {
        articleId: articleId.value,
        content: content.value
    }

    try {
        await postComment(addCommentRequest)
        content.value = '' 
    } catch (error) {
        console.error(error.message)
    }
}
</script>
