<template>
    <Header :card="lastCard" />
    <section class="viewAllArticle">
        <div class="viewAllArticle__content">

            <PopularTopicCard v-for="(card) in cards.slice(0, 8)" 
            :key="card.id"
            :card="card" />

        </div>
    </section>
    <Footer />
</template>
  
<script setup>
import Header from "@/components/layouts/Header.vue"
import Footer from "@/components/layouts/Footer.vue"
import { onMounted, computed } from 'vue'
import { fetchData, cards } from '@/utility/articleLogic';
import PopularTopicCard from '@/components/homepage/PopularTopicCard.vue';

onMounted(fetchData);

const lastCard = computed(() => {
    return cards.value.length > 0 ? cards.value[cards.value.length - 1] : null;
});

</script>