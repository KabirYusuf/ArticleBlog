<script setup>
import { ref, onMounted, computed } from 'vue';
import PopularTopicCard from '@/components/homepage/PopularTopicCard.vue';
import EditorsPickCard from '@/components/homepage/EditorsPickCard.vue';
import {getAllArticles} from '../utility/articleApiService';
import Header from "@/components/layouts/Header.vue"
import Footer from "@/components/layouts/Footer.vue"

const rawCards = ref([]);

onMounted(async () => {
  try {
    const articles = await getAllArticles();
    rawCards.value = articles._embedded.items;
  } catch (error) {
    console.error('Error fetching articles:', error);
  }
});

const formatDate = (dateString) => {
  const options = { year: '2-digit', month: '2-digit', day: '2-digit' };
  const formattedDate = new Intl.DateTimeFormat('en-GB', options).format(new Date(dateString));
  return formattedDate.replace(/\//g, '.');
};

const cards = computed(() => {
  return rawCards.value.map(card => {
    
    const formattedTime = formatDate(card.createdAt);

    let formattedContent = card.content;
    if (card.content.length > 5) {
      formattedContent = card.content.substring(0, 5) + '...';
    }

    return {
      ...card,
      createdAt: formattedTime,
      content: formattedContent
    };
  });
});

const lastCard = computed(() => {
  return cards.value.length > 0 ? cards.value[cards.value.length - 1] : 'null';
});





const editorsPicks = ref([
  {
    cardImage: "editors_images/Editor1.jpg",
    cardTime: "08.08.2021",
    cardTag: "FASHION",
    cardTitle: "Richard Norton photorealistic rendering as real photos",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data",
    icon: "fas fa-gem"
  },
  {
    cardImage: "editors_images/Editor2.jpg",
    cardTime: "08.08.2021",
    cardTag: "FASHION",
    cardTitle: "Richard Norton photorealistic rendering as real photos",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data",
  },
  {
    cardImage: "editors_images/Editor3.jpg",
    cardTime: "08.08.2021",
    cardTag: "FASHION",
    cardTitle: "Richard Norton photorealistic rendering as real photos",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data",
  },
]);
</script>


<template>
  <Header 
  :title="lastCard?.title"
  :date="lastCard?.createdAt"
  :content="lastCard?.content"
  :headerBackgroundImage="'/Image.jpg'"/>
  
        <section class="popularTopics">
            <h3 class="popularTopics__heading">Popular topics</h3>
            <ul class="popularTopics__list">
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">All</a></li>
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Adventure</a></li>
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Travel</a></li>
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Fashion</a></li>
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Technology</a></li>
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Branding</a></li>
                <li class="popularTopics__listItem popularTopics__listItem--right" ><a href="#" class="popularTopics__listLink">View All</a></li>
            </ul>
            <div class="popularTopics__card">
        
                <PopularTopicCard
                    v-for="(card, index) in cards.slice(0,8)"
                    :key="card.id"
                    :cardTime="card.createdAt"
                    :cardTitle="card.title"
                    :cardPara="card.content"
                    :cardAuthorName="card.user.firstName + ' ' + card.user.lastName"
                />
                            
            </div>
        </section>

        <section class="editorsPick">
            <h3 class="editorsPick__heading">Editor's Pick</h3>
            <div class="editorsPick__card">
                <EditorsPickCard
                    v-for="(editorPick, index) in editorsPicks"
                    :key="index"
                    :cardImage="editorPick.cardImage"
                    :cardTime="editorPick.cardTime"
                    :cardTag="editorPick.cardTag"
                    :cardTitle="editorPick.cardTitle"
                    :cardPara="editorPick.cardPara"
                    :icon="editorPick.icon"
                />
            </div>
            
        </section>

         <Footer />
</template>
