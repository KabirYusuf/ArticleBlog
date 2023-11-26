<template>
    <div class="commentCard__container">
        <div class="comment__card">
        <img src="../../../public/article-view.jpg" alt="User image" class="user__image" />
        <div class="comment__content">
            <h4 class="commentUser__name">{{ name }}</h4>
            <p class="comment__date">{{ date }} - {{ timeFromCommentPost }}</p>
            <p class="comment__text">{{ content }}</p>
        </div>
    </div>
    </div>
</template>

<script setup>
import { defineProps } from 'vue';

defineProps(['name', 'image', 'date', 'content', 'timeFromCommentPost']);
</script>