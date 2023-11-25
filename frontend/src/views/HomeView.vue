<script setup>
import { ref, onMounted } from 'vue';
import PopularTopicCard from '@/components/homepage/PopularTopicCard.vue';
import EditorsPickCard from '@/components/homepage/EditorsPickCard.vue';
import Header from "@/components/layouts/Header.vue"
import Footer from "@/components/layouts/Footer.vue"
import { fetchData, cards, lastCard } from '@/utility/articleLogic';

onMounted(fetchData);

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
                <li class="popularTopics__listItem popularTopics__listItem--right">
                  <router-link to="/view-all-articles" class="popularTopics__listLink">View All</router-link>
                </li>
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
