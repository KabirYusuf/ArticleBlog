<template>
  <Header
    :title="currentArticle?.title"
    :content="currentArticle?.content"
    :headerBackgroundImage="'/article-view.jpg'"
    :isCenter="true"
    :authorName="currentArticle?.user.firstName + ' ' + currentArticle?.user.lastName"
  />
  
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
        <img v-if="currentArticle?.user.image" :src="currentArticle?.user.image" alt="authorImage">
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
  </div>

<Footer/>

</template>

<script setup>

import { onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { useArticleStore } from '../store/articleStore'
import Header from "@/components/layouts/Header.vue"
import Footer from "@/components/layouts/Footer.vue"

const route = useRoute();
const articleStore = useArticleStore();

const currentArticle = computed(() => articleStore.currentArticle);

const fullName = computed(() => {
  if (currentArticle.value && currentArticle.value.user) {
    return `${currentArticle.value.user.firstName} ${currentArticle.value.user.lastName}`;
  }
  return '';
});

onMounted(() => {
  articleStore.fetchArticle(route.params.id);
});

</script>