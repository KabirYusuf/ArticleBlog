<template>
    <div class="writeComment__container">
        <form @submit.prevent="sendComment" class="writeComment__form">
            <div class="writeComment__inner">
                <img :src="commenterImage" alt="commenter image" class="commenter__image">
                <textarea v-model="content" placeholder="Write a comment..." class="writeComment__inputField"></textarea>
                <Button type="submit" class="writeComment__submitButton">Send</Button>
            </div>
        </form>
    </div>
</template>

<script setup>
import Button from '../inputs/Button.vue'
import { postComment } from '../../utility/Http'
import { ref, onMounted, computed } from 'vue'
import { useArticleStore } from '@/store/articleStore'
import { useRouter } from 'vue-router';
import { defineEmits } from 'vue';
import { useUserStore } from '../../store/userStore'
import {handleErrors} from '../../utility/handleErrors'

const emits = defineEmits(['commentSent']);

const router = useRouter();
const userStore = useUserStore();


const currentUser = ref(null);


const commenterImage = computed(() => {
    return currentUser.value?.userImage || '../../../public/profile-avatar.webp';
});



const articleStore = useArticleStore()
const articleId = ref(articleStore.currentArticle.id)
const content = ref('')

onMounted(async () => {
    await userStore.fetchUser();
    currentUser.value = userStore.user;
});

const sendComment = async () => {
    

    if (!currentUser.value) {
        return;
    }

    const fullComment = {
        articleId: articleId.value,
        content: content.value,
        createdAt: new Date().toISOString(),
        user: currentUser.value
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
        handleErrors(error)
    }
};

</script>
