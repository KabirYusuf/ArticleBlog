<template>
<Header 
  :title="lastCard?.title"
  :date="lastCard?.createdAt"
  :content="lastCard?.content"
  :headerBackgroundImage="'/Image.jpg'"/>
    <section class="viewAllArticle">
        <div class="viewAllArticle__content">

            <PopularTopicCard v-for="(card) in cards.slice(0, 8)" :key="card.id" :cardTime="card.createdAt"
                :cardTitle="card.title" :cardPara="card.content" :id="card.id"
                :cardAuthorName="card.user.firstName + ' ' + card.user.lastName" @clickCard="handleclickOfArticle" />

        </div>
    </section>
    <Footer />
</template>
  
<script setup>
import Header from "@/components/layouts/Header.vue"
import Footer from "@/components/layouts/Footer.vue"
import { onMounted } from 'vue'
import { fetchData, cards, lastCard } from '@/utility/articleLogic';
import PopularTopicCard from '@/components/homepage/PopularTopicCard.vue';
import {handleArticleClick} from '../utility/articleLogic'
import { useRouter } from 'vue-router';

const router = useRouter();

const handleclickOfArticle = (articleId) =>{
  handleArticleClick(articleId, router)
}
onMounted(fetchData);

</script>