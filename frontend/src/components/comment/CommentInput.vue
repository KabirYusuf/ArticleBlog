<template>
    <div class="writeComment__container">
        <form @submit.prevent="sendComment" class="writeComment__form">
            <div class="writeComment__inner">
                <img src="../../../public/card_images/BlogImage3.jpg" alt="commenter image" class="commenter__image">
                <textarea v-model="content" placeholder="Write a comment..." class="writeComment__inputField"></textarea>
                <Button type="submit" class="writeComment__submitButton">Send</Button>
            </div>
        </form>
    </div>
</template>

<script setup>
import Button from '../inputs/Button.vue'
import { postComment } from '../../utility/Http'
import { ref, onMounted } from 'vue'
import { useArticleStore } from '@/store/articleStore'
import { useRouter } from 'vue-router';
import { defineEmits } from 'vue';
import { useUserStore } from '../../store/userStore'

const emits = defineEmits(['commentSent']);

const router = useRouter();
const userStore = useUserStore();



const articleStore = useArticleStore()
const articleId = ref(articleStore.currentArticle.id)
const content = ref('')

onMounted(async () => {
    await userStore.fetchUser();
});

const sendComment = async () => {
    const currentUser = userStore.user;

    if (!currentUser) {
        return;
    }

    const fullComment = {
        articleId: articleId.value,
        content: content.value,
        createdAt: new Date().toISOString(),
        user: currentUser
    };

    const backendRequest = {
        articleId: articleId.value,
        content: content.value
    };

    try {
        await postComment(backendRequest);
        content.value = '';
        emits('commentSent', fullComment);
    } catch (error) {
        console.error(error.message);
    }
};

</script>
