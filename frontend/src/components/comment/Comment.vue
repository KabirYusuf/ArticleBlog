<template>
    <div class="commentCard__container">
        <div class="comment__card">
            <img src="../../../public/article-view.jpg" alt="User image" class="user__image" />
            <div class="comment__content">
                <h4 class="commentUser__name">{{ fullName }}</h4>
                <p class="comment__date">{{ comment?.createdAt }} - {{ comment?.timeFromCommentPost }}</p>
                <p class="comment__text">{{ comment?.content }}</p>
            </div>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps(['comment']);

const fullName = computed(() => {
    if (props.comment && props.comment.user) {
        return `${props.comment.user.firstName} ${props.comment.user.lastName}`;
    }
    return '';
});
</script>