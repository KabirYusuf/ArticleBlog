<template>
    <Header :card="currentArticle" :isCenter="true" />

    <section class="article__view">
        <div v-if="currentArticle" class="articleView__container">
            <div class="articleView__content">
                <p>{{ currentArticle.content }}</p>
            </div>
            <div class="articleView__tag">
                <p class="articleViewTag__content">ADVENTURE</p>
                <p class="articleViewTag__content">PHOTO</p>
                <p class="articleViewTag__content">DESIGN</p>
            </div>

            <div class="articleAuthorInfo">
                <div class="author__Info author--nameAndImage">
                    <img :src="authorImage" alt="authorImage" class="article__authorImage">
                    <div class="author__nameAndProfession">
                        <p class="author__name">{{ fullName }}</p>
                        <p class="author__profession">Thinker & Designer</p>
                    </div>
                </div>
                <div class="author__Info author--SocialMedia">
                    <i class="fab fa-facebook"></i>
                    <i class="fab fa-twitter"></i>
                    <i class="fab fa-instagram"></i>
                    <i class="fab fa-youtube"></i>
                </div>
            </div>
            <p class="comment__title">Comments:</p>

            <CommentInput @commentSent="handleNewComment" />

            <Comment v-for="comment in formattedComments" :comment="comment" :key="comment.id" />
        </div>
    </section>

    <section class="relatedPost">
        <div class="relatedPostContainer">
            <h5 class="relatedPost__title">Related Posts</h5>
            <div class="editorsPick__card">
                <EditorsPickCard v-for="(editorPick, index) in editorsPicks" :key="index" :card="editorPick" />
            </div>
        </div>
    </section>

    <Footer :isConnection="true" />
</template>

<script setup>

import { onMounted, computed, ref } from 'vue';
import EditorsPickCard from '@/components/homepage/EditorsPickCard.vue';
import { useRoute } from 'vue-router';
import { useArticleStore } from '../store/articleStore'
import Header from "@/components/layouts/Header.vue"
import Footer from "@/components/layouts/Footer.vue"
import Comment from "../components/comment/Comment.vue"
import { timeSince } from '../utility/dateAndTimeLogic';
import { formatDate } from '../utility/dateAndTimeLogic';
import CommentInput from '../components/comment/CommentInput.vue'


const route = useRoute();
const articleStore = useArticleStore();

const currentArticle = computed(() => articleStore.currentArticle);

const fullName = computed(() => {
    if (currentArticle.value && currentArticle.value.user) {
        return `${currentArticle.value.user.firstName} ${currentArticle.value.user.lastName}`;
    }
    return '';
});

const authorImage = computed(() => {
    return currentArticle.value?.user?.userImage || '../../public/profile-avatar.webp';
});


const formattedComments = computed(() => {
    if (!currentArticle.value || !currentArticle.value.comments) return [];

    return currentArticle.value.comments.map((comment) => {
        const commentDate = new Date(comment.createdAt);

        return {
            ...comment,
            createdAt: formatDate(commentDate),
            timeFromCommentPost: timeSince(commentDate)
        };
    });
});

const handleNewComment = (newComment) => {
    articleStore.addComment(newComment);
};

onMounted(() => {
    articleStore.fetchArticle(route.params.id);
});

const editorsPicks = ref([
    {
        image: "/public/editors_images/Editor1.jpg",
        createdAt: "08.08.2021",
        tag: "FASHION",
        title: "Richard Norton photorealistic rendering as real photos",
        content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data",
        icon: "fas fa-gem"
    },
    {
        image: "/public/editors_images/Editor2.jpg",
        createdAt: "08.08.2021",
        tag: "FASHION",
        title: "Richard Norton photorealistic rendering as real photos",
        content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data",
    },
    {
        image: "/public/editors_images/Editor3.jpg",
        createdAt: "08.08.2021",
        tag: "FASHION",
        title: "Richard Norton photorealistic rendering as real photos",
        content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data",
    },
]);

</script>
