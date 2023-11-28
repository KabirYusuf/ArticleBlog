<script setup>
import { ref, onMounted, computed } from 'vue';
import PopularTopicCard from '@/components/homepage/PopularTopicCard.vue';
import EditorsPickCard from '@/components/homepage/EditorsPickCard.vue';
import Header from "@/components/layouts/Header.vue"
import Footer from "@/components/layouts/Footer.vue"
import { fetchData, cards } from '@/utility/articleLogic';


onMounted(fetchData);

const firstCard = computed(() => {
    return cards.value.length > 0 ? cards.value[0] : null;
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
]);;
</script>


<template>
  <Header :card="firstCard" />

  <section class="popularTopics">
    <h3 class="popularTopics__heading">Popular topics</h3>
    <ul class="popularTopics__list">
      <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">All</a></li>
      <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Adventure</a></li>
      <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Travel</a></li>
      <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Fashion</a></li>
      <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Technology</a></li>
      <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Branding</a></li>
      <li class="popularTopics__listItem popularTopics__listItem--right">
        <router-link to="/view-all-articles" class="popularTopics__listLink">View All</router-link>
      </li>
    </ul>
    <div class="popularTopics__card">

      <PopularTopicCard v-for="card in cards.slice(0, 8)" 
        :key="card.id"
        :card="card"/>

    </div>
  </section>

  <section class="editorsPick">
    <h3 class="editorsPick__heading">Editor's Pick</h3>
    <div class="editorsPick__card">
      <EditorsPickCard v-for="(editorPick, index) in editorsPicks" 
      :key="index" 
      :card="editorPick"/>
    </div>

  </section>

  <Footer />
</template>
